package com.okeydokey.space.config;

import java.io.IOException;

import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityHeadersFilter extends OncePerRequestFilter {

	private static final String HSTS_POLICY = "max-age=31536000";

	private final boolean productionProfileActive;

	public SecurityHeadersFilter(Environment environment) {
		this.productionProfileActive = environment.acceptsProfiles(Profiles.of("prod"));
	}

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		response.setHeader("X-Content-Type-Options", "nosniff");
		response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
		response.setHeader("Permissions-Policy", "camera=(), microphone=(), geolocation=()");
		response.setHeader("X-Frame-Options", "DENY");
		if (productionProfileActive) {
			response.setHeader("Strict-Transport-Security", HSTS_POLICY);
		}
		filterChain.doFilter(request, response);
	}

}
