package com.webank.blockchain.gov.acct.contract;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.abi.FunctionEncoder;
import org.fisco.bcos.sdk.abi.FunctionReturnDecoder;
import org.fisco.bcos.sdk.abi.TypeReference;
import org.fisco.bcos.sdk.abi.datatypes.Address;
import org.fisco.bcos.sdk.abi.datatypes.Bool;
import org.fisco.bcos.sdk.abi.datatypes.Event;
import org.fisco.bcos.sdk.abi.datatypes.Function;
import org.fisco.bcos.sdk.abi.datatypes.Type;
import org.fisco.bcos.sdk.abi.datatypes.generated.Bytes32;
import org.fisco.bcos.sdk.abi.datatypes.generated.Uint8;
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
public class BaseAccount extends Contract {
    public static final String[] BINARY_ARRAY = {
        "60806040526000600160146101000a81548160ff021916908360ff16021790555034801561002c57600080fd5b50604051602080610bbb83398101806040528101908080519060200190929190505050336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555080600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050610adb806100e06000396000f30060806040526004361061008e576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680630fb3844c1461009357806313af4035146100c457806362a5af3b146101075780636a28f000146101365780638866eaec14610165578063b2623cb014610194578063b2bdfa7b146101eb578063ea8a1af014610242575b600080fd5b34801561009f57600080fd5b506100a8610271565b604051808260ff1660ff16815260200191505060405180910390f35b3480156100d057600080fd5b50610105600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610284565b005b34801561011357600080fd5b5061011c61040b565b604051808215151515815260200191505060405180910390f35b34801561014257600080fd5b5061014b610640565b604051808215151515815260200191505060405180910390f35b34801561017157600080fd5b5061017a610883565b604051808215151515815260200191505060405180910390f35b3480156101a057600080fd5b506101a961089f565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b3480156101f757600080fd5b506102006108c5565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561024e57600080fd5b506102576108ea565b604051808215151515815260200191505060405180910390f35b600160149054906101000a900460ff1681565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561036e576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260268152602001807f57454261736963417574683a206f6e6c79206f776e657220697320617574686f81526020017f72697a65642e000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055503073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff167fc66d1d23a5b7baf1f496bb19f580d7b12070ad5a08a758c990db97d961fa33a660405160405180910390a350565b6000600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156104f8576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260228152602001807f426173654163636f756e743a206f6e6c79206163636f756e74206d616e61676581526020017f722e00000000000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b610500610883565b151561059a576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602b8152602001807f426173654163636f756e743a206f6e6c79206163636f756e742073746174757381526020017f206973206e6f726d616c2e00000000000000000000000000000000000000000081525060400191505060405180910390fd5b60018060146101000a81548160ff021916908360ff1602179055507f667265657a6500000000000000000000000000000000000000000000000000007f7d78a1adf6a29dad801d43ddd0c4478ec0cbf1bd9bfdd2e007d90429959f363e30604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390a26001905090565b6000600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561072d576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260228152602001807f426173654163636f756e743a206f6e6c79206163636f756e74206d616e61676581526020017f722e00000000000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b60018060149054906101000a900460ff1660ff161415156107dc576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602d8152602001807f426173654163636f756e743a206f6e6c79206163636f756e742073746174757381526020017f2069732061626e6f726d616c2e0000000000000000000000000000000000000081525060400191505060405180910390fd5b6000600160146101000a81548160ff021916908360ff1602179055507f756e667265657a650000000000000000000000000000000000000000000000007f7d78a1adf6a29dad801d43ddd0c4478ec0cbf1bd9bfdd2e007d90429959f363e30604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390a26001905090565b600080600160149054906101000a900460ff1660ff1614905090565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b6000600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16148061099457506000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16145b1515610a08576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601d8152602001807f426173654163636f756e743a206f6e6c7920617574686f72697a65642e00000081525060200191505060405180910390fd5b6002600160146101000a81548160ff021916908360ff1602179055507f63616e63656c00000000000000000000000000000000000000000000000000007f7d78a1adf6a29dad801d43ddd0c4478ec0cbf1bd9bfdd2e007d90429959f363e30604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390a260019050905600a165627a7a723058204cc8a30c05c8e917f0db9807e8792b0aea6b676930327b90c65c92aea6b4eec00029"
    };

