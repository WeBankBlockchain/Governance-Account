pragma solidity ^0.4.25;

import "./WEBoardVoteGuard.sol";
import "./AccountManager.sol";


contract WEGovernance is WEBoardVoteGuard {
    address _accountManager;
    // 0: admin-mode; 1: vote-mode.
    uint8 public _mode = 0;

    event LogGovernSetAccountManager (
        address indexed accountManagerAddress,
        address governanceAddress
    );
    event LogGovernExternalAccount(
        uint256 indexed id,
        address indexed newExternalAccount,
        address indexed oldExternalAccount,
        address accountManagerAddress
    );
    event LogGovernOper(
        uint256 indexed id,
        address indexed externalAccount,
        uint8 oper,
        address accountManagerAddress
    );
    event LogGovernSetThreshold(
        uint256 indexed id,
        address indexed externalAccount,
        uint16 indexed threshold
    );
    event LogGovernSetWeight(
        uint256 indexed id,
        address indexed externalAccount,
        address indexed weightAddress,
        uint16 weight
    );
    event LogGovernInitWeight(
        address externalAccount,
        address[] key,
        uint16[] value,
        uint16 threshold
    );

    modifier validRequst (
        uint256 id,
        uint8 txType,
        address requestAddress,
        address newAddress,
        uint16 newValue
    ) {
        require(
            _mode == 0 ||
                (_mode != 0 &&
                    requestReady(
                        id,
                        txType,
                        requestAddress,
                        newAddress,
                        newValue
                    )),
            "WEGovernance: valid request failed."
        );
        _;
    }

    modifier validVoter() {
        require(
            _mode != 0 &&
                _voteWeight.isVoter (
                    AccountManager(_accountManager).getUserAccount(msg.sender)
                ),
            "Vote should be in the list"
        );
        _;
    }

    modifier validExternalAccount(address externalAccount){
        require(
            AccountManager(_accountManager).isExternalAccountNormal(externalAccount),
            "External Account should normal"
        );
        _;
    }

    constructor(uint8 mode) public {
        _mode = mode;
        AccountManager accountManager = new AccountManager();
        _accountManager = address(accountManager);
    }

    function transferOwner(address owner) public validExternalAccount(owner) onlyOwner {
        _owner = owner;
        emit LogSetOwner(owner, this);
    }

    function requestReady(
        uint256 id,
        uint8 txType,
        address requestAddress,
        address newAddress,
        uint16 newValue
    ) public view returns (bool) {
        return
            _voteInfo.validVote(
                id,
                txType,
                requestAddress,
                newAddress,
                newValue
            );
    }

    function getAccountManager() public view returns (address) {
        return _accountManager;
    }

    function init    (
        address[] key,
        uint16[] value,
        uint16 threshold
    ) public onlyOwner returns (bool b) {
        require(_mode != 0, "invalid mode");
        b = _voteWeight.init(key, value, threshold);
        emit LogGovernInitWeight(this, key, value, threshold);
    }

    // OPER_RESET_THRESHOLD   = 10;
    function setThreshold(uint256 id, uint16 threshold)
        public
        validRequst(id, 10, this, address(0), threshold)
        returns (bool b)
    {
        b = _voteWeight.setThreshold(threshold);
        if (b && _mode != 0) {
            unregister(id);
            emit LogGovernSetThreshold(id, this, threshold);
        }
    }

    // OPER_RESET_WEIGHT      = 11
    function setWeight(
        uint256 id,
        address externalAccount,
        uint16 weight
    )
        public
        validRequst(id, 11, externalAccount, address(0), weight)
        returns (bool b)
    {
        address a = AccountManager(_accountManager).getUserAccount(externalAccount);
        require (
            a != address(0),
            "WEGovernance: weight address should be existed."
        );
        b = _voteWeight.setWeight(a, weight);
        if (b && _mode != 0) {
            unregister(id);
            emit LogGovernSetWeight(id, this, a, weight);
        }
    }

    // 3-freeze 4-unfreeze 5-cancel
    function doOper(
        uint256 id,
        address externalAccount,
        uint8 oper
    )
        public
        validRequst(id, oper, externalAccount, address(0), 0)
        returns (bool b)
    {
        require(oper == 3 || oper == 4 || oper == 5, "unvalid oper type.");
        if (oper == 3) {
            b = AccountManager(_accountManager).freeze(externalAccount);
        } else if (oper == 4) {
            b = AccountManager(_accountManager).unfreeze(externalAccount);
        } else {
            b = AccountManager(_accountManager).cancelByGovernance(
                externalAccount
            );
        }
        if (b && _mode != 0) {
            unregister(id);
            emit LogGovernOper(id, externalAccount, oper, this);
            return true;
        }
    }

    // 2-change externalAccount
    function setExternalAccount (
        uint256 id,
        address newExternalAccount,
        address oldExternalAccount
    )
        public
        validRequst(id, 2, oldExternalAccount, newExternalAccount, 0)
        returns (bool)
    {
        if (
            AccountManager(_accountManager).setExternalAccountByGovernance(
                newExternalAccount,
                oldExternalAccount
            )
        ) {
            if (_mode == 0) {
                return true;
            } else {
                unregister(id);
                emit LogGovernExternalAccount(
                    id,
                    newExternalAccount,
                    oldExternalAccount,
                    this
                );
                return true;
            }
        } else {
            return false;
        }
    }

    function register(
        uint8 txType,
        address requestAddress,
        address newAddress,
        uint16 newValue
    ) external validVoter returns (bool b, uint256 id) {
        (b, id) = _voteInfo.register(
            _voteWeight,
            txType,
            requestAddress,
            newAddress,
            newValue
        );
        emit LogRegister(
            id,
            txType,
            requestAddress,
            newAddress,
            newValue,
            this
        );
    }

    function register(
        uint256 id,
        uint8 txType,
        address requestAddress,
        address newAddress,
        uint16 newValue
    ) external validVoter returns (bool b) {
        (b, ) = _voteInfo.register(
            _voteWeight,
            id,
            txType,
            requestAddress,
            newAddress,
            newValue
        );
        emit LogRegister(
            id,
            txType,
            requestAddress,
            newAddress,
            newValue,
            this
        );
    }

    function unregister(uint256 id) public validVoter returns (bool b) {
        _voteInfo.unregister(id);
        emit LogUnregister(id, this);
    }

    function vote(uint256 id, bool flag) external validVoter returns (bool b) {
        address from = AccountManager(_accountManager).getUserAccount(msg.sender);
        return vote(from, id, flag);
    }
}
