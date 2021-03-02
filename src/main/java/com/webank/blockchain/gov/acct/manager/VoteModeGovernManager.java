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
package com.webank.blockchain.gov.acct.manager;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import org.fisco.bcos.sdk.abi.ABICodecException;
import org.fisco.bcos.sdk.abi.datatypes.Address;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple3;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple8;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.transaction.codec.decode.TransactionDecoderInterface;
import org.fisco.bcos.sdk.transaction.codec.decode.TransactionDecoderService;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.fisco.bcos.sdk.transaction.model.exception.TransactionException;
import org.springframework.stereotype.Service;

import com.webank.blockchain.gov.acct.contract.WEGovernance;
import com.webank.blockchain.gov.acct.enums.RequestEnum;
import com.webank.blockchain.gov.acct.exception.InvalidParamException;
import com.webank.blockchain.gov.acct.exception.TransactionReceiptException;
import com.webank.blockchain.gov.acct.vo.VoteRequestInfo;
import com.webank.blockchain.gov.acct.vo.WeightInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * GovernUserManager @Description: GovernUserManager
 *
 * @author maojiayu
 * @data Feb 21, 2020 5:27:17 PM
 */
@Service
@Slf4j
public class VoteModeGovernManager extends BasicManager {
    
    public VoteModeGovernManager() {
        super();
    }

    public VoteModeGovernManager(WEGovernance governance, Client client, CryptoKeyPair credentials)
            throws ContractException {
        super(governance, client, credentials);
    }

    public BigInteger requestResetThreshold(int newThreshold) throws Exception {
        TransactionReceipt tr = governance.register(RequestEnum.OPER_RESET_THRESHOLD.getType(),
                governance.getContractAddress(), Address.DEFAULT.getValue(), BigInteger.valueOf(newThreshold));
        if (!tr.getStatus().equalsIgnoreCase("0x0")) {
            throw new TransactionReceiptException("Error request a vote of reset threshold: " + tr.getStatus());
        }
        log.info("Governance contract [ {} ] request reset threshold to [ {} ]", governance.getContractAddress(),
                newThreshold);
        return getId(tr);
    }

    public BigInteger requestRemoveGovernAccount(String externalAccount) throws Exception {
        if (!hasAccount(externalAccount)) {
            throw new InvalidParamException("Not an exsisted account: " + externalAccount);
        }
        TransactionReceipt tr = governance.register(RequestEnum.OPER_RESET_WEIGHT.getType(), externalAccount,
                Address.DEFAULT.getValue(), BigInteger.ZERO);
        if (!tr.getStatus().equalsIgnoreCase("0x0")) {
            throw new TransactionReceiptException("Error request a vote of remvoe govern account: " + tr.getStatus());
        }
        log.info("Governance contract [ {} ] request to remove governance account [ {} ]",
                governance.getContractAddress(), externalAccount);
        return getId(tr);
    }

    public BigInteger requestResetGovernAccount(String externalAccount, int weight) throws Exception {
        if (!hasAccount(externalAccount)) {
            throw new InvalidParamException("Not an exsisted account: " + externalAccount);
        }
        String ua = getUserAccount(externalAccount).getContractAddress();
        WeightInfo wi = getWeightInfo();
        if (!wi.getAddressList().contains(ua)) {
            throw new InvalidParamException("The external account is not a govern account: " + externalAccount);
        }
        TransactionReceipt tr = governance.register(RequestEnum.OPER_RESET_WEIGHT.getType(), externalAccount,
                Address.DEFAULT.getValue(), BigInteger.valueOf(weight));
        if (!tr.getStatus().equalsIgnoreCase("0x0")) {
            throw new TransactionReceiptException("Error request a vote of remvoe govern account: " + tr.getStatus());
        }
        log.info("Governance contract [ {} ] request to reset new weight [{}] of governance account [ {} ] ",
                governance.getContractAddress(), weight, externalAccount);
        return getId(tr);
    }

    public BigInteger requestAddGovernAccount(String externalAccount, int weight) throws Exception {
        if (!hasAccount(externalAccount)) {
            throw new InvalidParamException("Not an exsisted account: " + externalAccount);
        }
        TransactionReceipt tr =
                governance.register(
                        RequestEnum.OPER_RESET_WEIGHT.getType(),
                        externalAccount,
                        Address.DEFAULT.getValue(),
                        BigInteger.valueOf(weight));
        if (!tr.getStatus().equalsIgnoreCase("0x0")) {
            throw new TransactionReceiptException(
                    "Error request a vote of add govern account: " + tr.getStatus());
        }
        log.info("Governance contract [ {} ] request to add governance account [ {} ], weight [ {} ]", governance.getContractAddress(),
                externalAccount, weight);
        return getId(tr);
    }

    public BigInteger requestAddGovernAccount(String externalAccount) throws Exception {
        return requestAddGovernAccount(externalAccount, 1);
    }

