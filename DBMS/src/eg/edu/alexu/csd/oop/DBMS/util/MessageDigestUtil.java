package eg.edu.alexu.csd.oop.DBMS.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageDigestUtil {
    public static String getSecuredPassword(String password)
            throws NoSuchAlgorithmException, RuntimeException {

        if (!App.checkForExistence(password))
            throw new RuntimeException("Use your keyboard!");
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());

        byte[] byteData = md.digest();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}