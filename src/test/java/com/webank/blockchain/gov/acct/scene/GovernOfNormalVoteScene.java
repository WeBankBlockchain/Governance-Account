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
import com.webank.blockchain.gov.acct.enums.AccountStatusEnum;
import com.webank.blockchain.gov.acct.manager.GovernAccountInitializer;
import com.webank.blockchain.gov.acct.manager.VoteModeGovernManager;
import com.webank.blockchain.gov.acct.service.BaseAccountService;
import com.webank.blockchain.gov.acct.tool.JacksonUtils;
import com.webank.blockchain.gov.acct.vo.GovernAccountGroup;
import com.webank.blockchain.gov.acct.vo.GovernUser;
import java.math.BigInteger;
import org.fisco.bcos.sdk.abi.datatypes.Address;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * GovernOfNormalVoteScene @Description: GovernOfNormalVoteScene
 *
 * @author maojiayu
 * @data Feb 22, 2020 4:36:34 PM
 */
public class GovernOfNormalVoteScene extends BaseTests {
    @Autowired private GovernAccountInitializer governAccountInitializer;
    @Autowired private BaseAccountService baseAccountService;
    @Autowired private VoteModeGovernManager voteModeGovernManager;

    @Test
    public void testScene() throws Exception {
        GovernAccountGroup governAccountGroup = new GovernAccountGroup();
        governAccountGroup.setThreshold(2);
        GovernUser governUser1 = new GovernUser("user1", governanceUser1Keypair.getAddress());
        governAccountGroup.addGovernUser(governUser1);
        GovernUser governUser2 = new GovernUser("user2", governanceUser2Keypair.getAddress());
        governAccountGroup.addGovernUser(governUser2);
        GovernUser governUser3 = new GovernUser("user3", governanceUser3Keypair.getAddress());
        governAccountGroup.addGovernUser(governUser3);
        WEGovernance govern = governAccountInitializer.createGovernAccount(governAccountGroup);

        System.out.println(govern.getContractAddress());
        Assertions.assertNotNull(govern);
        String acctMgrAddr = govern.getAccountManager();
        AccountManager accountManager =
                AccountManager.load(acctMgrAddr, client, governanceUser1Keypair);

        Assertions.assertTrue(accountManager.hasAccount(governanceUser2Keypair.getAddress()));
        // prepare other govern acct
        WEGovernance governanceU1 =
                WEGovernance.load(govern.getContractAddress(), client, governanceUser2Keypair);
        WEGovernance governanceU2 =
                WEGovernance.load(govern.getContractAddress(), client, governanceUser3Keypair);
        governAccountInitializer.setGovernance(govern);
        governAccountInitializer.setAccountManager(accountManager);
        voteModeGovernManager.setGovernance(govern);
        voteModeGovernManager.setAccountManager(accountManager);

        // do create
        String p1Address = governAccountInitializer.createAccount(endUser1Keypair.getAddress());
        Assertions.assertNotNull(p1Address);
        Assertions.assertTrue(accountManager.hasAccount(endUser1Keypair.getAddress()));

        // set credential
        Assertions.assertEquals(1, govern._mode().intValue());
        voteModeGovernManager.changeCredentials(governanceUser1Keypair);
        BigInteger requestId =
                voteModeGovernManager.requestResetAccount(
                        endUser2Keypair.getAddress(), endUser1Keypair.getAddress());
        System.out.println(
                "vote info "
                        + JacksonUtils.toJson(voteModeGovernManager.getVoteRequestInfo(requestId)));
        voteModeGovernManager.vote(requestId, true);
        voteModeGovernManager.changeCredentials(governanceUser2Keypair);
        voteModeGovernManager.vote(requestId, true);
        voteModeGovernManager.changeCredentials(governanceUser1Keypair);
        TransactionReceipt tr = governanceU1.vote(requestId, true);
        Assertions.assertEquals("0x0", tr.getStatus());
        governanceU2.vote(requestId, true);
        Assertions.assertTrue(govern.passed(requestId));
        Assertions.assertTrue(
                govern.requestReady(
                        requestId,
                        BigInteger.valueOf(2),
                        endUser1Keypair.getAddress(),
                        endUser2Keypair.getAddress(),
                        BigInteger.ZERO));
        tr =
                voteModeGovernManager.resetAccount(
                        requestId, endUser2Keypair.getAddress(), endUser1Keypair.getAddress());
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertTrue(!accountManager.hasAccount(endUser1Keypair.getAddress()));
        Assertions.assertTrue(accountManager.hasAccount(endUser2Keypair.getAddress()));

        // freeze Account
        requestId = voteModeGovernManager.requestFreezeAccount(endUser2Keypair.getAddress());
        governanceU1.vote(requestId, true);
        governanceU2.vote(requestId, true);
        Assertions.assertTrue(govern.passed(requestId));
        tr = voteModeGovernManager.freezeAccount(requestId, endUser2Keypair.getAddress());
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertEquals(
                AccountStatusEnum.FROZEN.getStatus(), baseAccountService.getStatus(p1Address));

        // unfreeze Account
        requestId = voteModeGovernManager.requestUnfreezeAccount(endUser2Keypair.getAddress());
        governanceU1.vote(requestId, true);
        governanceU2.vote(requestId, true);
        Assertions.assertTrue(govern.passed(requestId));
        tr = voteModeGovernManager.unfreezeAccount(requestId, endUser2Keypair.getAddress());
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertEquals(
                AccountStatusEnum.NORMAL.getStatus(), baseAccountService.getStatus(p1Address));

        // cancel Account
        requestId = voteModeGovernManager.requestCancelAccount(endUser2Keypair.getAddress());
        governanceU1.vote(requestId, true);
        governanceU2.vote(requestId, true);
        Assertions.assertTrue(govern.passed(requestId));
        tr = voteModeGovernManager.cancelAccount(requestId, endUser2Keypair.getAddress());
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertEquals(
                AccountStatusEnum.CLOSED.getStatus(), baseAccountService.getStatus(p1Address));
        Assertions.assertTrue(!accountManager.hasAccount(endUser2Keypair.getAddress()));

        // set Govern account threshold
        requestId = voteModeGovernManager.requestResetThreshold(1);
        governanceU1.vote(requestId, true);
        governanceU2.vote(requestId, true);
        Assertions.assertTrue(govern.passed(requestId));
        tr = voteModeGovernManager.resetThreshold(requestId, 1);
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertEquals(1, govern.getWeightInfo().getValue3().intValue());

        // remove govern account
        requestId =
                voteModeGovernManager.requestRemoveGovernAccount(
                        governanceUser3Keypair.getAddress());
        governanceU1.vote(requestId, true);
        Assertions.assertTrue(govern.passed(requestId));
        Assertions.assertTrue(
                govern.requestReady(
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
                1,
                govern.getVoteWeight(
                                accountManager.getUserAccount(governanceUser1Keypair.getAddress()))
                        .intValue());

        Assertions.assertEquals(
                0,
                govern.getVoteWeight(
                                accountManager.getUserAccount(governanceUser3Keypair.getAddress()))
                        .intValue());

        // add govern account
        requestId =
                voteModeGovernManager.requestAddGovernAccount(governanceUser3Keypair.getAddress());
        governanceU1.vote(requestId, true);
        Assertions.assertTrue(govern.passed(requestId));
        tr = voteModeGovernManager.addGovernAccount(requestId, governanceUser3Keypair.getAddress());
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertEquals(
                1,
                govern.getVoteWeight(
                                accountManager.getUserAccount(governanceUser1Keypair.getAddress()))
                        .intValue());

        Assertions.assertEquals(
                1,
                govern.getVoteWeight(
                                accountManager.getUserAccount(governanceUser3Keypair.getAddress()))
                        .intValue());
    }
}
