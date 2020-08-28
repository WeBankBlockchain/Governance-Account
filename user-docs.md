

# 1. 功能概述

账户治理分为治理账户-普通账户两层结构的账户管理体系，提供了包括账户创建、冻结、解冻、更换私钥、销户等账户生命周期的各类账户管理功能。账户治理系统提供了灵活强大的治理合约和配套的Java SDK，方便FISCO-BCOS用户更有效地规范和便利联盟链账户体系的治理，提供可复用、可插拔、高效灵活和开箱即用的客户体验。

账户治理系统可以补齐现有联盟链治理过程中针对账户管理的一些短板。比如，对于区块链业务场景中常见的私钥强绑定的痛点，账户治理系统提供了使得区块链账户不依赖私钥，或者说不强绑定私钥，私钥只作为验证当前账户操作权限的工具，而非交易的权限，交易权限和账户强绑定，当私钥丢失，账户不变，资产还可以通过原来的账户操作。另一方面，当私钥丢失后，需要能够替换原有私钥地址。

更多的功能及概要设计可以参考： [《账户治理概要设计文档》](brief_design.md)


# 2. 快速开始

## 2.1 环境要求

 - Java版本
     JDK1.8 或者以上版本，推荐使用OracleJDK。  
     
     **注意**：CentOS的yum仓库的OpenJDK缺少JCE(Java Cryptography Extension)，会导致JavaSDK无法正常连接区块链节点。
    - Java安装
     参考 `Java环境配置 <../manual/console.html#java>`_
    - FISCO BCOS区块链环境搭建
     参考 `FISCO BCOS安装教程 <../installation.html>`_
    - 网络连通性
     检查Web3SDK连接的FISCO BCOS节点`channel_listen_port`是否能telnet通，若telnet不通，需要检查网络连通性和安全策略。



## 2.2 下载及部署
### 2.2.1 下载账户治理组件
由于暂未开源，暂时通过压缩包方式传递代码。
解压完成后，所有智能合约文件位于src/main/contracts路径下。

### 2.2.2 编译账户治理组件

获得本工程的代码，然后使用gradle来进行编译。

在工程目录执行编译命令：

```javascript
./gradlew clean jar
```

生成的Jar包位于： dist/acct-gov.jar


### 2.2.3 部署账户治理合约
首先，在链上需要生成一个治理账户，这个动作一般由区块链的平台方（即所谓链的治理者或管理者）发起。

账户治理支持通过手动的控制台部署，也支持通过SDK通过程序来进行部署。部署合约只需要一次。注意，以下三种模式是任选其一的，一旦选择了一种治理方式，后续无法再修改，请慎重选择。我们以手动控制台的部署为例。（SDK的部署方式可以参考《详细功能API说明说明》章节）

在部署前，首先将src/main/contracts 路径下所有的合约代码复制到

#### 部署管理员模式的治理合约

```javascript
deploy AdminGovernFacade
```

在控制台中，执行上述的命令，然后可以调用_governance函数来获得治理账户合约的地址。

```javascript
[group:1]> deploy AdminGovernFacade
contract address: 0x52ae0e6446ed179998b2821d47ca5fd0556ffa72

[group:1]> call AdminGovernFacade 0x52ae0e6446ed179998b2821d47ca5fd0556ffa72 _governance
0x6ca986047b4134b9997f873e09b3a10624ae159f

[group:1]>
```

#### 部署多签制模式的治理合约
在控制台部署的时候，需要输入部署的治理账户的外部账户地址列表和阈值。

```javascript
deploy VoteGovernFacade(externalAccounts, threshold)
```

例如，我们部署三个地址，分别为"0x1","0x2","0x3"，阈值为2：

```javascript
[group:1]> deploy VoteGovernFacade ["0x1","0x2","0x3"] 2
contract address: 0x51c52a8a86ea0c87904b63b588f13fbacbf0c282

[group:1]> call VoteGovernFacade 0x51c52a8a86ea0c87904b63b588f13fbacbf0c282 _governance
0xc4014561759324e42692f871ffa37377da197a69
```

#### 部署权重制模式的治理合约
在控制台部署的时候，需要输入部署的治理账户的外部账户地址列表，对应的每个账户的权重和阈值。

