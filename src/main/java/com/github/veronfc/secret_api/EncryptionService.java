package com.github.veronfc.secret_api;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.util.Base64;

@Service
class EncryptionService {
  public String generateKey() throws Exception {
    KeyGenerator generator = KeyGenerator.getInstance("AES");
    generator.init(128);
    SecretKey key = generator.generateKey();

    return Base64.getEncoder().encodeToString(key.getEncoded());
  }

  private SecretKey decodeKey(String key) {
    byte[] decoded = Base64.getDecoder().decode(key);
    return new SecretKeySpec(decoded, 0, decoded.length, "AES");
  }

  public String encryptMessage(String messsage, String key) throws Exception  {
    Cipher cipher = Cipher.getInstance("AES");
    SecretKey secretKey = decodeKey(key);

    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    byte[] encryptedMessage = cipher.doFinal(messsage.getBytes());

    return Base64.getEncoder().encodeToString(encryptedMessage);
  }

  public String decryptMessage(String encryptedMessage, String key) throws Exception{
    Cipher cipher = Cipher.getInstance("AES");
    SecretKey secretKey = decodeKey(key);

    cipher.init(Cipher.DECRYPT_MODE, secretKey);
    byte[] decoded = Base64.getDecoder().decode(encryptedMessage);
    byte[] message = cipher.doFinal(decoded);

    return new String(message);
  }
}
