package com.webank.blockchain.acct.gov.contract;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.channel.client.TransactionSucCallback;
import org.fisco.bcos.channel.event.filter.EventLogPushWithDecodeCallback;
import org.fisco.bcos.web3j.abi.EventEncoder;
import org.fisco.bcos.web3j.abi.FunctionReturnDecoder;
import org.fisco.bcos.web3j.abi.TypeReference;
import org.fisco.bcos.web3j.abi.datatypes.Address;
import org.fisco.bcos.web3j.abi.datatypes.Bool;
import org.fisco.bcos.web3j.abi.datatypes.Event;
import org.fisco.bcos.web3j.abi.datatypes.Function;
import org.fisco.bcos.web3j.abi.datatypes.Type;
import org.fisco.bcos.web3j.abi.datatypes.generated.Bytes32;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.crypto.EncryptType;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.RemoteCall;
import org.fisco.bcos.web3j.protocol.core.methods.response.Log;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tuples.generated.Tuple1;
import org.fisco.bcos.web3j.tx.Contract;
import org.fisco.bcos.web3j.tx.TransactionManager;
import org.fisco.bcos.web3j.tx.gas.ContractGasProvider;
import org.fisco.bcos.web3j.tx.txdecode.TransactionDecoder;

/**
 * Auto generated code.
 *
 * <p><strong>Do not modify!</strong>
 *
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.fisco.bcos.web3j.codegen.SolidityFunctionWrapperGenerator in the <a
 * href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version none.
 */
@SuppressWarnings("unchecked")
public class BaseAccountInterface extends Contract {
    public static final String[] BINARY_ARRAY = {};

