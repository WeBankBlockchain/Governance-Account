/*
 * Copyright 2014-2020  [fisco-dev]
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package com.webank.blockchain.acct.gov.vo;

import java.math.BigInteger;
import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * WeightInfo @Description: WeightInfo
 *
 * @author maojiayu
 * @data Aug 6, 2020 11:46:15 AM
 */
@Data
@Accessors(chain = true)
public class WeightInfo {
    private List<String> addressList;
    private List<BigInteger> weightList;
    private int threshold;
    private int acctType;
}
