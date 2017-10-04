package blockchain;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {

  /**
   *
   * @param element the String to get the SHA-256 hash
   * @return on successful: the hexadecimal representation in a String. On error: a null Object.
   *
   */
  public static String hash(String element) {
    if(element != null) {
      try {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(element.getBytes(StandardCharsets.UTF_8));

        return bytesToHex(encodedHash);
      }
      catch (NoSuchAlgorithmException exception) {
        System.out.println("Error with the SHA-256 instance of MessageDigest");
      }
    }
    return null;
  }

  private static String bytesToHex(byte[] hash) {
    StringBuffer hexString = new StringBuffer();
    for (int i = 0; i < hash.length; i++) {
      String hex = Integer.toHexString(0xff & hash[i]);
      if(hex.length() == 1) hexString.append('0');
      hexString.append(hex);
    }
    return hexString.toString();
  }
}
