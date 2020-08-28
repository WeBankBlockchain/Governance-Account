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
package com.webank.blockchain.acct.gov.account;

import com.webank.blockchain.acct.gov.BaseTests;
import com.webank.blockchain.acct.gov.contract.AccountManager;
import org.springframework.util.Assert;

/**
 * AccountManagerTest @Description: AccountManagerTest
 *
 * @author maojiayu
 * @data Jan 22, 2020 2:34:17 PM
 */
public class AccountManagerTest extends BaseTests {
    public static String accountManagerAddr = "0x091db17c50b1319b2017970ab9d0aad6933a31ed";
    public static String aclGuardAddr = "0xb306e6e532c760d7d9e1c83955562748d846f673";

    // @Test
    public void testNewAcctMgr() throws Exception {
        AccountManager accountManager =
                AccountManager.deploy(web3j, u1, contractGasProvider).send();
        System.out.println("account manager address: " + accountManager.getContractAddress());
        Assert.notNull(accountManager, "acctManager can't be null.");
    }
}
