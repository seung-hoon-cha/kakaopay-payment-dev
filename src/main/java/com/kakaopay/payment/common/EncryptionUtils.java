package com.kakaopay.payment.common;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.Key;

public class EncryptionUtils {

    private static final String key = "kakaoPay!@123456";
    private static Key keySpec;

    static {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        keySpec = new SecretKeySpec(keyBytes, "AES");
    }

    public static String encrypt(String str) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(key.getBytes()));
        byte[] encrypted = cipher.doFinal(str.getBytes(StandardCharsets.UTF_8));
        return new String(Base64.encodeBase64(encrypted));
    }

    public static String decrypt(String str) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(key.getBytes(StandardCharsets.UTF_8)));
        byte[] byteStr = Base64.decodeBase64(str.getBytes());
        return new String(cipher.doFinal(byteStr), StandardCharsets.UTF_8);
    }
}