    public BigInteger requestResetAccount(String newExternalAccount, String oldExternalAccount) throws Exception {
        TransactionReceipt tr = governance.register(RequestEnum.OPER_CHANGE_CREDENTIAL.getType(), oldExternalAccount,
                newExternalAccount, BigInteger.ZERO);
        if (!tr.getStatus().equalsIgnoreCase("0x0")) {
            throw new TransactionReceiptException("Error request a vote of reset account: " + tr.getStatus());
        }
        log.info("Governance contract [ {} ] request to reset account, from [ {} ] to [ {} ]",
                governance.getContractAddress(), oldExternalAccount, newExternalAccount);
        return getId(tr);
    }

    public BigInteger requestFreezeAccount(String externalAccount) throws Exception {
        TransactionReceipt tr =
                governance.register(
                        RequestEnum.OPER_FREEZE_ACCOUNT.getType(),
                        externalAccount,
                        Address.DEFAULT.getValue(),
                        BigInteger.ZERO);
        if (!tr.getStatus().equalsIgnoreCase("0x0")) {
            throw new TransactionReceiptException(
                    "Error request a vote of freeze account: " + tr.getStatus());
        }
        log.info("Governance contract [ {} ] request to freeze external account [ {} ]",
                governance.getContractAddress(), externalAccount);
        return getId(tr);
    }

    public BigInteger requestUnfreezeAccount(String externalAccount) throws Exception {
        TransactionReceipt tr =
                governance.register(
                        RequestEnum.OPER_UNFREEZE_ACCOUNT.getType(),
                        externalAccount,
                        Address.DEFAULT.getValue(),
                        BigInteger.ZERO);
        if (!tr.getStatus().equalsIgnoreCase("0x0")) {
            throw new TransactionReceiptException(
                    "Error request a vote of unfreeze account: " + tr.getStatus());
        }
        log.info("Governance contract [ {} ] request to unfreeze external account [ {} ]",
                governance.getContractAddress(), externalAccount);
        return getId(tr);
    }

    public BigInteger requestCancelAccount(String externalAccount) throws Exception {
        TransactionReceipt tr =
                governance.register(
                        RequestEnum.OPER_CANCEL_ACCOUNT.getType(),
                        externalAccount,
                        Address.DEFAULT.getValue(),
                        BigInteger.ZERO);
        if (!tr.getStatus().equalsIgnoreCase("0x0")) {
            throw new TransactionReceiptException(
                    "Error request a vote of cancel account: " + tr.getStatus());
        }
        log.info("Governance contract [ {} ] request to cancel external account [ {} ]",
                governance.getContractAddress(), externalAccount);
        return getId(tr);
    }

    public TransactionReceipt vote(BigInteger requestId, boolean agreed) throws Exception {
        log.info(
                "\n start vote, Request id: [ {} ] \n --------------------------------------  \n voter: [ {} ] \n voter weight is [ {} ] \n agreed: [ {} ] \n",
                requestId, this.credentials.getAddress(), governance.getVoteWeight(getUserAccount(this.credentials.getAddress()).getContractAddress()).intValue(),
                agreed);
        TransactionReceipt tr = governance.vote(requestId, agreed);
        if (!tr.getStatus().equalsIgnoreCase("0x0")) {
            throw new TransactionReceiptException("Error vote: " + tr.getStatus());
        }
        VoteRequestInfo voteRequestInfo = new VoteRequestInfo();
        voteRequestInfo.forward(governance.getRequestInfo(requestId)).print();
        return tr;
    }

    public TransactionReceipt resetAccount(
            BigInteger requestId, String newExternalAccount, String oldExternalAccount)
            throws Exception {
        TransactionReceipt tr =
                governance.setExternalAccount(requestId, newExternalAccount, oldExternalAccount);
        if (!tr.getStatus().equalsIgnoreCase("0x0")) {
            throw new TransactionReceiptException("Error reset account: " + tr.getStatus());
        }
        log.info("reset account succeed, from [ {} ] to [ {} ]", oldExternalAccount, newExternalAccount);
        return tr;
    }

    public TransactionReceipt freezeAccount(BigInteger requestId, String externalAccount)
            throws Exception {
        TransactionReceipt tr =
                governance.doOper(
                        requestId, externalAccount, RequestEnum.OPER_FREEZE_ACCOUNT.getType());
        if (!tr.getStatus().equalsIgnoreCase("0x0")) {
            throw new TransactionReceiptException("Error freeze account: " + tr.getStatus());
        }
        log.info("freeze account [ {} ] succeed ", externalAccount);
        return tr;
    }