```javascript
deploy WeightVoteGovernFacade(externalAccounts, weights, threshold)
```

例如，我们部署三个地址，分别为"0x1","0x2","0x3"，对应的权重为1，2，3， 阈值为2：

```javascript
[group:1]> deploy WeightVoteGovernFacade ["0x1","0x2","0x3"] [1,2,3] 2
contract address: 0x85e1715a3718eeb0d52918f149f1b5451c766ea1

[group:1]> call WeightVoteGovernFacade 0x85e1715a3718eeb0d52918f149f1b5451c766ea1 _governance
0x20a77a65a91fcffaabbbe8cb43b577ae57e8ed27
```

以上，我们已经成功部署了治理账户。注意，以上三种模式是任选其一的，一旦选择了其中的一种治理方式，后续无法再修改，请慎重选择。

## 2.3 业务系统配置及开发

### 2.3.1 组件合约引入
用户的具体业务合约开发中，也可以在智能合约中，直接采用引入的方式来使用智能合约。

```javascript
import "./AccountManager.sol"

XXContract {
  AccountManager _accountManager;
  
  construct(address accountManager) {
        _accountManager = AccountManager(accountManager);
  } 
  
    // ……
  function doSomeBiz() public {
        // 获得账户管理的地址
        address userAccountAddress = _accountManager.getAccount(msg.sender);
        doBiz(userAccountAddress);
  }

}
```

### 2.3.2 编译业务合约

