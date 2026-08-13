# OKEYDOKEY.SPACE

OKEYDOKEY.SPACE is a personal portfolio for a computer science student and
developer interested in web development, AI, computer vision and databases.
The V1 application is production-prepared but not yet deployed.

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

The current automated suite contains 31 tests covering controllers, services,
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

Production responses include basic application-level security headers. HSTS,
a strict Content Security Policy, canonical URLs, `og:url`, a sitemap and
forwarded-header configuration remain deferred until the real HTTPS host and
deployment platform are known.

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

Deployment is pending. This repository does not claim a live production URL,
domain, CI/CD pipeline or hosting provider.
