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
import com.webank.blockchain.gov.acct.manager.AdminModeGovernManager;
import com.webank.blockchain.gov.acct.manager.GovernAccountInitializer;
import com.webank.blockchain.gov.acct.service.BaseAccountService;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * GovernOfAdminModeScene @Description: GovernOfAdminModeScene
 *
 * @author maojiayu
 * @data Feb 22, 2020 3:11:09 PM
 */
public class GovernOfAdminModeScene extends BaseTests {
    @Autowired private GovernAccountInitializer manager;
    @Autowired private AdminModeGovernManager adminModeManager;
    @Autowired private BaseAccountService baseAccountService;

    @Test
    public void testAdminScene() throws Exception {
        WEGovernance govern = manager.createGovernAccount(governanceUser1Keypair);
        System.out.println(govern.getContractAddress());
        Assertions.assertNotNull(govern);
        adminModeManager.setGovernance(govern);
        AccountManager accountManager =
                AccountManager.load(govern.getAccountManager(), client, governanceUser1Keypair);
        adminModeManager.setAccountManager(accountManager);
        adminModeManager.setCredentials(governanceUser1Keypair);
        Assertions.assertEquals(0, govern._mode().intValue());

        // create account by admin
        if (!accountManager.hasAccount(governanceUser2Keypair.getAddress())) {
            adminModeManager.createAccount(governanceUser2Keypair.getAddress());
        }
        String u1Address =
                adminModeManager.getBaseAccountAddress(governanceUser2Keypair.getAddress());
        Assertions.assertNotNull(u1Address);
        String u1AccountAddress =
                accountManager.getUserAccount(governanceUser2Keypair.getAddress());
        Assertions.assertEquals(u1AccountAddress, u1Address);

        // reset acct
        Assertions.assertEquals(govern.getContractAddress(), accountManager._owner());
        TransactionReceipt tr =
                adminModeManager.resetAccount(
                        governanceUser2Keypair.getAddress(), governanceUser3Keypair.getAddress());
        Assertions.assertTrue(tr.isStatusOK());
        Assertions.assertTrue(!accountManager.hasAccount(governanceUser2Keypair.getAddress()));
        Assertions.assertTrue(accountManager.hasAccount(governanceUser3Keypair.getAddress()));

        // set back again
        tr =
                adminModeManager.resetAccount(
                        governanceUser3Keypair.getAddress(), governanceUser2Keypair.getAddress());
        Assertions.assertEquals("0x0", tr.getStatus());

        // freeze
        tr = adminModeManager.freezeAccount(governanceUser2Keypair.getAddress());
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertEquals(1, baseAccountService.getStatus(u1AccountAddress));

        // transfer to abnormal
        tr = adminModeManager.transferAdminAuth(governanceUser2Keypair.getAddress());
        Assertions.assertNotEquals("0x0", tr.getStatus());

        // transfer to not registered
        tr = adminModeManager.transferAdminAuth(endUser1Keypair.getAddress());
        Assertions.assertNotEquals("0x0", tr.getStatus());

        // unfreeze
        tr = adminModeManager.unfreezeAccount(governanceUser2Keypair.getAddress());
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertEquals(0, baseAccountService.getStatus(u1AccountAddress));

        // cancel
        tr = adminModeManager.cancelAccount(governanceUser2Keypair.getAddress());
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertEquals(2, baseAccountService.getStatus(u1AccountAddress));
        Assertions.assertTrue(!accountManager.hasAccount(governanceUser2Keypair.getAddress()));

        // create again
        Assertions.assertTrue(!accountManager.hasAccount(governanceUser2Keypair.getAddress()));
        String newAcct = adminModeManager.createAccount(governanceUser2Keypair.getAddress());
        Assertions.assertNotNull(newAcct);
        Assertions.assertTrue(accountManager.hasAccount(governanceUser2Keypair.getAddress()));

        // cancel
        tr = adminModeManager.cancelAccount(governanceUser2Keypair.getAddress());
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertEquals(2, baseAccountService.getStatus(u1AccountAddress));
        Assertions.assertTrue(!accountManager.hasAccount(governanceUser2Keypair.getAddress()));

        // transfer
        adminModeManager.createAccount(governanceUser3Keypair.getAddress());
        tr = adminModeManager.transferAdminAuth(governanceUser3Keypair.getAddress());
        Assertions.assertEquals("0x0", tr.getStatus());
        String owner = govern._owner();
        Assertions.assertEquals(governanceUser3Keypair.getAddress(), owner);
    }
}
