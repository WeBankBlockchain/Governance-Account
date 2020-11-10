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
package com.webank.blockchain.gov.acct.manager;

import com.webank.blockchain.gov.acct.contract.AccountManager;
import com.webank.blockchain.gov.acct.contract.AdminGovernBuilder;
import com.webank.blockchain.gov.acct.contract.VoteGovernBuilder;
import com.webank.blockchain.gov.acct.contract.WEGovernance;
import com.webank.blockchain.gov.acct.contract.WeightVoteGovernBuilder;
import com.webank.blockchain.gov.acct.exception.InvalidParamException;
import java.math.BigInteger;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.springframework.stereotype.Service;

/**
 * GovernAdminManager @Description: GovernAdminManager
 *
 * @author maojiayu
 * @data Feb 21, 2020 7:37:52 PM
 */
@Service
@Slf4j
public class GovernAccountInitializer extends BasicManager {

    public WEGovernance createGovernAccount(CryptoKeyPair credential) throws Exception {
        AdminGovernBuilder Builder =
                AdminGovernBuilder.deploy(client, credential);
        String governanceAddress = Builder._governance();
        WEGovernance governance =
                WEGovernance.load(governanceAddress, client, credential);
        log.info("Governance acct create succeed {} ", governance.getContractAddress());
        this.governance = governance;
        String accountManagerAddress = governance.getAccountManager();
        log.info("Account manager address is {}", accountManagerAddress);
        AccountManager accountManager =
                AccountManager.load(accountManagerAddress, client, credentials);
        log.info("Account manager created: {}", accountManager.getContractAddress());
        this.accountManager = accountManager;
        String userAddr = createAccount(credential.getAddress());
        log.info("User account is {}", userAddr);
        return governance;
    }

    public WEGovernance createGovernAccount(List<String> externalAccountList, int threshold)
            throws Exception {
        if (CollectionUtils.isEmpty(externalAccountList)) {
            log.error("externalAccountList can't be empty.");
            throw new InvalidParamException("externalAccountList can't be empty.");
        }
        VoteGovernBuilder Builder =
                VoteGovernBuilder.deploy(
                                client,
                                credentials,
                                externalAccountList,
                                BigInteger.valueOf(threshold));
        String governanceAddress = Builder._governance();
        WEGovernance governance =
                WEGovernance.load(governanceAddress, client, credentials);
        log.info("Governance acct create succeed {} ", governance.getContractAddress());
        this.governance = governance;
        String accountManagerAddress = governance.getAccountManager();
        log.info("Account manager address is {}", accountManagerAddress);
        AccountManager accountManager =
                AccountManager.load(accountManagerAddress, client, credentials);
        log.info("Account manager created: {}", accountManager.getContractAddress());
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
        WEGovernance governance =
                WEGovernance.load(governanceAddress, client, credentials);
        log.info("Governance acct create succeed {} ", governance.getContractAddress());
        this.governance = governance;
        String accountManagerAddress = governance.getAccountManager();
        log.info("Account manager address is {}", accountManagerAddress);
        AccountManager accountManager =
                AccountManager.load(accountManagerAddress, client, credentials);
        log.info("Account manager created: {}", accountManager.getContractAddress());
        this.accountManager = accountManager;
        return governance;
    }
}
