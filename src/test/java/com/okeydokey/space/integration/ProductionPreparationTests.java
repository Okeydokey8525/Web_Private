package com.okeydokey.space.integration;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.servlet.RequestDispatcher;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("prod")
class ProductionPreparationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private Environment environment;

	@Test
	void productionProfileBindsCacheCompressionTemplateAndErrorSettings() {
		assertEquals("true", environment.getProperty("spring.thymeleaf.cache"));
		assertEquals("true", environment.getProperty("server.compression.enabled"));
		assertEquals("native", environment.getProperty("server.forward-headers-strategy"));
		assertEquals("1h", environment.getProperty("spring.web.resources.cache.cachecontrol.max-age"));
		assertEquals("true", environment.getProperty("spring.web.resources.cache.cachecontrol.cache-public"));
		assertEquals("true", environment.getProperty("spring.web.resources.cache.use-last-modified"));
		assertEquals("never", environment.getProperty("spring.web.error.include-message"));
		assertEquals("never", environment.getProperty("spring.web.error.include-binding-errors"));
		assertEquals("never", environment.getProperty("spring.web.error.include-stacktrace"));
		assertEquals("false", environment.getProperty("spring.web.error.include-exception"));
	}

	@Test
	void custom404PageRendersWith404StatusWithoutInternalDetails() throws Exception {
		mockMvc.perform(get("/error")
						.accept(MediaType.TEXT_HTML)
						.requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 404)
						.requestAttr(RequestDispatcher.ERROR_REQUEST_URI, "/this-page-does-not-exist"))
				.andExpect(status().isNotFound())
				.andExpect(content().string(containsString("PAGE_NOT_FOUND_")))
				.andExpect(content().string(containsString("RETURN_HOME")))
				.andExpect(content().string(containsString("<meta name=\"robots\" content=\"noindex\">")))
				.andExpect(content().string(not(containsString("rel=\"canonical\""))))
				.andExpect(content().string(not(containsString("Whitelabel Error Page"))))
				.andExpect(content().string(not(containsString("java.lang."))))
				.andExpect(content().string(not(containsString("stacktrace"))));
	}

	@Test
	void custom5xxPageRendersWith500StatusWithoutInternalDetails() throws Exception {
		mockMvc.perform(get("/error")
						.accept(MediaType.TEXT_HTML)
						.requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 500)
						.requestAttr(RequestDispatcher.ERROR_REQUEST_URI, "/test-only-failure")
						.requestAttr(RequestDispatcher.ERROR_MESSAGE, "Hidden test exception"))
				.andExpect(status().isInternalServerError())
				.andExpect(content().string(containsString("SOMETHING_WENT_WRONG_")))
				.andExpect(content().string(containsString("RETURN_HOME")))
				.andExpect(content().string(containsString("<meta name=\"robots\" content=\"noindex\">")))
				.andExpect(content().string(not(containsString("rel=\"canonical\""))))
				.andExpect(content().string(not(containsString("Hidden test exception"))))
				.andExpect(content().string(not(containsString("java.lang."))))
				.andExpect(content().string(not(containsString("stacktrace"))));
	}

	@Test
	void securityHeadersApplyToHtmlAndStaticResources() throws Exception {
		for (String path : new String[] { "/", "/css/main.css", "/images/profile/okeydokey-profile.jpg" }) {
			mockMvc.perform(get(path))
					.andExpect(status().isOk())
					.andExpect(header().string("X-Content-Type-Options", "nosniff"))
					.andExpect(header().string("Referrer-Policy", "strict-origin-when-cross-origin"))
					.andExpect(header().string("Permissions-Policy", "camera=(), microphone=(), geolocation=()"))
					.andExpect(header().string("X-Frame-Options", "DENY"))
					.andExpect(header().string("Strict-Transport-Security", "max-age=31536000"));
		}
	}

	@Test
	void faviconAssetsAndHomepageDeclarationsAreAvailable() throws Exception {
		for (String path : new String[] {
				"/favicon.png",
				"/favicon-16x16.png",
				"/favicon-32x32.png",
				"/apple-touch-icon.png"
		}) {
			mockMvc.perform(get(path))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.IMAGE_PNG));
		}

		mockMvc.perform(get("/favicon.ico"))
				.andExpect(status().isOk());

		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString(
						"<link rel=\"icon\" type=\"image/png\" sizes=\"32x32\" href=\"/favicon-32x32.png\">")))
				.andExpect(content().string(containsString(
						"<link rel=\"icon\" type=\"image/png\" sizes=\"16x16\" href=\"/favicon-16x16.png\">")))
				.andExpect(content().string(containsString(
						"<link rel=\"apple-touch-icon\" sizes=\"180x180\" href=\"/apple-touch-icon.png\">")));
	}

	@Test
	void robotsAndSitemapDeclareExactlyThePublicProductionUrls() throws Exception {
		String robots = mockMvc.perform(get("/robots.txt"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith("text/plain"))
				.andReturn()
				.getResponse()
				.getContentAsString();
		assertTrue(robots.contains("User-agent: *"));
		assertTrue(robots.contains("Allow: /"));
		assertTrue(robots.contains("Sitemap: https://okeydokey-space.onrender.com/sitemap.xml"));

		String sitemap = mockMvc.perform(get("/sitemap.xml"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_XML))
				.andReturn()
				.getResponse()
				.getContentAsString();
		assertEquals(5, count(sitemap, "<url>"));
		for (String url : new String[] {
				"https://okeydokey-space.onrender.com/",
				"https://okeydokey-space.onrender.com/projects",
				"https://okeydokey-space.onrender.com/journey",
				"https://okeydokey-space.onrender.com/garden",
				"https://okeydokey-space.onrender.com/playground"
		}) {
			assertEquals(1, count(sitemap, "<loc>" + url + "</loc>"));
		}
		assertFalse(sitemap.contains("style-guide"));
		assertFalse(sitemap.contains("<lastmod>"));
		assertFalse(sitemap.contains("<priority>"));
		assertFalse(sitemap.contains("<changefreq>"));
	}

	@Test
	void publicPagesHaveOneMatchingCanonicalAndOpenGraphUrl() throws Exception {
		Map<String, String> canonicalUrls = Map.of(
				"/", "https://okeydokey-space.onrender.com/",
				"/projects", "https://okeydokey-space.onrender.com/projects",
				"/journey", "https://okeydokey-space.onrender.com/journey",
				"/garden", "https://okeydokey-space.onrender.com/garden",
				"/playground", "https://okeydokey-space.onrender.com/playground");

		for (Map.Entry<String, String> page : canonicalUrls.entrySet()) {
			String html = mockMvc.perform(get(page.getKey()))
					.andExpect(status().isOk())
					.andReturn()
					.getResponse()
					.getContentAsString();
			assertEquals(1, count(html, "<meta name=\"description\""), page.getKey());
			assertEquals(1, count(html, "<link rel=\"canonical\" href=\"" + page.getValue() + "\">"), page.getKey());
			assertEquals(1, count(html, "<meta property=\"og:url\" content=\"" + page.getValue() + "\">"), page.getKey());
			assertEquals(1, count(html, "<meta property=\"og:title\""), page.getKey());
			assertEquals(1, count(html, "<meta property=\"og:description\""), page.getKey());
			assertFalse(html.contains("property=\"og:image\""), page.getKey());
		}

		String styleGuide = mockMvc.perform(get("/style-guide"))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();
		assertEquals(1, count(styleGuide, "<meta name=\"robots\" content=\"noindex,nofollow\">"));
		assertEquals(1, count(styleGuide,
				"<link rel=\"canonical\" href=\"https://okeydokey-space.onrender.com/style-guide\">"));
	}

	private static int count(String value, String marker) {
		return value.split(java.util.regex.Pattern.quote(marker), -1).length - 1;
	}

}
