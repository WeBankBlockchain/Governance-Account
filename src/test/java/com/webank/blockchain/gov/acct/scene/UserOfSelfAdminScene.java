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
import com.webank.blockchain.gov.acct.contract.WEGovernance;
import com.webank.blockchain.gov.acct.enums.UserStaticsEnum;
import com.webank.blockchain.gov.acct.manager.EndUserOperManager;
import com.webank.blockchain.gov.acct.manager.GovernContractInitializer;
import com.webank.blockchain.gov.acct.service.BaseAccountService;
import java.util.List;
import org.assertj.core.util.Lists;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * UserOfSelfAdminScene @Description: 这是普通用户操作相关的样例 测试过程： 1. 创建治理合约和初始化初始化endUserAdminManager
 * 2.自助创建普通用户账户 3. 自助重置普通用户账户私钥 4. 自助注销普通用户账户 5. 自助重新创建普通用户账户 6. 修改普通用户账户私钥重置方式 7. 自助注销普通用户账户
 *
 * @author maojiayu
 * @data Feb 24, 2020 11:28:38 AM
 */
public class UserOfSelfAdminScene extends BaseTests {
    @Autowired private GovernContractInitializer gvernContractInitializer;
    @Autowired private BaseAccountService baseAccountService;

    @Test
    public void test() throws Exception {
        //  1. 创建治理合约
        WEGovernance governance =
                gvernContractInitializer.createGovernAccount(governanceUser1Keypair);
        // 初始化endUserAdminManager
        EndUserOperManager endUserOperManager =
                new EndUserOperManager(governance, client, endUser1Keypair);

        // 2. 自助创建普通用户账户： end user1
        if (!endUserOperManager.hasAccount()) {
            endUserOperManager.createAccount(endUser1Keypair.getAddress());
        }
        String accountAddress =
                endUserOperManager.getBaseAccountAddress(endUser1Keypair.getAddress());
        Assertions.assertNotNull(accountAddress);
        Assertions.assertTrue(endUserOperManager.hasAccount(endUser1Keypair.getAddress()));
        Assertions.assertEquals(
                UserStaticsEnum.NONE.getStatics(), endUserOperManager.getUserStatics());
        String p1AccountAddress =
                endUserOperManager
                        .getUserAccount(endUser1Keypair.getAddress())
                        .getContractAddress();
        Assertions.assertEquals(accountAddress, p1AccountAddress);

        // 3. 自助重置普通用户账户私钥: end user1 -> end user2
        TransactionReceipt tr = endUserOperManager.resetAccount(endUser2Keypair.getAddress());
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertTrue(!endUserOperManager.hasAccount(endUser1Keypair.getAddress()));
        Assertions.assertTrue(endUserOperManager.hasAccount(endUser2Keypair.getAddress()));

        // 4. 自助注销普通用户账户: end user2
        endUserOperManager.changeCredentials(endUser2Keypair);
        tr = endUserOperManager.cancelAccount();
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertEquals(2, baseAccountService.getStatus(p1AccountAddress));
        Assertions.assertTrue(!endUserOperManager.hasAccount(endUser1Keypair.getAddress()));
        endUserOperManager.changeCredentials(endUser1Keypair);

        // 5. 自助重新创建普通用户账户 end user1
        accountAddress = endUserOperManager.createAccount(endUser1Keypair.getAddress());
        Assertions.assertNotNull(accountAddress);
        Assertions.assertTrue(endUserOperManager.hasAccount(endUser1Keypair.getAddress()));
        p1AccountAddress =
                endUserOperManager
                        .getUserAccount(endUser1Keypair.getAddress())
                        .getContractAddress();

        // 6. 修改普通用户账户私钥重置方式： 增加配置支持社交好友投票重置私钥
        Assertions.assertEquals(
                UserStaticsEnum.NONE.getStatics(), endUserOperManager.getUserStatics());
        List<String> voters =
                Lists.newArrayList(
                        governanceUser1Keypair.getAddress(),
                        governanceUser2Keypair.getAddress(),
                        governanceUser3Keypair.getAddress());
        tr = endUserOperManager.modifyManagerType(voters);
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertEquals(
                UserStaticsEnum.SOCIAL.getStatics(), endUserOperManager.getUserStatics());

        // 7. 自助注销普通用户账户
        tr = endUserOperManager.cancelAccount();
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertEquals(2, baseAccountService.getStatus(p1AccountAddress));
        Assertions.assertTrue(!endUserOperManager.hasAccount(endUser1Keypair.getAddress()));
    }
}
