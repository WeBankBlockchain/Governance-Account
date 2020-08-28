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
package com.webank.blockchain.acct.gov.manager;

import com.webank.blockchain.acct.gov.contract.WEGovernance;
import com.webank.blockchain.acct.gov.enums.RequestEnum;
import com.webank.blockchain.acct.gov.exception.InvalidParamException;
import com.webank.blockchain.acct.gov.exception.TransactionReceiptException;
import com.webank.blockchain.acct.gov.vo.VoteRequestInfo;
import com.webank.blockchain.acct.gov.vo.WeightInfo;
import java.math.BigInteger;
import java.util.List;
import org.fisco.bcos.web3j.abi.datatypes.Address;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.protocol.exceptions.TransactionException;
import org.fisco.bcos.web3j.tuples.generated.Tuple3;
import org.fisco.bcos.web3j.tuples.generated.Tuple8;
import org.fisco.bcos.web3j.tx.txdecode.BaseException;
import org.fisco.bcos.web3j.tx.txdecode.InputAndOutputResult;
import org.fisco.bcos.web3j.tx.txdecode.ResultEntity;
import org.springframework.stereotype.Service;

/**
 * GovernUserManager @Description: GovernUserManager
 *
 * @author maojiayu
 * @data Feb 21, 2020 5:27:17 PM
 */
@Service
public class VoteModeGovernManager extends BasicManager {

    public BigInteger requestResetThreshold(int newThreshold) throws Exception {
        TransactionReceipt tr =
                governance
                        .register(
                                RequestEnum.OPER_RESET_THRESHOLD.getType(),
                                governance.getContractAddress(),
                                Address.DEFAULT.getValue(),
                                BigInteger.valueOf(newThreshold))
                        .send();
        if (!tr.getStatus().equalsIgnoreCase("0x0")) {
            throw new TransactionReceiptException(
                    "Error request a vote of reset threshold: " + tr.getStatus());
        }
        return getId(tr);
    }

    public BigInteger requestRemoveGovernAccount(String externalAccount) throws Exception {
        if (!hasAccount(externalAccount)) {
            throw new InvalidParamException("Not an exsisted account: " + externalAccount);
        }
        TransactionReceipt tr =
                governance
                        .register(
                                RequestEnum.OPER_RESET_WEIGHT.getType(),
                                externalAccount,
                                Address.DEFAULT.getValue(),
                                BigInteger.ZERO)
                        .send();
        if (!tr.getStatus().equalsIgnoreCase("0x0")) {
            throw new TransactionReceiptException(
                    "Error request a vote of remvoe govern account: " + tr.getStatus());
        }
        return getId(tr);
    }

    public BigInteger requestResetGovernAccount(String externalAccount, int weight)
            throws Exception {
        if (!hasAccount(externalAccount)) {
            throw new InvalidParamException("Not an exsisted account: " + externalAccount);
        }
        String ua = getUserAccount(externalAccount).getContractAddress();
        WeightInfo wi = getWeightInfo();
        if (!wi.getAddressList().contains(ua)) {
            throw new InvalidParamException(
                    "The external account is not a govern account: " + externalAccount);
        }
        TransactionReceipt tr =
                governance
                        .register(
                                RequestEnum.OPER_RESET_WEIGHT.getType(),
                                externalAccount,
                                Address.DEFAULT.getValue(),
                                BigInteger.valueOf(weight))
                        .send();
        if (!tr.getStatus().equalsIgnoreCase("0x0")) {
            throw new TransactionReceiptException(
                    "Error request a vote of remvoe govern account: " + tr.getStatus());
        }
        return getId(tr);
    }

    public BigInteger requestAddGovernAccount(String externalAccount, int weight) throws Exception {
        if (!hasAccount(externalAccount)) {
            throw new InvalidParamException("Not an exsisted account: " + externalAccount);
        }
        TransactionReceipt tr =
                governance
                        .register(
                                RequestEnum.OPER_RESET_WEIGHT.getType(),
                                externalAccount,
                                Address.DEFAULT.getValue(),
                                BigInteger.valueOf(weight))
                        .send();
        if (!tr.getStatus().equalsIgnoreCase("0x0")) {
            throw new TransactionReceiptException(
                    "Error request a vote of add govern account: " + tr.getStatus());
        }
        return getId(tr);
    }

