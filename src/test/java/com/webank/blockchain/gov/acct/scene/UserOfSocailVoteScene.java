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
package com.webank.blockchain.gov.acct.scene;

import com.webank.blockchain.gov.acct.BaseTests;
import com.webank.blockchain.gov.acct.contract.AccountManager;
import com.webank.blockchain.gov.acct.contract.UserAccount;
import com.webank.blockchain.gov.acct.enums.RequestEnum;
import com.webank.blockchain.gov.acct.enums.UserStaticsEnum;
import com.webank.blockchain.gov.acct.manager.EndUserOperManager;
import com.webank.blockchain.gov.acct.manager.SocialVoteManager;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.fisco.bcos.sdk.model.TransactionReceipt;
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
        String accountAddressP1 = endUserAdminManager.createAccount(endUser1Keypair.getAddress());
        Assertions.assertNotNull(accountAddressP1);
        Assertions.assertTrue(accountManager.hasAccount(endUser1Keypair.getAddress()));
        String accountAddressP2 = endUserAdminManager.createAccount(endUser2Keypair.getAddress());
        Assertions.assertNotNull(accountAddressP2);
        Assertions.assertTrue(accountManager.hasAccount(endUser2Keypair.getAddress()));
        String accountAddressP3 = endUserAdminManager.createAccount(endUser3Keypair.getAddress());
        Assertions.assertNotNull(accountAddressP3);
        Assertions.assertTrue(accountManager.hasAccount(endUser3Keypair.getAddress()));
        AccountManager accountManagerP1 =
                AccountManager.load(accountManager.getContractAddress(), client, endUser1Keypair);
        List<String> list = new ArrayList<>();
        list.add(accountAddressP1);
        list.add(accountAddressP2);
        list.add(accountAddressP3);
        endUserAdminManager.setCredentials(endUser1Keypair);
        // set account reset type
        TransactionReceipt tr = endUserAdminManager.modifyManagerType(list);
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertEquals(
                UserStaticsEnum.SOCIAL.getStatics(), endUserAdminManager.getUserStatics());
        socialVoteManager.changeCredentials(endUser1Keypair);
        UserAccount userAccount = endUserAdminManager.getUserAccount(endUser1Keypair.getAddress());

        // reset account
        socialVoteManager.requestResetAccount(
                governanceUser2Keypair.getAddress(), endUser1Keypair.getAddress());
        socialVoteManager.vote(endUser1Keypair.getAddress(), true);

        socialVoteManager.changeCredentials(endUser2Keypair);
        socialVoteManager.vote(endUser1Keypair.getAddress(), true);
        socialVoteManager.changeCredentials(endUser3Keypair);
        socialVoteManager.vote(endUser1Keypair.getAddress(), false);
        socialVoteManager.changeCredentials(endUser1Keypair);
        Assertions.assertTrue(
                userAccount.passed(
                        RequestEnum.OPER_CHANGE_CREDENTIAL.getType(),
                        endUser1Keypair.getAddress(),
                        governanceUser2Keypair.getAddress(),
                        BigInteger.ZERO));

        tr =
                socialVoteManager.resetAccount(
                        governanceUser2Keypair.getAddress(), endUser1Keypair.getAddress());
        System.out.println(accountManagerP1.getContractAddress());
        System.out.println(userAccount._accountManager());
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertTrue(!accountManager.hasAccount(endUser1Keypair.getAddress()));
        Assertions.assertTrue(accountManager.hasAccount(governanceUser2Keypair.getAddress()));
        Assertions.assertTrue(!userAccount.passed(RequestEnum.OPER_CHANGE_CREDENTIAL.getType()));

        // cancel
        endUserAdminManager.changeCredentials(endUser2Keypair);
        endUserAdminManager.cancelAccount();
    }
}
