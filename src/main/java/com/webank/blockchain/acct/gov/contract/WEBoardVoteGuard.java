package com.webank.blockchain.acct.gov.contract;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.fisco.bcos.channel.client.TransactionSucCallback;
import org.fisco.bcos.channel.event.filter.EventLogPushWithDecodeCallback;
import org.fisco.bcos.web3j.abi.EventEncoder;
import org.fisco.bcos.web3j.abi.FunctionReturnDecoder;
import org.fisco.bcos.web3j.abi.TypeReference;
import org.fisco.bcos.web3j.abi.datatypes.Address;
import org.fisco.bcos.web3j.abi.datatypes.Bool;
import org.fisco.bcos.web3j.abi.datatypes.DynamicArray;
import org.fisco.bcos.web3j.abi.datatypes.Event;
import org.fisco.bcos.web3j.abi.datatypes.Function;
import org.fisco.bcos.web3j.abi.datatypes.Type;
import org.fisco.bcos.web3j.abi.datatypes.generated.Uint16;
import org.fisco.bcos.web3j.abi.datatypes.generated.Uint256;
import org.fisco.bcos.web3j.abi.datatypes.generated.Uint8;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.crypto.EncryptType;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.RemoteCall;
import org.fisco.bcos.web3j.protocol.core.methods.response.Log;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tuples.generated.Tuple1;
import org.fisco.bcos.web3j.tuples.generated.Tuple3;
import org.fisco.bcos.web3j.tuples.generated.Tuple4;
import org.fisco.bcos.web3j.tuples.generated.Tuple8;
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
public class WEBoardVoteGuard extends Contract {
    public static final String[] BINARY_ARRAY = {
        "608060405234801561001057600080fd5b50336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550612710600560040181905550610dcb8061006c6000396000f300608060405260043610610083576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680630a0155e5146100885780630b816045146100cd57806313af4035146101bb578063a730117e146101fe578063adc1fb771461025d578063b2bdfa7b14610319578063c3d4cb0b14610370575b600080fd5b34801561009457600080fd5b506100b360048036038101908080359060200190929190505050610433565b604051808215151515815260200191505060405180910390f35b3480156100d957600080fd5b506100f860048036038101908080359060200190929190505050610450565b604051808981526020018873ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018761ffff1661ffff1681526020018661ffff1661ffff1681526020018560ff1660ff1681526020018460ff1660ff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018261ffff1661ffff1681526020019850505050505050505060405180910390f35b3480156101c757600080fd5b506101fc600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061048b565b005b34801561020a57600080fd5b5061023f600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610612565b604051808261ffff1661ffff16815260200191505060405180910390f35b34801561026957600080fd5b506102886004803603810190808035906020019092919050505061062f565b604051808560ff1660ff1681526020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018261ffff1661ffff16815260200194505050505060405180910390f35b34801561032557600080fd5b5061032e610658565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561037c57600080fd5b5061038561067d565b6040518080602001806020018461ffff1661ffff168152602001838103835286818151815260200191508051906020019060200280838360005b838110156103da5780820151818401526020810190506103bf565b50505050905001838103825285818151815260200191508051906020019060200280838360005b8381101561041c578082015181840152602081019050610401565b505050509050019550505050505060405180910390f35b600061044982600561069790919063ffffffff16565b9050919050565b6000806000806000806000806104708960056106ca90919063ffffffff16565b97509750975097509750975097509750919395975091939597565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610575576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260268152602001807f57454261736963417574683a206f6e6c79206f776e657220697320617574686f81526020017f72697a65642e000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055503073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff167fc66d1d23a5b7baf1f496bb19f580d7b12070ad5a08a758c990db97d961fa33a660405160405180910390a350565b600061062882600161099f90919063ffffffff16565b9050919050565b600080600080610649856005610a2d90919063ffffffff16565b93509350935093509193509193565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b606080600061068c6001610b68565b925092509250909192565b6000600183600501600084815260200190815260200160002060009054906101000a900460ff1660ff1614905092915050565b6000806000806000806000806106de610d1b565b8a8a600073ffffffffffffffffffffffffffffffffffffffff1682600601600083815260200190815260200160002060010160059054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16141515156107e3576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260268152602001807f4c6962426f617264566f74653a20766f746520696420616c726561647920657881526020017f69737465642e000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b8c60060160008d81526020019081526020016000206101006040519081016040529081600082015481526020016001820160009054906101000a900461ffff1661ffff1661ffff1681526020016001820160029054906101000a900461ffff1661ffff1661ffff1681526020016001820160049054906101000a900460ff1660ff1660ff1681526020016001820160059054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020016001820160199054906101000a900460ff1660ff1660ff1681526020016002820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020016002820160149054906101000a900461ffff1661ffff1661ffff16815250509250826000015183608001518460200151856040015186606001518760a001518860c001518960e001519a509a509a509a509a509a509a509a505050509295985092959890939650565b60008060008460000160008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054915084600201600183038154811015156109fc57fe5b90600052602060002090601091828204019190066002029054906101000a900461ffff169050809250505092915050565b6000806000808585600073ffffffffffffffffffffffffffffffffffffffff1682600601600083815260200190815260200160002060010160059054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1614151515610b38576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260268152602001807f4c6962426f617264566f74653a20766f746520696420616c726561647920657881526020017f69737465642e000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b610b55886006016000898152602001908152602001600020610c97565b9550955095509550505092959194509250565b606080600083600101846002018560030160009054906101000a900461ffff1682805480602002602001604051908101604052809291908181526020018280548015610c0957602002820191906000526020600020905b8160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019060010190808311610bbf575b5050505050925081805480602002602001604051908101604052809291908181526020018280548015610c8357602002820191906000526020600020906000905b82829054906101000a900461ffff1661ffff1681526020019060020190602082600101049283019260010382029150808411610c4a5790505b505050505091509250925092509193909250565b6000806000808460010160049054906101000a900460ff168560010160059054906101000a900473ffffffffffffffffffffffffffffffffffffffff168660020160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff168760020160149054906101000a900461ffff1693509350935093509193509193565b6101006040519081016040528060008152602001600061ffff168152602001600061ffff168152602001600060ff168152602001600073ffffffffffffffffffffffffffffffffffffffff168152602001600060ff168152602001600073ffffffffffffffffffffffffffffffffffffffff168152602001600061ffff16815250905600a165627a7a72305820273664962eb8b0b963b86bf3ead585a9d22be2b2222019e0a848816450e0ad2b0029"
    };

