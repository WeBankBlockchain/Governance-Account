package com.webank.blockchain.gov.acct.service;

import lombok.Data;
import lombok.experimental.Accessors;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wesleywang
 * @Description:
 * @date 2020/11/9
 */
@Data
@Accessors(chain = true)
public class JavaSDKBasicService {

    @Autowired
    protected Client client;
    @Autowired
    protected CryptoKeyPair credentials;
}
