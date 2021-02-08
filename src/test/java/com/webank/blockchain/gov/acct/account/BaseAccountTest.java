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
package com.webank.blockchain.gov.acct.account;

import com.webank.blockchain.gov.acct.BaseTests;
import com.webank.blockchain.gov.acct.contract.BaseAccount;
import org.junit.jupiter.api.Test;

/**
 * BaseAccountTest @Description: BaseAccountTest
 *
 * @author maojiayu
 * @data Feb 3, 2020 11:04:28 AM
 */
public class BaseAccountTest extends BaseTests {

    @Test
    public void deploy() throws Exception {
        BaseAccount account =
                BaseAccount.deploy(
                        client,
                        governanceUser2Keypair,
                        "0x9f4856c0d44415c5913ca8862cc30187f3b8c895");
        System.out.println(account.getContractAddress());
    }

    @Test
    public void freeze() throws Exception {
        BaseAccount accountP1 =
                BaseAccount.load(
                        "0x331c52cafb92cdc4df87d18b178edbfdb2f1c93c", client, endUser1Keypair);
        accountP1.freeze();
    }

    // @Test
    public void unfreeze() throws Exception {
        BaseAccount accountP1 =
                BaseAccount.load(
                        "0x331c52cafb92cdc4df87d18b178edbfdb2f1c93c", client, endUser1Keypair);
        accountP1.unfreeze();
    }

    // @Test
    public void cancel() throws Exception {
        BaseAccount accountP1 =
                BaseAccount.load(
                        "0x331c52cafb92cdc4df87d18b178edbfdb2f1c93c", client, endUser1Keypair);
        accountP1.cancel();
    }
}
