## Sample of Spring Boot project with Unit and Integration tests

### Technology stack:
- Java 17
- Spring Boot 3
- Spring Web
- Spring Data
- Lombok
- JUnit
- Mockito
- PostgreSQL
- Embedded DB H2
- Testcontainers

## Branches explanation
- **dev_UnitAndMockitoTests** - use for run service and repo layer tests with embedded database H2

- **dev_IntegrationTests** - use for run integration tests with local test database, which setup at `application-test.yaml`.

- **master** - use for run integration tests with docker and testcontainers lib.

