// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.

// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.

// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.

pragma solidity ^0.4.25;

import "./WEBasicAuth.sol";
import "./LibBoardVote.sol";


contract WEBoardVoteGuard is WEBasicAuth {
    using LibBoardVote for LibBoardVote.VoteWeightInfo;
    LibBoardVote.VoteWeightInfo _voteWeight;
    using LibBoardVote for LibBoardVote.VoteInfo;
    LibBoardVote.VoteInfo _voteInfo;

    event LogRegister(
        uint256 indexed id,
        uint8 indexed txType,
        address indexed requestAddress,
        address newAddress,
        uint16 newValue,
        address contractAddress
    );
    event LogUnregister(uint256 indexed id, address contractAddress);
    event LogVote(
        uint256 indexed id,
        address indexed externalAccount,
        bool flag,
        address contractAddress
    );

    constructor() public {
        _voteInfo.currentId = 10000;
    }

    function getRequestInfo(uint256 id)
        external
        view
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
        return _voteInfo.getRequestInfo(id);
    }

    function getVoteParas(uint256 id)
        public
        view
        returns (
            uint8,
            address,
            address,
            uint16
        )
    {
        return _voteInfo.getVoteParas(id);
    }

    function getVoteWeight(address externalAccount)
        public
        view
        returns (uint16)
    {
        return _voteWeight.getWeight(externalAccount);
    }

    function getWeightInfo()
        public
        view
        returns (
            address[],
            uint16[],
            uint16
        )
    {
        return _voteWeight.getInfo();
    }

    function vote(
        address from,
        uint256 id,
        bool flag
    ) internal returns (bool b) {
        uint16 u = 0;
        (b, u) = _voteInfo.vote(_voteWeight, from, id, flag);
        emit LogVote(id, from, flag, this);
    }

    function passed(uint256 id) public view returns (bool) {
        return _voteInfo.canCallById(id);
    }
}
