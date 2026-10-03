# GitHub Copilot System Instructions

## 🛠️ Technology Stack & Version Constraints
- **Java Version:** Java 25 (Mandatory modern features: Virtual Threads, Pattern Matching, Scoped Values, Structured Concurrency, Record Patterns, Text Blocks, and modern Switch expressions).
- **Framework:** Spring Boot 4.x / Spring Framework 7+ (Prioritize modern functional configurations, Jakarta EE 11 namespaces `jakarta.*`, and native virtual thread scheduling).
- **Template Engine:** JTE (Java Template Engine) - Use `.jte` file syntax, strict typing, explicit `@param` and `@import` definitions.
- **Frontend Framework:** Bootstrap 5.x (Strictly **NO jQuery**. Use vanilla JavaScript APIs, native components, and utility-first responsive classes).
- **Database:** PostgreSQL 18 (Utilize modern SQL, `jsonb` optimizations, snake_case column names, and pipeline mode if performance-critical).

## 🪐 IDE Environment (VS Code)
- Enforce clean file paths. When generating code, always output the target file path on the very first line as a comment (e.g., `// src/main/java/...` or `<!-- src/main/jte/... -->`).
- Keep code formatting clean, utilizing standard Java code style conventions.

## 🎨 Component-Specific Guidelines

### 1. Java 25 & Spring Boot 4 Idioms
- **Concurrency:** Always default to Virtual Threads for I/O and web request handling. Do not use legacy thread pools unless explicitly requested.
- **Data Carrier:** Prefer Java **Records** over traditional POJOs or Lombok for DTOs, projections, and view models.
- **Error Handling:** Standardize API and view exceptions using `ProblemDetail` (RFC 7807) or `@ControllerAdvice`.

### 2. JTE (Java Template Engine) Rules
- **Type Safety:** Every JTE file must explicitly declare all parameters with their full Java types at the very top of the template.
- **View Logic:** Templates must only contain display logic (loops, conditionals, expressions). Business logic or complex data mutations belong in the Java layer.
- **Output Escaping:** Rely on JTE's automatic context-sensitive HTML escaping for XSS prevention.

### 3. Bootstrap 5 & JavaScript
- Use native Bootstrap 5 JavaScript components using `new bootstrap.Modal()`, `bootstrap.Toast.getOrCreateInstance()`, etc.
- Always use standard `document.querySelector` or event listeners instead of obsolete `$` syntax.

### 4. PostgreSQL 18 & Data Layer
- Use Spring Data JPA or Spring Data JDBC. Map PostgreSQL data types cleanly (e.g., `java.time.OffsetDateTime` for `timestamptz`).
- Enforce `snake_case` in DB schemas and `camelCase` in Java properties.

## 📝 Code Style & Guardrails
- **Security First:** Guard against SQL injection, CSRF (via Spring Security), and XSS.
- **No Placeholders:** Avoid generating unhelpful `// TODO: implement method` blocks. Always provide a logical, structural implementation.
- **Commenting Language:** Use clear and concise English language for comments. Avoid ambiguous terms and ensure that comments accurately describe the intent and functionality of the code.
- **Conciseness:** Keep comments focused on "why" instead of "what". Eliminate redundant boilerplate.
- **Consistency:** Maintain consistent naming conventions, code structure, and formatting throughout the project to enhance readability and maintainability. Use 4 spaces for block indentation for java, jte, html, and other relevant files.


- **Spring Security & JPA Auditing:** Always leverage `@EntityListeners(AuditingEntityListener.class)` alongside `@CreatedBy` and `@LastModifiedBy`. Wire these fields transparently via an `AuditorAware<String>` implementation reading from the active `SecurityContextHolder`.
- **Database-Level Context Security:** Secure multi-tenant or owner-isolated operations within `@Query` annotations by using SpEL security expressions (e.g., `WHERE u.username = ?#{principal.username}`).
- **PostgreSQL 18 JSONB Mapping:** Map JSON structures directly to native Java maps or records using `@JdbcTypeCode(SqlTypes.JSON)` and `columnDefinition = "jsonb"`. Avoid explicit `AttributeConverter` boilerplate or raw string casting.
- **Temporal Handling:** Always map database timestamp fields containing time zones (`timestamptz`) to `java.time.OffsetDateTime` rather than legacy date variants.
