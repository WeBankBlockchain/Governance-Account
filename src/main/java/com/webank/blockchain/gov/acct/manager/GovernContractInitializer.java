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
package com.webank.blockchain.gov.acct.manager;

import com.webank.blockchain.gov.acct.contract.AccountManager;
import com.webank.blockchain.gov.acct.contract.AdminGovernBuilder;
import com.webank.blockchain.gov.acct.contract.VoteGovernBuilder;
import com.webank.blockchain.gov.acct.contract.WEGovernance;
import com.webank.blockchain.gov.acct.contract.WeightVoteGovernBuilder;
import com.webank.blockchain.gov.acct.exception.InvalidParamException;
import com.webank.blockchain.gov.acct.vo.GovernAccountGroup;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * GovernAdminManager @Description: GovernAdminManager
 *
 * @author maojiayu
 * @data Feb 21, 2020 7:37:52 PM
 */
@Service
@Slf4j
public class GovernContractInitializer extends BasicManager {

    public GovernContractInitializer() {
        super();
    }

    public GovernContractInitializer(Client client, CryptoKeyPair credentials) {
        super.client = client;
        super.credentials = credentials;
    }

    public WEGovernance createGovernAccount(CryptoKeyPair credential) throws Exception {
        AdminGovernBuilder Builder = AdminGovernBuilder.deploy(client, credential);
        String governanceAddress = Builder._governance();
        WEGovernance governance = WEGovernance.load(governanceAddress, client, credential);
        log.info("Governance account create succeed [ {} ] ", governance.getContractAddress());
        this.governance = governance;
        String accountManagerAddress = governance.getAccountManager();
        AccountManager accountManager =
                AccountManager.load(accountManagerAddress, client, credentials);
        log.info("Account manager created: [ {} ]", accountManager.getContractAddress());
        this.accountManager = accountManager;
        String userAddr = createAccount(credential.getAddress());
        log.info("User account created is [ {} ]", userAddr);
        return governance;
    }

    public WEGovernance createGovernAccount(GovernAccountGroup governAccountGroup)
            throws Exception {
        if (CollectionUtils.isEmpty(governAccountGroup.getGovernUserList())) {
            log.error("user list can't be empty.");
            throw new InvalidParamException("user list can't be empty.");
        }
        List<String> externalAccountList =
                governAccountGroup
                        .getGovernUserList()
                        .stream()
                        .map(u -> u.getExternalAccount())
                        .collect(Collectors.toList());
        List<BigInteger> weights =
                governAccountGroup
                        .getGovernUserList()
                        .stream()
                        .map(u -> (long) u.getWeight())
                        .map(i -> BigInteger.valueOf(i))
                        .collect(Collectors.toList());
        for (BigInteger b : weights) {
            if (!b.equals(1)) {
                return createGovernAccount(
                        externalAccountList, weights, governAccountGroup.getThreshold());
            }
        }
        return createGovernAccount(externalAccountList, governAccountGroup.getThreshold());
    }

    public WEGovernance createGovernAccount(List<String> externalAccountList, int threshold)
            throws Exception {
        if (CollectionUtils.isEmpty(externalAccountList)) {
            log.error("externalAccountList can't be empty.");
            throw new InvalidParamException("externalAccountList can't be empty.");
        }
        VoteGovernBuilder Builder =
                VoteGovernBuilder.deploy(
                        client, credentials, externalAccountList, BigInteger.valueOf(threshold));
        String governanceAddress = Builder._governance();
        WEGovernance governance = WEGovernance.load(governanceAddress, client, credentials);
        log.info("Governance account create succeed [ {} ] ", governance.getContractAddress());
        this.governance = governance;
        String accountManagerAddress = governance.getAccountManager();
        log.info("Account manager address is [ {} ]", accountManagerAddress);
        AccountManager accountManager =
                AccountManager.load(accountManagerAddress, client, credentials);
        log.info("Account manager created: [ {} ]", accountManager.getContractAddress());
        this.accountManager = accountManager;
        return governance;
    }

    public WEGovernance createGovernAccount(
            List<String> externalAccountList, List<BigInteger> weights, int threshold)
            throws Exception {
        if (CollectionUtils.isEmpty(externalAccountList) || CollectionUtils.isEmpty(weights)) {
            log.error("externalAccountList or weights can't be empty.");
            throw new InvalidParamException("externalAccountList or weights can't be empty.");
        }
        WeightVoteGovernBuilder Builder =
                WeightVoteGovernBuilder.deploy(
                        client,
                        credentials,
                        externalAccountList,
                        weights,
                        BigInteger.valueOf(threshold));
        String governanceAddress = Builder._governance();
        WEGovernance governance = WEGovernance.load(governanceAddress, client, credentials);
        log.info("Governance account create succeed [ {} ] ", governance.getContractAddress());
        this.governance = governance;
        String accountManagerAddress = governance.getAccountManager();
        log.info("Account manager address is [ {} ]", accountManagerAddress);
        AccountManager accountManager =
                AccountManager.load(accountManagerAddress, client, credentials);
        this.accountManager = accountManager;
        return governance;
    }
}
