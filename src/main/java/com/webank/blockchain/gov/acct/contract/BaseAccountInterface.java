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
package com.webank.blockchain.gov.acct.contract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.abi.FunctionReturnDecoder;
import org.fisco.bcos.sdk.abi.TypeReference;
import org.fisco.bcos.sdk.abi.datatypes.Address;
import org.fisco.bcos.sdk.abi.datatypes.Bool;
import org.fisco.bcos.sdk.abi.datatypes.Event;
import org.fisco.bcos.sdk.abi.datatypes.Function;
import org.fisco.bcos.sdk.abi.datatypes.Type;
import org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.contract.Contract;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.eventsub.EventCallback;
import org.fisco.bcos.sdk.model.CryptoType;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.model.callback.TransactionCallback;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class BaseAccountInterface extends Contract {
    public static final String[] BINARY_ARRAY = {};

    public static final String BINARY = String.join("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {};

    public static final String SM_BINARY = String.join("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {
        "[{\"constant\":false,\"inputs\":[],\"name\":\"freeze\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"unfreeze\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"isNormal\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"cancel\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"name\":\"eventType\",\"type\":\"bytes32\"},{\"indexed\":false,\"name\":\"contractAddress\",\"type\":\"address\"}],\"name\":\"LogBaseAccount\",\"type\":\"event\"}]"
    };

    public static final String ABI = String.join("", ABI_ARRAY);

    public static final String FUNC_FREEZE = "freeze";

    public static final String FUNC_UNFREEZE = "unfreeze";

    public static final String FUNC_ISNORMAL = "isNormal";

    public static final String FUNC_CANCEL = "cancel";

    public static final Event LOGBASEACCOUNT_EVENT =
            new Event(
                    "LogBaseAccount",
                    Arrays.<TypeReference<?>>asList(
                            new TypeReference<Bytes32>(true) {}, new TypeReference<Address>() {}));;

    protected BaseAccountInterface(
            String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public TransactionReceipt freeze() {
        final Function function =
                new Function(
                        FUNC_FREEZE,
                        Arrays.<Type>asList(),
                        Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void freeze(TransactionCallback callback) {
        final Function function =
                new Function(
                        FUNC_FREEZE,
                        Arrays.<Type>asList(),
                        Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForFreeze() {
        final Function function =
                new Function(
                        FUNC_FREEZE,
                        Arrays.<Type>asList(),
                        Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple1<Boolean> getFreezeOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function =
                new Function(
                        FUNC_FREEZE,
                        Arrays.<Type>asList(),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<Boolean>((Boolean) results.get(0).getValue());
    }

    public TransactionReceipt unfreeze() {
        final Function function =
                new Function(
                        FUNC_UNFREEZE,
                        Arrays.<Type>asList(),
                        Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void unfreeze(TransactionCallback callback) {
        final Function function =
                new Function(
                        FUNC_UNFREEZE,
                        Arrays.<Type>asList(),
                        Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForUnfreeze() {
        final Function function =
                new Function(
                        FUNC_UNFREEZE,
                        Arrays.<Type>asList(),
                        Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple1<Boolean> getUnfreezeOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function =
                new Function(
                        FUNC_UNFREEZE,
                        Arrays.<Type>asList(),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<Boolean>((Boolean) results.get(0).getValue());
    }

    public TransactionReceipt isNormal() {
        final Function function =
                new Function(
                        FUNC_ISNORMAL,
                        Arrays.<Type>asList(),
                        Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void isNormal(TransactionCallback callback) {
        final Function function =
                new Function(
                        FUNC_ISNORMAL,
                        Arrays.<Type>asList(),
                        Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForIsNormal() {
        final Function function =
                new Function(
                        FUNC_ISNORMAL,
                        Arrays.<Type>asList(),
                        Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple1<Boolean> getIsNormalOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function =
                new Function(
                        FUNC_ISNORMAL,
                        Arrays.<Type>asList(),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<Boolean>((Boolean) results.get(0).getValue());
    }

    public TransactionReceipt cancel() {
        final Function function =
                new Function(
                        FUNC_CANCEL,
                        Arrays.<Type>asList(),
                        Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void cancel(TransactionCallback callback) {
        final Function function =
                new Function(
                        FUNC_CANCEL,
                        Arrays.<Type>asList(),
                        Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForCancel() {
        final Function function =
                new Function(
                        FUNC_CANCEL,
                        Arrays.<Type>asList(),
                        Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple1<Boolean> getCancelOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function =
                new Function(
                        FUNC_CANCEL,
                        Arrays.<Type>asList(),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<Boolean>((Boolean) results.get(0).getValue());
    }

    public List<LogBaseAccountEventResponse> getLogBaseAccountEvents(
            TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList =
                extractEventParametersWithLog(LOGBASEACCOUNT_EVENT, transactionReceipt);
        ArrayList<LogBaseAccountEventResponse> responses =
                new ArrayList<LogBaseAccountEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            LogBaseAccountEventResponse typedResponse = new LogBaseAccountEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.eventType = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.contractAddress =
                    (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeLogBaseAccountEvent(
            String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(LOGBASEACCOUNT_EVENT);
        subscribeEvent(ABI, BINARY, topic0, fromBlock, toBlock, otherTopics, callback);
    }

    public void subscribeLogBaseAccountEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(LOGBASEACCOUNT_EVENT);
        subscribeEvent(ABI, BINARY, topic0, callback);
    }

    public static BaseAccountInterface load(
            String contractAddress, Client client, CryptoKeyPair credential) {
        return new BaseAccountInterface(contractAddress, client, credential);
    }

    public static BaseAccountInterface deploy(Client client, CryptoKeyPair credential)
            throws ContractException {
        return deploy(
                BaseAccountInterface.class,
                client,
                credential,
                getBinary(client.getCryptoSuite()),
                "");
    }

    public static class LogBaseAccountEventResponse {
        public TransactionReceipt.Logs log;

        public byte[] eventType;

        public String contractAddress;
    }
}
