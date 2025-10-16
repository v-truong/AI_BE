Digital Banking Auth Module (No Foreign Keys)
============================================

This generated Spring Boot 3.3 project models relationships without database foreign keys:
- Device and RefreshToken reference users by userId String (no JPA foreign keys).
- JWT RS256 access + refresh tokens
- BCrypt password hashing
- In-memory OTP service (demo)
- Max 3 trusted devices enforced in application logic

Run:
- Configure PostgreSQL connection in application.yml
- mvn package
- java -jar target/digital-banking-auth-no-fk-0.0.1-SNAPSHOT.jar

Keys in src/main/resources/keys are for demo only.
