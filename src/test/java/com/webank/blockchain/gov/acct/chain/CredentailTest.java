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
package com.webank.blockchain.gov.acct.chain;

import com.webank.blockchain.gov.acct.BaseTests;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

/**
 * CredentailTest @Description: CredentailTest
 *
 * @author maojiayu
 * @data Jan 21, 2020 4:14:19 PM
 */
public class CredentailTest extends BaseTests {

    @Test
    public void testCredentials() {
        System.out.println(u1);
        Assert.notNull(u1, "credential must be not null");
        System.out.println(p1);
        Assert.notNull(p1, "credential must be not null");
    }
}
