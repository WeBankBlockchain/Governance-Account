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
package com.webank.blockchain.gov.acct.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * System Environment Config. @Description: SystemEnvironmentConfig
 *
 * @author maojiayu
 * @data Dec 28, 2018 5:21:48 PM
 */
@Configuration
@ConfigurationProperties("system")
@Data
public class SystemEnvironmentConfig {
    private String nodeStr;
    private int groupId;
    private String configPath;
    private int encryptType = 0;
    private String hexPrivateKey;
    private boolean defaultGovernanceEnabled=false;
}
