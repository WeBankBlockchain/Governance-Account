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
package com.webank.blockchain.acct.gov.service;

import com.webank.blockchain.acct.gov.contract.BaseAccount;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.springframework.stereotype.Service;

/**
 * BaseAccountManager @Description: BaseAccountManager
 *
 * @author maojiayu
 * @data Mar 2, 2020 11:54:49 AM
 */
@Service
public class BaseAccountService extends Web3jBasicService {

    public BaseAccount getBaseAccount(Credentials credentials, String contractAddress) {
        return BaseAccount.load(contractAddress, web3j, credentials, contractGasProvider);
    }

    public int getStatus(Credentials credentials, String contractAddress) throws Exception {
        BaseAccount ba = getBaseAccount(credentials, contractAddress);
        return ba._status().send().intValue();
    }
}
