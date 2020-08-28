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
import com.webank.blockchain.acct.gov.enums.UserStaticsEnum;
import com.webank.blockchain.acct.gov.exception.InvalidParamException;
import com.webank.blockchain.acct.gov.tool.JacksonUtils;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.springframework.stereotype.Service;

/**
 * EndUserAdminManager @Description: EndUserAdminManager
 *
 * @author maojiayu
 * @data Feb 20, 2020 4:53:02 PM
 */
@Service
public class EndUserOperManager extends BasicManager {

    public TransactionReceipt setRelatedAccount(String account, int value) throws Exception {
        UserAccount accountConfig = getUserAccount(credentials.getAddress());
        int type = accountConfig._statics().send().intValue();
        if (type == UserStaticsEnum.SOCIAL.getStatics()) {
            if (accountConfig.getWeightInfo().send().getValue1().size() >= 3 && value > 0) {
                throw new InvalidParamException("Already too many voters.");
            }
            return accountConfig.setWeight(account, BigInteger.valueOf(value)).send();
        } else {
            throw new InvalidParamException("error account types.");
        }
    }

    public TransactionReceipt addRelatedAccount(String externalAccount) throws Exception {
        return setRelatedAccount(externalAccount, 1);
    }

    public TransactionReceipt removeRelatedAccount(String externalAccount) throws Exception {
        return setRelatedAccount(externalAccount, 0);
    }

    public TransactionReceipt modifyManagerType() throws Exception {
        UserAccount userAccount = getUserAccount(credentials.getAddress());
        BigInteger statics = userAccount._statics().send();
        if (statics.intValue() == UserStaticsEnum.NONE.getStatics()) {
            throw new InvalidParamException("Modify the same type.");
        }
        return userAccount.setStatics().send();
    }

    // social mode only support 2-3;
    public TransactionReceipt modifyManagerType(List<String> voters) throws Exception {
        if (voters.size() != 3) {
            throw new InvalidParamException("Invalid voters number, must be 3.");
        }
        UserAccount userAccount = getUserAccount(credentials.getAddress());
        BigInteger statics = userAccount._statics().send();
        if (statics.intValue() == UserStaticsEnum.SOCIAL.getStatics()) {
            throw new InvalidParamException("Modify the same type.");
        }
        List<BigInteger> value =
                voters.stream().map(c -> BigInteger.ONE).collect(Collectors.toList());
        System.out.println("ac owner is: " + userAccount._owner().send());
        System.out.println(credentials.getAddress());
        TransactionReceipt tr =
                userAccount.setVoteStatics(voters, value, BigInteger.valueOf(2)).send();
        System.out.println(tr.getStatus());
        System.out.println("Init vote: " + JacksonUtils.toJson(userAccount.getWeightInfo()));
        return tr;
    }

    public TransactionReceipt resetAccount(String newCredential) throws Exception {
        return accountManager.setExternalAccountByUser(newCredential).send();
    }

    public TransactionReceipt cancelAccount() throws Exception {
        return accountManager.cancelByUser().send();
    }

    public int getUserStatics() throws Exception {
        UserAccount userAccount = getUserAccount(credentials.getAddress());
        return userAccount._statics().send().intValue();
    }
}
