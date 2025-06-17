package com.github.veronfc.secret_api;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
class SecretController {
  private SecretService service;
  private EncryptionService encryption;
  private final SecretRepository repository;

  SecretController(SecretRepository repository, SecretService service, EncryptionService encryption) {
    this.repository = repository;
    this.service = service;
    this.encryption = encryption;
  }

  @GetMapping("/secrets")
  List<Secret> allSecrets() {
    return repository.findAll();
  }

  @GetMapping("/secret/{id}")
  String retrieveSecret(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
    Cookie[] cookies = request.getCookies();
    String secretKey = "";
    
    for (Cookie cookie: cookies) {
      if (cookie.getName().equals(String.format("secret-%s-key", id))) {
        secretKey = cookie.getValue();
        break;
      }
    }

    if (secretKey.isBlank()) {
      throw new UnauthorizedException();
    }

    Secret secret = repository.findById(id).orElseThrow(() -> new SecretNotFoundException());

    if (secret.getExpiresAt().isBefore(LocalDateTime.now())) {
      repository.deleteById(secret.getId());
      throw new SecretExpiredException();
    }

    Cookie cookie = new Cookie(String.format("secret-%s-key", id), null);
    cookie.setHttpOnly(true);
    //cookie.setSecure(true);
    cookie.setMaxAge(0);

    repository.deleteById(secret.getId());

    try {
      return encryption.decryptMessage(secret.getMessage(), secretKey);
    } catch (Exception ex) {
      throw new ServerException(ex.getMessage());
    }
  }

  @PostMapping("/secret")
  String createSecret(@RequestBody CreateSecretDto body,  HttpServletResponse response) {
    try {
      String key = encryption.generateKey();
      
      String encryptedMessage = encryption.encryptMessage(body.message(), key);
      LocalDateTime expiryDateTime = service.getExpiryDateTime(body.expiresIn());

      Secret secret = new Secret(encryptedMessage, expiryDateTime);

      Long id = repository.save(secret).getId();

      Cookie cookie = new Cookie(String.format("secret-%s-key", id), key);
      cookie.setHttpOnly(true);
      //cookie.setSecure(true);
      response.addCookie(cookie);

      return String.format("Secret ID: %s", id);
    } catch (Exception ex) {
      throw new BadRequestException(ex.getMessage());
    }
  }
}
