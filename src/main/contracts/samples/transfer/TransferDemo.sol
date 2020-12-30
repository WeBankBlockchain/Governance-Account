pragma solidity ^0.4.25;

import "./LibSafeMath.sol";
import "./AccountManager.sol";


contract TransferDemo {
    using LibSafeMath for uint256;
    mapping(address => uint256) private _balances;
    // import AccountManager
    AccountManager _accountManager;

    constructor(address accountManager, uint256 initBalance) public {
        // import accountManager
        _accountManager = AccountManager(accountManager);
        address owner = _accountManager.getUserAccount(msg.sender);
        _balances[owner] = initBalance;
    }

    modifier validateAccount(address addr) {
        require(
            // predicate account status
            _accountManager.isExternalAccountNormal(addr),
            "Account is abnormal!"
        );
        _;
    }

    modifier checkTargetAccount(address sender) {
        require(
            msg.sender != sender && sender != address(0),
            "Can't transfer to illegal address!"
        );
        _;
    }

    function balance(address owner) public view returns (uint256) {
        // 1.get account by external account, 2.get balace by account.
        return _balances[_accountManager.getUserAccount(owner)];
    }

    function transfer(address toAddress, uint256 value)
        public
        // validate source & target account
        validateAccount(msg.sender)
        validateAccount(toAddress)
        checkTargetAccount(toAddress)
        returns (bool)
    {
        // 1. get source account
        address fromAccount = _accountManager.getUserAccount(msg.sender);
        // 2. sub the balance of source account
        uint256 balanceOfFrom = _balances[fromAccount].sub(value);
        // 3. modify the balance of source account
        _balances[fromAccount] = balanceOfFrom;
        // 4. get target account
        address toAccount = _accountManager.getUserAccount(toAddress);
        // 5. add balance of target account
        uint256 balanceOfTo = _balances[toAccount].add(value);
        // set the new balance of target account
        _balances[toAccount] = balanceOfTo;
        return true;
    }
}
