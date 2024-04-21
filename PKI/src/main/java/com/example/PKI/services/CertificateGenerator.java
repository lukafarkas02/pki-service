package com.example.PKI.services;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.example.PKI.models.Certificate;
import com.example.PKI.models.Issuer;
import com.example.PKI.models.Subject;
import com.example.PKI.models.User;
import com.example.PKI.utils.KeyStoreUtils;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.stereotype.Component;
import java.util.Calendar;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class CertificateGenerator {
        public CertificateGenerator() {
            Security.addProvider(new BouncyCastleProvider());
        }

        public static X509Certificate generateCertificate(Subject subject, Issuer issuer, Date startDate, Date endDate, BigInteger serialNumber) {
            try {
                //Posto klasa za generisanje sertifiakta ne moze da primi direktno privatni kljuc pravi se builder za objekat
                //Ovaj objekat sadrzi privatni kljuc izdavaoca sertifikata i koristiti se za potpisivanje sertifikata
                //Parametar koji se prosledjuje je algoritam koji se koristi za potpisivanje sertifiakta
                JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");
                //Takodje se navodi koji provider se koristi, u ovom slucaju Bouncy Castle
                builder = builder.setProvider("BC");

                //Formira se objekat koji ce sadrzati privatni kljuc i koji ce se koristiti za potpisivanje sertifikata
                ContentSigner contentSigner = builder.build(issuer.getPrivateKey());

                //Postavljaju se podaci za generisanje sertifiakta
                X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(issuer.getX500Name(),
                        serialNumber,
                        startDate,
                        endDate,
                        subject.getX500Name(),
                        subject.getPublicKey());

                //Generise se sertifikat
                X509CertificateHolder certHolder = certGen.build(contentSigner);

                //Builder generise sertifikat kao objekat klase X509CertificateHolder
                //Nakon toga je potrebno certHolder konvertovati u sertifikat, za sta se koristi certConverter
                JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
                certConverter = certConverter.setProvider("BC");

                //Konvertuje objekat u sertifikat
                return certConverter.getCertificate(certHolder);

            } catch (CertificateEncodingException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (OperatorCreationException e) {
                e.printStackTrace();
            } catch (CertificateException e) {
                e.printStackTrace();
            }
            return null;
        }


    public static Subject generateSubject(User user) {
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

    public static Issuer generateIssuer(User user) {
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

    public static KeyPair generateKeyPair() {
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

    public static com.example.PKI.models.Certificate getCertificate(User user1, User user2) {
        Issuer issuer = generateIssuer(user1);
        Subject subject = generateSubject(user2);
        Date startDate = new Date();
        //ako je root 20 ali na to se vratiti posle
        Date endDate = addYearsToDate(startDate, 10);
        BigInteger serialNumber = KeyStoreUtils.generateSerialNumber();

        X509Certificate certificate = generateCertificate(subject,
                issuer, startDate, endDate, serialNumber);

        System.out.println(serialNumber.toString());

        return new Certificate(issuer, subject,
                serialNumber.toString(), startDate, endDate, "alias1", certificate);



    }




    //METHOD FOR ADDING NUMBER OF YEARS TO CURRENT DATE;
    public static Date addYearsToDate(Date date, int years) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, years);
        return calendar.getTime();
    }
}


