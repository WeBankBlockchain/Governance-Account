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
package com.webank.blockchain.gov.acct.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * UserStaticsEnum @Description: UserStaticsEnum
 *
 * @author maojiayu
 * @data Feb 20, 2020 5:17:10 PM
 */
@AllArgsConstructor
@Getter
public enum UserStaticsEnum {
    // 0-none, 1- social vote, 2-governance, 3-both 1 or 2;

    NONE(0),
    SOCIAL(1);

    private int statics;
}
