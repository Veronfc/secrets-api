package com.github.veronfc.secret_api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Secret key not found")
class UnauthorizedException extends RuntimeException  {
  
}
