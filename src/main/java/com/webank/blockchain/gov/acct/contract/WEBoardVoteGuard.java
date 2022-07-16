package com.webank.blockchain.gov.acct.contract;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.codec.datatypes.Address;
import org.fisco.bcos.sdk.v3.codec.datatypes.Bool;
import org.fisco.bcos.sdk.v3.codec.datatypes.DynamicArray;
import org.fisco.bcos.sdk.v3.codec.datatypes.Event;
import org.fisco.bcos.sdk.v3.codec.datatypes.Function;
import org.fisco.bcos.sdk.v3.codec.datatypes.Type;
import org.fisco.bcos.sdk.v3.codec.datatypes.TypeReference;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint16;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint8;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple3;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple4;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple8;
import org.fisco.bcos.sdk.v3.contract.Contract;
import org.fisco.bcos.sdk.v3.crypto.CryptoSuite;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.model.CryptoType;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.fisco.bcos.sdk.v3.model.callback.TransactionCallback;
import org.fisco.bcos.sdk.v3.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class WEBoardVoteGuard extends Contract {
    public static final String[] BINARY_ARRAY = {
        "608060405234801561001057600080fd5b50336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550612710600560040181905550610dcb8061006c6000396000f300608060405260043610610083576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680630a0155e5146100885780630b816045146100cd57806313af4035146101bb578063a730117e146101fe578063adc1fb771461025d578063b2bdfa7b14610319578063c3d4cb0b14610370575b600080fd5b34801561009457600080fd5b506100b360048036038101908080359060200190929190505050610433565b604051808215151515815260200191505060405180910390f35b3480156100d957600080fd5b506100f860048036038101908080359060200190929190505050610450565b604051808981526020018873ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018761ffff1661ffff1681526020018661ffff1661ffff1681526020018560ff1660ff1681526020018460ff1660ff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018261ffff1661ffff1681526020019850505050505050505060405180910390f35b3480156101c757600080fd5b506101fc600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061048b565b005b34801561020a57600080fd5b5061023f600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610612565b604051808261ffff1661ffff16815260200191505060405180910390f35b34801561026957600080fd5b506102886004803603810190808035906020019092919050505061062f565b604051808560ff1660ff1681526020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018261ffff1661ffff16815260200194505050505060405180910390f35b34801561032557600080fd5b5061032e610658565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561037c57600080fd5b5061038561067d565b6040518080602001806020018461ffff1661ffff168152602001838103835286818151815260200191508051906020019060200280838360005b838110156103da5780820151818401526020810190506103bf565b50505050905001838103825285818151815260200191508051906020019060200280838360005b8381101561041c578082015181840152602081019050610401565b505050509050019550505050505060405180910390f35b600061044982600561069790919063ffffffff16565b9050919050565b6000806000806000806000806104708960056106ca90919063ffffffff16565b97509750975097509750975097509750919395975091939597565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610575576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260268152602001807f57454261736963417574683a206f6e6c79206f776e657220697320617574686f81526020017f72697a65642e000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055503073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff167fc66d1d23a5b7baf1f496bb19f580d7b12070ad5a08a758c990db97d961fa33a660405160405180910390a350565b600061062882600161099f90919063ffffffff16565b9050919050565b600080600080610649856005610a2d90919063ffffffff16565b93509350935093509193509193565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b606080600061068c6001610b68565b925092509250909192565b6000600183600501600084815260200190815260200160002060009054906101000a900460ff1660ff1614905092915050565b6000806000806000806000806106de610d1b565b8a8a600073ffffffffffffffffffffffffffffffffffffffff1682600601600083815260200190815260200160002060010160059054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16141515156107e3576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260268152602001807f4c6962426f617264566f74653a20766f746520696420616c726561647920657881526020017f69737465642e000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b8c60060160008d81526020019081526020016000206101006040519081016040529081600082015481526020016001820160009054906101000a900461ffff1661ffff1661ffff1681526020016001820160029054906101000a900461ffff1661ffff1661ffff1681526020016001820160049054906101000a900460ff1660ff1660ff1681526020016001820160059054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020016001820160199054906101000a900460ff1660ff1660ff1681526020016002820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020016002820160149054906101000a900461ffff1661ffff1661ffff16815250509250826000015183608001518460200151856040015186606001518760a001518860c001518960e001519a509a509a509a509a509a509a509a505050509295985092959890939650565b60008060008460000160008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054915084600201600183038154811015156109fc57fe5b90600052602060002090601091828204019190066002029054906101000a900461ffff169050809250505092915050565b6000806000808585600073ffffffffffffffffffffffffffffffffffffffff1682600601600083815260200190815260200160002060010160059054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1614151515610b38576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260268152602001807f4c6962426f617264566f74653a20766f746520696420616c726561647920657881526020017f69737465642e000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b610b55886006016000898152602001908152602001600020610c97565b9550955095509550505092959194509250565b606080600083600101846002018560030160009054906101000a900461ffff1682805480602002602001604051908101604052809291908181526020018280548015610c0957602002820191906000526020600020905b8160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019060010190808311610bbf575b5050505050925081805480602002602001604051908101604052809291908181526020018280548015610c8357602002820191906000526020600020906000905b82829054906101000a900461ffff1661ffff1681526020019060020190602082600101049283019260010382029150808411610c4a5790505b505050505091509250925092509193909250565b6000806000808460010160049054906101000a900460ff168560010160059054906101000a900473ffffffffffffffffffffffffffffffffffffffff168660020160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff168760020160149054906101000a900461ffff1693509350935093509193509193565b6101006040519081016040528060008152602001600061ffff168152602001600061ffff168152602001600060ff168152602001600073ffffffffffffffffffffffffffffffffffffffff168152602001600060ff168152602001600073ffffffffffffffffffffffffffffffffffffffff168152602001600061ffff16815250905600a165627a7a723058207dec13af4e46ca4fc03ca6ae118da2021cc2845c853149ee3103349038e264780029"
    };

    public static final String BINARY =
            org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {
        "608060405234801561001057600080fd5b50336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550612710600560040181905550610dcb8061006c6000396000f300608060405260043610610083576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806305282c701461008857806320e462b0146100cb57806328e914891461011057806366a24696146101675780637c1a8d9514610223578063aef5ecfa14610311578063ef79841d14610370575b600080fd5b34801561009457600080fd5b506100c9600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610433565b005b3480156100d757600080fd5b506100f6600480360381019080803590602001909291905050506105ba565b604051808215151515815260200191505060405180910390f35b34801561011c57600080fd5b506101256105d7565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561017357600080fd5b50610192600480360381019080803590602001909291905050506105fc565b604051808560ff1660ff1681526020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018261ffff1661ffff16815260200194505050505060405180910390f35b34801561022f57600080fd5b5061024e60048036038101908080359060200190929190505050610625565b604051808981526020018873ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018761ffff1661ffff1681526020018661ffff1661ffff1681526020018560ff1660ff1681526020018460ff1660ff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018261ffff1661ffff1681526020019850505050505050505060405180910390f35b34801561031d57600080fd5b50610352600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610660565b604051808261ffff1661ffff16815260200191505060405180910390f35b34801561037c57600080fd5b5061038561067d565b6040518080602001806020018461ffff1661ffff168152602001838103835286818151815260200191508051906020019060200280838360005b838110156103da5780820151818401526020810190506103bf565b50505050905001838103825285818151815260200191508051906020019060200280838360005b8381101561041c578082015181840152602081019050610401565b505050509050019550505050505060405180910390f35b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561051d576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260268152602001807f57454261736963417574683a206f6e6c79206f776e657220697320617574686f81526020017f72697a65642e000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055503073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff167f480107a875206c9f5ec6e8b65d989106e27d0fc8b130625b25997540ddfc334a60405160405180910390a350565b60006105d082600561069790919063ffffffff16565b9050919050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b6000806000806106168560056106ca90919063ffffffff16565b93509350935093509193509193565b60008060008060008060008061064589600561080590919063ffffffff16565b97509750975097509750975097509750919395975091939597565b6000610676826001610ada90919063ffffffff16565b9050919050565b606080600061068c6001610b68565b925092509250909192565b6000600183600501600084815260200190815260200160002060009054906101000a900460ff1660ff1614905092915050565b6000806000808585600073ffffffffffffffffffffffffffffffffffffffff1682600601600083815260200190815260200160002060010160059054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16141515156107d5576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260268152602001807f4c6962426f617264566f74653a20766f746520696420616c726561647920657881526020017f69737465642e000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b6107f2886006016000898152602001908152602001600020610c97565b9550955095509550505092959194509250565b600080600080600080600080610819610d1b565b8a8a600073ffffffffffffffffffffffffffffffffffffffff1682600601600083815260200190815260200160002060010160059054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff161415151561091e576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260268152602001807f4c6962426f617264566f74653a20766f746520696420616c726561647920657881526020017f69737465642e000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b8c60060160008d81526020019081526020016000206101006040519081016040529081600082015481526020016001820160009054906101000a900461ffff1661ffff1661ffff1681526020016001820160029054906101000a900461ffff1661ffff1661ffff1681526020016001820160049054906101000a900460ff1660ff1660ff1681526020016001820160059054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020016001820160199054906101000a900460ff1660ff1660ff1681526020016002820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020016002820160149054906101000a900461ffff1661ffff1661ffff16815250509250826000015183608001518460200151856040015186606001518760a001518860c001518960e001519a509a509a509a509a509a509a509a505050509295985092959890939650565b60008060008460000160008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205491508460020160018303815481101515610b3757fe5b90600052602060002090601091828204019190066002029054906101000a900461ffff169050809250505092915050565b606080600083600101846002018560030160009054906101000a900461ffff1682805480602002602001604051908101604052809291908181526020018280548015610c0957602002820191906000526020600020905b8160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019060010190808311610bbf575b5050505050925081805480602002602001604051908101604052809291908181526020018280548015610c8357602002820191906000526020600020906000905b82829054906101000a900461ffff1661ffff1681526020019060020190602082600101049283019260010382029150808411610c4a5790505b505050505091509250925092509193909250565b6000806000808460010160049054906101000a900460ff168560010160059054906101000a900473ffffffffffffffffffffffffffffffffffffffff168660020160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff168760020160149054906101000a900461ffff1693509350935093509193509193565b6101006040519081016040528060008152602001600061ffff168152602001600061ffff168152602001600060ff168152602001600073ffffffffffffffffffffffffffffffffffffffff168152602001600060ff168152602001600073ffffffffffffffffffffffffffffffffffffffff168152602001600061ffff16815250905600a165627a7a7230582059fe2f91d2747aa28dff592f6541d1f4584b093ebb2ad362760755539f0395d30029"
    };

    public static final String SM_BINARY =
            org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {
        "[{\"conflictFields\":[{\"kind\":3,\"slot\":10,\"value\":[0]}],\"constant\":true,\"inputs\":[{\"name\":\"id\",\"type\":\"uint256\"}],\"name\":\"passed\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"selector\":[167859685,551838384],\"stateMutability\":\"view\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":3,\"slot\":11,\"value\":[0]}],\"constant\":true,\"inputs\":[{\"name\":\"id\",\"type\":\"uint256\"}],\"name\":\"getRequestInfo\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"},{\"name\":\"\",\"type\":\"address\"},{\"name\":\"\",\"type\":\"uint16\"},{\"name\":\"\",\"type\":\"uint16\"},{\"name\":\"\",\"type\":\"uint8\"},{\"name\":\"\",\"type\":\"uint8\"},{\"name\":\"\",\"type\":\"address\"},{\"name\":\"\",\"type\":\"uint16\"}],\"payable\":false,\"selector\":[193028165,2082114965],\"stateMutability\":\"view\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":4,\"value\":[0]}],\"constant\":false,\"inputs\":[{\"name\":\"owner\",\"type\":\"address\"}],\"name\":\"setOwner\",\"outputs\":[],\"payable\":false,\"selector\":[330252341,86518896],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":0},{\"kind\":4,\"value\":[3]}],\"constant\":true,\"inputs\":[{\"name\":\"externalAccount\",\"type\":\"address\"}],\"name\":\"getVoteWeight\",\"outputs\":[{\"name\":\"\",\"type\":\"uint16\"}],\"payable\":false,\"selector\":[2804945278,2935352570],\"stateMutability\":\"view\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":3,\"slot\":11,\"value\":[0]}],\"constant\":true,\"inputs\":[{\"name\":\"id\",\"type\":\"uint256\"}],\"name\":\"getVoteParas\",\"outputs\":[{\"name\":\"\",\"type\":\"uint8\"},{\"name\":\"\",\"type\":\"address\"},{\"name\":\"\",\"type\":\"address\"},{\"name\":\"\",\"type\":\"uint16\"}],\"payable\":false,\"selector\":[2915171191,1721910934],\"stateMutability\":\"view\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":4,\"value\":[0]}],\"constant\":true,\"inputs\":[],\"name\":\"_owner\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"selector\":[2998794875,686363785],\"stateMutability\":\"view\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":4,\"value\":[2]},{\"kind\":4,\"value\":[3]},{\"kind\":4,\"value\":[4]}],\"constant\":true,\"inputs\":[],\"name\":\"getWeightInfo\",\"outputs\":[{\"name\":\"\",\"type\":\"address[]\"},{\"name\":\"\",\"type\":\"uint16[]\"},{\"name\":\"\",\"type\":\"uint16\"}],\"payable\":false,\"selector\":[3285502731,4017718301],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"name\":\"id\",\"type\":\"uint256\"},{\"indexed\":true,\"name\":\"txType\",\"type\":\"uint8\"},{\"indexed\":true,\"name\":\"requestAddress\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"newAddress\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"newValue\",\"type\":\"uint16\"},{\"indexed\":false,\"name\":\"contractAddress\",\"type\":\"address\"}],\"name\":\"LogRegister\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"name\":\"id\",\"type\":\"uint256\"},{\"indexed\":false,\"name\":\"contractAddress\",\"type\":\"address\"}],\"name\":\"LogUnregister\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"name\":\"id\",\"type\":\"uint256\"},{\"indexed\":true,\"name\":\"externalAccount\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"flag\",\"type\":\"bool\"},{\"indexed\":false,\"name\":\"contractAddress\",\"type\":\"address\"}],\"name\":\"LogVote\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"name\":\"owner\",\"type\":\"address\"},{\"indexed\":true,\"name\":\"contractAddress\",\"type\":\"address\"}],\"name\":\"LogSetOwner\",\"type\":\"event\"}]"
    };

    public static final String ABI = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_PASSED = "passed";

    public static final String FUNC_GETREQUESTINFO = "getRequestInfo";

    public static final String FUNC_SETOWNER = "setOwner";

    public static final String FUNC_GETVOTEWEIGHT = "getVoteWeight";

    public static final String FUNC_GETVOTEPARAS = "getVoteParas";

    public static final String FUNC__OWNER = "_owner";

    public static final String FUNC_GETWEIGHTINFO = "getWeightInfo";

    public static final Event LOGREGISTER_EVENT =
            new Event(
                    "LogRegister",
                    Arrays.<TypeReference<?>>asList(
                            new TypeReference<Uint256>(true) {},
                            new TypeReference<Uint8>(true) {},
                            new TypeReference<Address>(true) {},
                            new TypeReference<Address>() {},
                            new TypeReference<Uint16>() {},
                            new TypeReference<Address>() {}));;

    public static final Event LOGUNREGISTER_EVENT =
            new Event(
                    "LogUnregister",
                    Arrays.<TypeReference<?>>asList(
                            new TypeReference<Uint256>(true) {}, new TypeReference<Address>() {}));;

    public static final Event LOGVOTE_EVENT =
            new Event(
                    "LogVote",
                    Arrays.<TypeReference<?>>asList(
                            new TypeReference<Uint256>(true) {},
                            new TypeReference<Address>(true) {},
                            new TypeReference<Bool>() {},
                            new TypeReference<Address>() {}));;

    public static final Event LOGSETOWNER_EVENT =
            new Event(
                    "LogSetOwner",
                    Arrays.<TypeReference<?>>asList(
                            new TypeReference<Address>(true) {},
                            new TypeReference<Address>(true) {}));;

    protected WEBoardVoteGuard(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public static String getABI() {
        return ABI;
    }

    public Boolean passed(BigInteger id) throws ContractException {
        final Function function =
                new Function(
                        FUNC_PASSED,
                        Arrays.<Type>asList(new Uint256(id)),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeCallWithSingleValueReturn(function, Boolean.class);
    }

    public Tuple8<
                    BigInteger,
                    String,
                    BigInteger,
                    BigInteger,
                    BigInteger,
                    BigInteger,
                    String,
                    BigInteger>
            getRequestInfo(BigInteger id) throws ContractException {
        final Function function =
                new Function(
                        FUNC_GETREQUESTINFO,
                        Arrays.<Type>asList(new Uint256(id)),
                        Arrays.<TypeReference<?>>asList(
                                new TypeReference<Uint256>() {},
                                new TypeReference<Address>() {},
                                new TypeReference<Uint16>() {},
                                new TypeReference<Uint16>() {},
                                new TypeReference<Uint8>() {},
                                new TypeReference<Uint8>() {},
                                new TypeReference<Address>() {},
                                new TypeReference<Uint16>() {}));
        List<Type> results = executeCallWithMultipleValueReturn(function);
        return new Tuple8<
                BigInteger,
                String,
                BigInteger,
                BigInteger,
                BigInteger,
                BigInteger,
                String,
                BigInteger>(
                (BigInteger) results.get(0).getValue(),
                (String) results.get(1).getValue(),
                (BigInteger) results.get(2).getValue(),
                (BigInteger) results.get(3).getValue(),
                (BigInteger) results.get(4).getValue(),
                (BigInteger) results.get(5).getValue(),
                (String) results.get(6).getValue(),
                (BigInteger) results.get(7).getValue());
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

    public BigInteger getVoteWeight(String externalAccount) throws ContractException {
        final Function function =
                new Function(
                        FUNC_GETVOTEWEIGHT,
                        Arrays.<Type>asList(new Address(externalAccount)),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Uint16>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public Tuple4<BigInteger, String, String, BigInteger> getVoteParas(BigInteger id)
            throws ContractException {
        final Function function =
                new Function(
                        FUNC_GETVOTEPARAS,
                        Arrays.<Type>asList(new Uint256(id)),
                        Arrays.<TypeReference<?>>asList(
                                new TypeReference<Uint8>() {},
                                new TypeReference<Address>() {},
                                new TypeReference<Address>() {},
                                new TypeReference<Uint16>() {}));
        List<Type> results = executeCallWithMultipleValueReturn(function);
        return new Tuple4<BigInteger, String, String, BigInteger>(
                (BigInteger) results.get(0).getValue(),
                (String) results.get(1).getValue(),
                (String) results.get(2).getValue(),
                (BigInteger) results.get(3).getValue());
    }

    public String _owner() throws ContractException {
        final Function function =
                new Function(
                        FUNC__OWNER,
                        Arrays.<Type>asList(),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeCallWithSingleValueReturn(function, String.class);
    }

    public Tuple3<List<String>, List<BigInteger>, BigInteger> getWeightInfo()
            throws ContractException {
        final Function function =
                new Function(
                        FUNC_GETWEIGHTINFO,
                        Arrays.<Type>asList(),
                        Arrays.<TypeReference<?>>asList(
                                new TypeReference<DynamicArray<Address>>() {},
                                new TypeReference<DynamicArray<Uint16>>() {},
                                new TypeReference<Uint16>() {}));
        List<Type> results = executeCallWithMultipleValueReturn(function);
        return new Tuple3<List<String>, List<BigInteger>, BigInteger>(
                convertToNative((List<Address>) results.get(0).getValue()),
                convertToNative((List<Uint16>) results.get(1).getValue()),
                (BigInteger) results.get(2).getValue());
    }

    public List<LogRegisterEventResponse> getLogRegisterEvents(
            TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList =
                extractEventParametersWithLog(LOGREGISTER_EVENT, transactionReceipt);
        ArrayList<LogRegisterEventResponse> responses =
                new ArrayList<LogRegisterEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            LogRegisterEventResponse typedResponse = new LogRegisterEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.id = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.txType = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.requestAddress =
                    (String) eventValues.getIndexedValues().get(2).getValue();
            typedResponse.newAddress = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.newValue =
                    (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.contractAddress =
                    (String) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public List<LogUnregisterEventResponse> getLogUnregisterEvents(
            TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList =
                extractEventParametersWithLog(LOGUNREGISTER_EVENT, transactionReceipt);
        ArrayList<LogUnregisterEventResponse> responses =
                new ArrayList<LogUnregisterEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            LogUnregisterEventResponse typedResponse = new LogUnregisterEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.id = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.contractAddress =
                    (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public List<LogVoteEventResponse> getLogVoteEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList =
                extractEventParametersWithLog(LOGVOTE_EVENT, transactionReceipt);
        ArrayList<LogVoteEventResponse> responses =
                new ArrayList<LogVoteEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            LogVoteEventResponse typedResponse = new LogVoteEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.id = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.externalAccount =
                    (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.flag = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.contractAddress =
                    (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
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

    public static WEBoardVoteGuard load(
            String contractAddress, Client client, CryptoKeyPair credential) {
        return new WEBoardVoteGuard(contractAddress, client, credential);
    }

    public static WEBoardVoteGuard deploy(Client client, CryptoKeyPair credential)
            throws ContractException {
        return deploy(
                WEBoardVoteGuard.class,
                client,
                credential,
                getBinary(client.getCryptoSuite()),
                getABI(),
                null,
                null);
    }

    public static class LogRegisterEventResponse {
        public TransactionReceipt.Logs log;

        public BigInteger id;

        public BigInteger txType;

        public String requestAddress;

        public String newAddress;

        public BigInteger newValue;

        public String contractAddress;
    }

    public static class LogUnregisterEventResponse {
        public TransactionReceipt.Logs log;

        public BigInteger id;

        public String contractAddress;
    }

    public static class LogVoteEventResponse {
        public TransactionReceipt.Logs log;

        public BigInteger id;

        public String externalAccount;

        public Boolean flag;

        public String contractAddress;
    }

    public static class LogSetOwnerEventResponse {
        public TransactionReceipt.Logs log;

        public String owner;

        public String contractAddress;
    }
}
