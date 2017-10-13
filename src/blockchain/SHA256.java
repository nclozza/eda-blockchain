package blockchain;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The SHA256 class represents the implementation of the well known SHA-256 algorithm used for encryptation,
 * particularly used by Bitcoin. The instances of this class are used to generate a secure hash for the blockchain
 * implementation.
 */
public class SHA256 {
    private static SHA256 sha256;
    private static MessageDigest digest;


    private SHA256() {
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException exception) {
            System.out.println("Error with the SHA-256 instance of MessageDigest.");
        }
    }

    public static SHA256 getInstance() {
        if (sha256 == null) {
            sha256 = new SHA256();
        }

        return sha256;
    }

    /**
     * This method is used to hash a string in a secure way according to the SHA-256 algorithm.
     * @param element The String to get the SHA-256 hash.
     * @return   The hexadecimal representation in a String if successfull, a null object otherwise.
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

            if (hex.length() == 1) {
                hexString.append('0');
            }

            hexString.append(hex);
        }

        return hexString.toString();
    }
}
