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
package com.webank.blockchain.gov.acct.factory;

import com.webank.blockchain.gov.acct.contract.AccountManager;
import com.webank.blockchain.gov.acct.contract.WEGovernance;
import com.webank.blockchain.gov.acct.exception.InvalidParamException;
import com.webank.blockchain.gov.acct.manager.AdminModeGovernManager;
import com.webank.blockchain.gov.acct.manager.EndUserOperManager;
import com.webank.blockchain.gov.acct.manager.GovernContractInitializer;
import com.webank.blockchain.gov.acct.manager.SocialVoteManager;
import com.webank.blockchain.gov.acct.manager.VoteModeGovernManager;
import lombok.Data;
import lombok.experimental.Accessors;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;

/**
 * AccountGovernManagerFactory @Description: AccountGovernManagerFactory
 *
 * @author maojiayu
 * @data Mar 10, 2020 3:08:53 PM
 */
@Data
@Accessors(chain = true)
public class AccountGovernManagerFactory {
    protected Client client;
    protected CryptoKeyPair credentials;
    protected WEGovernance governance;
    protected AccountManager accountManager;

    public AccountGovernManagerFactory(Client client, CryptoKeyPair credentials) {
        this.client = client;
        this.credentials = credentials;
    }

    public AccountGovernManagerFactory(
            Client client, CryptoKeyPair credentials, String governanceAddress) throws Exception {
        this.client = client;
        this.credentials = credentials;
        this.governance = WEGovernance.load(governanceAddress, client, credentials);
        String acctManagerAddress = governance.getAccountManager();
        this.accountManager = AccountManager.load(acctManagerAddress, client, credentials);
    }

    public GovernContractInitializer newGovernContractInitializer() {
        GovernContractInitializer manager = new GovernContractInitializer();
        manager.setClient(client).setCredentials(credentials);
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
                .setClient(client)
                .setCredentials(credentials);
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
                .setClient(client)
                .setCredentials(credentials);
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
                .setClient(client)
                .setCredentials(credentials);
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
                .setClient(client)
                .setCredentials(credentials);
        return manager;
    }
}