    public static final String BINARY = String.join("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {
        "60806040526000600160146101000a81548160ff021916908360ff16021790555034801561002c57600080fd5b50604051602080610bbb83398101806040528101908080519060200190929190505050336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555080600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050610adb806100e06000396000f30060806040526004361061008e576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806305282c701461009357806308165cd6146100d657806328e914891461010557806335968e681461015c578063398fc7811461018b5780638f55e225146101e2578063df3150aa14610211578063ede7ddf614610240575b600080fd5b34801561009f57600080fd5b506100d4600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610271565b005b3480156100e257600080fd5b506100eb6103f8565b604051808215151515815260200191505060405180910390f35b34801561011157600080fd5b5061011a61063b565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561016857600080fd5b50610171610660565b604051808215151515815260200191505060405180910390f35b34801561019757600080fd5b506101a061067c565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b3480156101ee57600080fd5b506101f76106a2565b604051808215151515815260200191505060405180910390f35b34801561021d57600080fd5b506102266108d7565b604051808215151515815260200191505060405180910390f35b34801561024c57600080fd5b50610255610a9c565b604051808260ff1660ff16815260200191505060405180910390f35b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561035b576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260268152602001807f57454261736963417574683a206f6e6c79206f776e657220697320617574686f81526020017f72697a65642e000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055503073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff167f480107a875206c9f5ec6e8b65d989106e27d0fc8b130625b25997540ddfc334a60405160405180910390a350565b6000600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156104e5576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260228152602001807f426173654163636f756e743a206f6e6c79206163636f756e74206d616e61676581526020017f722e00000000000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b60018060149054906101000a900460ff1660ff16141515610594576040517fc703cb1200000000000000000000000000000000000000000000000000000000815260040180806020018281038252602d8152602001807f426173654163636f756e743a206f6e6c79206163636f756e742073746174757381526020017f2069732061626e6f726d616c2e0000000000000000000000000000000000000081525060400191505060405180910390fd5b6000600160146101000a81548160ff021916908360ff1602179055507f756e667265657a650000000000000000000000000000000000000000000000007f598b76607bab91793e04db590052049ff4ca46cfc234328da5536f5169790af730604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390a26001905090565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600080600160149054906101000a900460ff1660ff1614905090565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b6000600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561078f576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260228152602001807f426173654163636f756e743a206f6e6c79206163636f756e74206d616e61676581526020017f722e00000000000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b610797610660565b1515610831576040517fc703cb1200000000000000000000000000000000000000000000000000000000815260040180806020018281038252602b8152602001807f426173654163636f756e743a206f6e6c79206163636f756e742073746174757381526020017f206973206e6f726d616c2e00000000000000000000000000000000000000000081525060400191505060405180910390fd5b60018060146101000a81548160ff021916908360ff1602179055507f667265657a6500000000000000000000000000000000000000000000000000007f598b76607bab91793e04db590052049ff4ca46cfc234328da5536f5169790af730604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390a26001905090565b6000600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16148061098157506000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16145b15156109f5576040517fc703cb1200000000000000000000000000000000000000000000000000000000815260040180806020018281038252601d8152602001807f426173654163636f756e743a206f6e6c7920617574686f72697a65642e00000081525060200191505060405180910390fd5b6002600160146101000a81548160ff021916908360ff1602179055507f63616e63656c00000000000000000000000000000000000000000000000000007f598b76607bab91793e04db590052049ff4ca46cfc234328da5536f5169790af730604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390a26001905090565b600160149054906101000a900460ff16815600a165627a7a72305820f117d6adeba3c53e26abce53ffb424f87ace3e8fa93b57137bda28d2801514370029"
    };

    public static final String SM_BINARY = String.join("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {
        "[{\"constant\":true,\"inputs\":[],\"name\":\"_status\",\"outputs\":[{\"name\":\"\",\"type\":\"uint8\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"owner\",\"type\":\"address\"}],\"name\":\"setOwner\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"freeze\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"unfreeze\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"isNormal\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"_accountManager\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"_owner\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"cancel\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"name\":\"accountManager\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"name\":\"owner\",\"type\":\"address\"},{\"indexed\":true,\"name\":\"contractAddress\",\"type\":\"address\"}],\"name\":\"LogSetOwner\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"name\":\"eventType\",\"type\":\"bytes32\"},{\"indexed\":false,\"name\":\"contractAddress\",\"type\":\"address\"}],\"name\":\"LogBaseAccount\",\"type\":\"event\"}]"
    };

