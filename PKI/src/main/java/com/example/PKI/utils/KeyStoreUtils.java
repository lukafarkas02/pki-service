package com.example.PKI.utils;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class KeyStoreUtils {

    public KeyStoreUtils() {

    }

    //METHOD FOR GENERATING SERIAL NUMBER FOR NEW CERTIFICATE
    public static BigInteger generateSerialNumber() {
        try {

            SecureRandom random = SecureRandom.getInstanceStrong();
            BigInteger serialNumber = new BigInteger(128, random);
            return serialNumber;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String generateRandomPassword(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
        StringBuilder password = new StringBuilder();
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            password.append(randomChar);
        }
        return password.toString();
    }


    public BigInteger getSerialNumber(String keystoreFile, String keystorePassword, String alias) {
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            FileInputStream fis = new FileInputStream(keystoreFile);
            keyStore.load(fis, keystorePassword.toCharArray());

            java.security.cert.Certificate certificate = keyStore.getCertificate(alias);
            if (certificate instanceof X509Certificate) {
                return ((X509Certificate) certificate).getSerialNumber();
            } else {
                System.out.println("The certificate is not an X.509 certificate.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
