package com.github.veronfc.secret_api;

class BadRequestException extends RuntimeException {
  BadRequestException(String message) {
    super(message);
  }
}
