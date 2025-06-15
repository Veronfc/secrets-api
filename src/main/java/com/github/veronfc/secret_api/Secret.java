package com.github.veronfc.secret_api;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
@ToString
class Secret {
  private @Id @GeneratedValue Long id;
  private String message;
  private @CreationTimestamp LocalDateTime createdAt;
  private LocalDateTime expiresAt;
  private @Setter Boolean viewed = false;

  public Secret(String message, LocalDateTime expiresAt) {
    this.message = message;
    this.expiresAt = expiresAt;
  }
}
