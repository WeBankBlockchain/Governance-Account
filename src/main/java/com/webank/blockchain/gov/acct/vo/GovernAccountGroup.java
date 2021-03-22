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
import lombok.extern.slf4j.Slf4j;

/**
 * GovernAccountGroup @Description: GovernAccountGroup
 *
 * @author maojiayu
 * @data Feb 18, 2021 3:09:14 PM
 */
@Data
@Accessors(chain = true)
@Slf4j
public class GovernAccountGroup {
    private List<GovernUser> governUserList = Lists.newArrayList();
    private int threshold = 0;
    private String name = "default group";

    public void addGovernUser(GovernUser governUser) {
        governUserList.add(governUser);
        log.info("After add governUser: " + toString());
    }

    public void addGovernUser(String name, String externalAccount) {
        GovernUser governUser = new GovernUser(name, externalAccount);
        addGovernUser(governUser);
    }

    public void addGovernUser(String name, String externalAccount, int weight) {
        GovernUser governUser = new GovernUser(name, externalAccount, weight);
        addGovernUser(governUser);
    }

    public void removeGovernUser(GovernUser governUser) {
        governUserList.removeIf(
                g -> g.getExternalAccount().equals(governUser.getExternalAccount()));
    }

    public String toString() {
        String tmp = "\n GovernAccountGroup [" + name + "] info: \n";
        int totalWeight = 0;
        for (GovernUser u : governUserList) {
            tmp += u.toString() + "\n";
            totalWeight += u.getWeight();
        }
        tmp += "threshold is [" + threshold + "], total weight is [" + totalWeight + "]\n";
        tmp +=
                "-------------------------------------------------------------------------------------------------";
        return tmp;
    }
}
