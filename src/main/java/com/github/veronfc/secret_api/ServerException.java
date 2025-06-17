package com.github.veronfc.secret_api;

class ServerException extends RuntimeException {
  ServerException (String message) {
    super(message);
  }
}