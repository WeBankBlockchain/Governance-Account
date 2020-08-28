pragma solidity ^0.4.25;

import "./WEBasicAuth.sol";
import "./BaseAccount.sol";
import "./UserAccount.sol";


contract AccountManager is WEBasicAuth {
    mapping(address => address) _externalAccountMapping;
    mapping(address => address) _userAccountMapping;

    event LogManageNewAccount(
        address indexed externalAccount,
        address indexed userAccount,
        address accountManagerAddress
    );
    event LogManageExternalAccount(
        address indexed newExternalAccount,
        address indexed oldExternalAccount,
        address accountManagerAddress
    );
    event LogManageFreeze(
        address indexed externalAccount,
        address accountManagerAddress
    );
    event LogManageUnfreeze(
        address indexed externalAccount,
        address accountManagerAddress
    );
    event LogManageCancel(
        address indexed externalAccount,
        address accountManagerAddress
    );

    modifier accountNotExisted(address externalAccount) {
        require(
            _externalAccountMapping[externalAccount] == 0x0,
            "AccountManager: externalAccount already existed."
        );
        _;
    }

    modifier accountExisted(address externalAccount) {
        require(
            _externalAccountMapping[externalAccount] != 0x0,
            "AccountManager: externalAccount not existed."
        );
        _;
    }

    modifier acctPermitted(
        address externalAccount,
        uint8 oper,
        address newAddress,
        uint16 newValue
    ) {
        require(
            accountPermitFunc(externalAccount, oper, newAddress, newValue),
            "AccountManager: user not userPermitted."
        );
        _;
    }

    function hasAccount(address externalAccount) public view returns (bool) {
        return _externalAccountMapping[externalAccount] != 0x0;
    }

    function isExternalAccountNormal(address externalAccount)
        public
        view
        returns (bool)
    {
        address account = _externalAccountMapping[externalAccount];
        if(account==0x0) {
            return false;
        }
        BaseAccount a = BaseAccount(account);
        return a.isNormal();
    }

    function accountPermitFunc(
        address externalAccount,
        uint8 operType,
        address newAddress,
        uint16 newValue
    ) public view returns (bool) {
        if (_externalAccountMapping[externalAccount] == address(0)) {
            return false;
        }
        address ac = _externalAccountMapping[externalAccount];
        UserAccount userAccount = UserAccount(ac);
        return
            userAccount.passed(operType, externalAccount, newAddress, newValue);
    }

    function newAccount(address externalAccount)
        public
        accountNotExisted(externalAccount)
        returns (bool, address)
    {
        UserAccount userAccount = new UserAccount(this, externalAccount);
        address ua = address(userAccount);
        _externalAccountMapping[externalAccount] = ua;
        _userAccountMapping[ua] = externalAccount;
        emit LogManageNewAccount(externalAccount, ua, this);
        return (true, ua);
    }

    function getExternalAccount(address userAddress) public view returns (address) {
        return _userAccountMapping[userAddress];
    }

    function setExternalAccount(
        address newExternalAccount,
        address oldExternalAccount
    ) internal accountExisted(oldExternalAccount) returns (bool) {
        address userAccount = _externalAccountMapping[oldExternalAccount];
        _externalAccountMapping[newExternalAccount] = userAccount;
        _externalAccountMapping[oldExternalAccount] = 0x0;
        _userAccountMapping[userAccount] = newExternalAccount;
        emit LogManageExternalAccount(
            newExternalAccount,
            oldExternalAccount,
            this
        );
        UserAccount ac = UserAccount(userAccount);
        ac.setOwnerByManager(newExternalAccount);
        return true;
    }

    function setExternalAccountByGovernance(
        address newExternalAccount,
        address oldExternalAccount
    ) public onlyOwner returns (bool) {
        return setExternalAccount(newExternalAccount, oldExternalAccount);
    }

    function setExternalAccountByUser(address newExternalAccount)
        public
        returns (bool)
    {
        return setExternalAccount(newExternalAccount, msg.sender);
    }

    function setExternalAccountBySocial(
        address newExternalAccount,
        address oldExternalAccount
    )
        public
        acctPermitted(oldExternalAccount, 2, newExternalAccount, 0)
        returns (bool b)
    {
        b = setExternalAccount(newExternalAccount, oldExternalAccount);
        if (b) {
            address userAccount = _externalAccountMapping[newExternalAccount];
            UserAccount ac = UserAccount(userAccount);
            b = ac.unregister(2);
        }
    }

    function getUserAccount(address externalAccount)
        public
        view
        returns (address)
    {
        return _externalAccountMapping[externalAccount];
    }

    function freeze(address externalAccount) public onlyOwner returns (bool) {
        address account = _externalAccountMapping[externalAccount];
        BaseAccount a = BaseAccount(account);
        emit LogManageFreeze(externalAccount, this);
        return a.freeze();
    }

    function unfreeze(address externalAccount) public onlyOwner returns (bool) {
        address account = _externalAccountMapping[externalAccount];
        BaseAccount a = BaseAccount(account);
        emit LogManageUnfreeze(externalAccount, this);
        return a.unfreeze();
    }

    function cancel(address externalAccount)
        internal
        accountExisted(externalAccount)
        returns (bool b)
    {
        address account = _externalAccountMapping[externalAccount];
        _externalAccountMapping[externalAccount] = 0x0;
        _userAccountMapping[account] = 0x0;
        BaseAccount a = BaseAccount(account);
        emit LogManageCancel(externalAccount, this);
        b = a.cancel();
    }

    function cancelByGovernance(address externalAccount)
        onlyOwner
        returns (bool)
    {
        return cancel(externalAccount);
    }

    function cancelByUser() returns (bool) {
        return cancel(msg.sender);
    }
}
