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

import com.webank.blockchain.gov.acct.enums.RequestEnum;
import java.math.BigInteger;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple8;

/**
 * VoteRequestInfo @Description: VoteRequestInfo
 *
 * @author maojiayu
 * @data Jul 24, 2020 6:13:55 PM
 */
@Data
@Accessors(chain = true)
@Slf4j
public class VoteRequestInfo {
    private BigInteger requestId;
    private String requestAddress = "";
    private int threshold;
    private int weight;
    private int txType;
    private int status;
    private String newAddress = "";
    private int newValue;

    public VoteRequestInfo forward(
            Tuple8<
                            BigInteger,
                            String,
                            BigInteger,
                            BigInteger,
                            BigInteger,
                            BigInteger,
                            String,
                            BigInteger>
                    t) {
        this.requestId = t.getValue1();
        this.requestAddress = t.getValue2();
        this.threshold = t.getValue3().intValue();
        this.weight = t.getValue4().intValue();
        this.txType = t.getValue5().intValue();
        this.status = t.getValue6().intValue();
        this.newAddress = t.getValue7();
        this.newValue = t.getValue8().intValue();
        return this;
    }

    public void print() {
        log.info(
                "the current vote info: \n -------------------------------------- \n request id [ {} ] \n request address is [ {} ] \n vote type: [ {} ] \n threshod is [ {} ] \n weight is [ {} ] \n vote passed? [ {} ] \n",
                requestId,
                requestAddress,
                RequestEnum.getNameByStatics(txType),
                threshold,
                weight,
                status == 1);
    }
}
