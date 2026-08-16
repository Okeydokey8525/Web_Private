# OKEYDOKEY.SPACE

OKEYDOKEY.SPACE is a personal portfolio for a computer science student and
developer interested in web development, AI, computer vision and databases.
Live site: <https://okeydokey-space.onrender.com>

## Features

- Responsive portfolio homepage with About, Projects, Journey, Digital Garden,
  Playground and Contact sections
- Project archive with progressively enhanced category filters
- Learning journey and curated digital garden backed by immutable in-memory data
- Pixel Brush and Dither Machine experiments built with vanilla JavaScript
- Accessible navigation, landmarks, focus states and reduced-motion support
- Custom production error pages, response compression, static caching, basic
  security headers and crawler/social metadata

## Stack

- Java 17
- Spring Boot 4.1.0
- Thymeleaf
- HTML and CSS
- Vanilla JavaScript
- Maven

V1 uses curated in-memory content and has no database or frontend framework.

## Architecture

```text
src/main/java/com/okeydokey/space/
|-- config/
|-- controller/
|-- model/
`-- service/

src/main/resources/
|-- templates/
`-- static/
    |-- css/
    |-- images/
    `-- js/

src/test/java/com/okeydokey/space/
|-- controller/
|-- integration/
`-- service/
```

## Routes

- `/`
- `/projects`
- `/journey`
- `/garden`
- `/playground`
- `/style-guide`

Contact is a section on the homepage rather than a standalone route.

## Run locally

On Windows:

```text
.\mvnw.cmd spring-boot:run
```

Open <http://localhost:8080/>.

## Tests

```text
.\mvnw.cmd test
```

The current automated suite contains 34 tests covering controllers, services,
site integration and production preparation.

## Production build

```text
.\mvnw.cmd clean package
java -jar target\okeydokey-space-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

The default configuration remains development-friendly. The `prod` profile
enables Thymeleaf template caching, response compression, conservative public
caching for versionless static assets, safe error-detail settings and custom
404/5xx pages. Spring Boot DevTools remains local-development-only and is
excluded from the executable JAR.

Production responses include basic application-level security headers,
including `Strict-Transport-Security: max-age=31536000` when the `prod` profile
is active. Canonical URLs, `og:url`, `sitemap.xml`, the robots sitemap
declaration and native forwarded-header handling are configured for the live
Render origin. A strict Content Security Policy remains deferred while the
site uses external Google Fonts.

## Accessibility

V1 completed a WCAG 2.2 AA-oriented structural accessibility audit covering
landmarks, headings, keyboard focus, ARIA, contrast, reduced motion and dynamic
feedback. This is not a formal WCAG certification. Manual keyboard, 200% zoom
and NVDA testing with Chrome or Edge is still recommended before deployment.

## Privacy and assets

The public email, GitHub and LinkedIn links displayed by the portfolio are
intentional contact information. The `CAFE_NOSQL_` source repository is
private and no repository URL is exposed; its Digital Garden relationship is
an internal link only.

The profile portrait at
`src/main/resources/static/images/profile/okeydokey-profile.jpg` is an
intentional production asset.

## Deployment

Live site: <https://okeydokey-space.onrender.com>

- Hosting: Render Free Web Service
- Runtime: Docker
- Region: Singapore
- Branch: `main`
- Spring profile: `prod`

Render supplies the application port through its `PORT` environment variable;
local production runs fall back to port 8080.

Required Render environment variable:

```text
SPRING_PROFILES_ACTIVE=prod
```

Render Free may spin down during inactivity, so the first request after an idle
period can be slower. This is hosting-tier behavior rather than an application
error.

The live Render URL is the preferred canonical origin. If a custom domain is
added later, update canonical URLs, `og:url`, `sitemap.xml` and the sitemap URL
in `robots.txt` together. Search engine indexing is not guaranteed by these
crawl and index signals.
