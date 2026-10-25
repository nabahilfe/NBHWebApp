
---
name: Codebase Exploration
alwaysApply: true
---

# Codebase Exploration Rules

## Before answering project-wide questions

When a request concerns a feature, workflow, architecture, or behavior
across the project:

1. Explore the repository before answering.
2. Identify the relevant source directories and file types.
3. Search across the codebase using appropriate search tools.
4. Do not restrict searches to Python files or any other single language unless explicitly requested.
5. For Java and Spring Boot projects, consider at least:
   - **/*.java
   - **/*.jte
   - **/*.html
   - **/*.xml
   - **/*.yml
   - **/*.yaml
   - **/*.properties
   - **/*.sql
   - **/*.md
   - pom.xml

6. Search for synonyms, class names, method names, routes, annotations,
   and related concepts instead of relying on one regular expression.
7. Read the relevant files after finding them.
8. Follow calls between controllers, services, repositories, entities,
   security configuration, and templates as appropriate.
9. Perform additional searches when the first results are incomplete.
10. Base conclusions on files actually inspected.

## Example: member registration

For questions about member registration, investigate relevant components
such as:

- Registration controllers and request mappings
- Registration forms and JTE templates
- Form objects, DTOs, and validation
- Services and business logic
- Member entities and repositories
- Password encoding and account activation
- Spring Security configuration
- Database constraints and related migrations

These are search targets, not assumptions that every component exists.

## Tool execution

- Execute available search and file-reading tools.
- Never present a proposed tool call as if it had already been executed.
- If a tool call fails, report the failure and try an appropriate
  alternative when available.
- Do not claim to have searched or read files that were not inspected.

## Final response

For a project analysis, provide:

- The relevant file paths and class names
- The verified execution flow
- Relationships between the relevant components
- Any unresolved questions or missing evidence
