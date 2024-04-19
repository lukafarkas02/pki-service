package com.example.PKI.models;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String userId;
    private String commonName;
    private String surname;
    private String givenName;
    private String organization;
    private String organizationalUnit;
    private String country;
    private String email;
    private String alias;
    private String publicKey;
}
