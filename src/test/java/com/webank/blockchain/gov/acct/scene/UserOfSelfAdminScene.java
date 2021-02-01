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
import com.webank.blockchain.gov.acct.contract.WEGovernance;
import com.webank.blockchain.gov.acct.enums.UserStaticsEnum;
import com.webank.blockchain.gov.acct.manager.EndUserOperManager;
import com.webank.blockchain.gov.acct.service.BaseAccountService;
import java.util.List;
import org.assertj.core.util.Lists;
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
public class UserOfSelfAdminScene extends BaseTests {
    @Autowired private EndUserOperManager endUserAdminManager;
    @Autowired private WEGovernance governanceU;
    @Autowired private AccountManager accountManagerU;
    @Autowired private BaseAccountService baseAccountService;

    @Test
    public void test() throws Exception {
        AccountManager accountManager =
                AccountManager.load(accountManagerU.getContractAddress(), client, p1);
        WEGovernance governance = WEGovernance.load(governanceU.getContractAddress(), client, p1);
        endUserAdminManager.setAccountManager(accountManager);
        endUserAdminManager.setGovernance(governance);
        endUserAdminManager.setCredentials(p1);

        // create account
        if (!endUserAdminManager.hasAccount()) {
            endUserAdminManager.createAccount(p1.getAddress());
        }
        String accountAddress = endUserAdminManager.getBaseAccountAddress(p1.getAddress());
        Assertions.assertNotNull(accountAddress);
        System.out.println("p1: " + p1.getAddress());
        Assertions.assertTrue(accountManager.hasAccount(p1.getAddress()));
        Assertions.assertEquals(
                UserStaticsEnum.NONE.getStatics(), endUserAdminManager.getUserStatics());
        String p1AccountAddress = accountManager.getUserAccount(p1.getAddress());
        Assertions.assertEquals(accountAddress, p1AccountAddress);

        // reset account
        endUserAdminManager.setAccountManager(accountManager);
        TransactionReceipt tr = endUserAdminManager.resetAccount(p2.getAddress());
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertTrue(!accountManager.hasAccount(p1.getAddress()));
        Assertions.assertTrue(accountManager.hasAccount(p2.getAddress()));

        // cancel account
        endUserAdminManager.changeCredentials(p2);
        tr = endUserAdminManager.cancelAccount();
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertEquals(2, baseAccountService.getStatus(p1AccountAddress));
        Assertions.assertTrue(!accountManager.hasAccount(p1.getAddress()));
        endUserAdminManager.changeCredentials(p1);

        // create again
        accountAddress = endUserAdminManager.createAccount(p1.getAddress());
        Assertions.assertNotNull(accountAddress);
        System.out.println("p1: " + p1.getAddress());
        Assertions.assertTrue(accountManager.hasAccount(p1.getAddress()));
        p1AccountAddress = accountManager.getUserAccount(p1.getAddress());

        // cancel account
        tr = endUserAdminManager.cancelAccount();
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertEquals(2, baseAccountService.getStatus(p1AccountAddress));
        Assertions.assertTrue(!accountManager.hasAccount(p1.getAddress()));

        // create again
        accountAddress = endUserAdminManager.createAccount(p1.getAddress());
        Assertions.assertNotNull(accountAddress);
        System.out.println("p1: " + p1.getAddress());
        Assertions.assertTrue(accountManager.hasAccount(p1.getAddress()));
        p1AccountAddress = accountManager.getUserAccount(p1.getAddress());

        // modify manager type
        Assertions.assertEquals(
                UserStaticsEnum.NONE.getStatics(), endUserAdminManager.getUserStatics());
        List<String> voters = Lists.newArrayList();
        voters.add(u.getAddress());
        voters.add(u1.getAddress());
        voters.add(u2.getAddress());
        tr = endUserAdminManager.modifyManagerType(voters);
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertEquals(
                UserStaticsEnum.SOCIAL.getStatics(), endUserAdminManager.getUserStatics());

        // cancel account
        tr = endUserAdminManager.cancelAccount();
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertEquals(2, baseAccountService.getStatus(p1AccountAddress));
        Assertions.assertTrue(!accountManager.hasAccount(p1.getAddress()));
    }
}
