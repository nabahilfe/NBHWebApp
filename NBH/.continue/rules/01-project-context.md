
---
name: Project Context
alwaysApply: true
---

# Project Context

This project is a Java web application.

## Technology stack

- Java 25
- Spring Boot 4
- Spring MVC
- Spring Security with role-based authorization
- JTE (Java Template Engine) for server-side HTML templates
- Bootstrap 5 for responsive user interfaces
- PostgreSQL as the database
- JPA / Hibernate for persistence
- Maven for build and dependency management

## Architecture

- Controllers handle HTTP requests and responses.
- Services contain business logic where appropriate.
- Repositories and JPA entities handle database persistence.
- JTE templates render server-side HTML.
- Spring Security controls authentication and authorization.

These are expected architectural conventions, not proof that every component exists.
Verify the actual implementation in the repository before describing it.

## General rules

- Use Java 25 and the existing Spring Boot 4 architecture.
- Do not assume this is a Python, Node.js, React, or other project.
- Inspect pom.xml and the actual source tree when confirming technologies.
- Follow existing naming conventions and coding patterns.
- Do not invent classes, methods, routes, templates, or database tables.
- Distinguish verified facts from assumptions.
- Prefer minimal changes that fit the existing architecture.
