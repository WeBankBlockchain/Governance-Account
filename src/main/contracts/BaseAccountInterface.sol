pragma solidity ^0.4.25;


contract BaseAccountInterface {
    event LogBaseAccount(bytes32 indexed eventType, address contractAddress);

    function isNormal() public returns (bool);

    function cancel() public returns (bool);

    function freeze() public returns (bool);

    function unfreeze() public returns (bool);
}
