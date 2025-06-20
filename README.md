✅ Complete

# 🔐 Secrets API
A Spring Boot backend service for securely creating and retrieving **one-time secrets**. Think of it like a more secure pastebin: the secret is encrypted, delivered once, and then destroyed—no lingering data.
<br/>
<br/>
## 🚀 Summary
Secrets API allows you to:
- **Store a secret** and set a time-to-expire.
- **Encrypt** the secret with a one-time key (never stored).
- **Access** the secret exactly **once**, then it self-destructs.
- **Expire** secrets that go unaccessed past their expiry.

This is ideal for sharing **self-destructing secrets**, passwords, tokens, or any sensitive data exposure that requires zero retention.
<br/>
<br/>
## ✅ Key Features
- **AES-256 encryption** of secrets at rest.
- **One-time access**: upon retrieval, the secret is removed from storage.
- **Configurable TTL**.
- **Access keys** passed via HTTP-only cookies or query parameter.
- **H2 in-memory database** (switchable to PostgreSQL or others easily).
- **Basic validation**, error handling, and status responses.
<br/>
<br/>
## 🛠️ Tech Stack & Dependencies
- **Spring Boot** — Framework for building standalone production apps.
- **Spring Web** — For building REST endpoints.
- **Spring Data JPA** — ORM for database access.
- **H2 Database** — Lightweight in-memory DB for local dev/testing.
- **Jakarta Persistence API** (`@Entity`, `@Repository`, etc.).
- **Java Cryptography (JCE)** — AES-based encryption utilities.
- **Maven** — For dependency management and build lifecycle.

All encryption and session logic is **implemented manually**, with **no external dependencies beyond Spring Boot and JPA**.
<br/>
<br/>
## 📦 How to Run
1. Clone the repo:
    ```zsh
    git clone https://github.com/Veronfc/secrets-api.git
    cd secrets-api
    ```

2. Build and run:
    ```zsh
    ./mvnw spring-boot:run
    ```
    or build the JAR and run:
    ```zsh
    ./mvnw package
    java -jar target/secrets-api-0.0.1-SNAPSHOT.jar
    ```

3. Send a secret:
    ```zsh
    curl -X POST http://localhost:8080/secret \
    -H "Content-Type: application/json" \
    -d '{"message":"My top secret","expiresIn": "5m"}'
    ```
    **'expiresIn' must be an integer value appended with a single letter (s = seconds, m = minutes and h = hours) eg. '5m' is 5 minutes**

    You'll receive JSON response with the secret's ID and key and the server will set an HTTP-only cookie with the encryption key.

4. Retrieve the secret (including an optional 'key' query parameter if not sending the request with the server-set cookie or when retrieving from a different client):
    ```zsh
    curl http://localhost:8080/secret/{id}?key={key} \
    --cookie "secret-{id}-key"={key}"
    ```

    The secret will be decrypted and returned in a JSON response (if not expired and the key is correct) - and be deleted immediately from the database.