接下来，用户可到FISCO BCOS控制台中，将XXContract的业务合约编译为具体的Java代码。控制台提供一个专门的编译合约工具，方便开发者将Solidity合约文件编译为Java合约文件，具体使用方式参考[这里](https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html#id10)

### 2.3.3 部署业务合约
用户可通过控制台来部署对应的业务合约，例如：
```
deploy XXContract
```

### 2.3.4 业务系统配置

引入刚才编译生成的账户治理的jar包到自己的Java项目中来使用。

将该文件复制到自己项目libs的路径下，在自己项目的gradle中引入该jar包。

```javascript
dependencies {
    compile fileTree(dir: 'libs', includes: ['*.jar'])
}
```


代码使用示例：

```javascript
		AccountGovernManagerFactory factory = new AccountGovernManagerFactory(fl.getWeb3j(), fl.getCredentials(),
                new StaticGasProvider(FBLedger.gasPrice, FBLedger.gasLimit));   
    GovernAccountInitializer initializer = factory.newGovernAccountInitializer();
    WEGovernance govAddress = initializer.createGovernAccount(fl.getCredentials());
    System.out.println("Gov address is " + govAddress.getContractAddress());
```
# 
恭喜，到了这一步，你已经成功导入了Jar包，并初始化了账户治理合约的生成器，并用生成器生成了一个管理员模式的账户治理合约。

为了使得所有参与方获得上述已经创建的治理账户的地址，我们需要将该地址配置到SDK中。

根据上一步，我们已经获得了治理账户地址，需要将这个地址配置到所有用户SDK的配置中。配置的文件路径在src/main/resources/application.properties中。同时，还需要配置私钥的地址。

例如：

```
## 治理账户地址
system.governanceAddress=0x65e0ffc9015b0c424d367184bb5862a1b659b349
## 用户私钥地址
system.privateKey=0xd6c0ea1fd988cbe26073141cef899f79d8283c966ef12d6366f6fd93aca1f041
```

### 2.3.5 业务系统开发
#### 治理账户相关操作

治理账户支持以下操作：

- 重置用户私钥
- 冻结账户
- 解冻账户
- 注销账户
- 移交管理员权限（管理员模式下）
- 添加或修改一个治理委员会的投票账户（多签投票或权重投票模式下）
- 删除一个治理委员会的投票账户（多签投票或不同权重投票模式下）
- 设置投票阈值 （多签投票或权重投票模式下）

其中，当处于多签投票或权重投票模式下时，所有操作需要经过发起投票、所有成员表决投票，当超过投票阈值时，才能发起对应的操作。

详细的操作API详见详细功能说明。

#### 普通账户相关操作

普通账户支持以下操作：

- 创建一个新账户
- 重置用户私钥
- 注销账户
- 添加社交好友来支持重置账户私钥
- 添加或删除关联的社交好友

详细的操作API详见详细功能说明。




# 3. 详细功能API使用说明

## 治理账户功能使用说明
### 使用SDK部署合约
账户治理支持通过控制台或SDK来发布和部署合约，这里介绍如何通过SDK来部署。

#### 管理员模式

假如平台方采用管理员的治理模式，那么需要首先生成一个管理员的治理账户。

**具体调用示例：**

```javascript
    // 自动注入AdminModeGovernAccountInitializer对象
    @Autowired
    private GovernAccountInitializer governAccountInitializer;
    // 调用 createGovernAccount 方法生成管理员账户
    WEGovernance govern = adminModeManager.createGovernAccount(u);
```

**函数签名：**

```
WEGovernance createGovernAccount(Credentials credential)
```

**输入参数：**

- credential 管理员的私钥

**返回参数：**

- WEGovernance 返回的治理账户对象

调用成功后，函数会返回对应的WEGovernance治理账户对象，通过getContractAddress()方法可以获得对应的治理合约的地址。

#### 多签制治理模式

例如，以下平台方选择了治理委员会的治理模式，一共有三个参与者参与治理，治理的规则为任意的交易请求获得其中两方的同意，即可获得通过。那么我们接下来将创建一个治理账户。

**具体调用示例：**

```javascript
    // 自动注入GovernAccountInitializer对象
    @Autowired
    private GovernAccountInitializer governAccountInitializer;
    // 准备3个治理账户管理成员的公钥地址，生成治理账户
    List<String> list = new ArrayList<>();
    list.add(address1);
    list.add(address2);
    list.add(address3);
    // 创建账户
    WEGovernance govern = adminModeManager.createGovernAccount(list, 2);
```

**函数签名：**

```
WEGovernance createGovernAccount(List<Credentials> credentials, int threshold)
```

**输入参数：**

- externalAccountList  治理委员会成员的外部账户信息。
- threshold 投票阈值，如超过该投票阈值，表示投票通过。

**返回参数：**

- WEGovernance 返回的治理账户对象

调用成功后，函数会返回对应的WEGovernance治理账户对象，通过getContractAddress()方法可以获得对应的治理合约的地址。

#### 权重投票治理模式

本模式类似于上一种多签制，区别在于每个投票者的投票权重可以是不相同的。

例如，以下平台方选择了治理委员会的权重投票的治理模式，一共有三个参与者参与治理，投票的权重分别为1、2、3，阈值为4，也就是说任意的赞同选票权重相加超过阈值即可获得通过。那么我们接下来将创建一个治理账户。

**具体调用示例：**

```javascript
    // 自动注入GovernAccountInitializer对象
    @Autowired
    private GovernAccountInitializer governAccountInitializer;
    // 准备3个治理账户管理成员的公钥地址，生成治理账户
    List<String> list = new ArrayList<>();
    list.add(address1);
    list.add(address2);
    list.add(address3);
    // 设置3个投票账户的权重分别为1、2、3
    List<BigInteger> weights = new ArrayList<>();
    weights.add(BigInteger.valueOf(1));
    weights.add(BigInteger.valueOf(2));
    weights.add(BigInteger.valueOf(3));
    // 创建账户
    WEGovernance govern = adminModeManager.createGovernAccount(list, weights, 4);
```

**函数签名：**

```
WEGovernance createGovernAccount(List<String> credentialsList, List<BigInteger> weights, int threshold)
```

**输入参数：**

- externalAccountList  治理委员会成员的外部账户信息。
- weights  治理委员会成员对应的投票权重。
- threshold 投票阈值，如超过该投票阈值，表示投票通过。

**返回参数：**

- WEGovernance 返回的治理账户对象

调用成功后，函数会返回对应的WEGovernance治理账户对象，通过getContractAddress()方法可以获得对应的治理合约的地址。

### 管理员模式

管理员模式下的管理功能均位于GovernAccountInitializer类中。

首先，注入该类：

```javascript
@Autowired
private AdminModeGovernManager adminModeManager;
```

#### 重置用户私钥

**具体调用示例：**

```javascript
    TransactionReceipt tr = adminModeManager.resetAccount(u1Address, u2Address);
```

**函数签名：**

```
    TransactionReceipt resetAccount(String oldAccount, String newAccount)
```

**输入参数：**

- oldAccount  用户的外部账户的原私钥地址。
- newAccount  该账户被重置后的私钥地址。

**返回参数：**

- TransactionReceipt 交易回执

#### 冻结普通账户

**具体调用示例：**

```javascript
    TransactionReceipt tr = adminModeManager.freezeAccount(u1Address);
```

**函数签名：**

```
    TransactionReceipt freezeAccount(String account)
```

**输入参数：**

- account  用户的外部账户的私钥地址。

**返回参数：**

- TransactionReceipt 交易回执

#### 解冻普通账户

**具体调用示例：**

```javascript
    TransactionReceipt tr = adminModeManager.unfreezeAccount(u1Address);
```

**函数签名：**

```
    TransactionReceipt unfreezeAccount(String account)
```

**输入参数：**

- account  用户的外部账户的私钥地址。

**返回参数：**

- TransactionReceipt 交易回执

#### 账户强制注销

**具体调用示例：**

```javascript
    TransactionReceipt tr = governAccountInitializer.cancelAccount(u1Address);
```

**函数签名：**

```
    TransactionReceipt cancelAccount(String account)
```

**输入参数：**

- account  用户的外部账户的私钥地址。

**返回参数：**

- TransactionReceipt 交易回执

#### 移交管理员的权限

**具体调用示例：**

```javascript
    TransactionReceipt tr = adminModeManager.transferAdminAuth(u1Address);
```

**函数签名：**

```
    TransactionReceipt transferAdminAuth(String account)
```

**输入参数：**

- account  用户的外部账户的私钥地址。

**返回参数：**

- TransactionReceipt 交易回执

### 相同权重的投票制治理模式(多签制)

治理委员会模式下的管理功能均位于 VoteModeGovernManager 类中。

首先，注入该类：

```
@Autowired
private VoteModeGovernManager voteModeGovernManager;
```

在本模式下，执行任何账户相关的业务操作需要遵循以下步骤：

1. 发起一个投票请求；
1. 治理账户成员赞同该投票；
1. 投票发起者确认投票已经通过后，发起操作。

我们首先来介绍下通用的投票接口：

#### 治理委员会成员投票

**具体调用示例：**

```
    TransactionReceipt tr = voteModeGovernManager.vote(requestId, true);
```

**函数签名：**

```
    TransactionReceipt vote(BigInteger requestId, boolean agreed)
```

**输入参数：**

- requestId  发起投票的requestId。
- agreed 是否同意，true/false

**返回参数：**

- TransactionReceipt 交易回执

#### 重置用户私钥

参考上文提及的三个步骤：发起投票请求、投票、执行操作。此处，使用了单SDK来处理多用户的操作，使用了changeCredentials函数来切换不同的用户。

**具体调用示例：**

```javascript
    // 发起投票请求
    BigInteger requestId = voteModeGovernManager.requestResetAccount(p2.getAddress(), p1.getAddress());
    // 执行投票
    voteModeGovernManager.vote(requestId, true);
    // 切换投票者
    voteModeGovernManager.changeCredentials(u1);
    voteModeGovernManager.vote(requestId, true);
    // 切换投票者
    voteModeGovernManager.changeCredentials(u);
    // 发起重置私钥操作
    TransactionReceipt tr = voteModeGovernManager.resetAccount(requestId, p2.getAddress(), p1.getAddress());
```

##### 发起重置用户私钥投票申请

**函数签名：**

```
    BigInteger requestResetAccount(String newCredential, String oldCredential)
```

**输入参数：**

- oldCredential  用户的外部账户的原私钥地址。
- newCredential  该账户被重置后的私钥地址

**返回参数：**

- BigInteger 投票ID，用户需保存该ID便于后续的交互和其他操作。

##### 重置用户私钥

**函数签名：**

```
    TransactionReceipt resetAccount(BigInteger requestId, String newCredential, String oldCredential)
```

**输入参数：**

- requestId  之前的投票请求返回的ID
- newCredential  该账户被重置后的私钥地址
- oldCredential  用户的外部账户的原私钥地址。

**返回参数：**

- TransactionReceipt 交易回执。

#### 冻结普通账户

参考上文提及的三个步骤：发起投票请求、投票、执行操作。此处，使用了单SDK来处理多用户的操作，使用了changeCredentials函数来切换不同的用户。

**具体调用示例：**

```javascript
    // 发起投票请求
    BigInteger requestId = voteModeGovernManager.requestFreezeAccount(p2.getAddress());
    // 执行投票
    voteModeGovernManager.vote(requestId, true);
    // 切换投票者
    voteModeGovernManager.changeCredentials(u1);
    voteModeGovernManager.vote(requestId, true);
    // 切换投票者
    voteModeGovernManager.changeCredentials(u);
    // 发起重置私钥操作
    TransactionReceipt tr = voteModeGovernManager.freezeAccount(requestId, p2.getAddress());
```

##### 发起冻结用户账户投票申请

**函数签名：**

```
   BigInteger requestFreezeAccount(String credential)
```

**输入参数：**

- externalAccount  用户的外部账户地址。

**返回参数：**

- BigInteger 投票ID，用户需保存该ID便于后续的交互和其他操作。

##### 冻结用户账户

**函数签名：**

```
   TransactionReceipt freezeAccount(BigInteger requestId, String credential)
```

**输入参数：**

- requestId  之前的投票请求返回的ID
- externalAccount  用户的外部账户地址。

**返回参数：**

- TransactionReceipt 交易回执。

#### 解冻普通账户

参考上文提及的三个步骤：发起投票请求、投票、执行操作。此处，使用了单SDK来处理多用户的操作，使用了changeCredentials函数来切换不同的用户。

**具体调用示例：**

```javascript
    // 发起投票请求
    BigInteger requestId = voteModeGovernManager.requestUnreezeAccount(p2.getAddress());
    // 执行投票
    voteModeGovernManager.vote(requestId, true);
    // 切换投票者
    voteModeGovernManager.changeCredentials(u1);
    voteModeGovernManager.vote(requestId, true);
    // 切换投票者
    voteModeGovernManager.changeCredentials(u);
    // 发起重置私钥操作
    TransactionReceipt tr = voteModeGovernManager.unfreezeAccount(requestId, p2.getAddress());
```

##### 发起解冻用户账户投票申请

**函数签名：**

```
   BigInteger requestunfreezeAccount(String credential)
```

**输入参数：**

- externalAccount  用户的外部账户地址。

**返回参数：**

- BigInteger 投票ID，用户需保存该ID便于后续的交互和其他操作。

##### 解冻用户账户

**函数签名：**

```
   TransactionReceipt unfreezeAccount(BigInteger requestId, String credential)
```

**输入参数：**

- requestId  之前的投票请求返回的ID
- externalAccount  用户的外部账户地址。

**返回参数：**

- TransactionReceipt 交易回执。

#### 账户强制注销

参考上文提及的三个步骤：发起投票请求、投票、执行操作。此处，使用了单SDK来处理多用户的操作，使用了changeCredentials函数来切换不同的用户。

**具体调用示例：**

```javascript
    // 发起投票请求
    BigInteger requestId = voteModeGovernManager.requestCancelAccount(p2.getAddress());
    // 执行投票
    voteModeGovernManager.vote(requestId, true);
    // 切换投票者
    voteModeGovernManager.changeCredentials(u1);
    voteModeGovernManager.vote(requestId, true);
    // 切换投票者
    voteModeGovernManager.changeCredentials(u);
    // 发起重置私钥操作
    TransactionReceipt tr = voteModeGovernManager.cancelAccount(requestId, p2.getAddress());
```

##### 发起注销用户账户投票申请

**函数签名：**

```
   BigInteger requestCancelAccount(String credential)
```

**输入参数：**

- externalAccount  用户的外部账户地址。

**返回参数：**

- BigInteger 投票ID，用户需保存该ID便于后续的交互和其他操作。

##### 注销用户账户

**函数签名：**

```
   TransactionReceipt cancelAccount(BigInteger requestId, String credential)
```

**输入参数：**

- requestId  之前的投票请求返回的ID
- externalAccount  用户的外部账户地址。

**返回参数：**

- TransactionReceipt 交易回执。

#### 设置治理账户投票的阈值

参考上文提及的三个步骤：发起投票请求、投票、执行操作。此处，使用了单SDK来处理多用户的操作，使用了changeCredentials函数来切换不同的用户。

**具体调用示例：**

```javascript
    // 发起投票请求
    BigInteger requestId = voteModeGovernManager.requestResetThreshold(newThreshold);
    // 执行投票
    voteModeGovernManager.vote(requestId, true);
    // 切换投票者
    voteModeGovernManager.changeCredentials(u1);
    voteModeGovernManager.vote(requestId, true);
    // 切换投票者
    voteModeGovernManager.changeCredentials(u);
    // 发起重置私钥操作
    TransactionReceipt tr = voteModeGovernManager.resetThreshold(requestId, newThreshold);
```

##### 发起设置治理账户投票申请

**函数签名：**

```
    BigInteger requestRemoveGovernAccount(int newThreshold)
```

**输入参数：**

- newThreshold  新阈值。

**返回参数：**

- BigInteger 投票ID，用户需保存该ID便于后续的交互和其他操作。

##### 设置新阈值

**函数签名：**

```
   TransactionReceipt resetThreshold(BigInteger requestId, int threshold)
```

**输入参数：**

- requestId  之前的投票请求返回的ID
- newThreshold  新阈值。

**返回参数：**

- TransactionReceipt 交易回执。

#### 治理账户删除一个投票账户

参考上文提及的三个步骤：发起投票请求、投票、执行操作。此处，使用了单SDK来处理多用户的操作，使用了changeCredentials函数来切换不同的用户。

**具体调用示例：**

```javascript
    // 发起投票请求
    BigInteger requestId = voteModeGovernManager.requestRemoveGovernAccount(p2.getAddress());
    // 执行投票
    voteModeGovernManager.vote(requestId, true);
    // 切换投票者
    voteModeGovernManager.changeCredentials(u1);
    voteModeGovernManager.vote(requestId, true);
    // 切换投票者
    voteModeGovernManager.changeCredentials(u);
    // 发起重置私钥操作
    TransactionReceipt tr = voteModeGovernManager.removeGovernAccount(requestId, p2.getAddress());
```

##### 发起删除一个治理账户投票申请

**函数签名：**

```
   BigInteger requestRemoveGovernAccount(String credential)
```

**输入参数：**

- externalAccount  用户的外部账户地址。

**返回参数：**

- BigInteger 投票ID，用户需保存该ID便于后续的交互和其他操作。

##### 删除一个投票账户

**函数签名：**

```
   TransactionReceipt removeGovernAccount(BigInteger requestId, String credential)
```

**输入参数：**

- requestId  之前的投票请求返回的ID
- externalAccount  用户的外部账户地址。

**返回参数：**

- TransactionReceipt 交易回执。

#### 治理账户添加一个投票新账户

参考上文提及的三个步骤：发起投票请求、投票、执行操作。此处，使用了单SDK来处理多用户的操作，使用了changeCredentials函数来切换不同的用户。

**具体调用示例：**

```javascript
    // 发起投票请求
    BigInteger requestId = voteModeGovernManager.requestAddGovernAccount(p2.getAddress());
    // 执行投票
    voteModeGovernManager.vote(requestId, true);
    // 切换投票者
    voteModeGovernManager.changeCredentials(u1);
    voteModeGovernManager.vote(requestId, true);
    // 切换投票者
    voteModeGovernManager.changeCredentials(u);
    // 发起重置私钥操作
    TransactionReceipt tr = voteModeGovernManager.addGovernAccount(requestId, p2.getAddress());
```

##### 发起添加一个治理账户投票申请

**函数签名：**

```
   BigInteger requestAddGovernAccount(String credential)
```

**输入参数：**

- externalAccount  用户的外部账户地址。

**返回参数：**

- BigInteger 投票ID，用户需保存该ID便于后续的交互和其他操作。

##### 添加一个投票账户

**函数签名：**

```
   TransactionReceipt addGovernAccount(BigInteger requestId, String credential)
```

**输入参数：**

- requestId  之前的投票请求返回的ID
- externalAccount  用户的外部账户地址。

**返回参数：**

- TransactionReceipt 交易回执。

### 不同权重的投票制治理模式

不同权重的投票制模式总体和多签制非常类似，此处不再做过多的赘述，请参考上节。

#### 治理账户添加一个投票新账户

参考上文提及的三个步骤：发起投票请求、投票、执行操作。此处，使用了单SDK来处理多用户的操作，使用了changeCredentials函数来切换不同的用户。

**具体调用示例：**

```javascript
    // 发起投票请求
    BigInteger requestId = voteModeGovernManager.requestAddGovernAccount(p2.getAddress() , weight);
    // 执行投票
    voteModeGovernManager.vote(requestId, true);
    // 切换投票者
    voteModeGovernManager.changeCredentials(u1);
    voteModeGovernManager.vote(requestId, true);
    // 切换投票者
    voteModeGovernManager.changeCredentials(u);
    // 发起重置私钥操作
    TransactionReceipt tr = voteModeGovernManager.addGovernAccount(requestId, p2.getAddress(), weight);
```

##### 发起添加一个治理账户投票申请

**函数签名：**

```
   BigInteger requestAddGovernAccount(String credential, int weight)
```

**输入参数：**

- externalAccount  用户的外部账户地址。
- weight 新加入账户的权重

**返回参数：**

- BigInteger 投票ID，用户需保存该ID便于后续的交互和其他操作。

##### 添加一个投票账户

**函数签名：**

```
   TransactionReceipt addGovernAccount(BigInteger requestId, String credential, int weight)
```

**输入参数：**

- requestId  之前的投票请求返回的ID
- externalAccount  用户的外部账户地址。
- weight 新加入账户的权重

**返回参数：**

- TransactionReceipt 交易回执。

#### 其他交易

其余部分颇为相似，可参考基于相同权重的投票模式

## 普通账户管理模式功能说明

### 普通账户主要功能

普通账户的功能均位于EndUserOperManager类中。

首先，注入该类：

```
@Autowired
private EndUserOperManager endUserOperManager;
```

#### 创建新账户

**具体调用示例：**

```
    String account = endUserAdminManager.createAccount(p1Address);
```

**函数签名：**

```
    String createAccount(String who)
```

**输入参数：**

- externalAccount  待创建的账户的外部账户的私钥地址。

**返回参数：**

- String 新建的账户地址

#### 重置用户私钥

**具体调用示例：**

```
    TransactionReceipt tr = endUserAdminManager.resetAccount(p2Address);
```

**函数签名：**

```
    TransactionReceipt resetAccount(String newCredential)
```

**输入参数：**

- newCredential  待更换的私钥地址。

**返回参数：**

- TransactionReceipt 交易回执

#### 账户强制注销

**具体调用示例：**

```
    TransactionReceipt tr = endUserAdminManager.cancelAccount();
```

**函数签名：**

```
    TransactionReceipt cancelAccount()
```

**返回参数：**

- TransactionReceipt 交易回执


#### 修改普通账户的管理类型

**具体调用示例：**

```
    List<String> voters = Lists.newArrayList();
    voters.add(u.getAddress());
    voters.add(u1.getAddress());
    voters.add(u2.getAddress());
    TransactionReceipt tr = endUserAdminManager.modifyManagerType(voters);
```

**函数签名：**

```
    TransactionReceipt modifyManagerType(List<String> voters)
    //重载函数，设置为仅自己管理
    TransactionReceipt modifyManagerType()
```

**输入参数：**

- voters （可选） 当变更为支持社交好友投票时需要传入，且voters的大小必须为3。投票本身为2-3的规则。如果voters不传入，则默认不开启投票模式。

**返回参数：**

- TransactionReceipt 交易回执

#### 添加一个社交好友

**具体调用示例：**

```
    TransactionReceipt tr = endUserAdminManager.addRelatedAccount(userAddress);
```

**函数签名：**

```
    TransactionReceipt addRelatedAccount(String account)
```

**输入参数：**

- account  被添加好友的外部账户地址。

**返回参数：**

- TransactionReceipt 交易回执

#### 删除一个社交好友

**具体调用示例：**

```
    TransactionReceipt tr = endUserAdminManager.removeRelatedAccount(userAddress);
```

**函数签名：**

```
    TransactionReceipt removeRelatedAccount(String account)
```

**输入参数：**

- account  被删除好友的外部账户地址。

**返回参数：**

- TransactionReceipt 交易回执

### 使用社交好友投票重置私钥

#### 重置用户私钥

参考上文提及的三个步骤：发起投票请求、投票、执行操作。此处，使用了单SDK来处理多用户的操作，使用了changeCredentials函数来切换不同的用户。

社交好友投票相关的操作在 SocialVoteManager 类中。

**具体调用示例：**

```javascript
    // 发起投票请求
    TransactionReceipt t = socialVoteManager.requestResetAccount(u1.getAddress(), p1.getAddress());
    // 执行投票
    socialVoteManager.vote(requestId, true);
    // 切换投票者
    socialVoteManager.changeCredentials(u1);
    socialVoteManager.vote(requestId, true);
    // 切换投票者
    socialVoteManager.changeCredentials(u);
    // 发起重置私钥操作
    TransactionReceipt tr = socialVoteManager.resetAccount(u1.getAddress(), p1.getAddress());
```

##### 发起重置用户私钥投票申请

**函数签名：**

```
    TransactionReceipt requestResetAccount(String newCredential, String oldCredential)
```

**输入参数：**

- oldCredential  用户的外部账户的原私钥地址。
- newCredential  该账户被重置后的私钥地址

**返回参数：**

- TransactionReceipt 交易回执。

##### 投票

**函数签名：**

```
    TransactionReceipt vote(String oldCredential, boolean agreed)
```

**输入参数：**

- oldCredential  申请变更账户的外部账户地址。
- agreed  是否同意

**返回参数：**

- TransactionReceipt 交易回执。

##### 重置用户私钥

**函数签名：**

```
    TransactionReceipt resetAccount(String newCredential, String oldCredential)
```

**输入参数：**

- newCredential  该账户被重置后的私钥地址
- oldCredential  用户的外部账户的原私钥地址。

**返回参数：**

- TransactionReceipt 交易回执。

# 
# 4. 测试说明

## 执行代码

本工程已经提供了丰富的各类使用场景，涵盖了所有的正常业务流程。且本工程已经为用户提供了各类的业务角色。例如共计有6个用户，分别为u, u1, u2, p1, p2, p3。对应的用户密钥证书位于src/main/resources路径下。

可以直接执行以下命令来执行所有的业务场景测试代码：

```
    ./gradlew test
```

如果想模拟真实的业务场景，可以按照以上部分的文档来进行二次开发。

## 治理账户的测试场景

### 测试场景一：治理账户管理员模式：

详见com.webank.scott.scene.GovernOfAdminModeScene

### 测试场景二：相同权重的投票制治理模式：

详见com.webank.scott.scene.GovernOfNormalVoteScene

### 测试场景三：不同权重的投票制治理模式：

详见com.webank.scott.scene.GovernOfBoardVoteScene

## 普通账户的测试场景

### 测试场景一：普通账户自主管理场景：

详见com.webank.scott.scene.UserOfSelfAdminScene

### 测试场景二：普通账户平台代管场景：

详见com.webank.scott.scene.GovernOfAdminScene

### 测试场景三：普通账户社交好友投票来重置私钥：

详见com.webank.scott.scene.UserOfSocailVoteScene

# 附录一、私钥准备

我们首先预先准备一些私钥对，来完成Demo。私钥的生成可以使用Java相关的API来生成，也可以通过一些常用的keytool来生成。详情可参考WEB3SDK等相关文档，此处不再赘述。

假设我们以JKS文件为例，我们预先准备完了6个不同的密钥对，直接转为了Credentials对象来便于操作：

```
    @PostConstruct
    public void init() throws Exception {
        u = CredentialUtils.loadKey("user.jks", "123456", "123456");
        u1 = CredentialUtils.loadKey("user1.jks", "123456", "123456");
        u2 = CredentialUtils.loadKey("user2.jks", "123456", "123456");
        p1 = CredentialUtils.loadKey("p1.jks", "123456", "123456");
        p2 = CredentialUtils.loadKey("p2.jks", "123456", "123456");
        p3 = CredentialUtils.loadKey("p3.jks", "123456", "123456");
    }
```
