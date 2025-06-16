package com.github.veronfc.secret_api;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
@ToString
class Secret {
  @JsonIgnore private @Id @GeneratedValue Long id;
  private String message;
  private @CreationTimestamp LocalDateTime createdAt;
  @JsonIgnore private LocalDateTime expiresAt;

  public Secret(String message, LocalDateTime expiresAt) {
    this.message = message;
    this.expiresAt = expiresAt;
  }
}