    public BigInteger requestAddGovernAccount(String externalAccount) throws Exception {
        return requestAddGovernAccount(externalAccount, 1);
    }

    public BigInteger requestResetAccount(String newExternalAccount, String oldExternalAccount)
            throws Exception {
        TransactionReceipt tr =
                governance
                        .register(
                                RequestEnum.OPER_CHANGE_CREDENTIAL.getType(),
                                oldExternalAccount,
                                newExternalAccount,
                                BigInteger.ZERO)
                        .send();
        if (!tr.getStatus().equalsIgnoreCase("0x0")) {
            throw new TransactionReceiptException(
                    "Error request a vote of reset account: " + tr.getStatus());
        }
        return getId(tr);
    }

    public BigInteger requestFreezeAccount(String externalAccount) throws Exception {
        TransactionReceipt tr =
                governance
                        .register(
                                RequestEnum.OPER_FREEZE_ACCOUNT.getType(),
                                externalAccount,
                                Address.DEFAULT.getValue(),
                                BigInteger.ZERO)
                        .send();
        if (!tr.getStatus().equalsIgnoreCase("0x0")) {
            throw new TransactionReceiptException(
                    "Error request a vote of freeze account: " + tr.getStatus());
        }
        return getId(tr);
    }

    public BigInteger requestUnfreezeAccount(String externalAccount) throws Exception {
        TransactionReceipt tr =
                governance
                        .register(
                                RequestEnum.OPER_UNFREEZE_ACCOUNT.getType(),
                                externalAccount,
                                Address.DEFAULT.getValue(),
                                BigInteger.ZERO)
                        .send();
        if (!tr.getStatus().equalsIgnoreCase("0x0")) {
            throw new TransactionReceiptException(
                    "Error request a vote of unfreeze account: " + tr.getStatus());
        }
        return getId(tr);
    }

    public BigInteger requestCancelAccount(String externalAccount) throws Exception {
        TransactionReceipt tr =
                governance
                        .register(
                                RequestEnum.OPER_CANCEL_ACCOUNT.getType(),
                                externalAccount,
                                Address.DEFAULT.getValue(),
                                BigInteger.ZERO)
                        .send();
        if (!tr.getStatus().equalsIgnoreCase("0x0")) {
            throw new TransactionReceiptException(
                    "Error request a vote of cancel account: " + tr.getStatus());
        }
        return getId(tr);
    }

    public TransactionReceipt vote(BigInteger requestId, boolean agreed) throws Exception {
        TransactionReceipt tr = governance.vote(requestId, agreed).send();
        if (!tr.getStatus().equalsIgnoreCase("0x0")) {
            throw new TransactionReceiptException("Error vote: " + tr.getStatus());
        }
        return tr;
    }

    public TransactionReceipt resetAccount(
            BigInteger requestId, String newExternalAccount, String oldExternalAccount)
            throws Exception {
        TransactionReceipt tr =
                governance
                        .setExternalAccount(requestId, newExternalAccount, oldExternalAccount)
                        .send();
        if (!tr.getStatus().equalsIgnoreCase("0x0")) {
            throw new TransactionReceiptException("Error reset account: " + tr.getStatus());
        }
        return tr;
    }

    public TransactionReceipt freezeAccount(BigInteger requestId, String externalAccount)
            throws Exception {
        TransactionReceipt tr =
                governance
                        .doOper(
                                requestId,
                                externalAccount,
                                RequestEnum.OPER_FREEZE_ACCOUNT.getType())
                        .send();
        if (!tr.getStatus().equalsIgnoreCase("0x0")) {
            throw new TransactionReceiptException("Error freeze account: " + tr.getStatus());
        }
        return tr;
    }

