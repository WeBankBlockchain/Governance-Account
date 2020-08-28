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
package com.webank.blockchain.acct.gov.factory;

import com.webank.blockchain.acct.gov.contract.AccountManager;
import com.webank.blockchain.acct.gov.contract.WEGovernance;
import com.webank.blockchain.acct.gov.exception.InvalidParamException;
import com.webank.blockchain.acct.gov.manager.AdminModeGovernManager;
import com.webank.blockchain.acct.gov.manager.EndUserOperManager;
import com.webank.blockchain.acct.gov.manager.GovernAccountInitializer;
import com.webank.blockchain.acct.gov.manager.SocialVoteManager;
import com.webank.blockchain.acct.gov.manager.VoteModeGovernManager;
import lombok.Data;
import lombok.experimental.Accessors;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.crypto.EncryptType;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.tx.gas.ContractGasProvider;

/**
 * AccountGovernManagerFactory @Description: AccountGovernManagerFactory
 *
 * @author maojiayu
 * @data Mar 10, 2020 3:08:53 PM
 */
@Data
@Accessors(chain = true)
public class AccountGovernManagerFactory {
    protected Web3j web3j;
    protected Credentials credentials;
    protected ContractGasProvider contractGasProvider;
    protected WEGovernance governance;
    protected AccountManager accountManager;

    public AccountGovernManagerFactory(
            Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        this(web3j, credentials, EncryptType.ECDSA_TYPE, contractGasProvider);
    }

    public AccountGovernManagerFactory(
            Web3j web3j,
            Credentials credentials,
            int encryptType,
            ContractGasProvider contractGasProvider) {
        this.web3j = web3j;
        this.credentials = credentials;
        EncryptType.encryptType = encryptType;
        this.contractGasProvider = contractGasProvider;
    }

    public AccountGovernManagerFactory(
            Web3j web3j,
            Credentials credentials,
            ContractGasProvider contractGasProvider,
            String governanceAddress)
            throws Exception {
        this(web3j, credentials, EncryptType.ECDSA_TYPE, contractGasProvider, governanceAddress);
    }

    public AccountGovernManagerFactory(
            Web3j web3j,
            Credentials credentials,
            int encryptType,
            ContractGasProvider contractGasProvider,
            String governanceAddress)
            throws Exception {
        this.web3j = web3j;
        this.credentials = credentials;
        EncryptType.encryptType = encryptType;
        this.contractGasProvider = contractGasProvider;
        this.governance =
                WEGovernance.load(governanceAddress, web3j, credentials, contractGasProvider);
        String acctManagerAddress = governance.getAccountManager().send();
        this.accountManager =
                AccountManager.load(acctManagerAddress, web3j, credentials, contractGasProvider);
    }

    public GovernAccountInitializer newGovernAccountInitializer() {
        GovernAccountInitializer manager = new GovernAccountInitializer();
        manager.setWeb3j(web3j)
                .setCredentials(credentials)
                .setContractGasProvider(contractGasProvider);
        return manager;
    }

    public AdminModeGovernManager newAdminModeGovernManager() {
        AdminModeGovernManager manager = new AdminModeGovernManager();
        if (governance == null || accountManager == null) {
            throw new InvalidParamException(
                    "Can't find the governance account or the account manager!");
        }
        manager.setGovernance(governance)
                .setAccountManager(accountManager)
                .setWeb3j(web3j)
                .setCredentials(credentials)
                .setContractGasProvider(contractGasProvider);
        return manager;
    }

    public EndUserOperManager newEndUserOperManager() {
        EndUserOperManager manager = new EndUserOperManager();
        if (governance == null || accountManager == null) {
            throw new InvalidParamException(
                    "Can't find the governance account or the account manager!");
        }
        manager.setGovernance(governance)
                .setAccountManager(accountManager)
                .setWeb3j(web3j)
                .setCredentials(credentials)
                .setContractGasProvider(contractGasProvider);
        return manager;
    }

    public SocialVoteManager newSocialVoteManager() {
        SocialVoteManager manager = new SocialVoteManager();
        if (governance == null || accountManager == null) {
            throw new InvalidParamException(
                    "Can't find the governance account or the account manager!");
        }
        manager.setGovernance(governance)
                .setAccountManager(accountManager)
                .setWeb3j(web3j)
                .setCredentials(credentials)
                .setContractGasProvider(contractGasProvider);
        return manager;
    }

    public VoteModeGovernManager newVoteModeGovernManager() {
        VoteModeGovernManager manager = new VoteModeGovernManager();
        if (governance == null || accountManager == null) {
            throw new InvalidParamException(
                    "Can't find the governance account or the account manager!");
        }
        manager.setGovernance(governance)
                .setAccountManager(accountManager)
                .setWeb3j(web3j)
                .setCredentials(credentials)
                .setContractGasProvider(contractGasProvider);
        return manager;
    }
}
