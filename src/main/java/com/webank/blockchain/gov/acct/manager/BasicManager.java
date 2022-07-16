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
import com.webank.blockchain.gov.acct.contract.UserAccount;
import com.webank.blockchain.gov.acct.contract.WEGovernance;
import com.webank.blockchain.gov.acct.exception.TransactionReceiptException;
import com.webank.blockchain.gov.acct.service.JavaSDKBasicService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.fisco.bcos.sdk.v3.transaction.model.exception.ContractException;
import org.fisco.bcos.sdk.v3.transaction.tools.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * BaseManager @Description: BaseManager
 *
 * @author maojiayu
 * @data Feb 21, 2020 5:32:09 PM
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Slf4j
public class BasicManager extends JavaSDKBasicService {

    @Autowired(required = false)
    protected WEGovernance governance;

    @Autowired(required = false)
    protected AccountManager accountManager;

    public BasicManager() {}

    public BasicManager(WEGovernance governance, Client client, CryptoKeyPair credentials)
            throws ContractException {
        super.client = client;
        super.credentials = credentials;
        this.governance = governance;
        this.accountManager =
                AccountManager.load(governance.getAccountManager(), client, credentials);
    }

    public String getAccountAddress() throws Exception {
        return accountManager.getUserAccount(credentials.getAddress());
    }

    public String createAccount(String externalAccount) throws Exception {
        return createAccount(this.accountManager, externalAccount);
    }

    public String createAccount(AccountManager accountManager, String externalAccount)
            throws Exception {
        if (hasAccount(externalAccount)) {
            log.info("Account [ {} ] already created.", externalAccount);
            return getBaseAccountAddress(externalAccount);
        }
        TransactionReceipt tr = accountManager.newAccount(externalAccount);
        if (tr.getStatus() != 0) {
            log.error("create new Account error: {}", JsonUtils.toJson(tr));
            throw new TransactionReceiptException("Error create account error");
        }
        String addr = accountManager.getUserAccount(externalAccount);
        log.info("new account created: [ {} ], created by [ {} ]", addr, externalAccount);
        return addr;
    }

    public String getExternalAccount(String userAccount) throws Exception {
        String externalAddress = accountManager.getExternalAccount(userAccount);
        log.info("external address is [ {} ], userAccount is [ {} ]", externalAddress, userAccount);
        return externalAddress;
    }

    public UserAccount getUserAccount(String externalAccount) throws Exception {
        String configAddress = accountManager.getUserAccount(externalAccount);
        log.debug(
                "User account config address is [ {} ], cryptoKeyPair is [ {} ]",
                configAddress,
                credentials.getAddress());
        return UserAccount.load(configAddress, client, credentials);
    }

    public int getUserAccountStatus(String externalAccount) throws Exception {
        UserAccount userAccount = getUserAccount(externalAccount);
        return userAccount._status().intValue();
    }

    public String getBaseAccountAddress(String externalAccount) throws Exception {
        return accountManager.getUserAccount(externalAccount);
    }

    public boolean hasAccount() throws Exception {
        return accountManager.hasAccount(credentials.getAddress());
    }

    public boolean hasAccount(String externalAccount) throws Exception {
        return accountManager.hasAccount(externalAccount);
    }

    public boolean isExternalAccountNormal(String externalAccount) throws Exception {
        return accountManager.isExternalAccountNormal(externalAccount);
    }

    public void changeCredentials(CryptoKeyPair credentials) throws Exception {
        log.info(
                "credentials change to [ {} ] from [ {} ]",
                credentials.getAddress(),
                this.credentials.getAddress());
        this.credentials = credentials;
        this.governance =
                WEGovernance.load(this.governance.getContractAddress(), client, credentials);
        this.accountManager =
                AccountManager.load(this.accountManager.getContractAddress(), client, credentials);
    }
}
