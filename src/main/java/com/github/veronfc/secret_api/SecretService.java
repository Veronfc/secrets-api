package com.github.veronfc.secret_api;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
class SecretService {
  @Value("${encryptionSecret}") private String secret;
  Map<String, TemporalUnit> units = Map.of("S", ChronoUnit.SECONDS, "M", ChronoUnit.MINUTES, "H", ChronoUnit.HOURS);

  public LocalDateTime getExpiryDateTime(String expiresIn) {
    Pattern pattern = Pattern.compile("^(\\d+)([a-zA-Z]+)$");
    Matcher matcher = pattern.matcher(expiresIn);

    if (!matcher.matches()) {
      throw new IllegalArgumentException("Invalid format. Expected something like '10m', '30s', '5h', etc.");
    }
    
    Long value = Long.parseLong(matcher.group(1));
    String unit = matcher.group(2).toUpperCase();

    if (!units.containsKey(unit)) {
      throw new IllegalArgumentException("Invalid unit. Valid units are 's' | 'S' for seconds, 'm' | 'M' for minutes and 'h' | 'H' for hours");
    }

    return LocalDateTime.now().plus(value, units.get(unit));
  } 
}
