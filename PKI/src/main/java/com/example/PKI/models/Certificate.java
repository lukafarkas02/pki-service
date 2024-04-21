package com.example.PKI.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Certificate {
//    private String issuerSerialNumber;
//    private CertificateStatus status;


    private Issuer issuer;
    private Subject subject;
    private String serialNumber;
    private Date startDate;
    private CertificateType type;
    private Date endDate;
    private String alias;

    // svi prethodni podaci mogu da se izvuku i iz X509Certificate, osim privatnog kljuca issuera
    private X509Certificate x509Certificate;


    public Certificate(Issuer issuer, Subject subject, String serialNumber, Date startDate, Date endDate, String alias, X509Certificate certificate) {
        this.issuer = issuer;
        this.subject = subject;
        this.serialNumber = serialNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.alias = alias;
        this.x509Certificate = certificate;
    }

}
