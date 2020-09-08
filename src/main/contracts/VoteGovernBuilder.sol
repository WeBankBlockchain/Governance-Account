pragma solidity ^0.4.25;

import "./WEGovernance.sol";
import "./AccountManager.sol";


contract VoteGovernBuilder {
    address public _governance;

    constructor(address[] externalAccounts, uint16 threshold) public {
        uint16[] memory weights;
        weights = new uint16[](externalAccounts.length);
        for (uint8 j = 0; j < externalAccounts.length; j++) {
            weights[j] = 1;
        }
        WEGovernance governance = new WEGovernance(1);
        _governance = address(governance);
        address _accountManager = governance.getAccountManager();
        AccountManager accountManager = AccountManager(_accountManager);
        for (uint8 i = 0; i < externalAccounts.length; i++) {
            bool b;
            (b, externalAccounts[i]) = accountManager.newAccount(
                externalAccounts[i]
            );
            require(b == true, "Builder: create account fail.");
        }
        bool r = governance.init(externalAccounts, weights, threshold);
        require(r == true, "Builder: create account fail.");
    }

    function getGovernance() public view returns (address) {
        return _governance;
    }

    function getAccountManager() public view returns (address) {
        WEGovernance governance = WEGovernance(_governance);
        return governance.getAccountManager();
    }
}
