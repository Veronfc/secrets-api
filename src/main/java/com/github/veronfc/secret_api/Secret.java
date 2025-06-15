package com.github.veronfc.secret_api;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
class Secret {
  private @Id @GeneratedValue Long id;
  private final String message;
  private @CreationTimestamp LocalDateTime createdAt;
  private final LocalDateTime expiresAt;
  private @Setter Boolean viewed = false;
}
