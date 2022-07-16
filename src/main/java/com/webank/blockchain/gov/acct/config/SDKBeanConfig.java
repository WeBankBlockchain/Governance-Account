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
package com.webank.blockchain.gov.acct.config;

import com.webank.blockchain.gov.acct.constant.AccountConstants;
import com.webank.blockchain.gov.acct.contract.AccountManager;
import com.webank.blockchain.gov.acct.contract.WEGovernance;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.fisco.bcos.sdk.v3.BcosSDK;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.config.exceptions.ConfigException;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wesleywang @Description:
 * @date 2020/11/9
 */
@Slf4j
@Configuration
public class SDKBeanConfig {

    @Autowired private SystemEnvironmentConfig systemEnvironmentConfig;

    @Bean
    public CryptoKeyPair cryptoKeyPair(Client client) throws ConfigException {
        if (StringUtils.isNotBlank(systemEnvironmentConfig.getHexPrivateKey())) {
            log.info("Found hex private key in application.properties.");
            return client.getCryptoSuite().loadKeyPair(systemEnvironmentConfig.getHexPrivateKey());
        }
        log.info("Hex private key not found.");
        CryptoKeyPair cryptoKeyPair = client.getCryptoSuite().generateRandomKeyPair();
        log.info("Generate the default private key: {}", cryptoKeyPair.getHexPrivateKey());
        log.info("The default address is {}", cryptoKeyPair.getAddress());
        return cryptoKeyPair;
    }

    @Bean
    public Client getClient(BcosSDK bcosSDK) throws ConfigException {
        Client c = bcosSDK.getClient();
        return c;
    }

    @Bean
    public BcosSDK getSDK() throws ConfigException {
        return BcosSDK.build(systemEnvironmentConfig.getConfigPath());
    }

    @Bean
    @ConditionalOnProperty(name = "system.defaultGovernanceEnabled", havingValue = "true")
    public WEGovernance getGovernance(
            @Autowired Client client, @Autowired CryptoKeyPair cryptoKeyPair) throws Exception {
        WEGovernance governance =
                WEGovernance.deploy(client, cryptoKeyPair, AccountConstants.ADMIN_MODE);
        log.info("Default governance acct create succeed {} ", governance.getContractAddress());
        return governance;
    }

    @Bean
    @ConditionalOnProperty(name = "system.defaultGovernanceEnabled", havingValue = "true")
    public AccountManager getAccountManager(
            @Autowired WEGovernance weGovernance,
            @Autowired Client client,
            @Autowired CryptoKeyPair cryptoKeyPair)
            throws Exception {
        String address = weGovernance.getAccountManager();
        log.info("Default accountManager address is {}", address);
        return AccountManager.load(address, client, cryptoKeyPair);
    }
}
