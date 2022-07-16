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
import com.webank.blockchain.gov.acct.enums.AccountStatusEnum;
import com.webank.blockchain.gov.acct.manager.GovernContractInitializer;
import com.webank.blockchain.gov.acct.manager.VoteModeGovernManager;
import com.webank.blockchain.gov.acct.service.BaseAccountService;
import com.webank.blockchain.gov.acct.vo.GovernAccountGroup;
import java.math.BigInteger;
import org.fisco.bcos.sdk.v3.codec.datatypes.Address;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * GovernOfNormalVoteScene @Description: 这是多签制模式的样例 测试过程： 1. 配置治理账户信息 2. 创建治理合约 3. 创建普通用户账户 4.
 * 重置普通用户账户私钥 5. 冻结普通用户账户 6. 解冻普通用户账户 7. 注销普通用户账户 8. 重置治理合约的投票阈值 9. 删除治理账户 10. 添加治理账户
 *
 * @author maojiayu
 * @data Feb 22, 2020 4:36:34 PM
 */
public class GovernOfNormalVoteScene extends BaseTests {
    @Autowired private GovernContractInitializer governAccountInitializer;
    @Autowired private BaseAccountService baseAccountService;

    @Test
    public void testScene() throws Exception {
        // 1. 配置治理账户信息
        GovernAccountGroup governAccountGroup = new GovernAccountGroup();
        // 投票阈值2， 初始设置3个治理账户。
        governAccountGroup.setThreshold(2);
        governAccountGroup.addGovernUser("user1", governanceUser1Keypair.getAddress());
        governAccountGroup.addGovernUser("user2", governanceUser2Keypair.getAddress());
        governAccountGroup.addGovernUser("user3", governanceUser3Keypair.getAddress());

        // 2. 创建治理合约
        WEGovernance governance = governAccountInitializer.createGovernAccount(governAccountGroup);
        Assertions.assertNotNull(governance);
        // set voteModeGovernManager
        VoteModeGovernManager voteModeGovernManager =
                new VoteModeGovernManager(governance, client, governanceUser1Keypair);

        // 3. 创建普通用户账户
        String p1Address = voteModeGovernManager.createAccount(endUser1Keypair.getAddress());
        Assertions.assertNotNull(p1Address);
        Assertions.assertTrue(voteModeGovernManager.hasAccount(endUser1Keypair.getAddress()));

        // 4. 重置普通用户账户私钥: governance user 1
        // switch credential to governance user 1
        voteModeGovernManager.changeCredentials(governanceUser1Keypair);
        // request vote: user1 and user2 approve.
        BigInteger requestId =
                voteModeGovernManager.requestResetAccount(
                        endUser2Keypair.getAddress(), endUser1Keypair.getAddress());
        // user1 approve
        voteModeGovernManager.vote(requestId, true);
        voteModeGovernManager.changeCredentials(governanceUser2Keypair);
        // user2 approve
        voteModeGovernManager.vote(requestId, true);
        voteModeGovernManager.changeCredentials(governanceUser1Keypair);
        // threshold is 2, vote passed.
        Assertions.assertTrue(governance.passed(requestId));
        Assertions.assertTrue(
                governance.requestReady(
                        requestId,
                        BigInteger.valueOf(2),
                        endUser1Keypair.getAddress(),
                        endUser2Keypair.getAddress(),
                        BigInteger.ZERO));
        TransactionReceipt tr =
                voteModeGovernManager.resetAccount(
                        requestId, endUser2Keypair.getAddress(), endUser1Keypair.getAddress());
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertTrue(!voteModeGovernManager.hasAccount(endUser1Keypair.getAddress()));
        Assertions.assertTrue(voteModeGovernManager.hasAccount(endUser2Keypair.getAddress()));

        // 5. 冻结普通用户账户: end user 2
        requestId = voteModeGovernManager.requestFreezeAccount(endUser2Keypair.getAddress());
        voteModeGovernManager.changeCredentials(governanceUser1Keypair);
        voteModeGovernManager.vote(requestId, true);
        voteModeGovernManager.changeCredentials(governanceUser2Keypair);
        voteModeGovernManager.vote(requestId, true);
        Assertions.assertTrue(governance.passed(requestId));
        tr = voteModeGovernManager.freezeAccount(requestId, endUser2Keypair.getAddress());
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertEquals(
                AccountStatusEnum.FROZEN.getStatus(), baseAccountService.getStatus(p1Address));

        // 6. 解冻普通用户账户: end user 2
        requestId = voteModeGovernManager.requestUnfreezeAccount(endUser2Keypair.getAddress());
        voteModeGovernManager.changeCredentials(governanceUser1Keypair);
        voteModeGovernManager.vote(requestId, true);
        voteModeGovernManager.changeCredentials(governanceUser2Keypair);
        voteModeGovernManager.vote(requestId, true);
        Assertions.assertTrue(governance.passed(requestId));
        tr = voteModeGovernManager.unfreezeAccount(requestId, endUser2Keypair.getAddress());
        Assertions.assertEquals("0x0", tr.getStatus());
        // assert end user2 status: normal
        Assertions.assertEquals(
                AccountStatusEnum.NORMAL.getStatus(), baseAccountService.getStatus(p1Address));

        // 7. 注销普通用户账户: end user2
        requestId = voteModeGovernManager.requestCancelAccount(endUser2Keypair.getAddress());
        voteModeGovernManager.changeCredentials(governanceUser1Keypair);
        voteModeGovernManager.vote(requestId, true);
        voteModeGovernManager.changeCredentials(governanceUser2Keypair);
        voteModeGovernManager.vote(requestId, true);
        Assertions.assertTrue(governance.passed(requestId));
        tr = voteModeGovernManager.cancelAccount(requestId, endUser2Keypair.getAddress());
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertEquals(
                AccountStatusEnum.CLOSED.getStatus(), baseAccountService.getStatus(p1Address));
        Assertions.assertTrue(!voteModeGovernManager.hasAccount(endUser2Keypair.getAddress()));

        // 8. 重置治理合约的投票阈值
        requestId = voteModeGovernManager.requestResetThreshold(1);
        voteModeGovernManager.changeCredentials(governanceUser1Keypair);
        voteModeGovernManager.vote(requestId, true);
        voteModeGovernManager.changeCredentials(governanceUser2Keypair);
        voteModeGovernManager.vote(requestId, true);
        Assertions.assertTrue(governance.passed(requestId));
        tr = voteModeGovernManager.resetThreshold(requestId, 1);
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertEquals(1, governance.getWeightInfo().getValue3().intValue());

        // 9. 删除治理账户: governance user 3
        requestId =
                voteModeGovernManager.requestRemoveGovernAccount(
                        governanceUser3Keypair.getAddress());
        voteModeGovernManager.changeCredentials(governanceUser1Keypair);
        voteModeGovernManager.vote(requestId, true);
        Assertions.assertTrue(governance.passed(requestId));
        Assertions.assertTrue(
                governance.requestReady(
                        requestId,
                        BigInteger.valueOf(11),
                        governanceUser3Keypair.getAddress(),
                        Address.DEFAULT.getValue(),
                        BigInteger.ZERO));
        tr =
                voteModeGovernManager.removeGovernAccount(
                        requestId, governanceUser3Keypair.getAddress());
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertEquals(
                0,
                governance
                        .getVoteWeight(
                                voteModeGovernManager
                                        .getUserAccount(governanceUser3Keypair.getAddress())
                                        .getContractAddress())
                        .intValue());

        // 10. 添加治理账户: governance user 3
        requestId =
                voteModeGovernManager.requestAddGovernAccount(governanceUser3Keypair.getAddress());
        voteModeGovernManager.vote(requestId, true);
        Assertions.assertTrue(governance.passed(requestId));
        tr = voteModeGovernManager.addGovernAccount(requestId, governanceUser3Keypair.getAddress());
        Assertions.assertEquals(0, tr.getStatus());
        Assertions.assertEquals(
                1,
                governance
                        .getVoteWeight(
                                voteModeGovernManager
                                        .getUserAccount(governanceUser3Keypair.getAddress())
                                        .getContractAddress())
                        .intValue());
    }
}
