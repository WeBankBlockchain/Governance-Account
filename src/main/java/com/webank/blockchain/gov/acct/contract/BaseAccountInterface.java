package com.webank.blockchain.gov.acct.contract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.codec.datatypes.Address;
import org.fisco.bcos.sdk.v3.codec.datatypes.Bool;
import org.fisco.bcos.sdk.v3.codec.datatypes.Event;
import org.fisco.bcos.sdk.v3.codec.datatypes.Function;
import org.fisco.bcos.sdk.v3.codec.datatypes.Type;
import org.fisco.bcos.sdk.v3.codec.datatypes.TypeReference;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.Bytes32;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.v3.contract.Contract;
import org.fisco.bcos.sdk.v3.crypto.CryptoSuite;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.model.CryptoType;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.fisco.bcos.sdk.v3.model.callback.TransactionCallback;
import org.fisco.bcos.sdk.v3.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class BaseAccountInterface extends Contract {
    public static final String[] BINARY_ARRAY = {};

    public static final String BINARY =
            org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {};

    public static final String SM_BINARY =
            org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {
        "[{\"constant\":false,\"inputs\":[],\"name\":\"freeze\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"selector\":[1655025467,2404770341],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"unfreeze\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"selector\":[1781067776,135683286],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"isNormal\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"selector\":[2288446188,899059304],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"cancel\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"selector\":[3934919408,3744551082],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"name\":\"eventType\",\"type\":\"bytes32\"},{\"indexed\":false,\"name\":\"contractAddress\",\"type\":\"address\"}],\"name\":\"LogBaseAccount\",\"type\":\"event\"}]"
    };

    public static final String ABI = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", ABI_ARRAY);

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

    public static String getABI() {
        return ABI;
    }

    public TransactionReceipt freeze() {
        final Function function =
                new Function(
                        FUNC_FREEZE,
                        Arrays.<Type>asList(),
                        Collections.<TypeReference<?>>emptyList(),
                        0);
        return executeTransaction(function);
    }

    public String freeze(TransactionCallback callback) {
        final Function function =
                new Function(
                        FUNC_FREEZE,
                        Arrays.<Type>asList(),
                        Collections.<TypeReference<?>>emptyList(),
                        0);
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForFreeze() {
        final Function function =
                new Function(
                        FUNC_FREEZE,
                        Arrays.<Type>asList(),
                        Collections.<TypeReference<?>>emptyList(),
                        0);
        return createSignedTransaction(function);
    }

    public Tuple1<Boolean> getFreezeOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function =
                new Function(
                        FUNC_FREEZE,
                        Arrays.<Type>asList(),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> results =
                this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<Boolean>((Boolean) results.get(0).getValue());
    }

    public TransactionReceipt unfreeze() {
        final Function function =
                new Function(
                        FUNC_UNFREEZE,
                        Arrays.<Type>asList(),
                        Collections.<TypeReference<?>>emptyList(),
                        0);
        return executeTransaction(function);
    }

    public String unfreeze(TransactionCallback callback) {
        final Function function =
                new Function(
                        FUNC_UNFREEZE,
                        Arrays.<Type>asList(),
                        Collections.<TypeReference<?>>emptyList(),
                        0);
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForUnfreeze() {
        final Function function =
                new Function(
                        FUNC_UNFREEZE,
                        Arrays.<Type>asList(),
                        Collections.<TypeReference<?>>emptyList(),
                        0);
        return createSignedTransaction(function);
    }

    public Tuple1<Boolean> getUnfreezeOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function =
                new Function(
                        FUNC_UNFREEZE,
                        Arrays.<Type>asList(),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> results =
                this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<Boolean>((Boolean) results.get(0).getValue());
    }

    public TransactionReceipt isNormal() {
        final Function function =
                new Function(
                        FUNC_ISNORMAL,
                        Arrays.<Type>asList(),
                        Collections.<TypeReference<?>>emptyList(),
                        0);
        return executeTransaction(function);
    }

    public String isNormal(TransactionCallback callback) {
        final Function function =
                new Function(
                        FUNC_ISNORMAL,
                        Arrays.<Type>asList(),
                        Collections.<TypeReference<?>>emptyList(),
                        0);
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForIsNormal() {
        final Function function =
                new Function(
                        FUNC_ISNORMAL,
                        Arrays.<Type>asList(),
                        Collections.<TypeReference<?>>emptyList(),
                        0);
        return createSignedTransaction(function);
    }

    public Tuple1<Boolean> getIsNormalOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function =
                new Function(
                        FUNC_ISNORMAL,
                        Arrays.<Type>asList(),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> results =
                this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<Boolean>((Boolean) results.get(0).getValue());
    }

    public TransactionReceipt cancel() {
        final Function function =
                new Function(
                        FUNC_CANCEL,
                        Arrays.<Type>asList(),
                        Collections.<TypeReference<?>>emptyList(),
                        0);
        return executeTransaction(function);
    }

    public String cancel(TransactionCallback callback) {
        final Function function =
                new Function(
                        FUNC_CANCEL,
                        Arrays.<Type>asList(),
                        Collections.<TypeReference<?>>emptyList(),
                        0);
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForCancel() {
        final Function function =
                new Function(
                        FUNC_CANCEL,
                        Arrays.<Type>asList(),
                        Collections.<TypeReference<?>>emptyList(),
                        0);
        return createSignedTransaction(function);
    }

    public Tuple1<Boolean> getCancelOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function =
                new Function(
                        FUNC_CANCEL,
                        Arrays.<Type>asList(),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> results =
                this.functionReturnDecoder.decode(data, function.getOutputParameters());
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
                getABI(),
                null,
                null);
    }

    public static class LogBaseAccountEventResponse {
        public TransactionReceipt.Logs log;

        public byte[] eventType;

        public String contractAddress;
    }
}
