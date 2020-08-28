package com.webank.blockchain.acct.gov;

import com.webank.blockchain.acct.gov.tool.CredentialUtils;
import javax.annotation.PostConstruct;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.tx.gas.ContractGasProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BaseTests {
    @Autowired protected Web3j web3j;
    @Autowired protected ContractGasProvider contractGasProvider;

    protected Credentials u;
    protected Credentials u1;
    protected Credentials u2;
    protected Credentials p1;
    protected Credentials p2;
    protected Credentials p3;

    @PostConstruct
    public void init() throws Exception {
        u = CredentialUtils.loadKey("user.jks", "123456", "123456");
        u1 = CredentialUtils.loadKey("user1.jks", "123456", "123456");
        u2 = CredentialUtils.loadKey("user2.jks", "123456", "123456");
        p1 = CredentialUtils.loadKey("p1.jks", "123456", "123456");
        p2 = CredentialUtils.loadKey("p2.jks", "123456", "123456");
        p3 = CredentialUtils.loadKey("p3.jks", "123456", "123456");
    }
}
