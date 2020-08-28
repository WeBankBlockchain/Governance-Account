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

import lombok.Data;
import lombok.experimental.Accessors;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.tx.gas.ContractGasProvider;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Web3jBasicService @Description: Web3jBasicService
 *
 * @author maojiayu
 * @data Mar 2, 2020 11:57:28 AM
 */
@Data
@Accessors(chain = true)
public class Web3jBasicService {
    @Autowired protected Web3j web3j;
    @Autowired protected Credentials credentials;
    @Autowired protected ContractGasProvider contractGasProvider;
}
