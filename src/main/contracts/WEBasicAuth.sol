pragma solidity ^0.4.25;


contract WEBasicAuth {
    address public _owner;
    event LogSetOwner(address indexed owner, address indexed contractAddress);

    constructor() public {
        _owner = msg.sender;
    }

    function setOwner(address owner) public onlyOwner {
        _owner = owner;
        emit LogSetOwner(owner, this);
    }

    function getOwner() public view returns (address) {
        return _owner;
    }

    modifier onlyOwner() {
        require(msg.sender == _owner, "WEBasicAuth: only owner is authorized.");
        _;
    }
}
