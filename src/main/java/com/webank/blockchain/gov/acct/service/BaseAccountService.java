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
package com.webank.blockchain.gov.acct.service;

import com.webank.blockchain.gov.acct.contract.BaseAccount;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * BaseAccountManager @Description: BaseAccountManager
 *
 * @author maojiayu
 * @data Mar 2, 2020 11:54:49 AM
 */
@Service
public class BaseAccountService {

    @Autowired private Client client;
    @Autowired private CryptoKeyPair credentials;

    public BaseAccount getBaseAccount(String contractAddress) {
        return BaseAccount.load(contractAddress, client, credentials);
    }

    public int getStatus(String contractAddress) throws Exception {
        BaseAccount ba = getBaseAccount(contractAddress);
        return ba._status().intValue();
    }
}
