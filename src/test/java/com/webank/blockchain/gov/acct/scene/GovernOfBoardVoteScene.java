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
import com.webank.blockchain.gov.acct.factory.AccountGovernManagerFactory;
import com.webank.blockchain.gov.acct.manager.GovernAccountInitializer;
import com.webank.blockchain.gov.acct.manager.VoteModeGovernManager;
import com.webank.blockchain.gov.acct.service.BaseAccountService;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.fisco.bcos.sdk.abi.datatypes.Address;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * GovernOfBoardVoteScene @Description: GovernOfBoardVoteScene
 *
 * @author maojiayu
 * @data Feb 22, 2020 4:36:34 PM
 */
public class GovernOfBoardVoteScene extends BaseTests {
    @Autowired private GovernAccountInitializer governAccountInitializer;
    @Autowired private BaseAccountService baseAccountService;
    @Autowired private VoteModeGovernManager voteModeGovernManager;
    @Autowired protected Client client;
    @Autowired protected CryptoKeyPair credentials;

    // @Test
    // create govern account of admin by user, and set the address in application.properties
    public void createGovernAcct() throws Exception {
        AccountGovernManagerFactory factory =
                new AccountGovernManagerFactory(client, credentials);
        GovernAccountInitializer initializer = factory.newGovernAccountInitializer();
        List<String> list = new ArrayList<>();
        list.add(u.getAddress());
        list.add(u1.getAddress());
        list.add(u2.getAddress());
        List<BigInteger> weights = new ArrayList<>();
        weights.add(BigInteger.valueOf(1));
        weights.add(BigInteger.valueOf(2));
        weights.add(BigInteger.valueOf(3));
        WEGovernance govern = initializer.createGovernAccount(list, weights, 4);
        // WEGovernance govern = governAccountInitializer.createGovernAccount(list, weights, 4);
        System.out.println(govern.getContractAddress());
        Assertions.assertNotNull(govern);
    }

