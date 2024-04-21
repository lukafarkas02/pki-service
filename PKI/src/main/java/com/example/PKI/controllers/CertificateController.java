package com.example.PKI.controllers;

import com.example.PKI.services.KeyStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.PKI.dtos.CertificatePostDTO;
import java.security.cert.X509Certificate;


@RequestMapping(value = "/certificates")
@Controller
@CrossOrigin(origins = "http://localhost:4200")
public class CertificateController {
    
    @Autowired
    public KeyStoreService keyStoreService;
    
    

    @PostMapping(value = "/createCertificate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity selfSignedGenerateCertificate(@RequestBody CertificatePostDTO certificateParamsDTO) {

        X509Certificate certificate = null;
        switch (certificateParamsDTO.certificateType){

            case "selfSigned":
                certificate = keyStoreService.generateRootCertificate(certificateParamsDTO);
                break;
            case "intermediary":
                certificate = keyStoreService.intermediaryCertificate(certificateParamsDTO);
                break;
            case "endEntity":
                certificate = keyStoreService.endEntityCertificate(certificateParamsDTO);
                break;
            default:
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        if(certificate != null) {
            System.out.print(certificate + "\n");
            return new ResponseEntity(HttpStatus.CREATED);
        }
        else {
            System.out.print("There was a problem Creating sertificate!\n");
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }
}
