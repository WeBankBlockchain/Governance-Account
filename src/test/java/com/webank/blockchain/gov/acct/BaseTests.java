package com.webank.blockchain.gov.acct;

import com.webank.blockchain.gov.acct.config.SystemEnvironmentConfig;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.PostConstruct;

@SpringBootTest
public class BaseTests {
    @Autowired protected Client client;
//    @Autowired private SystemEnvironmentConfig systemEnvironmentConfig;
    @Autowired protected CryptoKeyPair credentials;


    protected CryptoKeyPair u;
    protected CryptoKeyPair u1;
    protected CryptoKeyPair u2;
    protected CryptoKeyPair p1;
    protected CryptoKeyPair p2;
    protected CryptoKeyPair p3;


    @PostConstruct
    public void init() throws Exception {
        u =  credentials.generateKeyPair();
        u1 =  credentials.generateKeyPair();
        u2 =  credentials.generateKeyPair();
        p1 =  credentials.generateKeyPair();
        p2 =  credentials.generateKeyPair();
        p3 =  credentials.generateKeyPair();
//            u = CredentialUtils.loadKey("user.jks", "123456", "123456");
//            u1 = CredentialUtils.loadKey("user1.jks", "123456", "123456");
//            u2 = CredentialUtils.loadKey("user2.jks", "123456", "123456");
//            p1 = CredentialUtils.loadKey("p1.jks", "123456", "123456");
//            p2 = CredentialUtils.loadKey("p2.jks", "123456", "123456");
//            p3 = CredentialUtils.loadKey("p3.jks", "123456", "123456");
//            u = new SM2KeyPair().generateKeyPair();
//            u1 = new SM2KeyPair().generateKeyPair();
//            u2 = new SM2KeyPair().generateKeyPair();
//            p1 = new SM2KeyPair().generateKeyPair();
//            p2 = new SM2KeyPair().generateKeyPair();
//            p3 = new SM2KeyPair().generateKeyPair();

    }
}
