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

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * GovernUser @Description: GovernUser
 *
 * @author maojiayu
 * @data Feb 8, 2021 3:55:04 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GovernUser extends User {
    private int weight = 1;

    public GovernUser(String externalAccount) {
        super(externalAccount);
    }

    public GovernUser(String name, String externalAccount) {
        super(name, externalAccount);
    }

    public GovernUser(String externalAccount, int weight) {
        super(externalAccount);
        this.weight = weight;
    }

    public GovernUser(String name, String externalAccount, int weight) {
        super(name, externalAccount);
        this.weight = weight;
    }

    public String toString() {
        return "GovernUser["
                + name
                + "]: "
                + "weight is ["
                + weight
                + "] external account is ["
                + externalAccount
                + "]";
    }
}
