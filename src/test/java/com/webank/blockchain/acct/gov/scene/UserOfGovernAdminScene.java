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
import com.webank.blockchain.acct.gov.contract.WEGovernance;
import com.webank.blockchain.acct.gov.enums.UserStaticsEnum;
import com.webank.blockchain.acct.gov.manager.AdminModeGovernManager;
import com.webank.blockchain.acct.gov.manager.EndUserOperManager;
import com.webank.blockchain.acct.gov.manager.GovernAccountInitializer;
import com.webank.blockchain.acct.gov.service.BaseAccountService;
import java.util.List;
import org.assertj.core.util.Lists;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * UserOfGovernAdminScene @Description: UserOfGovernAdminScene
 *
 * @author maojiayu
 * @data Feb 22, 2020 3:11:09 PM
 */
public class UserOfGovernAdminScene extends BaseTests {
    @Autowired private GovernAccountInitializer governAdminManager;
    @Autowired private AdminModeGovernManager adminModeManager;
    @Autowired private AccountManager accountManager;
    @Autowired private EndUserOperManager endUserAdminManager;
    @Autowired private BaseAccountService baseAccountService;

    // @Test
    // create govern account of admin by user, and set the address in application.properties
    public void testCreate() throws Exception {
        WEGovernance govern = governAdminManager.createGovernAccount(u);
        System.out.println(govern.getContractAddress());
        Assertions.assertNotNull(govern);
    }

    @Test
    public void testAdminScene() throws Exception {
        // create account by admin
        if (!accountManager.hasAccount(u1.getAddress()).send()) {
            adminModeManager.createAccount(u1.getAddress());
        }
        String u1Acct = adminModeManager.getBaseAccountAddress(u1.getAddress());
        Assertions.assertNotNull(u1Acct);
        Assertions.assertTrue(accountManager.hasAccount(u1.getAddress()).send());
        String u1AccountAddress = accountManager.getUserAccount(u1.getAddress()).send();
        Assertions.assertEquals(u1Acct, u1AccountAddress);

        // reset acct
        TransactionReceipt tr = adminModeManager.resetAccount(u1.getAddress(), u2.getAddress());
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertTrue(!accountManager.hasAccount(u1.getAddress()).send());
        Assertions.assertTrue(accountManager.hasAccount(u2.getAddress()).send());

        // set back again
        tr = adminModeManager.resetAccount(u2.getAddress(), u1.getAddress());
        Assertions.assertEquals("0x0", tr.getStatus());

        // cancel
        tr = adminModeManager.cancelAccount(u1.getAddress());
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertEquals(2, baseAccountService.getStatus(u1, u1Acct));
        Assertions.assertTrue(!accountManager.hasAccount(u1.getAddress()).send());

        // create again
        Assertions.assertTrue(!accountManager.hasAccount(u1.getAddress()).send());
        u1Acct = adminModeManager.createAccount(u1.getAddress());
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertTrue(accountManager.hasAccount(u1.getAddress()).send());

        // modify manager type
        List<String> voters = Lists.newArrayList();
        voters.add(u.getAddress());
        voters.add(u1.getAddress());
        voters.add(u2.getAddress());
        endUserAdminManager.setCredentials(u1);
        tr = endUserAdminManager.modifyManagerType(voters);
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertEquals(
                UserStaticsEnum.SOCIAL.getStatics(),
                UserAccount.load(
                                accountManager.getUserAccount(u1.getAddress()).send(),
                                web3j,
                                u1,
                                contractGasProvider)
                        ._statics()
                        .send()
                        .intValue());

        // cancel
        tr = adminModeManager.cancelAccount(u1.getAddress());
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertEquals(2, baseAccountService.getStatus(u1, u1Acct));
        Assertions.assertTrue(!accountManager.hasAccount(u1.getAddress()).send());
    }
}
