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
package com.webank.blockchain.gov.acct.tool;

import org.apache.commons.codec.binary.StringUtils;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes16;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes2;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32;

public class BytesUtils {

    public static Bytes32 stringToBytes32(String string) {
        byte[] byteValue = string.getBytes();
        byte[] byteValueLen32 = new byte[32];
        System.arraycopy(byteValue, 0, byteValueLen32, 0, byteValue.length);
        return new Bytes32(byteValueLen32);
    }

    public static String Bytes32ToString(Bytes32 b) {
        return StringUtils.newStringUsAscii(b.getValue()).trim();
    }

    public static Bytes16 stringToBytes16(String string) {
        byte[] byteValue = string.getBytes();
        byte[] byteValueLen16 = new byte[16];
        System.arraycopy(byteValue, 0, byteValueLen16, 0, byteValue.length);
        return new Bytes16(byteValueLen16);
    }

    public static String Bytes16ToString(Bytes16 b) {
        return StringUtils.newStringUsAscii(b.getValue()).trim();
    }

    public static Bytes2 stringToBytes2(String string) {
        byte[] byteValue = string.getBytes();
        byte[] byteValueLen2 = new byte[2];
        System.arraycopy(byteValue, 0, byteValueLen2, 0, byteValue.length);
        return new Bytes2(byteValueLen2);
    }

    public static String Bytes2ToString(Bytes2 b) {
        return StringUtils.newStringUsAscii(b.getValue()).trim();
    }
}
