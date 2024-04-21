package com.example.PKI.services;

import com.example.PKI.dtos.CertificatePostDTO;
import com.example.PKI.models.Certificate;
import com.example.PKI.models.Issuer;
import com.example.PKI.models.Subject;
import com.example.PKI.models.User;
import com.example.PKI.repositories.KeyStoreRepository;
import com.example.PKI.repositories.UserRepository;
import com.example.PKI.utils.KeyStoreUtils;
import lombok.Setter;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import static com.example.PKI.services.CertificateGenerator.*;


@Service
public class KeyStoreService {

    public KeyStoreRepository keyStoreRepository;

    public  CertificateGenerator certificateGenerator;

    @Autowired
    public UserRepository userRepository;



    public KeyStoreService() {

        keyStoreRepository = new KeyStoreRepository();


    }


    public X509Certificate generateRootCertificate(CertificatePostDTO certificatePostDTO) {
//        User subject = CertificateGenerator.generateSubject();
        User subject = userRepository.findUserByEmail(certificatePostDTO.email);
        if (subject == null) {
            //UPISEM GA U BAZU?
        }

        //klasa X500NameBuilder pravi X500Name objekat koji predstavlja podatke o vlasniku
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, certificatePostDTO.getCommonName());
        builder.addRDN(BCStyle.SURNAME, certificatePostDTO.getSurname());
        builder.addRDN(BCStyle.GIVENNAME, certificatePostDTO.getGivenName());
        builder.addRDN(BCStyle.O, certificatePostDTO.getOrganization());
        builder.addRDN(BCStyle.OU, certificatePostDTO.getOrganizationUnit());
        builder.addRDN(BCStyle.C, certificatePostDTO.getCountry());
        builder.addRDN(BCStyle.E, certificatePostDTO.getEmail());
        //UID (USER ID) je ID korisnika
//        builder.addRDN(BCStyle.UID, certificatePostDTO.getUserId());
//-------------------------------------------------
        KeyPair keyPairSubject = generateKeyPair();
        //AKO USER VEC POSTOJI U BAZI
        //ONDA NE MORAM DA MU GENERISEM PRIVATE KLJUC?
        /////////////////////////
        PrivateKey privateKey = keyPairSubject.getPrivate();

        //ISSUER I SUBJECT SAME FOR ROOT
        X500Name x500NameSubject = builder.build();
        X500Name x500NameIssuer = x500NameSubject;

        // KREIRANJE SERTIFIKATA
        X509Certificate certificate;

        Subject subject1 = new Subject(keyPairSubject.getPublic(), x500NameSubject);
        Issuer issuer = new Issuer(keyPairSubject.getPrivate(), keyPairSubject.getPublic(), x500NameIssuer);
        Date startDate = certificatePostDTO.notBefore;
        Date endDate = addYearsToDate(startDate, 20);
        BigInteger serialNumber = KeyStoreUtils.generateSerialNumber();
        certificate = generateCertificate(subject1, issuer, startDate, endDate, serialNumber);
//                certificateParamsDTO.keyUsage, certificateParamsDTO.extendedKeyUsage);
//        subjectUser.getCertificatesSerialNumbers().add(
//                certificate.getSerialNumber().toString()
//        );



        Certificate c = new Certificate(issuer, subject1,
                serialNumber.toString(), startDate, endDate, "root", certificate);

        HashMap<String, char[]> fileData = keyStoreRepository.createAndLoadNewKeyStore();
        keyStoreRepository.saveRootCertificate(certificate, c.getAlias(), fileData);


        return certificate;

    }











}
