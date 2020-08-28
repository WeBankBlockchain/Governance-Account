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
import com.webank.blockchain.acct.gov.contract.UserAccount;
import com.webank.blockchain.acct.gov.contract.WEGovernance;
import com.webank.blockchain.acct.gov.exception.TransactionReceiptException;
import com.webank.blockchain.acct.gov.service.Web3jBasicService;
import com.webank.blockchain.acct.gov.tool.JacksonUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
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
public class BasicManager extends Web3jBasicService {
    @Autowired(required = false)
    protected WEGovernance governance;

    @Autowired(required = false)
    protected AccountManager accountManager;

    public String getAccountAddress() throws Exception {
        return accountManager.getUserAccount(credentials.getAddress()).send();
    }

    public String createAccount(String externalAccount) throws Exception {
        return createAccount(this.accountManager, externalAccount);
    }

    public String createAccount(AccountManager accountManager, String externalAccount)
            throws Exception {
        if (hasAccount(externalAccount)) {
            return getBaseAccountAddress(externalAccount);
        }
        TransactionReceipt tr = accountManager.newAccount(externalAccount).send();
        if (!tr.getStatus().equalsIgnoreCase("0x0")) {
            log.error("create new Account error: {}", JacksonUtils.toJson(tr));
            throw new TransactionReceiptException("Error create account error");
        }
        String addr =
                (String)
                        AccountManager.transactionDecoder
                                .decodeOutputReturnObject(tr.getInput(), tr.getOutput())
                                .getResult()
                                .get(1)
                                .getData();
        log.info("new acct {}, created by {}", addr, externalAccount);
        return addr;
    }

    public String getExternalAccount(String userAccount) throws Exception {
        String externalAddress = accountManager.getExternalAccount(userAccount).send();
        log.info("external address is {}, userAccount is {}", externalAddress, userAccount);
        return externalAddress;
    }

    public UserAccount getUserAccount(String externalAccount) throws Exception {
        String configAddress = accountManager.getUserAccount(externalAccount).send();
        log.info(
                "Account config address is {}, credentials is {}",
                configAddress,
                credentials.getAddress());
        return UserAccount.load(configAddress, web3j, credentials, contractGasProvider);
    }

    public int getUserAccountStatus(String externalAccount) throws Exception {
        UserAccount userAccount = getUserAccount(externalAccount);
        return userAccount._status().send().intValue();
    }

    public String getBaseAccountAddress(String externalAccount) throws Exception {
        return accountManager.getUserAccount(externalAccount).send();
    }

    public boolean hasAccount() throws Exception {
        return accountManager.hasAccount(credentials.getAddress()).send();
    }

    public boolean hasAccount(String externalAccount) throws Exception {
        return accountManager.hasAccount(externalAccount).send();
    }

    public boolean isExternalAccountNormal(String externalAccount) throws Exception {
        return accountManager.isExternalAccountNormal(externalAccount).send();
    }

    public void changeCredentials(Credentials credentials) throws Exception {
        super.credentials = credentials;
        this.governance =
                WEGovernance.load(
                        this.governance.getContractAddress(),
                        web3j,
                        credentials,
                        contractGasProvider);
        this.accountManager =
                AccountManager.load(
                        this.accountManager.getContractAddress(),
                        web3j,
                        credentials,
                        contractGasProvider);
    }
}
