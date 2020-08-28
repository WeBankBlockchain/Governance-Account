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

import com.webank.blockchain.acct.gov.contract.UserAccount;
import com.webank.blockchain.acct.gov.enums.RequestEnum;
import com.webank.blockchain.acct.gov.exception.TransactionReceiptException;
import com.webank.blockchain.acct.gov.tool.JacksonUtils;
import java.math.BigInteger;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.springframework.stereotype.Service;

/**
 * SocialVoteManager @Description: SocialVoteManager
 *
 * @author maojiayu
 * @data Mar 4, 2020 4:55:00 PM
 */
@Service
public class SocialVoteManager extends BasicManager {

    public TransactionReceipt requestResetAccount(
            String newExternalAccount, String oldExternalAccount) throws Exception {
        UserAccount accountConfig = getUserAccount(oldExternalAccount);
        TransactionReceipt tr =
                accountConfig
                        .register(
                                RequestEnum.OPER_CHANGE_CREDENTIAL.getType(),
                                RequestEnum.OPER_CHANGE_CREDENTIAL.getType(),
                                oldExternalAccount,
                                newExternalAccount,
                                BigInteger.ZERO)
                        .send();
        if (!tr.getStatus().equalsIgnoreCase("0x0")) {
            throw new TransactionReceiptException(
                    "Error request a vote of reset account: " + tr.getStatus());
        }
        return tr;
    }

    public TransactionReceipt vote(String oldExternalAccount, boolean agreed) throws Exception {
        UserAccount accountConfig = getUserAccount(oldExternalAccount);
        System.out.println("vote: " + accountConfig.getContractAddress());
        System.out.println(
                "before vote: "
                        + JacksonUtils.toJson(
                                accountConfig
                                        .getRequestInfo(
                                                RequestEnum.OPER_CHANGE_CREDENTIAL.getType())
                                        .send()));
        TransactionReceipt tr =
                accountConfig.vote(RequestEnum.OPER_CHANGE_CREDENTIAL.getType(), agreed).send();
        if (!tr.getStatus().equalsIgnoreCase("0x0")) {
            throw new TransactionReceiptException("Error vote: " + tr.getStatus());
        }
        System.out.println(
                "after vote: "
                        + JacksonUtils.toJson(
                                accountConfig
                                        .getRequestInfo(
                                                RequestEnum.OPER_CHANGE_CREDENTIAL.getType())
                                        .send()));
        return tr;
    }

    public TransactionReceipt resetAccount(String newExternalAccount, String oldExternalAccount)
            throws Exception {
        TransactionReceipt tr =
                accountManager
                        .setExternalAccountBySocial(newExternalAccount, oldExternalAccount)
                        .send();
        if (!tr.getStatus().equalsIgnoreCase("0x0")) {
            throw new TransactionReceiptException("Error reset account: " + tr.getStatus());
        }
        return tr;
    }
}
