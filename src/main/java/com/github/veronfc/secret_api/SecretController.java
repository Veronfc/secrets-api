package com.github.veronfc.secret_api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class SecretController {
  private final SecretRepository repository;

  SecretController(SecretRepository repository) {
    this.repository = repository;
  }

  @GetMapping("/secrets")
  List<Secret> allSecrets() {
    return repository.findAll();
  }

  @PostMapping("/secret")
  Secret newSecret(@RequestBody Secret newSecret) {
    return repository.save(newSecret);
  }
}
