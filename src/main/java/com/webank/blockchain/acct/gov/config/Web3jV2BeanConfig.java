/**
 * Copyright 2014-2019 the original author or authors.
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
package com.webank.blockchain.acct.gov.config;

import com.google.common.collect.Lists;
import com.webank.blockchain.acct.gov.constant.AccountConstants;
import com.webank.blockchain.acct.gov.constant.GasConstants;
import com.webank.blockchain.acct.gov.contract.AccountManager;
import com.webank.blockchain.acct.gov.contract.WEGovernance;
import com.webank.blockchain.acct.gov.exception.InvalidParamException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.fisco.bcos.channel.client.Service;
import org.fisco.bcos.channel.handler.ChannelConnections;
import org.fisco.bcos.channel.handler.GroupChannelConnectionsConfig;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.crypto.ECKeyPair;
import org.fisco.bcos.web3j.crypto.EncryptType;
import org.fisco.bcos.web3j.crypto.gm.GenCredential;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.channel.ChannelEthereumService;
import org.fisco.bcos.web3j.tx.gas.ContractGasProvider;
import org.fisco.bcos.web3j.tx.gas.StaticGasProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * Web3jV2BeanConfig @Description: Web3jV2BeanConfig
 *
 * @author maojiayu
 * @data Apr 16, 2019 11:13:23 AM
 */
@Slf4j
@Configuration
@Order(2)
public class Web3jV2BeanConfig {

    @Autowired private SystemEnvironmentConfig systemEnvironmentConfig;

    @Bean
    public Web3j getWeb3j() throws Exception {
        ChannelEthereumService channelEthereumService = new ChannelEthereumService();
        Service service = getService();
        service.run();
        channelEthereumService.setChannelService(service);
        // default sync transactions timeout: 30s
        channelEthereumService.setTimeout(30000);
        return Web3j.build(channelEthereumService, service.getGroupId());
    }

    @Bean
    public Service getService() {
        GroupChannelConnectionsConfig groupChannelConnectionsConfig = getGroupChannelConnections();
        Service channelService = new Service();
        channelService.setOrgID(systemEnvironmentConfig.getOrgId());
        channelService.setGroupId(systemEnvironmentConfig.getGroupId());
        channelService.setAllChannelConnections(groupChannelConnectionsConfig);
        // set some default connect timeout seconds
        channelService.setConnectSeconds(60);
        channelService.setConnectSleepPerMillis(30);

        return channelService;
    }

    @Bean
    public GroupChannelConnectionsConfig getGroupChannelConnections() {
        GroupChannelConnectionsConfig groupChannelConnectionsConfig =
                new GroupChannelConnectionsConfig();
        ChannelConnections con = new ChannelConnections();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource ca = resolver.getResource("ca.crt");
        Resource nodeCrt = resolver.getResource("node.crt");
        Resource nodeKey = resolver.getResource("node.key");
        groupChannelConnectionsConfig.setCaCert(ca);
        groupChannelConnectionsConfig.setSslCert(nodeCrt);
        groupChannelConnectionsConfig.setSslKey(nodeKey);
        ArrayList<String> list = new ArrayList<>();
        List<ChannelConnections> allChannelConnections = new ArrayList<>();
        String[] nodes = StringUtils.split(systemEnvironmentConfig.getNodeStr(), ";");
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i].contains("@")) {
                nodes[i] = StringUtils.substringAfter(nodes[i], "@");
            }
        }
        List<String> nodesList = Lists.newArrayList(nodes);
        list.addAll(nodesList);
        list.stream()
                .forEach(
                        s -> {
                            log.info("connect address: {}", s);
                        });
        con.setConnectionsStr(list);
        con.setGroupId(systemEnvironmentConfig.getGroupId());
        allChannelConnections.add(con);
        groupChannelConnectionsConfig.setAllChannelConnections(allChannelConnections);
        return groupChannelConnectionsConfig;
    }

    @Bean
    public EncryptType getEncryptType() {
        return new EncryptType(systemEnvironmentConfig.getEncryptType());
    }

    @Bean
    public ContractGasProvider getContractGasProvider() {
        return new StaticGasProvider(GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT);
    }

    @Bean
    public Credentials getCredentials() throws Exception {
        String privateKey = systemEnvironmentConfig.getPrivateKey();
        if (StringUtils.isBlank(privateKey)
                || !StringUtils.startsWithIgnoreCase(privateKey, "0x")) {
            throw new InvalidParamException("Invalid private key format! " + privateKey);
        }
        privateKey = StringUtils.substring(privateKey, 2);
        new EncryptType(systemEnvironmentConfig.getEncryptType());
        ECKeyPair keyPair = GenCredential.createKeyPair(privateKey);
        return Credentials.create(keyPair);
    }

    @Bean
    public WEGovernance getGovernance() throws Exception {
        System.out.println(getCredentials().getAddress());
        System.out.println(getCredentials().getAddress());
        WEGovernance governance =
                WEGovernance.deploy(
                                getWeb3j(),
                                getCredentials(),
                                getContractGasProvider(),
                                AccountConstants.ADMIN_MODE)
                        .send();
        log.info("Governance acct create succeed {} ", governance.getContractAddress());
        return governance;
    }

    @Bean
    public AccountManager getAccountManager() throws Exception {
        System.out.println(getCredentials().getAddress());
        String address = getGovernance().getAccountManager().send();
        log.info("AccountManager address is {}", address);
        return AccountManager.load(address, getWeb3j(), getCredentials(), getContractGasProvider());
    }
}