    public static final String BINARY = String.join("", BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {
        "[{\"constant\":true,\"inputs\":[{\"name\":\"id\",\"type\":\"uint256\"}],\"name\":\"passed\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"id\",\"type\":\"uint256\"}],\"name\":\"getRequestInfo\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"},{\"name\":\"\",\"type\":\"address\"},{\"name\":\"\",\"type\":\"uint16\"},{\"name\":\"\",\"type\":\"uint16\"},{\"name\":\"\",\"type\":\"uint8\"},{\"name\":\"\",\"type\":\"uint8\"},{\"name\":\"\",\"type\":\"address\"},{\"name\":\"\",\"type\":\"uint16\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"owner\",\"type\":\"address\"}],\"name\":\"setOwner\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"externalAccount\",\"type\":\"address\"}],\"name\":\"getVoteWeight\",\"outputs\":[{\"name\":\"\",\"type\":\"uint16\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"id\",\"type\":\"uint256\"}],\"name\":\"getVoteParas\",\"outputs\":[{\"name\":\"\",\"type\":\"uint8\"},{\"name\":\"\",\"type\":\"address\"},{\"name\":\"\",\"type\":\"address\"},{\"name\":\"\",\"type\":\"uint16\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"_owner\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"getWeightInfo\",\"outputs\":[{\"name\":\"\",\"type\":\"address[]\"},{\"name\":\"\",\"type\":\"uint16[]\"},{\"name\":\"\",\"type\":\"uint16\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"name\":\"id\",\"type\":\"uint256\"},{\"indexed\":true,\"name\":\"txType\",\"type\":\"uint8\"},{\"indexed\":true,\"name\":\"requestAddress\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"newAddress\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"newValue\",\"type\":\"uint16\"},{\"indexed\":false,\"name\":\"contractAddress\",\"type\":\"address\"}],\"name\":\"LogRegister\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"name\":\"id\",\"type\":\"uint256\"},{\"indexed\":false,\"name\":\"contractAddress\",\"type\":\"address\"}],\"name\":\"LogUnregister\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"name\":\"id\",\"type\":\"uint256\"},{\"indexed\":true,\"name\":\"externalAccount\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"flag\",\"type\":\"bool\"},{\"indexed\":false,\"name\":\"contractAddress\",\"type\":\"address\"}],\"name\":\"LogVote\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"name\":\"owner\",\"type\":\"address\"},{\"indexed\":true,\"name\":\"contractAddress\",\"type\":\"address\"}],\"name\":\"LogSetOwner\",\"type\":\"event\"}]"
    };

    public static final String ABI = String.join("", ABI_ARRAY);

    public static final TransactionDecoder transactionDecoder = new TransactionDecoder(ABI, BINARY);

    public static final String[] SM_BINARY_ARRAY = {
        "608060405234801561001057600080fd5b50336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550612710600560040181905550610dcb8061006c6000396000f300608060405260043610610083576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806305282c701461008857806320e462b0146100cb57806328e914891461011057806366a24696146101675780637c1a8d9514610223578063aef5ecfa14610311578063ef79841d14610370575b600080fd5b34801561009457600080fd5b506100c9600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610433565b005b3480156100d757600080fd5b506100f6600480360381019080803590602001909291905050506105ba565b604051808215151515815260200191505060405180910390f35b34801561011c57600080fd5b506101256105d7565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561017357600080fd5b50610192600480360381019080803590602001909291905050506105fc565b604051808560ff1660ff1681526020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018261ffff1661ffff16815260200194505050505060405180910390f35b34801561022f57600080fd5b5061024e60048036038101908080359060200190929190505050610625565b604051808981526020018873ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018761ffff1661ffff1681526020018661ffff1661ffff1681526020018560ff1660ff1681526020018460ff1660ff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018261ffff1661ffff1681526020019850505050505050505060405180910390f35b34801561031d57600080fd5b50610352600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610660565b604051808261ffff1661ffff16815260200191505060405180910390f35b34801561037c57600080fd5b5061038561067d565b6040518080602001806020018461ffff1661ffff168152602001838103835286818151815260200191508051906020019060200280838360005b838110156103da5780820151818401526020810190506103bf565b50505050905001838103825285818151815260200191508051906020019060200280838360005b8381101561041c578082015181840152602081019050610401565b505050509050019550505050505060405180910390f35b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561051d576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260268152602001807f57454261736963417574683a206f6e6c79206f776e657220697320617574686f81526020017f72697a65642e000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055503073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff167f480107a875206c9f5ec6e8b65d989106e27d0fc8b130625b25997540ddfc334a60405160405180910390a350565b60006105d082600561069790919063ffffffff16565b9050919050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b6000806000806106168560056106ca90919063ffffffff16565b93509350935093509193509193565b60008060008060008060008061064589600561080590919063ffffffff16565b97509750975097509750975097509750919395975091939597565b6000610676826001610ada90919063ffffffff16565b9050919050565b606080600061068c6001610b68565b925092509250909192565b6000600183600501600084815260200190815260200160002060009054906101000a900460ff1660ff1614905092915050565b6000806000808585600073ffffffffffffffffffffffffffffffffffffffff1682600601600083815260200190815260200160002060010160059054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16141515156107d5576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260268152602001807f4c6962426f617264566f74653a20766f746520696420616c726561647920657881526020017f69737465642e000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b6107f2886006016000898152602001908152602001600020610c97565b9550955095509550505092959194509250565b600080600080600080600080610819610d1b565b8a8a600073ffffffffffffffffffffffffffffffffffffffff1682600601600083815260200190815260200160002060010160059054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff161415151561091e576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260268152602001807f4c6962426f617264566f74653a20766f746520696420616c726561647920657881526020017f69737465642e000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b8c60060160008d81526020019081526020016000206101006040519081016040529081600082015481526020016001820160009054906101000a900461ffff1661ffff1661ffff1681526020016001820160029054906101000a900461ffff1661ffff1661ffff1681526020016001820160049054906101000a900460ff1660ff1660ff1681526020016001820160059054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020016001820160199054906101000a900460ff1660ff1660ff1681526020016002820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020016002820160149054906101000a900461ffff1661ffff1661ffff16815250509250826000015183608001518460200151856040015186606001518760a001518860c001518960e001519a509a509a509a509a509a509a509a505050509295985092959890939650565b60008060008460000160008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205491508460020160018303815481101515610b3757fe5b90600052602060002090601091828204019190066002029054906101000a900461ffff169050809250505092915050565b606080600083600101846002018560030160009054906101000a900461ffff1682805480602002602001604051908101604052809291908181526020018280548015610c0957602002820191906000526020600020905b8160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019060010190808311610bbf575b5050505050925081805480602002602001604051908101604052809291908181526020018280548015610c8357602002820191906000526020600020906000905b82829054906101000a900461ffff1661ffff1681526020019060020190602082600101049283019260010382029150808411610c4a5790505b505050505091509250925092509193909250565b6000806000808460010160049054906101000a900460ff168560010160059054906101000a900473ffffffffffffffffffffffffffffffffffffffff168660020160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff168760020160149054906101000a900461ffff1693509350935093509193509193565b6101006040519081016040528060008152602001600061ffff168152602001600061ffff168152602001600060ff168152602001600073ffffffffffffffffffffffffffffffffffffffff168152602001600060ff168152602001600073ffffffffffffffffffffffffffffffffffffffff168152602001600061ffff16815250905600a165627a7a723058205ed7d72337d0ffda0d2a978dcde7432b1ec81599241f49ab2dfdeead5e6580000029"
    };

    public static final String SM_BINARY = String.join("", SM_BINARY_ARRAY);

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

    @Deprecated
    protected WEBoardVoteGuard(
            String contractAddress,
            Web3j web3j,
            Credentials credentials,
            BigInteger gasPrice,
            BigInteger gasLimit) {
        super(getBinary(), contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected WEBoardVoteGuard(
            String contractAddress,
            Web3j web3j,
            Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(getBinary(), contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected WEBoardVoteGuard(
            String contractAddress,
            Web3j web3j,
            TransactionManager transactionManager,
            BigInteger gasPrice,
            BigInteger gasLimit) {
        super(getBinary(), contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected WEBoardVoteGuard(
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

    public RemoteCall<Boolean> passed(BigInteger id) {
        final Function function =
                new Function(
                        FUNC_PASSED,
                        Arrays.<Type>asList(
                                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(id)),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<
                    Tuple8<
                            BigInteger,
                            String,
                            BigInteger,
                            BigInteger,
                            BigInteger,
                            BigInteger,
                            String,
                            BigInteger>>
            getRequestInfo(BigInteger id) {
        final Function function =
                new Function(
                        FUNC_GETREQUESTINFO,
                        Arrays.<Type>asList(
                                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(id)),
                        Arrays.<TypeReference<?>>asList(
                                new TypeReference<Uint256>() {},
                                new TypeReference<Address>() {},
                                new TypeReference<Uint16>() {},
                                new TypeReference<Uint16>() {},
                                new TypeReference<Uint8>() {},
                                new TypeReference<Uint8>() {},
                                new TypeReference<Address>() {},
                                new TypeReference<Uint16>() {}));
        return new RemoteCall<
                Tuple8<
                        BigInteger,
                        String,
                        BigInteger,
                        BigInteger,
                        BigInteger,
                        BigInteger,
                        String,
                        BigInteger>>(
                new Callable<
                        Tuple8<
                                BigInteger,
                                String,
                                BigInteger,
                                BigInteger,
                                BigInteger,
                                BigInteger,
                                String,
                                BigInteger>>() {
                    @Override
                    public Tuple8<
                                    BigInteger,
                                    String,
                                    BigInteger,
                                    BigInteger,
                                    BigInteger,
                                    BigInteger,
                                    String,
                                    BigInteger>
                            call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
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
                });
    }

    public RemoteCall<TransactionReceipt> setOwner(String owner) {
        final Function function =
                new Function(
                        FUNC_SETOWNER,
                        Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(owner)),
                        Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void setOwner(String owner, TransactionSucCallback callback) {
        final Function function =
                new Function(
                        FUNC_SETOWNER,
                        Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(owner)),
                        Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String setOwnerSeq(String owner) {
        final Function function =
                new Function(
                        FUNC_SETOWNER,
                        Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(owner)),
                        Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple1<String> getSetOwnerInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function =
                new Function(
                        FUNC_SETOWNER,
                        Arrays.<Type>asList(),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        ;
        return new Tuple1<String>((String) results.get(0).getValue());
    }

    public RemoteCall<BigInteger> getVoteWeight(String externalAccount) {
        final Function function =
                new Function(
                        FUNC_GETVOTEWEIGHT,
                        Arrays.<Type>asList(
                                new org.fisco.bcos.web3j.abi.datatypes.Address(externalAccount)),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Uint16>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Tuple4<BigInteger, String, String, BigInteger>> getVoteParas(BigInteger id) {
        final Function function =
                new Function(
                        FUNC_GETVOTEPARAS,
                        Arrays.<Type>asList(
                                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(id)),
                        Arrays.<TypeReference<?>>asList(
                                new TypeReference<Uint8>() {},
                                new TypeReference<Address>() {},
                                new TypeReference<Address>() {},
                                new TypeReference<Uint16>() {}));
        return new RemoteCall<Tuple4<BigInteger, String, String, BigInteger>>(
                new Callable<Tuple4<BigInteger, String, String, BigInteger>>() {
                    @Override
                    public Tuple4<BigInteger, String, String, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple4<BigInteger, String, String, BigInteger>(
                                (BigInteger) results.get(0).getValue(),
                                (String) results.get(1).getValue(),
                                (String) results.get(2).getValue(),
                                (BigInteger) results.get(3).getValue());
                    }
                });
    }

    public RemoteCall<String> _owner() {
        final Function function =
                new Function(
                        FUNC__OWNER,
                        Arrays.<Type>asList(),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<Tuple3<List<String>, List<BigInteger>, BigInteger>> getWeightInfo() {
        final Function function =
                new Function(
                        FUNC_GETWEIGHTINFO,
                        Arrays.<Type>asList(),
                        Arrays.<TypeReference<?>>asList(
                                new TypeReference<DynamicArray<Address>>() {},
                                new TypeReference<DynamicArray<Uint16>>() {},
                                new TypeReference<Uint16>() {}));
        return new RemoteCall<Tuple3<List<String>, List<BigInteger>, BigInteger>>(
                new Callable<Tuple3<List<String>, List<BigInteger>, BigInteger>>() {
                    @Override
                    public Tuple3<List<String>, List<BigInteger>, BigInteger> call()
                            throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<List<String>, List<BigInteger>, BigInteger>(
                                convertToNative((List<Address>) results.get(0).getValue()),
                                convertToNative((List<Uint16>) results.get(1).getValue()),
                                (BigInteger) results.get(2).getValue());
                    }
                });
    }

    public List<LogRegisterEventResponse> getLogRegisterEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList =
                extractEventParametersWithLog(LOGREGISTER_EVENT, transactionReceipt);
        ArrayList<LogRegisterEventResponse> responses =
                new ArrayList<LogRegisterEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
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

    public void registerLogRegisterEventLogFilter(
            String fromBlock,
            String toBlock,
            List<String> otherTopics,
            EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(LOGREGISTER_EVENT);
        registerEventLogPushFilter(ABI, BINARY, topic0, fromBlock, toBlock, otherTopics, callback);
    }

    public void registerLogRegisterEventLogFilter(EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(LOGREGISTER_EVENT);
        registerEventLogPushFilter(ABI, BINARY, topic0, callback);
    }

    public List<LogUnregisterEventResponse> getLogUnregisterEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList =
                extractEventParametersWithLog(LOGUNREGISTER_EVENT, transactionReceipt);
        ArrayList<LogUnregisterEventResponse> responses =
                new ArrayList<LogUnregisterEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            LogUnregisterEventResponse typedResponse = new LogUnregisterEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.id = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.contractAddress =
                    (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void registerLogUnregisterEventLogFilter(
            String fromBlock,
            String toBlock,
            List<String> otherTopics,
            EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(LOGUNREGISTER_EVENT);
        registerEventLogPushFilter(ABI, BINARY, topic0, fromBlock, toBlock, otherTopics, callback);
    }

    public void registerLogUnregisterEventLogFilter(EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(LOGUNREGISTER_EVENT);
        registerEventLogPushFilter(ABI, BINARY, topic0, callback);
    }

    public List<LogVoteEventResponse> getLogVoteEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList =
                extractEventParametersWithLog(LOGVOTE_EVENT, transactionReceipt);
        ArrayList<LogVoteEventResponse> responses =
                new ArrayList<LogVoteEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
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

    public void registerLogVoteEventLogFilter(
            String fromBlock,
            String toBlock,
            List<String> otherTopics,
            EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(LOGVOTE_EVENT);
        registerEventLogPushFilter(ABI, BINARY, topic0, fromBlock, toBlock, otherTopics, callback);
    }

    public void registerLogVoteEventLogFilter(EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(LOGVOTE_EVENT);
        registerEventLogPushFilter(ABI, BINARY, topic0, callback);
    }

    public List<LogSetOwnerEventResponse> getLogSetOwnerEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList =
                extractEventParametersWithLog(LOGSETOWNER_EVENT, transactionReceipt);
        ArrayList<LogSetOwnerEventResponse> responses =
                new ArrayList<LogSetOwnerEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            LogSetOwnerEventResponse typedResponse = new LogSetOwnerEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.contractAddress =
                    (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void registerLogSetOwnerEventLogFilter(
            String fromBlock,
            String toBlock,
            List<String> otherTopics,
            EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(LOGSETOWNER_EVENT);
        registerEventLogPushFilter(ABI, BINARY, topic0, fromBlock, toBlock, otherTopics, callback);
    }

    public void registerLogSetOwnerEventLogFilter(EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(LOGSETOWNER_EVENT);
        registerEventLogPushFilter(ABI, BINARY, topic0, callback);
    }

    @Deprecated
    public static WEBoardVoteGuard load(
            String contractAddress,
            Web3j web3j,
            Credentials credentials,
            BigInteger gasPrice,
            BigInteger gasLimit) {
        return new WEBoardVoteGuard(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static WEBoardVoteGuard load(
            String contractAddress,
            Web3j web3j,
            TransactionManager transactionManager,
            BigInteger gasPrice,
            BigInteger gasLimit) {
        return new WEBoardVoteGuard(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static WEBoardVoteGuard load(
            String contractAddress,
            Web3j web3j,
            Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return new WEBoardVoteGuard(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static WEBoardVoteGuard load(
            String contractAddress,
            Web3j web3j,
            TransactionManager transactionManager,
            ContractGasProvider contractGasProvider) {
        return new WEBoardVoteGuard(
                contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<WEBoardVoteGuard> deploy(
            Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(
                WEBoardVoteGuard.class, web3j, credentials, contractGasProvider, getBinary(), "");
    }

    public static RemoteCall<WEBoardVoteGuard> deploy(
            Web3j web3j,
            TransactionManager transactionManager,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(
                WEBoardVoteGuard.class,
                web3j,
                transactionManager,
                contractGasProvider,
                getBinary(),
                "");
    }

    @Deprecated
    public static RemoteCall<WEBoardVoteGuard> deploy(
            Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(
                WEBoardVoteGuard.class, web3j, credentials, gasPrice, gasLimit, getBinary(), "");
    }

    @Deprecated
    public static RemoteCall<WEBoardVoteGuard> deploy(
            Web3j web3j,
            TransactionManager transactionManager,
            BigInteger gasPrice,
            BigInteger gasLimit) {
        return deployRemoteCall(
                WEBoardVoteGuard.class,
                web3j,
                transactionManager,
                gasPrice,
                gasLimit,
                getBinary(),
                "");
    }

    public static class LogRegisterEventResponse {
        public Log log;

        public BigInteger id;

        public BigInteger txType;

        public String requestAddress;

        public String newAddress;

        public BigInteger newValue;

        public String contractAddress;
    }

    public static class LogUnregisterEventResponse {
        public Log log;

        public BigInteger id;

        public String contractAddress;
    }

    public static class LogVoteEventResponse {
        public Log log;

        public BigInteger id;

        public String externalAccount;

        public Boolean flag;

        public String contractAddress;
    }

    public static class LogSetOwnerEventResponse {
        public Log log;

        public String owner;

        public String contractAddress;
    }
}
