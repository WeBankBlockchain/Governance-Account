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
import org.fisco.bcos.sdk.abi.datatypes.Address;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * GovernOfBoardVoteScene @Description: 这是权重投票模式的样例 测试过程： 1. 创建账户和权重列表 2. 创建治理合约 3. 创建普通用户账户 4.
 * 重置普通用户账户私钥 5. 冻结普通用户账户 6. 解冻普通用户账户 7. 注销普通用户账户 8. 重置治理合约的投票阈值 9. 删除治理账户 10. 添加治理账户
 *
 * @author maojiayu
 * @data Feb 22, 2020 4:36:34 PM
 */
public class GovernOfBoardVoteScene extends BaseTests {
    @Autowired private GovernContractInitializer governAccountInitializer;
    @Autowired private BaseAccountService baseAccountService;
    @Autowired protected Client client;
    @Autowired protected CryptoKeyPair credentials;

    @Test
    public void testScene() throws Exception {
        // 1. 创建账户和权重列表
        GovernAccountGroup governAccountGroup = new GovernAccountGroup();
        // 创建3个治理账户，合约投票阈值为4，三个账户的投票权重分别为1、2、3
        governAccountGroup.setThreshold(4);
        governAccountGroup.addGovernUser("user1", governanceUser1Keypair.getAddress(), 1);
        governAccountGroup.addGovernUser("user2", governanceUser2Keypair.getAddress(), 2);
        governAccountGroup.addGovernUser("user3", governanceUser3Keypair.getAddress(), 3);

        // 2. 创建治理合约
        WEGovernance governance = governAccountInitializer.createGovernAccount(governAccountGroup);
        Assertions.assertNotNull(governance);
        VoteModeGovernManager voteModeGovernManager =
                new VoteModeGovernManager(governance, client, governanceUser1Keypair);

        // 3. 创建普通用户账户
        String p1Address = governAccountInitializer.createAccount(endUser1Keypair.getAddress());
        Assertions.assertNotNull(p1Address);
        Assertions.assertTrue(voteModeGovernManager.hasAccount(endUser1Keypair.getAddress()));
        Assertions.assertEquals(1, governance._mode().intValue());

        // 4. 重置普通用户账户私钥
        voteModeGovernManager.changeCredentials(governanceUser1Keypair);
        // request for vote，
        BigInteger requestId =
                voteModeGovernManager.requestResetAccount(
                        endUser2Keypair.getAddress(), endUser1Keypair.getAddress());
        // switch user and vote
        voteModeGovernManager.changeCredentials(governanceUser2Keypair);
        voteModeGovernManager.vote(requestId, true);
        voteModeGovernManager.changeCredentials(governanceUser3Keypair);
        voteModeGovernManager.vote(requestId, true);
        // user2 & user3 voted.  2+3>4, votes should be passed.
        Assertions.assertTrue(governance.passed(requestId));
        // switch back to user1
        voteModeGovernManager.changeCredentials(governanceUser1Keypair);
        Assertions.assertTrue(
                governance.requestReady(
                        requestId,
                        BigInteger.valueOf(2),
                        endUser1Keypair.getAddress(),
                        endUser2Keypair.getAddress(),
                        BigInteger.ZERO));
        // vote passed，do the action of reset account.
        TransactionReceipt tr =
                voteModeGovernManager.resetAccount(
                        requestId, endUser2Keypair.getAddress(), endUser1Keypair.getAddress());
        Assertions.assertTrue(tr.isStatusOK());
        Assertions.assertTrue(!voteModeGovernManager.hasAccount(endUser1Keypair.getAddress()));
        Assertions.assertTrue(voteModeGovernManager.hasAccount(endUser2Keypair.getAddress()));

        //  5. 冻结普通用户账户
        requestId = voteModeGovernManager.requestFreezeAccount(endUser2Keypair.getAddress());
        // user2 & user3 voted.  2+3>4, votes should be passed.
        voteModeGovernManager.changeCredentials(governanceUser2Keypair);
        voteModeGovernManager.vote(requestId, true);
        voteModeGovernManager.changeCredentials(governanceUser3Keypair);
        voteModeGovernManager.vote(requestId, true);
        Assertions.assertTrue(governance.passed(requestId));
        tr = voteModeGovernManager.freezeAccount(requestId, endUser2Keypair.getAddress());
        Assertions.assertTrue(tr.isStatusOK());
        Assertions.assertEquals(
                AccountStatusEnum.FROZEN.getStatus(), baseAccountService.getStatus(p1Address));

        // 6. 解冻普通用户账户
        requestId = voteModeGovernManager.requestUnfreezeAccount(endUser2Keypair.getAddress());
        voteModeGovernManager.changeCredentials(governanceUser2Keypair);
        voteModeGovernManager.vote(requestId, true);
        voteModeGovernManager.changeCredentials(governanceUser3Keypair);
        voteModeGovernManager.vote(requestId, true);
        Assertions.assertTrue(governance.passed(requestId));
        tr = voteModeGovernManager.unfreezeAccount(requestId, endUser2Keypair.getAddress());
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertEquals(
                AccountStatusEnum.NORMAL.getStatus(), baseAccountService.getStatus(p1Address));

        // 7. 注销普通用户账户
        requestId = voteModeGovernManager.requestCancelAccount(endUser2Keypair.getAddress());
        voteModeGovernManager.changeCredentials(governanceUser2Keypair);
        voteModeGovernManager.vote(requestId, true);
        voteModeGovernManager.changeCredentials(governanceUser3Keypair);
        voteModeGovernManager.vote(requestId, true);
        Assertions.assertTrue(governance.passed(requestId));
        tr = voteModeGovernManager.cancelAccount(requestId, endUser2Keypair.getAddress());
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertEquals(
                AccountStatusEnum.CLOSED.getStatus(), baseAccountService.getStatus(p1Address));
        Assertions.assertTrue(!voteModeGovernManager.hasAccount(endUser2Keypair.getAddress()));

        // 8. 重置治理合约的投票阈值
        requestId = voteModeGovernManager.requestResetThreshold(1);
        voteModeGovernManager.changeCredentials(governanceUser2Keypair);
        voteModeGovernManager.vote(requestId, true);
        voteModeGovernManager.changeCredentials(governanceUser3Keypair);
        voteModeGovernManager.vote(requestId, true);
        Assertions.assertTrue(governance.passed(requestId));
        // threshold: 4->1
        tr = voteModeGovernManager.resetThreshold(requestId, 1);
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertEquals(1, governance.getWeightInfo().getValue3().intValue());

        // 9. 删除治理账户
        voteModeGovernManager.changeCredentials(governanceUser2Keypair);
        requestId =
                voteModeGovernManager.requestRemoveGovernAccount(
                        governanceUser3Keypair.getAddress());
        // after step 8, threshold: 4->1.  2>1, vote passed
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
        // after removed, weight should be 0.
        Assertions.assertEquals(
                0,
                governance
                        .getVoteWeight(
                                voteModeGovernManager
                                        .getUserAccount(governanceUser3Keypair.getAddress())
                                        .getContractAddress())
                        .intValue());

        // 10. 添加治理账户
        requestId =
                voteModeGovernManager.requestAddGovernAccount(
                        governanceUser3Keypair.getAddress(), 5);
        voteModeGovernManager.vote(requestId, true);
        // after step 8, threshold: 4->1.  2>1, vote passed
        Assertions.assertTrue(governance.passed(requestId));
        tr =
                voteModeGovernManager.addGovernAccount(
                        requestId, governanceUser3Keypair.getAddress(), 5);
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertEquals(
                5,
                governance
                        .getVoteWeight(
                                voteModeGovernManager
                                        .getUserAccount(governanceUser3Keypair.getAddress())
                                        .getContractAddress())
                        .intValue());
    }
}
