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
package com.webank.blockchain.acct.gov.chain;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.webank.blockchain.acct.gov.BaseTests;
import com.webank.blockchain.acct.gov.tool.JacksonUtils;
import java.io.IOException;
import java.math.BigInteger;
import org.fisco.bcos.web3j.protocol.core.DefaultBlockParameter;
import org.fisco.bcos.web3j.protocol.core.methods.response.BcosBlock;
import org.fisco.bcos.web3j.protocol.core.methods.response.GroupList;
import org.fisco.bcos.web3j.protocol.core.methods.response.GroupPeers;
import org.fisco.bcos.web3j.protocol.core.methods.response.NodeIDList;
import org.fisco.bcos.web3j.protocol.core.methods.response.ObserverList;
import org.fisco.bcos.web3j.protocol.core.methods.response.Peers;
import org.fisco.bcos.web3j.protocol.core.methods.response.SealerList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * ChainInfoTest @Description: ChainInfoTest
 *
 * @author maojiayu
 * @data Jan 21, 2020 3:48:23 PM
 */
public class ChainInfoTest extends BaseTests {

    @Test
    public void testChainInfo() throws IOException {
        long id = web3j.getBlockNumber().send().getBlockNumber().longValue();
        System.out.println(id);
        Assertions.assertTrue(id >= 0);
    }

    @Test
    public void getConsensusStatus() throws Exception {
        String consensusStatus = web3j.getConsensusStatus().sendForReturnString();
        assertNotNull(consensusStatus);
    }

    @Test
    public void getSyncStatus() throws Exception {
        String syncStatus = web3j.getSyncStatus().sendForReturnString();
        assertNotNull(syncStatus);
    }

    @Test
    public void peers() throws Exception {
        Peers peers = web3j.getPeers().send();
        assertNotNull(peers.getPeers());
    }

    @Test
    public void groupPeers() throws Exception {
        GroupPeers groupPeers = web3j.getGroupPeers().send();
        assertNotNull(groupPeers.getGroupPeers());
    }

    @Test
    public void groupList() throws Exception {
        GroupList groupList = web3j.getGroupList().send();
        assertNotNull(groupList.getGroupList());
    }

    @Test
    public void getSealerList() throws Exception {
        SealerList sealerList = web3j.getSealerList().send();
        assertNotNull(sealerList.getSealerList());
    }

    @Test
    public void getObserverList() throws Exception {
        ObserverList observerList = web3j.getObserverList().send();
        assertNotNull(observerList.getObserverList());
    }

    @Test
    public void getNodeIDList() throws Exception {
        NodeIDList nodeIDList = web3j.getNodeIDList().send();
        assertNotNull(nodeIDList.getNodeIDList());
    }

    @Test
    public void getBlock() throws IOException {
        BcosBlock bcosBlock =
                web3j.getBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.ZERO), true).send();
        System.out.println(JacksonUtils.toJson("bcosBlock: " + bcosBlock.getBlock()));
        assertNotNull(bcosBlock.getBlock());
    }
}
