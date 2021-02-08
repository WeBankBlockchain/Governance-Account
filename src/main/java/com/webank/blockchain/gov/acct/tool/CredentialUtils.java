/**
 * Copyright 2020 Webank.
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
            CryptoKeyPair credentials =
                    new ECDSAKeyPair().createKeyPair(((ECPrivateKey) key).getS());
            if (credentials != null) {
                return credentials;
            } else {
                log.error("秘钥参数输入有误！");
            }
        }
        return null;
    }
}
