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

    protected CryptoKeyPair governanceUser1Keypair;
    protected CryptoKeyPair governanceUser2Keypair;
    protected CryptoKeyPair governanceUser3Keypair;
    protected CryptoKeyPair endUser1Keypair;
    protected CryptoKeyPair endUser2Keypair;
    protected CryptoKeyPair endUser3Keypair;

    @PostConstruct
    public void init() throws Exception {
        governanceUser1Keypair = credentials.generateKeyPair();
        governanceUser2Keypair = credentials.generateKeyPair();
        governanceUser3Keypair = credentials.generateKeyPair();
        endUser1Keypair = credentials.generateKeyPair();
        endUser2Keypair = credentials.generateKeyPair();
        endUser3Keypair = credentials.generateKeyPair();
    }
}
