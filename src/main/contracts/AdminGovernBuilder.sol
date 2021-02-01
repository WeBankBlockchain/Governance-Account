pragma solidity ^0.4.25;

import "./WEGovernance.sol";
import "./AccountManager.sol";


contract AdminGovernBuilder {
    address public _governance;
     event LogCreate(
        address governanceAddress,
        address accountManagerAddress
    );

    constructor() public {
        WEGovernance governance = new WEGovernance(0);
        governance.setOwner(msg.sender);
        _governance = address(governance);
        address _accountManager = governance.getAccountManager();
        AccountManager accountManager = AccountManager(_accountManager);
        accountManager.newAccount(msg.sender);
        emit LogCreate(_governance, _accountManager);
    }

    function getGovernance() public view returns (address) {
        return _governance;
    }

    function getAccountManager() public view returns (address) {

    	WEGovernance governance = WEGovernance(_governance);
        return governance.getAccountManager();
    }
}
