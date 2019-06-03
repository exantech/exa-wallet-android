package com.exawallet.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class NativeCryptoUtils extends ModelInitializer {
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    private static NativeCryptoUtils Instance = null;

    public static synchronized NativeCryptoUtils cryptoUtils() {
        if (NativeCryptoUtils.Instance == null) {
            NativeCryptoUtils.Instance = new NativeCryptoUtils();
        }
        return NativeCryptoUtils.Instance;
    }

    public native String signMessageJ(String message, String secretKey);

    public native String chachaDecryptJ(String cipher, String key);

    public native String chachaEncryptJ(String message, String key);

    public static String getHash(String message) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(message.getBytes());
        return bytesToHex(messageDigest.digest());
    }

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}