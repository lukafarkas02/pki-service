package com.example.PKI;

import com.example.PKI.models.User;
import com.example.PKI.repositories.KeyStoreRepository;

import com.example.PKI.services.KeyStoreService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.math.BigInteger;
import java.security.cert.Certificate;

@SpringBootApplication
public class PkiApplication {

//	private static ApplicationContext context;
	public static void main(String[] args) {
		SpringApplication.run(PkiApplication.class, args);
//		KeyStoreRepository keyStoreRepository = new KeyStoreRepository();
//		keyStoreRepository.createAndLoadNewKeyStore();
//		bezbednost alias pkiservice
//		Certificate loadedCertificate = keyStoreRepository.readCertificate("src/main/resources/keystores/novifajl.jks", "newpassword", "cert1");
//		System.out.println(loadedCertificate.getPublicKey());

//		KeyStoreService keyStoreService = new KeyStoreService();
//		BigInteger big = keyStoreService.getSerialNumber("src/main/resources/keystores/novifajl.jks", "newpassword", "cert1");
//		System.out.println("Serijski broj: " + big);

//		User user1 = new User("123", "John Doe", "Doe", "John",
//				"Example Org", "IT Department", "US",
//				"john.doe@example.com", "johnny", "abc123xyz");
//
//		// Kreiranje drugog korisnika
//		User user2 = new User("456", "Jane Smith", "Smith", "Jane",
//				"Another Org", "HR Department", "CA",
//				"jane.smith@example.com", "janes", "def456uvw");
//		KeyStoreRepository certificateRepository = new KeyStoreRepository();
		//TREBAM GENERISATI PRIVATE KEY-JEVE I PUB
//		X509Certificate certificate = newCertificate.getX509Certificate();
//
//		keyStoreRepository.saveCertificate(user1, user2);
//		private static KeyStoreWriter keyStoreWriter;
//
//		private static ApplicationContext context;
//
//		public static void main(String[] args) {
//			context = SpringApplication.run(ExampleApplication.class, args);
//
//			keyStoreReader = (KeyStoreReader) context.getBean("keyStoreReader");
//			keyStoreWriter = (KeyStoreWriter) context.getBean("keyStoreWriter");
//			certExample = (CertificateExample) context.getBean("certificateExample");
//
//			com.pki.example.data.Certificate certificate = certExample.getCertificate();
//			System.out.println("Novi sertifikat:");
//			System.out.println(certificate.getX509Certificate());
//
//			// Inicijalizacija fajla za cuvanje sertifikata
//			System.out.println("Cuvanje certifikata u jks fajl:");
//			keyStoreWriter.loadKeyStore("src/main/resources/static/example.jks",  "password".toCharArray());
//			PrivateKey pk = certificate.getIssuer().getPrivateKey();
//			keyStoreWriter.write("cert1", pk, "password".toCharArray(), certificate.getX509Certificate());
//			keyStoreWriter.saveKeyStore("src/main/resources/static/example.jks",  "password".toCharArray());
//			System.out.println("Cuvanje certifikata u jks fajl zavrseno.");
	}

}
