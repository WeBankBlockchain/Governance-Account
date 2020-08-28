pragma solidity ^0.4.25;

import "./WEBasicAuth.sol";
import "./BaseAccountInterface.sol";
import "./BaseAccount.sol";

contract BaseAccount is BaseAccountInterface, WEBasicAuth {
    address public _accountManager;
    // 0-normal, 1-frozen, 2-closed
    uint8 public _status = 0;

    modifier onlyAccountManager() {
        require(
            msg.sender == _accountManager,
            "BaseAccount: only account manager."
        );
        _;
    }

    modifier onlyNormal() {
        require(isNormal(), "BaseAccount: only account status is normal.");
        _;
    }

    modifier onlyFrozen() {
        require(_status == 1, "BaseAccount: only account status is abnormal.");
        _;
    }

    modifier onlyAuthorized() {
        require(
            msg.sender == _accountManager || msg.sender == _owner,
            "BaseAccount: only authorized."
        );
        _;
    }

    constructor(address accountManager) public {
        _accountManager = accountManager;
    }

    function isNormal() public returns (bool) {
        return _status == 0;
    }

    function cancel() public onlyAuthorized returns (bool) {
        _status = 2;
        emit LogBaseAccount("cancel", this);
        return true;
    }

    function freeze() public onlyAccountManager onlyNormal returns (bool) {
        _status = 1;
        emit LogBaseAccount("freeze", this);
        return true;
    }

    function unfreeze() public onlyAccountManager onlyFrozen returns (bool) {
        _status = 0;
        emit LogBaseAccount("unfreeze", this);
        return true;
    }

    /*
    function send() public onlyAuthorized(msg.sig) onlyNormal returns (bool) {
		return true;
	}
	*/
}
