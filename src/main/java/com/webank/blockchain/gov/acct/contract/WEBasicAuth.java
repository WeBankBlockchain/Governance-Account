package com.webank.blockchain.gov.acct.contract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.codec.datatypes.Address;
import org.fisco.bcos.sdk.v3.codec.datatypes.Event;
import org.fisco.bcos.sdk.v3.codec.datatypes.Function;
import org.fisco.bcos.sdk.v3.codec.datatypes.Type;
import org.fisco.bcos.sdk.v3.codec.datatypes.TypeReference;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.v3.contract.Contract;
import org.fisco.bcos.sdk.v3.crypto.CryptoSuite;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.model.CryptoType;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.fisco.bcos.sdk.v3.model.callback.TransactionCallback;
import org.fisco.bcos.sdk.v3.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class WEBasicAuth extends Contract {
    public static final String[] BINARY_ARRAY = {
        "608060405234801561001057600080fd5b50336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555061034e806100606000396000f300608060405260043610610057576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806313af40351461005c578063893d20e81461009f578063b2bdfa7b146100f6575b600080fd5b34801561006857600080fd5b5061009d600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061014d565b005b3480156100ab57600080fd5b506100b46102d4565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561010257600080fd5b5061010b6102fd565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610237576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260268152602001807f57454261736963417574683a206f6e6c79206f776e657220697320617574686f81526020017f72697a65642e000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055503073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff167fc66d1d23a5b7baf1f496bb19f580d7b12070ad5a08a758c990db97d961fa33a660405160405180910390a350565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff16815600a165627a7a723058203097576fc7d8ee33094b37982cead29ce976346a19ea1e1fd5c9d0c3a3ea099c0029"
    };

    public static final String BINARY =
            org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {
        "608060405234801561001057600080fd5b50336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555061034e806100606000396000f300608060405260043610610057576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806305282c701461005c57806328e914891461009f578063c624d534146100f6575b600080fd5b34801561006857600080fd5b5061009d600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061014d565b005b3480156100ab57600080fd5b506100b46102d4565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561010257600080fd5b5061010b6102f9565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610237576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260268152602001807f57454261736963417574683a206f6e6c79206f776e657220697320617574686f81526020017f72697a65642e000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055503073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff167f480107a875206c9f5ec6e8b65d989106e27d0fc8b130625b25997540ddfc334a60405160405180910390a350565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050905600a165627a7a723058206513aa7696b397b53f96af1ea3620b70f590e8a804799d0a068c81b09e285e2f0029"
    };

    public static final String SM_BINARY =
            org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {
        "[{\"conflictFields\":[{\"kind\":4,\"value\":[0]}],\"constant\":false,\"inputs\":[{\"name\":\"owner\",\"type\":\"address\"}],\"name\":\"setOwner\",\"outputs\":[],\"payable\":false,\"selector\":[330252341,86518896],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":4,\"value\":[0]}],\"constant\":true,\"inputs\":[],\"name\":\"getOwner\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"selector\":[2302484712,3324302644],\"stateMutability\":\"view\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":4,\"value\":[0]}],\"constant\":true,\"inputs\":[],\"name\":\"_owner\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"selector\":[2998794875,686363785],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"name\":\"owner\",\"type\":\"address\"},{\"indexed\":true,\"name\":\"contractAddress\",\"type\":\"address\"}],\"name\":\"LogSetOwner\",\"type\":\"event\"}]"
    };

    public static final String ABI = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_SETOWNER = "setOwner";

    public static final String FUNC_GETOWNER = "getOwner";

    public static final String FUNC__OWNER = "_owner";

    public static final Event LOGSETOWNER_EVENT =
            new Event(
                    "LogSetOwner",
                    Arrays.<TypeReference<?>>asList(
                            new TypeReference<Address>(true) {},
                            new TypeReference<Address>(true) {}));;

    protected WEBasicAuth(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public static String getABI() {
        return ABI;
    }

    public TransactionReceipt setOwner(String owner) {
        final Function function =
                new Function(
                        FUNC_SETOWNER,
                        Arrays.<Type>asList(new Address(owner)),
                        Collections.<TypeReference<?>>emptyList(),
                        0);
        return executeTransaction(function);
    }

    public String setOwner(String owner, TransactionCallback callback) {
        final Function function =
                new Function(
                        FUNC_SETOWNER,
                        Arrays.<Type>asList(new Address(owner)),
                        Collections.<TypeReference<?>>emptyList(),
                        0);
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForSetOwner(String owner) {
        final Function function =
                new Function(
                        FUNC_SETOWNER,
                        Arrays.<Type>asList(new Address(owner)),
                        Collections.<TypeReference<?>>emptyList(),
                        0);
        return createSignedTransaction(function);
    }

    public Tuple1<String> getSetOwnerInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function =
                new Function(
                        FUNC_SETOWNER,
                        Arrays.<Type>asList(),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        List<Type> results =
                this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<String>((String) results.get(0).getValue());
    }

    public String getOwner() throws ContractException {
        final Function function =
                new Function(
                        FUNC_GETOWNER,
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

    public static WEBasicAuth load(
            String contractAddress, Client client, CryptoKeyPair credential) {
        return new WEBasicAuth(contractAddress, client, credential);
    }

    public static WEBasicAuth deploy(Client client, CryptoKeyPair credential)
            throws ContractException {
        return deploy(
                WEBasicAuth.class,
                client,
                credential,
                getBinary(client.getCryptoSuite()),
                getABI(),
                null,
                null);
    }

    public static class LogSetOwnerEventResponse {
        public TransactionReceipt.Logs log;

        public String owner;

        public String contractAddress;
    }
}
