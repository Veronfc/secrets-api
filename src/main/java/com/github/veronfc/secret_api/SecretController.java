package com.github.veronfc.secret_api;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class SecretController {
  private SecretService service;
  private final SecretRepository repository;

  SecretController(SecretRepository repository, SecretService service) {
    this.repository = repository;
    this.service = service;
  }

  @GetMapping("/secrets")
  List<Secret> allSecrets() {
    return repository.findAll();
  }

  @GetMapping("/secret/{id}")
  Secret retrieveSecret(@PathVariable Long id) {
    Secret secret = repository.findById(id).orElseThrow(() -> new SecretNotFoundException());

    if (secret.getExpiresAt().isBefore(LocalDateTime.now())) {
      repository.deleteById(secret.getId());
      throw new SecretExpiredException();
    }

    repository.deleteById(secret.getId());
    return secret;
  }

  @PostMapping("/secret")
  String createSecret(@RequestBody CreateSecretDto body) {
    try {
      LocalDateTime expiryDateTime = service.getExpiryDateTime(body.expiresIn());
      Secret newSecret = new Secret(body.message(), expiryDateTime);
      Long id = repository.save(newSecret).getId();
      return String.format("Secret ID: %s", id);
    } catch (Exception ex) {
      throw new BadRequestException(ex.getMessage());
    }
  }
}