    public static final String ABI = String.join("", ABI_ARRAY);

    public static final String FUNC__STATUS = "_status";

    public static final String FUNC_SETOWNER = "setOwner";

    public static final String FUNC_FREEZE = "freeze";

    public static final String FUNC_UNFREEZE = "unfreeze";

    public static final String FUNC_ISNORMAL = "isNormal";

    public static final String FUNC__ACCOUNTMANAGER = "_accountManager";

    public static final String FUNC__OWNER = "_owner";

    public static final String FUNC_CANCEL = "cancel";

    public static final Event LOGSETOWNER_EVENT =
            new Event(
                    "LogSetOwner",
                    Arrays.<TypeReference<?>>asList(
                            new TypeReference<Address>(true) {},
                            new TypeReference<Address>(true) {}));;

    public static final Event LOGBASEACCOUNT_EVENT =
            new Event(
                    "LogBaseAccount",
                    Arrays.<TypeReference<?>>asList(
                            new TypeReference<Bytes32>(true) {}, new TypeReference<Address>() {}));;

    protected BaseAccount(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public BigInteger _status() throws ContractException {
        final Function function =
                new Function(
                        FUNC__STATUS,
                        Arrays.<Type>asList(),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public TransactionReceipt setOwner(String owner) {
        final Function function =
                new Function(
                        FUNC_SETOWNER,
                        Arrays.<Type>asList(new Address(owner)),
                        Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void setOwner(String owner, TransactionCallback callback) {
        final Function function =
                new Function(
                        FUNC_SETOWNER,
                        Arrays.<Type>asList(new Address(owner)),
                        Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForSetOwner(String owner) {
        final Function function =
                new Function(
                        FUNC_SETOWNER,
                        Arrays.<Type>asList(new Address(owner)),
                        Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple1<String> getSetOwnerInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function =
                new Function(
                        FUNC_SETOWNER,
                        Arrays.<Type>asList(),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<String>((String) results.get(0).getValue());
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

    public String _accountManager() throws ContractException {
        final Function function =
                new Function(
                        FUNC__ACCOUNTMANAGER,
                        Arrays.<Type>asList(),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeCallWithSingleValueReturn(function, String.class);
    }

    public String _owner() throws ContractException {
        final Function function =
                new Function(
                        FUNC__OWNER,
                        Arrays.<Type>asList(),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeCallWithSingleValueReturn(function, String.class);
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

    public List<LogSetOwnerEventResponse> getLogSetOwnerEvents(
            TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList =
                extractEventParametersWithLog(LOGSETOWNER_EVENT, transactionReceipt);
        ArrayList<LogSetOwnerEventResponse> responses =
                new ArrayList<LogSetOwnerEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            LogSetOwnerEventResponse typedResponse = new LogSetOwnerEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.contractAddress =
                    (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeLogSetOwnerEvent(
            String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(LOGSETOWNER_EVENT);
        subscribeEvent(ABI, BINARY, topic0, fromBlock, toBlock, otherTopics, callback);
    }

    public void subscribeLogSetOwnerEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(LOGSETOWNER_EVENT);
        subscribeEvent(ABI, BINARY, topic0, callback);
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

    public static BaseAccount load(
            String contractAddress, Client client, CryptoKeyPair credential) {
        return new BaseAccount(contractAddress, client, credential);
    }

    public static BaseAccount deploy(Client client, CryptoKeyPair credential, String accountManager)
            throws ContractException {
        String encodedConstructor =
                FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(accountManager)));
        return deploy(
                BaseAccount.class,
                client,
                credential,
                getBinary(client.getCryptoSuite()),
                encodedConstructor);
    }

    public static class LogSetOwnerEventResponse {
        public TransactionReceipt.Logs log;

        public String owner;

        public String contractAddress;
    }

    public static class LogBaseAccountEventResponse {
        public TransactionReceipt.Logs log;

        public byte[] eventType;

        public String contractAddress;
    }
}
