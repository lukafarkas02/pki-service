package com.example.PKI.services;

import com.example.PKI.models.Issuer;
import com.example.PKI.models.Subject;
import com.example.PKI.models.Certificate;
import com.example.PKI.models.User;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;

import java.security.*;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class GenerateCertificatesService {

    public Subject generateSubject(User user) {
        KeyPair keyPairSubject = generateKeyPair();

        //klasa X500NameBuilder pravi X500Name objekat koji predstavlja podatke o vlasniku
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, user.getCommonName());
        builder.addRDN(BCStyle.SURNAME, user.getSurname());
        builder.addRDN(BCStyle.GIVENNAME, user.getGivenName());
        builder.addRDN(BCStyle.O, user.getOrganization());
        builder.addRDN(BCStyle.OU, user.getOrganizationalUnit());
        builder.addRDN(BCStyle.C, user.getCountry());
        builder.addRDN(BCStyle.E, user.getEmail());
        //UID (USER ID) je ID korisnika
        builder.addRDN(BCStyle.UID, user.getUserId());

        return new Subject(keyPairSubject.getPublic(), builder.build());
    }

    public Issuer generateIssuer(User user) {
        KeyPair kp = generateKeyPair();
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, user.getCommonName());
        builder.addRDN(BCStyle.SURNAME, user.getSurname());
        builder.addRDN(BCStyle.GIVENNAME, user.getGivenName());
        builder.addRDN(BCStyle.O, user.getOrganization());
        builder.addRDN(BCStyle.OU, user.getOrganizationalUnit());
        builder.addRDN(BCStyle.C, user.getCountry());
        builder.addRDN(BCStyle.E, user.getEmail());
        //UID (USER ID) je ID korisnika
        builder.addRDN(BCStyle.UID, user.getUserId());

        //Kreiraju se podaci za issuer-a, sto u ovom slucaju ukljucuje:
        // - privatni kljuc koji ce se koristiti da potpise sertifikat koji se izdaje
        // - podatke o vlasniku sertifikata koji izdaje nov sertifikat
        return new Issuer(kp.getPrivate(), kp.getPublic(), builder.build());
    }

    private KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(2048, random);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Certificate getCertificate() {

        try {
            Issuer issuer = generateIssuer(new User());
            Subject subject = generateSubject(new User());

            //Datumi od kad do kad vazi sertifikat
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = sdf.parse("2023-03-25");
            Date endDate = sdf.parse("2028-03-25");

//            X509Certificate certificate = CertificateGenerator.generateCertificate(subject,
//                    issuer, startDate, endDate, "1");

//            return new Certificate(subject, issuer,
//                    "1", startDate, endDate, certificate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }





}
