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
package com.webank.blockchain.gov.acct.manager;

import com.webank.blockchain.gov.acct.BaseTests;
import com.webank.blockchain.gov.acct.contract.AdminGovernBuilder;
import com.webank.blockchain.gov.acct.contract.WEGovernance;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * GovernAdminManagerTest @Description: GovernAdminManagerTest
 *
 * @author maojiayu
 * @data Feb 21, 2020 8:49:25 PM
 */
public class GovernAdminManagerTest extends BaseTests {
    @Autowired private GovernAccountInitializer manager;

    @Test
    // create govern account of admin by user
    public void testCreate() throws Exception {
        AdminGovernBuilder a = AdminGovernBuilder.deploy(client, governanceUser1Keypair);
        System.out.println("AdminGovernBuilder " + a.getContractAddress());
        WEGovernance govern = manager.createGovernAccount(governanceUser1Keypair);
        System.out.println(govern.getContractAddress());
        Assertions.assertNotNull(govern);
    }
}
