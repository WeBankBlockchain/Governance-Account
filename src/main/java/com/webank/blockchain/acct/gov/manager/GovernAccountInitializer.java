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
package com.webank.blockchain.acct.gov.manager;

import com.webank.blockchain.acct.gov.contract.AccountManager;
import com.webank.blockchain.acct.gov.contract.AdminGovernFacade;
import com.webank.blockchain.acct.gov.contract.VoteGovernFacade;
import com.webank.blockchain.acct.gov.contract.WEGovernance;
import com.webank.blockchain.acct.gov.contract.WeightVoteGovernFacade;
import com.webank.blockchain.acct.gov.exception.InvalidParamException;
import java.math.BigInteger;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.fisco.bcos.web3j.crypto.Credentials;
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

    public WEGovernance createGovernAccount(Credentials credential) throws Exception {
        AdminGovernFacade facade =
                AdminGovernFacade.deploy(web3j, credential, contractGasProvider).send();
        String governanceAddress = facade._governance().send();
        WEGovernance governance =
                WEGovernance.load(governanceAddress, web3j, credential, contractGasProvider);
        log.info("Governance acct create succeed {} ", governance.getContractAddress());
        this.governance = governance;
        String accountManagerAddress = governance.getAccountManager().send();
        log.info("Account manager address is {}", accountManagerAddress);
        AccountManager accountManager =
                AccountManager.load(accountManagerAddress, web3j, credentials, contractGasProvider);
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
        VoteGovernFacade facade =
                VoteGovernFacade.deploy(
                                web3j,
                                credentials,
                                contractGasProvider,
                                externalAccountList,
                                BigInteger.valueOf(threshold))
                        .send();
        String governanceAddress = facade._governance().send();
        WEGovernance governance =
                WEGovernance.load(governanceAddress, web3j, credentials, contractGasProvider);
        log.info("Governance acct create succeed {} ", governance.getContractAddress());
        this.governance = governance;
        String accountManagerAddress = governance.getAccountManager().send();
        log.info("Account manager address is {}", accountManagerAddress);
        AccountManager accountManager =
                AccountManager.load(accountManagerAddress, web3j, credentials, contractGasProvider);
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
        WeightVoteGovernFacade facade =
                WeightVoteGovernFacade.deploy(
                                web3j,
                                credentials,
                                contractGasProvider,
                                externalAccountList,
                                weights,
                                BigInteger.valueOf(threshold))
                        .send();
        String governanceAddress = facade._governance().send();
        WEGovernance governance =
                WEGovernance.load(governanceAddress, web3j, credentials, contractGasProvider);
        log.info("Governance acct create succeed {} ", governance.getContractAddress());
        this.governance = governance;
        String accountManagerAddress = governance.getAccountManager().send();
        log.info("Account manager address is {}", accountManagerAddress);
        AccountManager accountManager =
                AccountManager.load(accountManagerAddress, web3j, credentials, contractGasProvider);
        log.info("Account manager created: {}", accountManager.getContractAddress());
        this.accountManager = accountManager;
        return governance;
    }
}
