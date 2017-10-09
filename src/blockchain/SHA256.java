package blockchain;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {

  private static SHA256 sha256;
  private static MessageDigest digest;

  private SHA256() {
    try {
      digest = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException exception) {
      System.out.println("Error with the SHA-256 instance of MessageDigest");
    }
  }

  public static SHA256 getInstance() {
    if (sha256 == null) {
      sha256 = new SHA256();
    }
    return sha256;
  }

  /**
   * @param element the String to get the SHA-256 hash
   * @return on successful: the hexadecimal representation in a String. On error: a null Object.
   */
  public String hash(String element) {
    if (element != null) {
      byte[] encodedHash = digest.digest(element.getBytes(StandardCharsets.UTF_8));
      return bytesToHex(encodedHash);
    }
    return null;
  }

  private static String bytesToHex(byte[] hash) {
    StringBuffer hexString = new StringBuffer();
    for (int i = 0; i < hash.length; i++) {
      String hex = Integer.toHexString(0xff & hash[i]);
      if (hex.length() == 1) hexString.append('0');
      hexString.append(hex);
    }
    return hexString.toString();
  }
}
