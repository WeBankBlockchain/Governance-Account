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
import com.webank.blockchain.acct.gov.contract.WEGovernance;
import com.webank.blockchain.acct.gov.enums.AccountStatusEnum;
import com.webank.blockchain.acct.gov.factory.AccountGovernManagerFactory;
import com.webank.blockchain.acct.gov.manager.GovernAccountInitializer;
import com.webank.blockchain.acct.gov.manager.VoteModeGovernManager;
import com.webank.blockchain.acct.gov.service.BaseAccountService;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.fisco.bcos.web3j.abi.datatypes.Address;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tx.gas.ContractGasProvider;
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
    @Autowired protected Web3j web3j;
    @Autowired protected Credentials credentials;
    @Autowired protected ContractGasProvider contractGasProvider;

    // @Test
    // create govern account of admin by user, and set the address in application.properties
    public void createGovernAcct() throws Exception {
        AccountGovernManagerFactory factory =
                new AccountGovernManagerFactory(web3j, credentials, contractGasProvider);
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
        String acctMgrAddr = govern.getAccountManager().send();
        AccountManager accountManager =
                AccountManager.load(acctMgrAddr, web3j, u, contractGasProvider);
        Assertions.assertTrue(accountManager.hasAccount(u1.getAddress()).send());
        System.out.println("acctmanager address is " + acctMgrAddr);
        System.out.println("u1 address is " + u1.getAddress());

        Assertions.assertTrue(accountManager.hasAccount(u1.getAddress()).send());
        // prepare other govern acct
        WEGovernance governanceU1 =
                WEGovernance.load(govern.getContractAddress(), web3j, u1, contractGasProvider);
        WEGovernance governanceU2 =
                WEGovernance.load(govern.getContractAddress(), web3j, u2, contractGasProvider);
        governAccountInitializer.setGovernance(govern);
        governAccountInitializer.setAccountManager(accountManager);
        voteModeGovernManager.setGovernance(govern);
        voteModeGovernManager.setAccountManager(accountManager);

        // do create
        String p1Address = governAccountInitializer.createAccount(p1.getAddress());
        Assertions.assertNotNull(p1Address);
        Assertions.assertTrue(accountManager.hasAccount(p1.getAddress()).send());

        // set credential
        Assertions.assertEquals(1, govern._mode().send().intValue());
        BigInteger requestId =
                voteModeGovernManager.requestResetAccount(p2.getAddress(), p1.getAddress());
        voteModeGovernManager.vote(requestId, true);
        voteModeGovernManager.changeCredentials(u1);
        voteModeGovernManager.vote(requestId, true);
        voteModeGovernManager.changeCredentials(u);
        governanceU2.vote(requestId, true).send();
        Assertions.assertTrue(govern.passed(requestId).send());
        Assertions.assertTrue(
                govern.requestReady(
                                requestId,
                                BigInteger.valueOf(2),
                                p1.getAddress(),
                                p2.getAddress(),
                                BigInteger.ZERO)
                        .send());
        TransactionReceipt tr =
                voteModeGovernManager.resetAccount(requestId, p2.getAddress(), p1.getAddress());
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertTrue(!accountManager.hasAccount(p1.getAddress()).send());
        Assertions.assertTrue(accountManager.hasAccount(p2.getAddress()).send());

        // freeze Account
        requestId = voteModeGovernManager.requestFreezeAccount(p2.getAddress());
        governanceU1.vote(requestId, true).send();
        governanceU2.vote(requestId, true).send();
        Assertions.assertTrue(govern.passed(requestId).send());
        tr = voteModeGovernManager.freezeAccount(requestId, p2.getAddress());
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertEquals(
                AccountStatusEnum.FROZEN.getStatus(), baseAccountService.getStatus(p2, p1Address));

        // unfreeze Account
        requestId = voteModeGovernManager.requestUnfreezeAccount(p2.getAddress());
        governanceU1.vote(requestId, true).send();
        governanceU2.vote(requestId, true).send();
        Assertions.assertTrue(govern.passed(requestId).send());
        tr = voteModeGovernManager.unfreezeAccount(requestId, p2.getAddress());
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertEquals(
                AccountStatusEnum.NORMAL.getStatus(), baseAccountService.getStatus(p2, p1Address));

        // cancel Account
        requestId = voteModeGovernManager.requestCancelAccount(p2.getAddress());
        governanceU1.vote(requestId, true).send();
        governanceU2.vote(requestId, true).send();
        Assertions.assertTrue(govern.passed(requestId).send());
        tr = voteModeGovernManager.cancelAccount(requestId, p2.getAddress());
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertEquals(
                AccountStatusEnum.CLOSED.getStatus(), baseAccountService.getStatus(p2, p1Address));
        Assertions.assertTrue(!accountManager.hasAccount(p2.getAddress()).send());

        // set Govern account threshold
        requestId = voteModeGovernManager.requestResetThreshold(1);
        governanceU1.vote(requestId, true).send();
        governanceU2.vote(requestId, true).send();
        Assertions.assertTrue(govern.passed(requestId).send());
        tr = voteModeGovernManager.resetThreshold(requestId, 1);
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertEquals(1, govern.getWeightInfo().send().getValue3().intValue());

        // remove govern account
        requestId = voteModeGovernManager.requestRemoveGovernAccount(u2.getAddress());
        governanceU1.vote(requestId, true).send();
        Assertions.assertTrue(govern.passed(requestId).send());
        Assertions.assertTrue(
                govern.requestReady(
                                requestId,
                                BigInteger.valueOf(11),
                                u2.getAddress(),
                                Address.DEFAULT.getValue(),
                                BigInteger.ZERO)
                        .send());
        tr = voteModeGovernManager.removeGovernAccount(requestId, u2.getAddress());
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertEquals(
                1,
                govern.getVoteWeight(accountManager.getUserAccount(u.getAddress()).send())
                        .send()
                        .intValue());
        Assertions.assertEquals(
                0,
                govern.getVoteWeight(accountManager.getUserAccount(u2.getAddress()).send())
                        .send()
                        .intValue());

        // add govern account
        requestId = voteModeGovernManager.requestAddGovernAccount(u2.getAddress(), 5);
        governanceU1.vote(requestId, true).send();
        Assertions.assertTrue(govern.passed(requestId).send());
        tr = voteModeGovernManager.addGovernAccount(requestId, u2.getAddress(), 5);
        Assertions.assertEquals("0x0", tr.getStatus());
        Assertions.assertEquals(
                1,
                govern.getVoteWeight(accountManager.getUserAccount(u.getAddress()).send())
                        .send()
                        .intValue());
        Assertions.assertEquals(
                5,
                govern.getVoteWeight(accountManager.getUserAccount(u2.getAddress()).send())
                        .send()
                        .intValue());
    }
}
