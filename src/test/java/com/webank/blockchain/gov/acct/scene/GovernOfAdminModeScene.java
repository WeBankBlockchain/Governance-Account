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
import com.webank.blockchain.gov.acct.manager.GovernContractInitializer;
import com.webank.blockchain.gov.acct.service.BaseAccountService;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * GovernOfAdminModeScene 
 * @Description: 这是管理员投票模式的样例 
 * 测试过程： 
 * 1. 创建治理合约 
 * 2. 创建普通用户账户 
 * 3. 重置普通用户账户私钥 
 * 4.重置回普通用户账户私钥，便于后续测试 
 * 5. 冻结普通用户账户 
 * 6. 移交管理员权限给冻结账户 
 * 7. 移交管理员权限给未注册的账户 
 * 8. 解冻普通用户账户 
 * 9. 注销普通用户账户 
 * 10.重新创建普通用户账户 
 * 11. 再次注销普通用户账户 
 * 12. 先创建新账户，然后移交管理员权限给该账户
 *
 * @author maojiayu
 * @data Feb 22, 2020 3:11:09 PM
 */
public class GovernOfAdminModeScene extends BaseTests {
    @Autowired private GovernContractInitializer governContractInitializer;
    @Autowired private AdminModeGovernManager adminModeManager;
    @Autowired private BaseAccountService baseAccountService;

    @Test
    public void testAdminScene() throws Exception {
        // 1. 创建治理合约
        WEGovernance govern = governContractInitializer.createGovernAccount(governanceUser1Keypair);
        Assertions.assertNotNull(govern);
        adminModeManager.setGovernance(govern);
        AccountManager accountManager =
                AccountManager.load(govern.getAccountManager(), client, governanceUser1Keypair);
        adminModeManager.setAccountManager(accountManager);
        adminModeManager.setCredentials(governanceUser1Keypair);
        Assertions.assertEquals(0, govern._mode().intValue());

        // 2. 创建普通用户账户: governance user2
        if (!accountManager.hasAccount(governanceUser2Keypair.getAddress())) {
            adminModeManager.createAccount(governanceUser2Keypair.getAddress());
        }
        // get user contract account address in governance contract
        String u1AccountAddress =
                adminModeManager.getBaseAccountAddress(governanceUser2Keypair.getAddress());
        Assertions.assertNotNull(u1AccountAddress);

        // 3. 重置普通用户账户私钥: governance user2 --> governance user3
        Assertions.assertEquals(govern.getContractAddress(), accountManager._owner());
        TransactionReceipt tr =
                adminModeManager.resetAccount(
                        governanceUser2Keypair.getAddress(), governanceUser3Keypair.getAddress());
        Assertions.assertTrue(tr.isStatusOK());
        Assertions.assertTrue(!accountManager.hasAccount(governanceUser2Keypair.getAddress()));
        Assertions.assertTrue(accountManager.hasAccount(governanceUser3Keypair.getAddress()));

        // 4. 重置回普通用户账户私钥，便于后续测试: : governance user3 --> governance user2
        tr =
                adminModeManager.resetAccount(
                        governanceUser3Keypair.getAddress(), governanceUser2Keypair.getAddress());
        Assertions.assertEquals("0x0", tr.getStatus());

        // 5. 冻结普通用户账户: governance user2
        tr = adminModeManager.freezeAccount(governanceUser2Keypair.getAddress());
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertEquals(1, baseAccountService.getStatus(u1AccountAddress));

        // 6. 移交管理员权限给冻结账户
        // in step 5, governance user2 is frozen.
        tr = adminModeManager.transferAdminAuth(governanceUser2Keypair.getAddress());
        Assertions.assertNotEquals("0x0", tr.getStatus());

        // 7. 移交管理员权限给未注册的账户: end user1
        tr = adminModeManager.transferAdminAuth(endUser1Keypair.getAddress());
        Assertions.assertNotEquals("0x0", tr.getStatus());

        // 8. 解冻普通用户账户: governance user2
        tr = adminModeManager.unfreezeAccount(governanceUser2Keypair.getAddress());
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertEquals(0, baseAccountService.getStatus(u1AccountAddress));

        //  9. 注销普通用户账户: governance user2
        tr = adminModeManager.cancelAccount(governanceUser2Keypair.getAddress());
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertEquals(2, baseAccountService.getStatus(u1AccountAddress));
        Assertions.assertTrue(!accountManager.hasAccount(governanceUser2Keypair.getAddress()));

        // 10. 重新创建普通用户账户: governance user2
        Assertions.assertTrue(!accountManager.hasAccount(governanceUser2Keypair.getAddress()));
        String newAcct = adminModeManager.createAccount(governanceUser2Keypair.getAddress());
        Assertions.assertNotNull(newAcct);
        Assertions.assertTrue(accountManager.hasAccount(governanceUser2Keypair.getAddress()));

        // 11. 再次注销普通用户账户: governance user2
        tr = adminModeManager.cancelAccount(governanceUser2Keypair.getAddress());
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertEquals(2, baseAccountService.getStatus(u1AccountAddress));
        Assertions.assertTrue(!accountManager.hasAccount(governanceUser2Keypair.getAddress()));

        // 12. 先创建governance user3 账户， 然后将管理员权限移交给新创建的合约
        adminModeManager.createAccount(governanceUser3Keypair.getAddress());
        tr = adminModeManager.transferAdminAuth(governanceUser3Keypair.getAddress());
        Assertions.assertEquals("0x0", tr.getStatus());
        String owner = govern._owner();
        Assertions.assertEquals(governanceUser3Keypair.getAddress(), owner);
    }
}
