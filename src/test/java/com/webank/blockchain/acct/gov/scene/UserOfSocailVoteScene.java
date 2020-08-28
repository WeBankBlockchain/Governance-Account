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
package com.webank.blockchain.acct.gov.scene;

import com.webank.blockchain.acct.gov.BaseTests;
import com.webank.blockchain.acct.gov.contract.AccountManager;
import com.webank.blockchain.acct.gov.contract.UserAccount;
import com.webank.blockchain.acct.gov.enums.RequestEnum;
import com.webank.blockchain.acct.gov.enums.UserStaticsEnum;
import com.webank.blockchain.acct.gov.manager.EndUserOperManager;
import com.webank.blockchain.acct.gov.manager.SocialVoteManager;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * UserOfSelfAdminScene @Description: UserOfSelfAdminScene
 *
 * @author maojiayu
 * @data Feb 24, 2020 11:28:38 AM
 */
public class UserOfSocailVoteScene extends BaseTests {
    @Autowired private EndUserOperManager endUserAdminManager;
    @Autowired private AccountManager accountManager;
    @Autowired private SocialVoteManager socialVoteManager;

    @Test
    public void test() throws Exception {

        // create account
        String accountAddressP1 = endUserAdminManager.createAccount(p1.getAddress());
        Assertions.assertNotNull(accountAddressP1);
        Assertions.assertTrue(accountManager.hasAccount(p1.getAddress()).send());
        String accountAddressP2 = endUserAdminManager.createAccount(p2.getAddress());
        Assertions.assertNotNull(accountAddressP2);
        Assertions.assertTrue(accountManager.hasAccount(p2.getAddress()).send());
        String accountAddressP3 = endUserAdminManager.createAccount(p3.getAddress());
        Assertions.assertNotNull(accountAddressP3);
        Assertions.assertTrue(accountManager.hasAccount(p3.getAddress()).send());
        AccountManager accountManagerP1 =
                AccountManager.load(
                        accountManager.getContractAddress(), web3j, p1, contractGasProvider);
        List<String> list = new ArrayList<>();
        list.add(accountAddressP1);
        list.add(accountAddressP2);
        list.add(accountAddressP3);
        endUserAdminManager.setCredentials(p1);
        // set account reset type
        TransactionReceipt tr = endUserAdminManager.modifyManagerType(list);
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertEquals(
                UserStaticsEnum.SOCIAL.getStatics(), endUserAdminManager.getUserStatics());
        socialVoteManager.changeCredentials(p1);
        UserAccount userAccount = endUserAdminManager.getUserAccount(p1.getAddress());

        // reset account
        socialVoteManager.requestResetAccount(u1.getAddress(), p1.getAddress());
        socialVoteManager.vote(p1.getAddress(), true);

        socialVoteManager.changeCredentials(p2);
        socialVoteManager.vote(p1.getAddress(), true);
        socialVoteManager.changeCredentials(p3);
        socialVoteManager.vote(p1.getAddress(), false);
        socialVoteManager.changeCredentials(p1);
        Assertions.assertTrue(
                userAccount
                        .passed(
                                RequestEnum.OPER_CHANGE_CREDENTIAL.getType(),
                                p1.getAddress(),
                                u1.getAddress(),
                                BigInteger.ZERO)
                        .send());

        tr = socialVoteManager.resetAccount(u1.getAddress(), p1.getAddress());
        System.out.println(accountManagerP1.getContractAddress());
        System.out.println(userAccount._accountManager().send());
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertTrue(!accountManager.hasAccount(p1.getAddress()).send());
        Assertions.assertTrue(accountManager.hasAccount(u1.getAddress()).send());
        Assertions.assertTrue(
                !userAccount.passed(RequestEnum.OPER_CHANGE_CREDENTIAL.getType()).send());

        // cancel
        endUserAdminManager.changeCredentials(p2);
        endUserAdminManager.cancelAccount();
    }
}
