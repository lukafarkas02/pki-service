package com.example.PKI.repositories;

import com.example.PKI.models.CertificateType;
import com.example.PKI.models.Issuer;
import com.example.PKI.models.User;
import com.example.PKI.utils.KeyStoreUtils;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;

import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;

public class KeyStoreRepository {

    //KeyStore je Java klasa za citanje specijalizovanih datoteka koje se koriste za cuvanje kljuceva
    //Tri tipa entiteta koji se obicno nalaze u ovakvim datotekama su:
    // - Sertifikati koji ukljucuju javni kljuc
    // - Privatni kljucevi
    // - Tajni kljucevi, koji se koriste u simetricnima siframa
    private KeyStore keyStore;


    public KeyStoreRepository() {

        try {
            keyStore = KeyStore.getInstance("JKS", "SUN");
//            generateCertificatesService = new GenerateCertificatesService();
            
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    /**
     * Zadatak ove funkcije jeste da ucita podatke o izdavaocu i odgovarajuci privatni kljuc.
     * Ovi podaci se mogu iskoristiti da se novi sertifikati izdaju.
     *
     * @param keyStoreFile - datoteka odakle se citaju podaci
     * @param alias - alias putem kog se identifikuje sertifikat izdavaoca
     * @param password - lozinka koja je neophodna da se otvori key store
     * @param keyPass - lozinka koja je neophodna da se izvuce privatni kljuc
     * @return - podatke o izdavaocu i odgovarajuci privatni kljuc
     */
    public Issuer readIssuerFromStore(String keyStoreFile, String alias, char[] password, char[] keyPass) {
        try {
            //Datoteka se ucitava
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
            keyStore.load(in, password);

            //Iscitava se sertifikat koji ima dati alias
            Certificate cert = keyStore.getCertificate(alias);

            //Iscitava se privatni kljuc vezan za javni kljuc koji se nalazi na sertifikatu sa datim aliasom
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, keyPass);

            X500Name issuerName = new JcaX509CertificateHolder((X509Certificate) cert).getSubject();
            return new Issuer(privateKey, cert.getPublicKey(), issuerName);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Ucitava sertifikat is KS fajla
     */
    public Certificate readCertificate(String keyStoreFile, String keyStorePass, String alias) {
        try {
            //kreiramo instancu KeyStore
            KeyStore ks = KeyStore.getInstance("JKS", "SUN");
            //ucitavamo podatke
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
            ks.load(in, keyStorePass.toCharArray());

            if(ks.isKeyEntry(alias)) {
                Certificate cert = ks.getCertificate(alias);
                return cert;
            }
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Ucitava privatni kljuc is KS fajla
     */

    //ovo ne radim iz JKS vec iz -pem
    public PrivateKey readPrivateKey(String keyStoreFile, String keyStorePass, String alias, String pass) {
        try {
            //kreiramo instancu KeyStore
            KeyStore ks = KeyStore.getInstance("JKS", "SUN");
            //ucitavamo podatke
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
            ks.load(in, keyStorePass.toCharArray());

            if(ks.isKeyEntry(alias)) {
                PrivateKey pk = (PrivateKey) ks.getKey(alias, pass.toCharArray());
                return pk;
            }
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void loadKeyStore(String fileName, char[] password) {
        try {
            if(fileName != null) {
                keyStore.load(new FileInputStream(fileName), password);
            } else {
                //Ako je cilj kreirati novi KeyStore poziva se i dalje load, pri cemu je prvi parametar null
                keyStore.load(null, password);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveKeyStore(String fileName, char[] password) {
        try {
            keyStore.store(new FileOutputStream(fileName), password);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String alias, char[] password, Certificate certificate) {
        try {
//            keyStore.setKeyEntry(alias, privateKey, password, new Certificate[] {certificate});
            keyStore.setCertificateEntry(alias, certificate);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }


    //THIS MEAN THAT I WANT TO MAKE A NEW CA OR ROOT
    public HashMap<String, char[]> createAndLoadNewKeyStore() {
        HashMap<String, char[]> fileMap = new HashMap<>();
        int numberOfFiles = getLastFileNumber("src/main/resources/keystores");
        keyStore = null;
        try {
            keyStore = KeyStore.getInstance("JKS", "SUN");
        } catch (KeyStoreException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
        char[] password = KeyStoreUtils.generateRandomPassword(20).toCharArray(); // Postavite željenu šifru
        String fileName = "keystore"  + (numberOfFiles + 1) + ".jks";
        //saveToTxt (filename, password)
        saveToTxt(fileName, password);
        fileMap.put(fileName, password);

        try {
            keyStore.load(null, password);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        }

        // Sačuvajte keystore u fajl
        try (FileOutputStream fos = new FileOutputStream("src/main/resources/keystores/" + fileName)) {
            keyStore.store(fos, password);
        } catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException e) {
            e.printStackTrace();
        }

        return fileMap;

    }


    public void saveCertificate(User user1, User user2) {

//        loadKeyStore("src/main/resources/keystores/keystore.jks", "bezbednost".toCharArray());
//        write(newCertificate.getAlias(), "bezbednost".toCharArray(), certificate);
//        saveKeyStore("src/main/resources/keystores/keystore.jks",  "bezbednost".toCharArray());


    }

    public void saveRootCertificate(Certificate certificate, String alias, HashMap<String, char[]> fileData) {
        if (!fileData.isEmpty()) {
            String key = fileData.keySet().iterator().next();
            char[] value = fileData.get(key);
            loadKeyStore("src/main/resources/keystores/" + key, value);
            write(alias, value, certificate);
            saveKeyStore("src/main/resources/keystores/" + key, value);

        }
    }


    public static int getLastFileNumber(String directoryPath) {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();

        if (files == null) return 0;
        return files.length;
    }

    public static void saveToTxt(String fileName, char[] password) {
        try (FileWriter writer = new FileWriter("src/main/resources/passwords/passwords.txt", true)) {
            // Formatiranje zapisivanja: imeFajla,lozinka
            writer.write(fileName + "," + new String(password) + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
