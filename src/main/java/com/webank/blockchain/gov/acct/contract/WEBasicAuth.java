package com.webank.blockchain.gov.acct.contract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.abi.FunctionReturnDecoder;
import org.fisco.bcos.sdk.abi.TypeReference;
import org.fisco.bcos.sdk.abi.datatypes.Address;
import org.fisco.bcos.sdk.abi.datatypes.Event;
import org.fisco.bcos.sdk.abi.datatypes.Function;
import org.fisco.bcos.sdk.abi.datatypes.Type;
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
public class WEBasicAuth extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b50336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506102c3806100606000396000f30060806040526004361061004c576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806313af403514610051578063b2bdfa7b14610094575b600080fd5b34801561005d57600080fd5b50610092600480360381019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506100eb565b005b3480156100a057600080fd5b506100a9610272565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156101d5576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260268152602001807f57454261736963417574683a206f6e6c79206f776e657220697320617574686f81526020017f72697a65642e000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055503073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff167fc66d1d23a5b7baf1f496bb19f580d7b12070ad5a08a758c990db97d961fa33a660405160405180910390a350565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff16815600a165627a7a72305820a01f4263d3dd23eff0f5e2eeb783ed27cb707078601da83cdbebfd378a78b43e0029"};

    public static final String BINARY = String.join("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"608060405234801561001057600080fd5b50336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506102c3806100606000396000f30060806040526004361061004c576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806305282c701461005157806328e9148914610094575b600080fd5b34801561005d57600080fd5b50610092600480360381019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506100eb565b005b3480156100a057600080fd5b506100a9610272565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156101d5576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260268152602001807f57454261736963417574683a206f6e6c79206f776e657220697320617574686f81526020017f72697a65642e000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055503073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff167f480107a875206c9f5ec6e8b65d989106e27d0fc8b130625b25997540ddfc334a60405160405180910390a350565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff16815600a165627a7a7230582008c8ae0ed192a43439b604e299db9634dd993587057d15bcbcb219447f5242240029"};

    public static final String SM_BINARY = String.join("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"constant\":false,\"inputs\":[{\"name\":\"owner\",\"type\":\"address\"}],\"name\":\"setOwner\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"_owner\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"name\":\"owner\",\"type\":\"address\"},{\"indexed\":true,\"name\":\"contractAddress\",\"type\":\"address\"}],\"name\":\"LogSetOwner\",\"type\":\"event\"}]"};

    public static final String ABI = String.join("", ABI_ARRAY);

    public static final String FUNC_SETOWNER = "setOwner";

    public static final String FUNC__OWNER = "_owner";

    public static final Event LOGSETOWNER_EVENT = new Event("LogSetOwner", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    protected WEBasicAuth(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public TransactionReceipt setOwner(String owner) {
        final Function function = new Function(
                FUNC_SETOWNER, 
                Arrays.<Type>asList(new Address(owner)),
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void setOwner(String owner, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_SETOWNER, 
                Arrays.<Type>asList(new Address(owner)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForSetOwner(String owner) {
        final Function function = new Function(
                FUNC_SETOWNER, 
                Arrays.<Type>asList(new Address(owner)),
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple1<String> getSetOwnerInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_SETOWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<String>(

                (String) results.get(0).getValue()
                );
    }

    public String _owner() throws ContractException {
        final Function function = new Function(FUNC__OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeCallWithSingleValueReturn(function, String.class);
    }

    public List<LogSetOwnerEventResponse> getLogSetOwnerEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(LOGSETOWNER_EVENT, transactionReceipt);
        ArrayList<LogSetOwnerEventResponse> responses = new ArrayList<LogSetOwnerEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            LogSetOwnerEventResponse typedResponse = new LogSetOwnerEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.contractAddress = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeLogSetOwnerEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(LOGSETOWNER_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeLogSetOwnerEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(LOGSETOWNER_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public static WEBasicAuth load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new WEBasicAuth(contractAddress, client, credential);
    }

    public static WEBasicAuth deploy(Client client, CryptoKeyPair credential) throws ContractException {
        return deploy(WEBasicAuth.class, client, credential, getBinary(client.getCryptoSuite()), "");
    }

    public static class LogSetOwnerEventResponse {
        public TransactionReceipt.Logs log;

        public String owner;

        public String contractAddress;
    }
}
