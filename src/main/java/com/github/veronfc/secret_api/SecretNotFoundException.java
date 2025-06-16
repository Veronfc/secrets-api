package com.github.veronfc.secret_api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Secret not found")
class SecretNotFoundException extends RuntimeException {
  
}
