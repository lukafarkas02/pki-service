package com.example.PKI.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
@Getter
@Setter
@Data
public class CertificatePostDTO {
    public String certificateType;
    public Date notBefore;
    public String issuer;
    public ArrayList<String> keyUsage;
    public ArrayList<String> extendedKeyUsage;

    public String commonName;
    public String surname;
    public String givenName;
    public String organization;
    public String organizationUnit;
    public String country;
    public String email;

}
