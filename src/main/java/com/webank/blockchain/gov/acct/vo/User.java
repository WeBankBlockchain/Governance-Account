/**
 * Copyright 2020 Webank.
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
package com.webank.blockchain.gov.acct.vo;

import com.google.common.collect.Lists;
import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * User @Description: User
 *
 * @author maojiayu
 * @data Feb 8, 2021 3:37:06 PM
 */
@Data
@Accessors(chain = true)
public class User {
    protected String name = "uname";
    protected String externalAccount;
    protected List<User> friends = Lists.newArrayList();

    public User(String externalAccount) {
        this.externalAccount = externalAccount;
    }

    public User(String name, String externalAccount) {
        this.name = name;
        this.externalAccount = externalAccount;
    }

    public User(String name, String externalAccount, List<User> friends) {
        this.name = name;
        this.externalAccount = externalAccount;
        this.friends = friends;
    }
}
