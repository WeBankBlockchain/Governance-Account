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
package com.webank.blockchain.gov.acct.enums;

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
    OPER_CREATE_ACCOUNT(BigInteger.valueOf(1), "create account"),
    OPER_CHANGE_CREDENTIAL(BigInteger.valueOf(2), "change credential"),
    OPER_FREEZE_ACCOUNT(BigInteger.valueOf(3), "freeze account"),
    OPER_UNFREEZE_ACCOUNT(BigInteger.valueOf(4), "unfreeze account"),
    OPER_CANCEL_ACCOUNT(BigInteger.valueOf(5), "cancel account"),
    OPER_RESET_MANAGER_TYPE(BigInteger.valueOf(6), "reset manager type"),
    OPER_RESET_THRESHOLD(BigInteger.valueOf(10), "reset threshold"),
    OPER_RESET_WEIGHT(BigInteger.valueOf(11), "reset weight"),
    OPER_ADD_WEIGHT(BigInteger.valueOf(12), "add weight"),
    OPER_RM_WEIGHT(BigInteger.valueOf(13), "remove weight"),
    OPER_RESET_ACCOUNT_MANAGER(BigInteger.valueOf(50), "reset account manager");

    private BigInteger type;
    private String name;

    public static String getNameByStatics(int type) {
        RequestEnum[] enums = values();
        for (RequestEnum e : enums) {
            if (e.type.intValue() == type) {
                return e.getName();
            }
        }
        return "undefined";
    }
}
