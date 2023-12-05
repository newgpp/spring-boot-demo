package com.felix;

import org.junit.Test;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author felix
 * @desc some desc
 */
public class DigestTest {

    @Test
    public void md5_hex_should_success() {
        String input = "ILoveJava";
        System.out.println(md5Hex(input));
    }

    @Test
    public void sha1_hex_should_success() {
        String input = "ILoveJava";
        System.out.println(sha1Hex(input));
    }

    @Test
    public void sha256_hex_should_success() {
        String input = "ILoveJava";
        System.out.println(sha256Hex(input));
    }


    /**
     * md5 hex upperCase
     */
    private static String md5Hex(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();
            String sign = DatatypeConverter.printHexBinary(digest).toUpperCase();
            System.out.println("签名串: " + input);
            System.out.println("签名结果: " + sign);
            System.out.println("签名结果长度: " + sign.length());
            return sign;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * sha1 hex upperCase
     */
    private static String sha1Hex(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(input.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                hexString.append(String.format("%02x", b));
            }
            String sign = hexString.toString().toUpperCase();
            System.out.println("签名串: " + input);
            System.out.println("签名结果: " + sign);
            System.out.println("签名结果长度: " + sign.length());
            return sign;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * sha256 hex upperCase
     */
    private static String sha256Hex(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(input.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                hexString.append(String.format("%02x", b));
            }
            String sign = hexString.toString().toUpperCase();
            System.out.println("签名串: " + input);
            System.out.println("签名结果: " + sign);
            System.out.println("签名结果长度: " + sign.length());
            return sign;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
