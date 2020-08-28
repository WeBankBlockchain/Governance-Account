pragma solidity ^0.4.25;


library LibBoardVote {
    struct VoteRequestInfo {
        uint256 id;
        uint16 threshold;
        uint16 weight;
        uint8 txType;
        address requestAddress;
        // 0-init, 1-approved, 2-closed
        uint8 status;
        address newAddress;
        uint16 newValue;
        mapping(address => bool) votedMapping;
        mapping(address => bool) resultMapping;
    }

    struct VoteWeightInfo {
        mapping(address => uint256) index;
        address[] keys;
        uint16[] values;
        uint16 threshold;
    }

    struct VoteInfo {
        VoteWeightInfo voteWeight;
        uint256 currentId;
        mapping(uint256 => uint8) requestFlag;
        mapping(uint256 => VoteRequestInfo) requestMapping;
    }

    modifier voteNotExisted(VoteInfo storage voteInfo, uint256 id) {
        require(
            voteInfo.requestMapping[id].requestAddress == address(0),
            "LibBoardVote: vote id not existed."
        );
        _;
    }

    modifier voteExisted(VoteInfo storage voteInfo, uint256 id) {
        require(
            voteInfo.requestMapping[id].requestAddress != address(0),
            "LibBoardVote: vote id already existed."
        );
        _;
    }

    modifier isWeightEmpty(VoteWeightInfo storage self) {
        require(
            weightValueEmpty(self),
            "LibBoardVote: vote weight is not empty."
        );
        _;
    }

    function weightValueEmpty(VoteWeightInfo storage self) returns (bool) {
        if (self.keys.length > 0 || self.values.length > 0) {
            return false;
        } else {
            return true;
        }
    }

    function getRequestInfo(VoteInfo storage self, uint256 id)
        internal
        view
        voteExisted(self, id)
        returns (
            uint256,
            address,
            uint16,
            uint16,
            uint8,
            uint8,
            address,
            uint16
        )
    {
        VoteRequestInfo memory i = self.requestMapping[id];
        return (
            i.id,
            i.requestAddress,
            i.threshold,
            i.weight,
            i.txType,
            i.status,
            i.newAddress,
            i.newValue
        );
    }

    function getVoteParas(VoteInfo storage self, uint256 id)
        internal
        view
        voteExisted(self, id)
        returns (
            uint8,
            address,
            address,
            uint16
        )
    {
        return getRequestParas(self.requestMapping[id]);
    }

    function register(
        VoteInfo storage self,
        VoteWeightInfo voteWeight,
        uint8 txType,
        address requestAddress,
        address newAddress,
        uint16 newValue
    ) internal returns (bool, uint256) {
        self.currentId++;
        return
            register(
                self,
                voteWeight,
                self.currentId,
                txType,
                requestAddress,
                newAddress,
                newValue
            );
    }

    function register(
        VoteInfo storage self,
        VoteWeightInfo voteWeight,
        uint256 id,
        uint8 txType,
        address requestAddress,
        address newAddress,
        uint16 newValue
    ) internal voteNotExisted(self, id) returns (bool, uint256) {
        uint16 threshold = voteWeight.threshold;
        VoteRequestInfo memory info = VoteRequestInfo(
            id,
            threshold,
            0,
            txType,
            requestAddress,
            0,
            newAddress,
            newValue
        );
        self.requestMapping[id] = info;
        self.requestFlag[id] = 1;
        return (true, id);
    }

    function unregister(VoteInfo storage self, uint256 id)
        internal
        voteExisted(self, id)
        returns (bool b)
    {
        //set the request status: done.
        self.requestFlag[id] = 0;
        delete self.requestMapping[id];
    }

    function vote(
        VoteInfo storage self,
        VoteWeightInfo storage voteWeight,
        address from,
        uint256 id,
        bool flag
    ) internal voteExisted(self, id) returns (bool b, uint16 u) {
        uint16 weight = getWeight(voteWeight, from);
        require(weight > 0, "vote error, not a valid voter.");
        VoteRequestInfo storage request = self.requestMapping[id];
        (b, u) = vote(request, from, weight, flag);
    }

    function canCallById(VoteInfo storage self, uint256 id)
        internal
        view
        returns (bool)
    {
        return self.requestFlag[id] == 1;
    }

    function validVote(
        VoteInfo storage self,
        uint256 id,
        uint8 txType,
        address requestAddress,
        address newAddress,
        uint16 newValue
    ) internal view returns (bool) {
        if (canCallById(self, id)) {
            if (
                self.requestMapping[id].requestAddress == requestAddress &&
                self.requestMapping[id].txType == txType &&
                self.requestMapping[id].newAddress == newAddress &&
                self.requestMapping[id].newValue == newValue && self.requestMapping[id].status== 1
            ) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    function init(
        VoteWeightInfo storage self,
        address[] key,
        uint16[] value,
        uint16 threshold
    ) internal isWeightEmpty(self) returns (bool) {
        self.keys = key;
        self.values = value;
        self.threshold = threshold;
        for (uint256 i = 0; i < key.length; i++) {
            self.index[key[i]] = (i + 1);
        }
        return true;
    }

    function clean(VoteWeightInfo storage self) internal returns (bool) {
        for (uint256 i = 0; i < self.keys.length; i++) {
            delete self.index[self.keys[i]];
        }
        delete self.keys;
        delete self.values;
        delete self.threshold;
        return true;
    }

    function setWeight(
        VoteWeightInfo storage self,
        address key,
        uint16 value
    ) internal returns (bool) {
        uint256 idx = self.index[key];
        if (idx == 0) {
            self.keys.push(key);
            self.values.push(value);
            self.index[key] = self.keys.length;
        } else {
            self.values[idx - 1] = value;
        }
        return true;
    }

    function isVoter(VoteWeightInfo storage self, address src)
        internal
        view
        returns (bool)
    {
        uint256 idx = self.index[src];
        if (idx == 0 || self.values[idx - 1] == 0) {
            return false;
        } else {
            return true;
        }
    }

    function setThreshold(VoteWeightInfo storage self, uint16 value)
        internal
        returns (bool)
    {
        self.threshold = value;
        return true;
    }

    function getWeight(VoteWeightInfo storage self, address key)
        internal
        view
        returns (uint16)
    {
        uint256 idx = self.index[key];
        uint16 value = self.values[idx - 1];
        return value;
    }

    function getThreshold(VoteWeightInfo storage self)
        internal
        view
        returns (uint16)
    {
        return (self.threshold);
    }

    function getInfo(VoteWeightInfo storage self)
        internal
        view
        returns (
            address[],
            uint16[],
            uint16
        )
    {
        return (self.keys, self.values, self.threshold);
    }

    function isVoted(VoteRequestInfo storage self, address src)
        internal
        view
        returns (bool)
    {
        return self.votedMapping[src];
    }

    function isAgree(VoteRequestInfo storage self, address src)
        internal
        view
        returns (bool)
    {
        return self.resultMapping[src];
    }

    function getRequestParas(VoteRequestInfo storage self)
        internal
        view
        returns (
            uint8,
            address,
            address,
            uint16
        )
    {
        return (
            self.txType,
            self.requestAddress,
            self.newAddress,
            self.newValue
        );
    }

    function vote(
        VoteRequestInfo storage self,
        address src,
        uint16 weight,
        bool flag
    ) internal returns (bool, uint16) {
        if (self.votedMapping[src] && self.resultMapping[src] == flag) {
            return (false, self.weight);
        }
        if (flag) {
            self.weight += weight;
        } 
        self.votedMapping[src] = true;
        self.resultMapping[src] = flag;
        if (self.weight >= self.threshold) {
            self.status = 1;
        } else {
            self.status = 0;
        }
        return (true, self.weight);
    }
}
