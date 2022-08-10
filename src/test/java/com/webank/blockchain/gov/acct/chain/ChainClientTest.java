package com.webank.blockchain.gov.acct.chain;

import com.webank.blockchain.gov.acct.BaseTests;
import java.math.BigInteger;
import org.fisco.bcos.sdk.jni.common.JniException;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.client.protocol.response.*;
import org.fisco.bcos.sdk.v3.config.exceptions.ConfigException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ChainClientTest extends BaseTests {
    @Autowired Client client;

    @Test
    public void testClient() throws ConfigException, JniException {

        // test getBlockNumber
        BlockNumber blockNumber = client.getBlockNumber();
        System.out.println("blockNumber=" + blockNumber.getBlockNumber());

        // test getBlockByNumber only header
        BcosBlock onlyHeader = client.getBlockByNumber(BigInteger.ZERO, false, false);
        System.out.println("genesis header=" + onlyHeader.getBlock());

        // test getBlockByNumber
        BcosBlock block = client.getBlockByNumber(BigInteger.ZERO, false, false);
        System.out.println("genesis block=" + block.getBlock());
        // getBlockByHash
        BcosBlock block0 = client.getBlockByHash(block.getBlock().getHash(), false, false);
        System.out.println("genesis block=" + block0.getBlock());
        Assertions.assertEquals(block.getBlock(), block0.getBlock());

        // get SealerList
        SealerList sealerList = client.getSealerList();
        System.out.println(sealerList.getSealerList());

        // get observerList
        ObserverList observerList = client.getObserverList();
        System.out.println(observerList.getObserverList());

        // get pbftView
        PbftView pbftView = client.getPbftView();
        System.out.println(pbftView.getPbftView());

        // get getPendingTxSize
        PendingTxSize pendingTxSize = client.getPendingTxSize();
        System.out.println(pendingTxSize.getPendingTxSize());

        // get getSystemConfigByKey
        SystemConfig tx_count_limit = client.getSystemConfigByKey("tx_count_limit");
        System.out.println(tx_count_limit.getSystemConfig());

        // get getTotalTransactionCount
        TotalTransactionCount totalTransactionCount = client.getTotalTransactionCount();
        System.out.println(totalTransactionCount.getTotalTransactionCount());

        // get getPeers
        Peers peers = client.getPeers();
        System.out.println(peers.getPeers());

        // get NodeInfo
        BcosGroupInfo.GroupInfo groupInfo = client.getGroupInfo().getResult();
        System.out.println(groupInfo);

        // get getSyncStatus
        SyncStatus syncStatus = client.getSyncStatus();
        System.out.println(syncStatus.getSyncStatus().toString());
    }
}
