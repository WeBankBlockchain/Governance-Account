package com.webank.blockchain.gov.acct;

import javax.annotation.PostConstruct;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
        u = credentials.generateKeyPair();
        u1 = credentials.generateKeyPair();
        u2 = credentials.generateKeyPair();
        p1 = credentials.generateKeyPair();
        p2 = credentials.generateKeyPair();
        p3 = credentials.generateKeyPair();
    }
}
