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

import java.math.BigInteger;

import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.springframework.stereotype.Service;

import com.webank.blockchain.gov.acct.contract.UserAccount;
import com.webank.blockchain.gov.acct.enums.RequestEnum;
import com.webank.blockchain.gov.acct.exception.TransactionReceiptException;
import com.webank.blockchain.gov.acct.vo.VoteRequestInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * SocialVoteManager @Description: SocialVoteManager
 *
 * @author maojiayu
 * @data Mar 4, 2020 4:55:00 PM
 */
@Service
@Slf4j
public class SocialVoteManager extends BasicManager {

    public TransactionReceipt requestResetAccount(
            String newExternalAccount, String oldExternalAccount) throws Exception {
        UserAccount accountConfig = getUserAccount(oldExternalAccount);
        TransactionReceipt tr =
                accountConfig.register(
                        RequestEnum.OPER_CHANGE_CREDENTIAL.getType(),
                        RequestEnum.OPER_CHANGE_CREDENTIAL.getType(),
                        oldExternalAccount,
                        newExternalAccount,
                        BigInteger.ZERO);
        if (!tr.getStatus().equalsIgnoreCase("0x0")) {
            throw new TransactionReceiptException(
                    "Error request a vote of reset account: " + tr.getStatus());
        }
        log.info("Request reset account [ {} ] to new account [ {} ]", oldExternalAccount, newExternalAccount);
        return tr;
    }

    public TransactionReceipt vote(String oldExternalAccount, boolean agreed) throws Exception {
        UserAccount accountConfig = getUserAccount(oldExternalAccount);
        log.info(
                "\n start vote of account config: [ {} ] \n --------------------------------------  \n voter: [ {} ] \n agreed: [ {} ] \n",
                accountConfig.getContractAddress(),
                this.credentials.getAddress(),
                agreed);
        TransactionReceipt tr =
                accountConfig.vote(RequestEnum.OPER_CHANGE_CREDENTIAL.getType(), agreed);
        if (!tr.getStatus().equalsIgnoreCase("0x0")) {
            throw new TransactionReceiptException("Error vote: " + tr.getStatus());
        }
        VoteRequestInfo voteRequestInfo = new VoteRequestInfo();
        voteRequestInfo.forward(accountConfig.getRequestInfo(RequestEnum.OPER_CHANGE_CREDENTIAL.getType())).print();
        return tr;
    }

    public TransactionReceipt resetAccount(String newExternalAccount, String oldExternalAccount)
            throws Exception {
        TransactionReceipt tr =
                accountManager.setExternalAccountBySocial(newExternalAccount, oldExternalAccount);
        if (!tr.getStatus().equalsIgnoreCase("0x0")) {
            throw new TransactionReceiptException("Error reset account: " + tr.getStatus());
        }
        log.info("External account reset to [ {} ] from [ {} ] ", newExternalAccount, oldExternalAccount);
        return tr;
    }
}
