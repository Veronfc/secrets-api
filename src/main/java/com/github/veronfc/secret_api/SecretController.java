package com.github.veronfc.secret_api;

import java.time.LocalDateTime;
import java.util.List;

import javax.crypto.BadPaddingException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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

  @GetMapping("/secret/{id}")
  String retrieveSecret(@PathVariable Long id, @RequestParam(required = false) String key, HttpServletRequest request, HttpServletResponse response) {
    Cookie[] cookies = request.getCookies();
    
    if (key == null) {
      for (Cookie cookie: cookies) {
        if (cookie.getName().equals(String.format("secret-%s-key", id))) {
          key = cookie.getValue();
          break;
        }
      }
    }

    if (key == null || key.isBlank())  {
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

    
    try {
      String message = encryption.decryptMessage(secret.getMessage(), key);
      
      repository.deleteById(secret.getId());

      return message;
    } catch (Exception ex) {
      if (ex instanceof BadPaddingException) {
        throw new UnauthorizedException();
      }

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

      return String.format("Secret ID: %s\nSecret key: %s", id, key);
    } catch (Exception ex) {
      throw new BadRequestException(ex.getMessage());
    }
  }
}
