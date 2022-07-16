package com.webank.blockchain.gov.acct;

import javax.annotation.PostConstruct;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BaseTests {
    @Autowired protected Client client;
    //    @Autowired private SystemEnvironmentConfig systemEnvironmentConfig;
    @Autowired protected CryptoKeyPair cryptoKeyPair;

    protected CryptoKeyPair governanceUser1Keypair;
    protected CryptoKeyPair governanceUser2Keypair;
    protected CryptoKeyPair governanceUser3Keypair;
    protected CryptoKeyPair endUser1Keypair;
    protected CryptoKeyPair endUser2Keypair;
    protected CryptoKeyPair endUser3Keypair;

    @PostConstruct
    public void init() throws Exception {
        // prepare 3 governance users & 3 end users.
        governanceUser1Keypair = cryptoKeyPair.generateKeyPair();
        governanceUser2Keypair = cryptoKeyPair.generateKeyPair();
        governanceUser3Keypair = cryptoKeyPair.generateKeyPair();
        endUser1Keypair = cryptoKeyPair.generateKeyPair();
        endUser2Keypair = cryptoKeyPair.generateKeyPair();
        endUser3Keypair = cryptoKeyPair.generateKeyPair();
    }
}
