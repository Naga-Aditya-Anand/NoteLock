package com.example.notessecurity;

import android.util.Base64;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptDecypt {

    private static final String KEY = "20nov18051973dlasrishn1su5230749"; // Replace with your own secret key
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    public static String encrypt(String data) {
        try {
            byte[] iv = generateIV();
            byte[] encryptedData;

            SecretKey secretKey = new SecretKeySpec(KEY.getBytes(), "AES");
            AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
            encryptedData = cipher.doFinal(data.getBytes());

            return Base64.encodeToString(iv, Base64.DEFAULT) + ":" +
                    Base64.encodeToString(encryptedData, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String encryptedData) {
        try {
            String[] parts = encryptedData.split(":");
            byte[] iv = Base64.decode(parts[0], Base64.DEFAULT);
            byte[] data = Base64.decode(parts[1], Base64.DEFAULT);
            byte[] decryptedData;

            SecretKey secretKey = new SecretKeySpec(KEY.getBytes(), "AES");
            AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
            decryptedData = cipher.doFinal(data);

            return new String(decryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] generateIV() {
        byte[] iv = new byte[16];
        // Generate random IV
        // Replace with your own logic to generate a secure random IV
        return iv;
    }
}