pragma solidity ^0.4.25;

import "./WEBoardVoteGuard.sol";
import "./AccountManager.sol";
import "./BaseAccount.sol";


contract UserAccount is WEBoardVoteGuard, BaseAccount {
    // 0-none, 1- social vote;
    uint8 public _statics;

    modifier validVoter() {
        require(
            _voteWeight.isVoter(
                AccountManager(_accountManager).getUserAccount(msg.sender)
            ),
            "Vote should be in the list"
        );
        _;
    }

    constructor(address accountManager, address owner)
        public
        BaseAccount(accountManager)
    {
        _statics = 0;
        setOwner(owner);
    }

    function getSocialInfo()
        internal
        view
        returns (
            address[],
            uint16[],
            uint16
        )
    {
        return _voteWeight.getInfo();
    }

    function setOwnerByManager(address owner)
        public
        onlyAccountManager
        returns (bool)
    {
        _owner = owner;
        return true;
    }

    function setStatics() public onlyOwner returns (bool) {
        _statics = 0;
        return true;
    }

    function setVoteStatics(
        address[] key,
        uint16[] value,
        uint16 threshold
    ) public onlyOwner returns (bool) {
        _statics = 1;
        _voteWeight.clean();
        _voteWeight.init(key, value, threshold);
        return true;
    }

    // OPER_RESET_WEIGHT      = 11
    function setWeight(address externalAccount, uint16 weight)
        public
        onlyOwner
        returns (bool b)
    {
        address a = AccountManager(_accountManager).getUserAccount(externalAccount);
        require(
            _statics != 0 && a != address(0),
            "WEGovernance: weight address should be existed."
        );
        b = _voteWeight.setWeight(a, weight);
    }

    function register(
        uint8 txType,
        address requestAddress,
        address newAddress,
        uint16 newValue
    ) external onlyOwner returns (bool b, uint256 id) {
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
    ) external onlyOwner returns (bool b) {
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

    function unregister(uint256 id) public returns (bool b) {
        require(
            msg.sender == _owner || msg.sender == _accountManager,
            "Only account manager."
        );
        b = _voteInfo.unregister(id);
        emit LogUnregister(id, this);
    }

    function vote(uint256 id, bool flag) external validVoter returns (bool b) {
        return
            vote(
                AccountManager(_accountManager).getUserAccount(msg.sender),
                id,
                flag
            );
    }

    function passed(
        uint8 txType,
        address requestAddress,
        address newAddress,
        uint16 newValue
    ) public view returns (bool) {
        if (_statics == 0) {
            return true;
        } else {
            return
                _voteInfo.validVote(
                    txType,
                    txType,
                    requestAddress,
                    newAddress,
                    newValue
                );
        }
    }
}
