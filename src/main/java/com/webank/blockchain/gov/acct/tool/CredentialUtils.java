package com.webank.blockchain.gov.acct.tool;

import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.interfaces.ECPrivateKey;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.crypto.keypair.ECDSAKeyPair;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

@Slf4j
public class CredentialUtils {

    public static CryptoKeyPair loadKey(
            String keyStoreFileName, String keyStorePassword, String keyPassword) throws Exception {
        KeyStore ks = KeyStore.getInstance("JKS");
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource cdResource = resolver.getResource(keyStoreFileName);
        try (InputStream stream = cdResource.getInputStream()) {
            ks.load(stream, keyStorePassword.toCharArray());
            Key key = ks.getKey("ec", keyPassword.toCharArray());
            CryptoKeyPair credentials = new ECDSAKeyPair().createKeyPair(((ECPrivateKey) key).getS());
            if (credentials != null) {
                return credentials;
            } else {
                log.error("秘钥参数输入有误！");
            }
        }
        return null;
    }
}
