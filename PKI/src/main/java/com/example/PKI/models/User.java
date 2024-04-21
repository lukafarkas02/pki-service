package com.example.PKI.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name ="users")
//kasnije entitet
public class User {
    @Id
    @Column(name = "id")
    private String userId;
    @Column(name = "commonName", unique = false)
    private String commonName;
    @Column(name = "surname", unique = false)
    private String surname;
    @Column(name = "givenName", unique = false)
    private String givenName;
    @Column(name = "organization", unique = false)
    private String organization;
    @Column(name = "organizationUnit", unique = false)
    private String organizationalUnit;
    @Column(name = "country", unique = false)
    private String country;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "password", unique = true)
    private String password;

//    private String userId;
//    private String commonName;
//    private String surname;
//    private String givenName;
//    private String organization;
//    private String organizationalUnit;
//    private String country;
//    private String email;
//    private String alias;
//    private String publicKey;
//    private List<String> serialNumber;
    //po public key trazim cert?
}