    public TransactionReceipt unfreezeAccount(BigInteger requestId, String externalAccount)
            throws Exception {
        TransactionReceipt tr =
                governance
                        .doOper(
                                requestId,
                                externalAccount,
                                RequestEnum.OPER_UNFREEZE_ACCOUNT.getType())
                        .send();
        if (!tr.getStatus().equalsIgnoreCase("0x0")) {
            throw new TransactionReceiptException("Error unfreeze account: " + tr.getStatus());
        }
        return tr;
    }

    public TransactionReceipt cancelAccount(BigInteger requestId, String externalAccount)
            throws Exception {
        TransactionReceipt tr =
                governance
                        .doOper(
                                requestId,
                                externalAccount,
                                RequestEnum.OPER_CANCEL_ACCOUNT.getType())
                        .send();
        if (!tr.getStatus().equalsIgnoreCase("0x0")) {
            throw new TransactionReceiptException("Error cancel account" + tr.getStatus());
        }
        return tr;
    }

    public TransactionReceipt resetThreshold(BigInteger requestId, int threshold) throws Exception {
        TransactionReceipt tr =
                governance.setThreshold(requestId, BigInteger.valueOf(threshold)).send();
        if (!tr.getStatus().equalsIgnoreCase("0x0")) {
            throw new TransactionReceiptException(
                    "Error reset account threshold: " + tr.getStatus());
        }
        return tr;
    }

    public TransactionReceipt removeGovernAccount(BigInteger requestId, String externalAccount)
            throws Exception {
        if (!hasAccount(externalAccount)) {
            throw new InvalidParamException("Not an exsisted account: " + externalAccount);
        }
        TransactionReceipt tr =
                governance.setWeight(requestId, externalAccount, BigInteger.ZERO).send();
        if (!tr.getStatus().equalsIgnoreCase("0x0")) {
            throw new TransactionReceiptException("Error remove govern account" + tr.getStatus());
        }
        return tr;
    }

    public TransactionReceipt resetGovernAccount(
            BigInteger requestId, String externalAccount, int weight) throws Exception {
        if (!hasAccount(externalAccount)) {
            throw new InvalidParamException("Not an exsisted account: " + externalAccount);
        }
        TransactionReceipt tr =
                governance.setWeight(requestId, externalAccount, BigInteger.valueOf(weight)).send();
        if (!tr.getStatus().equalsIgnoreCase("0x0")) {
            throw new TransactionReceiptException("Error remove govern account" + tr.getStatus());
        }
        return tr;
    }

    public TransactionReceipt addGovernAccount(
            BigInteger requestId, String externalAccount, int weight) throws Exception {
        if (!hasAccount(externalAccount)) {
            throw new InvalidParamException("Not an exsisted account: " + externalAccount);
        }
        TransactionReceipt tr =
                governance.setWeight(requestId, externalAccount, BigInteger.valueOf(weight)).send();
        if (!tr.getStatus().equalsIgnoreCase("0x0")) {
            throw new TransactionReceiptException("Error add govern account" + tr.getStatus());
        }
        return tr;
    }

    public TransactionReceipt addGovernAccount(BigInteger requestId, String externalAccount)
            throws Exception {
        return addGovernAccount(requestId, externalAccount, 1);
    }

    public BigInteger getId(TransactionReceipt tr) throws TransactionException, BaseException {
        InputAndOutputResult r =
                WEGovernance.transactionDecoder.decodeOutputReturnObject(
                        tr.getInput(), tr.getOutput());
        ResultEntity entity = r.getResult().get(1);
        BigInteger v = (BigInteger) entity.getData();
        System.out.println("request id is " + v);
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
                t = governance.getRequestInfo(requestId).send();
        VoteRequestInfo info = new VoteRequestInfo();
        return info.forward(t);
    }

    public WeightInfo getWeightInfo() throws Exception {
        Tuple3<List<String>, List<BigInteger>, BigInteger> tuple =
                governance.getWeightInfo().send();
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
