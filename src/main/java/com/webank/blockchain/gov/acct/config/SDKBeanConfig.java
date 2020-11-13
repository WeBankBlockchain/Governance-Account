package com.webank.blockchain.gov.acct.config;

import com.google.common.collect.Maps;
import com.webank.blockchain.gov.acct.constant.AccountConstants;
import com.webank.blockchain.gov.acct.contract.AccountManager;
import com.webank.blockchain.gov.acct.contract.WEGovernance;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.config.ConfigOption;
import org.fisco.bcos.sdk.config.exceptions.ConfigException;
import org.fisco.bcos.sdk.config.model.ConfigProperty;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.springframework.beans.factory.annotation.Autowired;
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
    public CryptoKeyPair cryptoKeyPair() throws ConfigException {
        Client client = getClient();
        return client.getCryptoSuite().createKeyPair();
    }

    @Bean
    public Client getClient() throws ConfigException {
        BcosSDK sdk = getSDK();
        return sdk.getClient(systemEnvironmentConfig.getGroupId());
    }

    @Bean
    public BcosSDK getSDK() throws ConfigException {
        ConfigProperty configProperty = new ConfigProperty();
        setPeers(configProperty);
        setCertPath(configProperty);
        ConfigOption option =
                new ConfigOption(configProperty, systemEnvironmentConfig.getEncryptType());
        log.info("Is gm {}", systemEnvironmentConfig.getEncryptType() == 1);
        return new BcosSDK(option);
    }

    public void setPeers(ConfigProperty configProperty) {
        String[] nodes = StringUtils.split(systemEnvironmentConfig.getNodeStr(), ";");
        List<String> peers = Arrays.asList(nodes);
        Map<String, Object> network = Maps.newHashMapWithExpectedSize(1);
        network.put("peers", peers);
        configProperty.setNetwork(network);
    }

    public void setCertPath(ConfigProperty configProperty) {
        Map<String, Object> cryptoMaterial = Maps.newHashMapWithExpectedSize(1);
        cryptoMaterial.put("certPath", "config");
        configProperty.setCryptoMaterial(cryptoMaterial);
    }

    @Bean
    public WEGovernance getGovernance(
            @Autowired Client client, @Autowired CryptoKeyPair cryptoKeyPair) throws Exception {
        WEGovernance governance =
                WEGovernance.deploy(client, cryptoKeyPair, AccountConstants.ADMIN_MODE);
        log.info("Governance acct create succeed {} ", governance.getContractAddress());
        return governance;
    }

    @Bean
    public AccountManager getAccountManager(
            @Autowired WEGovernance weGovernance,
            @Autowired Client client,
            @Autowired CryptoKeyPair cryptoKeyPair)
            throws Exception {
        String address = weGovernance.getAccountManager();
        log.info("AccountManager address is {}", address);
        return AccountManager.load(address, client, cryptoKeyPair);
    }
}
