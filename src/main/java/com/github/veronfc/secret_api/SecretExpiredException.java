package com.github.veronfc.secret_api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.GONE, reason = "Secret has expired")
class SecretExpiredException extends RuntimeException {
}