    public static final String BINARY = String.join("", BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {
        "[{\"constant\":false,\"inputs\":[],\"name\":\"freeze\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"unfreeze\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"isNormal\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"cancel\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"name\":\"eventType\",\"type\":\"bytes32\"},{\"indexed\":false,\"name\":\"contractAddress\",\"type\":\"address\"}],\"name\":\"LogBaseAccount\",\"type\":\"event\"}]"
    };

    public static final String ABI = String.join("", ABI_ARRAY);

    public static final TransactionDecoder transactionDecoder = new TransactionDecoder(ABI, BINARY);

    public static final String[] SM_BINARY_ARRAY = {};

    public static final String SM_BINARY = String.join("", SM_BINARY_ARRAY);

    public static final String FUNC_FREEZE = "freeze";

    public static final String FUNC_UNFREEZE = "unfreeze";

    public static final String FUNC_ISNORMAL = "isNormal";

    public static final String FUNC_CANCEL = "cancel";

    public static final Event LOGBASEACCOUNT_EVENT =
            new Event(
                    "LogBaseAccount",
                    Arrays.<TypeReference<?>>asList(
                            new TypeReference<Bytes32>(true) {}, new TypeReference<Address>() {}));;

    @Deprecated
    protected BaseAccountInterface(
            String contractAddress,
            Web3j web3j,
            Credentials credentials,
            BigInteger gasPrice,
            BigInteger gasLimit) {
        super(getBinary(), contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected BaseAccountInterface(
            String contractAddress,
            Web3j web3j,
            Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(getBinary(), contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected BaseAccountInterface(
            String contractAddress,
            Web3j web3j,
            TransactionManager transactionManager,
            BigInteger gasPrice,
            BigInteger gasLimit) {
        super(getBinary(), contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected BaseAccountInterface(
            String contractAddress,
            Web3j web3j,
            TransactionManager transactionManager,
            ContractGasProvider contractGasProvider) {
        super(getBinary(), contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static String getBinary() {
        return (EncryptType.encryptType == EncryptType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public static TransactionDecoder getTransactionDecoder() {
        return transactionDecoder;
    }

    public RemoteCall<TransactionReceipt> freeze() {
        final Function function =
                new Function(
                        FUNC_FREEZE,
                        Arrays.<Type>asList(),
                        Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void freeze(TransactionSucCallback callback) {
        final Function function =
                new Function(
                        FUNC_FREEZE,
                        Arrays.<Type>asList(),
                        Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String freezeSeq() {
        final Function function =
                new Function(
                        FUNC_FREEZE,
                        Arrays.<Type>asList(),
                        Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple1<Boolean> getFreezeOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function =
                new Function(
                        FUNC_FREEZE,
                        Arrays.<Type>asList(),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        ;
        return new Tuple1<Boolean>((Boolean) results.get(0).getValue());
    }

    public RemoteCall<TransactionReceipt> unfreeze() {
        final Function function =
                new Function(
                        FUNC_UNFREEZE,
                        Arrays.<Type>asList(),
                        Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void unfreeze(TransactionSucCallback callback) {
        final Function function =
                new Function(
                        FUNC_UNFREEZE,
                        Arrays.<Type>asList(),
                        Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String unfreezeSeq() {
        final Function function =
                new Function(
                        FUNC_UNFREEZE,
                        Arrays.<Type>asList(),
                        Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple1<Boolean> getUnfreezeOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function =
                new Function(
                        FUNC_UNFREEZE,
                        Arrays.<Type>asList(),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        ;
        return new Tuple1<Boolean>((Boolean) results.get(0).getValue());
    }

    public RemoteCall<TransactionReceipt> isNormal() {
        final Function function =
                new Function(
                        FUNC_ISNORMAL,
                        Arrays.<Type>asList(),
                        Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void isNormal(TransactionSucCallback callback) {
        final Function function =
                new Function(
                        FUNC_ISNORMAL,
                        Arrays.<Type>asList(),
                        Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String isNormalSeq() {
        final Function function =
                new Function(
                        FUNC_ISNORMAL,
                        Arrays.<Type>asList(),
                        Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple1<Boolean> getIsNormalOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function =
                new Function(
                        FUNC_ISNORMAL,
                        Arrays.<Type>asList(),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        ;
        return new Tuple1<Boolean>((Boolean) results.get(0).getValue());
    }

    public RemoteCall<TransactionReceipt> cancel() {
        final Function function =
                new Function(
                        FUNC_CANCEL,
                        Arrays.<Type>asList(),
                        Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void cancel(TransactionSucCallback callback) {
        final Function function =
                new Function(
                        FUNC_CANCEL,
                        Arrays.<Type>asList(),
                        Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String cancelSeq() {
        final Function function =
                new Function(
                        FUNC_CANCEL,
                        Arrays.<Type>asList(),
                        Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple1<Boolean> getCancelOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function =
                new Function(
                        FUNC_CANCEL,
                        Arrays.<Type>asList(),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        ;
        return new Tuple1<Boolean>((Boolean) results.get(0).getValue());
    }

    public List<LogBaseAccountEventResponse> getLogBaseAccountEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList =
                extractEventParametersWithLog(LOGBASEACCOUNT_EVENT, transactionReceipt);
        ArrayList<LogBaseAccountEventResponse> responses =
                new ArrayList<LogBaseAccountEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            LogBaseAccountEventResponse typedResponse = new LogBaseAccountEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.eventType = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.contractAddress =
                    (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void registerLogBaseAccountEventLogFilter(
            String fromBlock,
            String toBlock,
            List<String> otherTopics,
            EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(LOGBASEACCOUNT_EVENT);
        registerEventLogPushFilter(ABI, BINARY, topic0, fromBlock, toBlock, otherTopics, callback);
    }

    public void registerLogBaseAccountEventLogFilter(EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(LOGBASEACCOUNT_EVENT);
        registerEventLogPushFilter(ABI, BINARY, topic0, callback);
    }

    @Deprecated
    public static BaseAccountInterface load(
            String contractAddress,
            Web3j web3j,
            Credentials credentials,
            BigInteger gasPrice,
            BigInteger gasLimit) {
        return new BaseAccountInterface(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static BaseAccountInterface load(
            String contractAddress,
            Web3j web3j,
            TransactionManager transactionManager,
            BigInteger gasPrice,
            BigInteger gasLimit) {
        return new BaseAccountInterface(
                contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static BaseAccountInterface load(
            String contractAddress,
            Web3j web3j,
            Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return new BaseAccountInterface(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static BaseAccountInterface load(
            String contractAddress,
            Web3j web3j,
            TransactionManager transactionManager,
            ContractGasProvider contractGasProvider) {
        return new BaseAccountInterface(
                contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<BaseAccountInterface> deploy(
            Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(
                BaseAccountInterface.class,
                web3j,
                credentials,
                contractGasProvider,
                getBinary(),
                "");
    }

    @Deprecated
    public static RemoteCall<BaseAccountInterface> deploy(
            Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(
                BaseAccountInterface.class,
                web3j,
                credentials,
                gasPrice,
                gasLimit,
                getBinary(),
                "");
    }

    public static RemoteCall<BaseAccountInterface> deploy(
            Web3j web3j,
            TransactionManager transactionManager,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(
                BaseAccountInterface.class,
                web3j,
                transactionManager,
                contractGasProvider,
                getBinary(),
                "");
    }

    @Deprecated
    public static RemoteCall<BaseAccountInterface> deploy(
            Web3j web3j,
            TransactionManager transactionManager,
            BigInteger gasPrice,
            BigInteger gasLimit) {
        return deployRemoteCall(
                BaseAccountInterface.class,
                web3j,
                transactionManager,
                gasPrice,
                gasLimit,
                getBinary(),
                "");
    }

    public static class LogBaseAccountEventResponse {
        public Log log;

        public byte[] eventType;

        public String contractAddress;
    }
}
