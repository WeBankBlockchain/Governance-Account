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
package com.webank.blockchain.acct.gov.enums;

import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * RequestEnum @Description: RequestEnum
 *
 * @author maojiayu
 * @data Feb 22, 2020 8:46:46 PM
 */
@AllArgsConstructor
@Getter
public enum RequestEnum {
    OPER_CREATE_ACCOUNT(BigInteger.valueOf(1)),
    OPER_CHANGE_CREDENTIAL(BigInteger.valueOf(2)),
    OPER_FREEZE_ACCOUNT(BigInteger.valueOf(3)),
    OPER_UNFREEZE_ACCOUNT(BigInteger.valueOf(4)),
    OPER_CANCEL_ACCOUNT(BigInteger.valueOf(5)),
    OPER_RESET_MANAGER_TYPE(BigInteger.valueOf(6)),
    OPER_RESET_THRESHOLD(BigInteger.valueOf(10)),
    OPER_RESET_WEIGHT(BigInteger.valueOf(11)),
    OPER_ADD_WEIGHT(BigInteger.valueOf(12)),
    OPER_RM_WEIGHT(BigInteger.valueOf(13)),
    OPER_RESET_ACCOUNT_MANAGER(BigInteger.valueOf(50));
    private BigInteger type;
}