    @Test
    public void testScene() throws Exception {
        List<String> list = new ArrayList<>();
        list.add(u.getAddress());
        list.add(u1.getAddress());
        list.add(u2.getAddress());
        List<BigInteger> weights = new ArrayList<>();
        weights.add(BigInteger.valueOf(1));
        weights.add(BigInteger.valueOf(2));
        weights.add(BigInteger.valueOf(3));
        WEGovernance govern = governAccountInitializer.createGovernAccount(list, weights, 4);
        System.out.println(govern.getContractAddress());
        Assertions.assertNotNull(govern);
        String acctMgrAddr = govern.getAccountManager();
        AccountManager accountManager =
                AccountManager.load(acctMgrAddr, client, u);
        Assertions.assertTrue(accountManager.hasAccount(u1.getAddress()));
        System.out.println("acctmanager address is " + acctMgrAddr);
        System.out.println("u1 address is " + u1.getAddress());

        Assertions.assertTrue(accountManager.hasAccount(u1.getAddress()));
        // prepare other govern acct
        WEGovernance governanceU1 =
                WEGovernance.load(govern.getContractAddress(), client, u1);
        WEGovernance governanceU2 =
                WEGovernance.load(govern.getContractAddress(), client, u2);
        governAccountInitializer.setGovernance(govern);
        governAccountInitializer.setAccountManager(accountManager);
        voteModeGovernManager.setGovernance(govern);
        voteModeGovernManager.setAccountManager(accountManager);

        // do create
        String p1Address = governAccountInitializer.createAccount(p1.getAddress());
        Assertions.assertNotNull(p1Address);
        Assertions.assertTrue(accountManager.hasAccount(p1.getAddress()));

        // set credential
        Assertions.assertEquals(1, govern._mode().intValue());
        voteModeGovernManager.changeCredentials(u);
        BigInteger requestId =
                voteModeGovernManager.requestResetAccount(p2.getAddress(), p1.getAddress());
        voteModeGovernManager.vote(requestId, true);
        voteModeGovernManager.changeCredentials(u1);
        voteModeGovernManager.vote(requestId, true);
        voteModeGovernManager.changeCredentials(u);
        governanceU2.vote(requestId, true);
        Assertions.assertTrue(govern.passed(requestId));
        Assertions.assertTrue(
                govern.requestReady(
                                requestId,
                                BigInteger.valueOf(2),
                                p1.getAddress(),
                                p2.getAddress(),
                                BigInteger.ZERO)
                        );
        TransactionReceipt tr =
                voteModeGovernManager.resetAccount(requestId, p2.getAddress(), p1.getAddress());
        Assertions.assertTrue(tr.isStatusOK());
        Assertions.assertTrue(!accountManager.hasAccount(p1.getAddress()));
        Assertions.assertTrue(accountManager.hasAccount(p2.getAddress()));

        // freeze Account
        requestId = voteModeGovernManager.requestFreezeAccount(p2.getAddress());
        governanceU1.vote(requestId, true);
        governanceU2.vote(requestId, true);
        Assertions.assertTrue(govern.passed(requestId));
        tr = voteModeGovernManager.freezeAccount(requestId, p2.getAddress());
        Assertions.assertTrue(tr.isStatusOK());
        Assertions.assertEquals(
                AccountStatusEnum.FROZEN.getStatus(), baseAccountService.getStatus(p1Address));

        // unfreeze Account
        requestId = voteModeGovernManager.requestUnfreezeAccount(p2.getAddress());
        governanceU1.vote(requestId, true);
        governanceU2.vote(requestId, true);
        Assertions.assertTrue(govern.passed(requestId));
        tr = voteModeGovernManager.unfreezeAccount(requestId, p2.getAddress());
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertEquals(
                AccountStatusEnum.NORMAL.getStatus(), baseAccountService.getStatus(p1Address));

        // cancel Account
        requestId = voteModeGovernManager.requestCancelAccount(p2.getAddress());
        governanceU1.vote(requestId, true);
        governanceU2.vote(requestId, true);
        Assertions.assertTrue(govern.passed(requestId));
        tr = voteModeGovernManager.cancelAccount(requestId, p2.getAddress());
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertEquals(
                AccountStatusEnum.CLOSED.getStatus(), baseAccountService.getStatus(p1Address));
        Assertions.assertTrue(!accountManager.hasAccount(p2.getAddress()));

        // set Govern account threshold
        requestId = voteModeGovernManager.requestResetThreshold(1);
        governanceU1.vote(requestId, true);
        governanceU2.vote(requestId, true);
        Assertions.assertTrue(govern.passed(requestId));
        tr = voteModeGovernManager.resetThreshold(requestId, 1);
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertEquals(1, govern.getWeightInfo().getValue3().intValue());

        // remove govern account
        requestId = voteModeGovernManager.requestRemoveGovernAccount(u2.getAddress());
        governanceU1.vote(requestId, true);
        Assertions.assertTrue(govern.passed(requestId));
        Assertions.assertTrue(
                govern.requestReady(
                                requestId,
                                BigInteger.valueOf(11),
                                u2.getAddress(),
                                Address.DEFAULT.getValue(),
                                BigInteger.ZERO)
                        );
        tr = voteModeGovernManager.removeGovernAccount(requestId, u2.getAddress());
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertEquals(
                1,
                govern.getVoteWeight(accountManager.getUserAccount(u.getAddress()))

                        .intValue());
        Assertions.assertEquals(
                0,
                govern.getVoteWeight(accountManager.getUserAccount(u2.getAddress()))

                        .intValue());

        // add govern account
        requestId = voteModeGovernManager.requestAddGovernAccount(u2.getAddress(), 5);
        governanceU1.vote(requestId, true);
        Assertions.assertTrue(govern.passed(requestId));
        tr = voteModeGovernManager.addGovernAccount(requestId, u2.getAddress(), 5);
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertEquals(
                1,
                govern.getVoteWeight(accountManager.getUserAccount(u.getAddress()))

                        .intValue());
        Assertions.assertEquals(
                5,
                govern.getVoteWeight(accountManager.getUserAccount(u2.getAddress()))

                        .intValue());
    }
}