    public TransactionReceipt unfreezeAccount(BigInteger requestId, String externalAccount)
            throws Exception {
        TransactionReceipt tr =
                governance.doOper(
                        requestId, externalAccount, RequestEnum.OPER_UNFREEZE_ACCOUNT.getType());
        if (!tr.getStatus().equalsIgnoreCase("0x0")) {
            throw new TransactionReceiptException("Error unfreeze account: " + tr.getStatus());
        }
        log.info("unfreeze account [ {} ] succeed ", externalAccount);
        return tr;
    }

    public TransactionReceipt cancelAccount(BigInteger requestId, String externalAccount)
            throws Exception {
        TransactionReceipt tr =
                governance.doOper(
                        requestId, externalAccount, RequestEnum.OPER_CANCEL_ACCOUNT.getType());
        if (!tr.getStatus().equalsIgnoreCase("0x0")) {
            throw new TransactionReceiptException("Error cancel account" + tr.getStatus());
        }
        log.info("cancel account [ {} ] succeed ", externalAccount);
        return tr;
    }

    public TransactionReceipt resetThreshold(BigInteger requestId, int threshold) throws Exception {
        TransactionReceipt tr = governance.setThreshold(requestId, BigInteger.valueOf(threshold));
        if (!tr.getStatus().equalsIgnoreCase("0x0")) {
            throw new TransactionReceiptException(
                    "Error reset account threshold: " + tr.getStatus());
        }
        log.info("reset threshold [ {} ] succeed ", threshold);
        return tr;
    }

    public TransactionReceipt removeGovernAccount(BigInteger requestId, String externalAccount)
            throws Exception {
        if (!hasAccount(externalAccount)) {
            throw new InvalidParamException("Not an exsisted account: " + externalAccount);
        }
        TransactionReceipt tr = governance.setWeight(requestId, externalAccount, BigInteger.ZERO);
        if (!tr.getStatus().equalsIgnoreCase("0x0")) {
            throw new TransactionReceiptException("Error remove govern account" + tr.getStatus());
        }
        log.info("Contract [ {} ] remove governance account [ {} ] succeed ", governance.getContractAddress(), externalAccount);
        return tr;
    }

    public TransactionReceipt resetGovernAccount(
            BigInteger requestId, String externalAccount, int weight) throws Exception {
        if (!hasAccount(externalAccount)) {
            throw new InvalidParamException("Not an exsisted account: " + externalAccount);
        }
        TransactionReceipt tr =
                governance.setWeight(requestId, externalAccount, BigInteger.valueOf(weight));
        if (!tr.getStatus().equalsIgnoreCase("0x0")) {
            throw new TransactionReceiptException("Error remove govern account" + tr.getStatus());
        }
        log.info("reset account [ {} ] weight [ {} ] succeed ", externalAccount, weight);
        return tr;
    }

    public TransactionReceipt addGovernAccount(
            BigInteger requestId, String externalAccount, int weight) throws Exception {
        if (!hasAccount(externalAccount)) {
            throw new InvalidParamException("Not an exsisted account: " + externalAccount);
        }
        TransactionReceipt tr =
                governance.setWeight(requestId, externalAccount, BigInteger.valueOf(weight));
        if (!tr.getStatus().equalsIgnoreCase("0x0")) {
            throw new TransactionReceiptException("Error add govern account" + tr.getStatus());
        }
        log.info("add account [ {} ] weight [ {} ] succeed ", externalAccount, weight);
        return tr;
    }

    public TransactionReceipt addGovernAccount(BigInteger requestId, String externalAccount)
            throws Exception {
        return addGovernAccount(requestId, externalAccount, 1);
    }

    public BigInteger getId(TransactionReceipt tr)
            throws TransactionException, IOException, ABICodecException {
        TransactionDecoderInterface decoder =
                new TransactionDecoderService(client.getCryptoSuite());
        BigInteger v =
                new BigInteger(
                        decoder.decodeReceiptWithValues(WEGovernance.ABI, "register", tr)
                                .getValuesList()
                                .get(1)
                                .toString());
        log.info("Vote request id is [ {} ]", v);
        return v;
    }

    public VoteRequestInfo getVoteRequestInfo(BigInteger requestId) throws Exception {
        Tuple8<
                        BigInteger,
                        String,
                        BigInteger,
                        BigInteger,
                        BigInteger,
                        BigInteger,
                        String,
                        BigInteger>
                t = governance.getRequestInfo(requestId);
        VoteRequestInfo info = new VoteRequestInfo();
        return info.forward(t);
    }

    public WeightInfo getWeightInfo() throws Exception {
        Tuple3<List<String>, List<BigInteger>, BigInteger> tuple = governance.getWeightInfo();
        WeightInfo wi = new WeightInfo();
        wi.setAddressList(tuple.getValue1())
                .setWeightList(tuple.getValue2())
                .setThreshold(tuple.getValue3().intValue());
        int acctType = 1;
        for (BigInteger b : wi.getWeightList()) {
            if (b.intValue() != 1) {
                acctType = 2;
                break;
            }
        }
        wi.setAcctType(acctType);
        return wi;
    }
}
