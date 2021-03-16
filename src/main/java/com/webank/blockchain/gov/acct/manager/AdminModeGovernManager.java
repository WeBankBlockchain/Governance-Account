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

import com.webank.blockchain.gov.acct.contract.WEGovernance;
import com.webank.blockchain.gov.acct.enums.RequestEnum;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.springframework.stereotype.Service;

/**
 * AdminModeManager @Description: AdminModeManager
 *
 * @author maojiayu
 * @data Feb 22, 2020 11:20:09 AM
 */
@Service
@Slf4j
public class AdminModeGovernManager extends BasicManager {

    public AdminModeGovernManager() {
        super();
    }

    public AdminModeGovernManager(WEGovernance governance, Client client, CryptoKeyPair credentials)
            throws ContractException {
        super(governance, client, credentials);
    }

    public TransactionReceipt transferAdminAuth(String newAdminAddr) throws Exception {
        log.info(
                "Contract [ {} ] transfer owner to address [ {} ]",
                governance.getContractAddress(),
                newAdminAddr);
        return governance.transferOwner(newAdminAddr);
    }

    public TransactionReceipt resetAccount(String oldAccount, String newAccount) throws Exception {
        log.info("reset account to [ {} ] from [ {} ]", newAccount, oldAccount);
        return governance.setExternalAccount(
                RequestEnum.OPER_CHANGE_CREDENTIAL.getType(), newAccount, oldAccount);
    }

    public TransactionReceipt freezeAccount(String externalAccount) throws Exception {
        log.info("freeze account [ {} ]", externalAccount);
        return governance.doOper(
                RequestEnum.OPER_FREEZE_ACCOUNT.getType(),
                externalAccount,
                RequestEnum.OPER_FREEZE_ACCOUNT.getType());
    }

    public TransactionReceipt unfreezeAccount(String externalAccount) throws Exception {
        log.info("unfreeze account [ {} ]", externalAccount);
        return governance.doOper(
                RequestEnum.OPER_UNFREEZE_ACCOUNT.getType(),
                externalAccount,
                RequestEnum.OPER_UNFREEZE_ACCOUNT.getType());
    }

    public TransactionReceipt cancelAccount(String userAccount) throws Exception {
        log.info("cancel account [ {} ]", userAccount);
        return governance.doOper(
                RequestEnum.OPER_CANCEL_ACCOUNT.getType(),
                userAccount,
                RequestEnum.OPER_CANCEL_ACCOUNT.getType());
    }
}
