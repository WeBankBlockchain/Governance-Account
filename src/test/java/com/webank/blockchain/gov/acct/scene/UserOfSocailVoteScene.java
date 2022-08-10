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

import com.google.common.collect.Lists;
import com.webank.blockchain.gov.acct.BaseTests;
import com.webank.blockchain.gov.acct.contract.UserAccount;
import com.webank.blockchain.gov.acct.contract.WEGovernance;
import com.webank.blockchain.gov.acct.enums.RequestEnum;
import com.webank.blockchain.gov.acct.enums.UserStaticsEnum;
import com.webank.blockchain.gov.acct.manager.EndUserOperManager;
import com.webank.blockchain.gov.acct.manager.GovernContractInitializer;
import com.webank.blockchain.gov.acct.manager.SocialVoteManager;
import java.math.BigInteger;
import java.util.List;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * UserOfSelfAdminScene @Description: 这是普通用户操作相关的样例 测试过程： 1. 创建治理合约和初始化endUserAdminManager
 * 2.自助创建普通用户账户 3. 自助修改普通用户账户私钥重置方式 4. 发起社交好友重置私钥 5. 自助注销普通用户账户
 *
 * @author maojiayu
 * @data Feb 24, 2020 11:28:38 AM
 */
public class UserOfSocailVoteScene extends BaseTests {
    @Autowired private GovernContractInitializer gvernContractInitializer;

    @Test
    public void test() throws Exception {

        //  1. 创建治理合约
        WEGovernance governance = gvernContractInitializer.createGovernAccount(endUser1Keypair);
        // 初始化endUserAdminManager 和 socialVoteManager
        EndUserOperManager endUserOperManager =
                new EndUserOperManager(governance, client, endUser1Keypair);
        SocialVoteManager socialVoteManager =
                new SocialVoteManager(governance, client, endUser1Keypair);

        // 2. 自助创建普通用户账户， end user 1,2&3
        String accountAddressP1 = endUserOperManager.createAccount(endUser1Keypair.getAddress());
        Assertions.assertNotNull(accountAddressP1);
        Assertions.assertTrue(endUserOperManager.hasAccount(endUser1Keypair.getAddress()));
        String accountAddressP2 = endUserOperManager.createAccount(endUser2Keypair.getAddress());
        Assertions.assertNotNull(accountAddressP2);
        Assertions.assertTrue(endUserOperManager.hasAccount(endUser2Keypair.getAddress()));
        String accountAddressP3 = endUserOperManager.createAccount(endUser3Keypair.getAddress());
        Assertions.assertNotNull(accountAddressP3);
        Assertions.assertTrue(endUserOperManager.hasAccount(endUser3Keypair.getAddress()));

        // 3. 自助修改普通用户账户私钥重置方式
        // 注意：此处传入的是内部账户的地址，而非用户外部地址。
        List<String> list =
                Lists.newArrayList(accountAddressP1, accountAddressP2, accountAddressP3);
        // set account reset type
        TransactionReceipt tr = endUserOperManager.modifyManagerType(list);
        Assertions.assertEquals(0, tr.getStatus());
        Assertions.assertEquals(
                UserStaticsEnum.SOCIAL.getStatics(), endUserOperManager.getUserStatics());
        UserAccount userAccount = endUserOperManager.getUserAccount(endUser1Keypair.getAddress());

        // 4. 发起社交好友重置私钥
        socialVoteManager.requestResetAccount(
                governanceUser2Keypair.getAddress(), endUser1Keypair.getAddress());
        socialVoteManager.changeCredentials(endUser2Keypair);
        socialVoteManager.vote(endUser1Keypair.getAddress(), true);
        socialVoteManager.changeCredentials(endUser3Keypair);
        socialVoteManager.vote(endUser1Keypair.getAddress(), true);
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
        Assertions.assertEquals(0, tr.getStatus());
        Assertions.assertTrue(!endUserOperManager.hasAccount(endUser1Keypair.getAddress()));
        Assertions.assertTrue(endUserOperManager.hasAccount(governanceUser2Keypair.getAddress()));
        Assertions.assertTrue(!userAccount.passed(RequestEnum.OPER_CHANGE_CREDENTIAL.getType()));

        // 5. 自助注销普通用户账户
        endUserOperManager.changeCredentials(endUser2Keypair);
        endUserOperManager.cancelAccount();
    }
}
