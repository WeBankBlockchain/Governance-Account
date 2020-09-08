# 7、Java SDK 详细功能API使用说明


## 治理账户功能使用说明


### 使用SDK部署合约

<br />账户治理支持通过控制台或SDK来发布和部署合约，这里介绍如何通过SDK来部署。<br />

#### 管理员模式

<br />假如平台方采用管理员的治理模式，那么需要首先生成一个管理员的治理账户。<br />
<br />**具体调用示例：**<br />

```javascript
    // 自动注入AdminModeGovernAccountInitializer对象
    @Autowired
    private GovernAccountInitializer governAccountInitializer;
    // 调用 createGovernAccount 方法生成管理员账户
    WEGovernance govern = adminModeManager.createGovernAccount(u);
```

<br />**函数签名：**<br />

```
WEGovernance createGovernAccount(Credentials credential)
```

<br />**输入参数：**<br />

- credential 管理员的私钥


<br />**返回参数：**<br />

- WEGovernance 返回的治理账户对象


<br />调用成功后，函数会返回对应的WEGovernance治理账户对象，通过getContractAddress()方法可以获得对应的治理合约的地址。<br />

#### 多签制治理模式

<br />例如，以下平台方选择了治理委员会的治理模式，一共有三个参与者参与治理，治理的规则为任意的交易请求获得其中两方的同意，即可获得通过。那么我们接下来将创建一个治理账户。<br />
<br />**具体调用示例：**<br />

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

<br />**函数签名：**<br />

```
WEGovernance createGovernAccount(List<Credentials> credentials, int threshold)
```

<br />**输入参数：**<br />

- externalAccountList  治理委员会成员的外部账户信息。
- threshold 投票阈值，如超过该投票阈值，表示投票通过。


<br />**返回参数：**<br />

- WEGovernance 返回的治理账户对象


<br />调用成功后，函数会返回对应的WEGovernance治理账户对象，通过getContractAddress()方法可以获得对应的治理合约的地址。<br />

#### 权重投票治理模式

<br />本模式类似于上一种多签制，区别在于每个投票者的投票权重可以是不相同的。<br />
<br />例如，以下平台方选择了治理委员会的权重投票的治理模式，一共有三个参与者参与治理，投票的权重分别为1、2、3，阈值为4，也就是说任意的赞同选票权重相加超过阈值即可获得通过。那么我们接下来将创建一个治理账户。<br />
<br />**具体调用示例：**<br />

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

<br />**函数签名：**<br />

```
WEGovernance createGovernAccount(List<String> credentialsList, List<BigInteger> weights, int threshold)
```

<br />**输入参数：**<br />

- externalAccountList  治理委员会成员的外部账户信息。
- weights  治理委员会成员对应的投票权重。
- threshold 投票阈值，如超过该投票阈值，表示投票通过。


<br />**返回参数：**<br />

- WEGovernance 返回的治理账户对象


<br />调用成功后，函数会返回对应的WEGovernance治理账户对象，通过getContractAddress()方法可以获得对应的治理合约的地址。<br />

### 管理员模式

<br />管理员模式下的管理功能均位于GovernAccountInitializer类中。<br />
<br />首先，注入该类：<br />

```javascript
@Autowired
private AdminModeGovernManager adminModeManager;
```


#### 重置用户私钥

<br />**具体调用示例：**<br />

```javascript
    TransactionReceipt tr = adminModeManager.resetAccount(u1Address, u2Address);
```

<br />**函数签名：**<br />

```
    TransactionReceipt resetAccount(String oldAccount, String newAccount)
```

<br />**输入参数：**<br />

- oldAccount  用户的外部账户的原私钥地址。
- newAccount  该账户被重置后的私钥地址。


<br />**返回参数：**<br />

- TransactionReceipt 交易回执


#### 冻结普通账户

<br />**具体调用示例：**<br />

```javascript
    TransactionReceipt tr = adminModeManager.freezeAccount(u1Address);
```

<br />**函数签名：**<br />

```
    TransactionReceipt freezeAccount(String account)
```

<br />**输入参数：**<br />

- account  用户的外部账户的私钥地址。


<br />**返回参数：**<br />

- TransactionReceipt 交易回执


#### 解冻普通账户

<br />**具体调用示例：**<br />

```javascript
    TransactionReceipt tr = adminModeManager.unfreezeAccount(u1Address);
```

<br />**函数签名：**<br />

```
    TransactionReceipt unfreezeAccount(String account)
```

<br />**输入参数：**<br />

- account  用户的外部账户的私钥地址。


<br />**返回参数：**<br />

- TransactionReceipt 交易回执


#### 账户强制注销

<br />**具体调用示例：**<br />

```javascript
    TransactionReceipt tr = governAccountInitializer.cancelAccount(u1Address);
```

<br />**函数签名：**<br />

```
    TransactionReceipt cancelAccount(String account)
```

<br />**输入参数：**<br />

- account  用户的外部账户的私钥地址。


<br />**返回参数：**<br />

- TransactionReceipt 交易回执



#### 移交管理员的权限

<br />移交管理员账户时，需要确保被移交的账户已注册，且账户状态正常。<br />
<br />**具体调用示例：**<br />

```javascript
    TransactionReceipt tr = adminModeManager.transferAdminAuth(u1Address);
```

<br />**函数签名：**<br />

```
    TransactionReceipt transferAdminAuth(String account)
```

<br />**输入参数：**<br />

- account  用户的外部账户的私钥地址。


<br />**返回参数：**<br />

- TransactionReceipt 交易回执



### 相同权重的投票制治理模式(多签制)

<br />治理委员会模式下的管理功能均位于 VoteModeGovernManager 类中。<br />
<br />首先，注入该类：<br />

```
@Autowired
private VoteModeGovernManager voteModeGovernManager;
```

<br />在本模式下，执行任何账户相关的业务操作需要遵循以下步骤：<br />

1. 发起一个投票请求；
1. 治理账户成员赞同该投票；
1. 投票发起者确认投票已经通过后，发起操作。


<br />我们首先来介绍下通用的投票接口：<br />

#### 治理委员会成员投票

<br />**具体调用示例：**<br />

```
    TransactionReceipt tr = voteModeGovernManager.vote(requestId, true);
```

<br />**函数签名：**<br />

```
    TransactionReceipt vote(BigInteger requestId, boolean agreed)
```

<br />**输入参数：**<br />

- requestId  发起投票的requestId。
- agreed 是否同意，true/false


<br />**返回参数：**<br />

- TransactionReceipt 交易回执



#### 重置用户私钥

<br />参考上文提及的三个步骤：发起投票请求、投票、执行操作。此处，使用了单SDK来处理多用户的操作，使用了changeCredentials函数来切换不同的用户。<br />
<br />**具体调用示例：**<br />

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

<br />**函数签名：**<br />

```
    BigInteger requestResetAccount(String newCredential, String oldCredential)
```

<br />**输入参数：**<br />

- oldCredential  用户的外部账户的原私钥地址。
- newCredential  该账户被重置后的私钥地址


<br />**返回参数：**<br />

- BigInteger 投票ID，用户需保存该ID便于后续的交互和其他操作。


##### 重置用户私钥

<br />**函数签名：**<br />

```
    TransactionReceipt resetAccount(BigInteger requestId, String newCredential, String oldCredential)
```

<br />**输入参数：**<br />

- requestId  之前的投票请求返回的ID
- newCredential  该账户被重置后的私钥地址
- oldCredential  用户的外部账户的原私钥地址。


<br />**返回参数：**<br />

- TransactionReceipt 交易回执。


#### 冻结普通账户

<br />参考上文提及的三个步骤：发起投票请求、投票、执行操作。此处，使用了单SDK来处理多用户的操作，使用了changeCredentials函数来切换不同的用户。<br />
<br />**具体调用示例：**<br />

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

<br />**函数签名：**<br />

```
   BigInteger requestFreezeAccount(String credential)
```

<br />**输入参数：**<br />

- externalAccount  用户的外部账户地址。


<br />**返回参数：**<br />

- BigInteger 投票ID，用户需保存该ID便于后续的交互和其他操作。


##### 冻结用户账户

<br />**函数签名：**<br />

```
   TransactionReceipt freezeAccount(BigInteger requestId, String credential)
```

<br />**输入参数：**<br />

- requestId  之前的投票请求返回的ID
- externalAccount  用户的外部账户地址。


<br />**返回参数：**<br />

- TransactionReceipt 交易回执。


#### 解冻普通账户

<br />参考上文提及的三个步骤：发起投票请求、投票、执行操作。此处，使用了单SDK来处理多用户的操作，使用了changeCredentials函数来切换不同的用户。<br />
<br />**具体调用示例：**<br />

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

<br />**函数签名：**<br />

```
   BigInteger requestunfreezeAccount(String credential)
```

<br />**输入参数：**<br />

- externalAccount  用户的外部账户地址。


<br />**返回参数：**<br />

- BigInteger 投票ID，用户需保存该ID便于后续的交互和其他操作。


##### 解冻用户账户

<br />**函数签名：**<br />

```
   TransactionReceipt unfreezeAccount(BigInteger requestId, String credential)
```

<br />**输入参数：**<br />

- requestId  之前的投票请求返回的ID
- externalAccount  用户的外部账户地址。


<br />**返回参数：**<br />

- TransactionReceipt 交易回执。


#### 账户强制注销

<br />参考上文提及的三个步骤：发起投票请求、投票、执行操作。此处，使用了单SDK来处理多用户的操作，使用了changeCredentials函数来切换不同的用户。<br />
<br />**具体调用示例：**<br />

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

<br />**函数签名：**<br />

```
   BigInteger requestCancelAccount(String credential)
```

<br />**输入参数：**<br />

- externalAccount  用户的外部账户地址。


<br />**返回参数：**<br />

- BigInteger 投票ID，用户需保存该ID便于后续的交互和其他操作。



##### 注销用户账户

<br />**函数签名：**<br />

```
   TransactionReceipt cancelAccount(BigInteger requestId, String credential)
```

<br />**输入参数：**<br />

- requestId  之前的投票请求返回的ID
- externalAccount  用户的外部账户地址。


<br />**返回参数：**<br />

- TransactionReceipt 交易回执。


#### 设置治理账户投票的阈值

<br />参考上文提及的三个步骤：发起投票请求、投票、执行操作。此处，使用了单SDK来处理多用户的操作，使用了changeCredentials函数来切换不同的用户。<br />
<br />**具体调用示例：**<br />

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

<br />**函数签名：**<br />

```
    BigInteger requestRemoveGovernAccount(int newThreshold)
```

<br />**输入参数：**<br />

- newThreshold  新阈值。


<br />**返回参数：**<br />

- BigInteger 投票ID，用户需保存该ID便于后续的交互和其他操作。


##### 设置新阈值

<br />**函数签名：**<br />

```
   TransactionReceipt resetThreshold(BigInteger requestId, int threshold)
```

<br />**输入参数：**<br />

- requestId  之前的投票请求返回的ID
- newThreshold  新阈值。


<br />**返回参数：**<br />

- TransactionReceipt 交易回执。


#### 治理账户删除一个投票账户

<br />参考上文提及的三个步骤：发起投票请求、投票、执行操作。此处，使用了单SDK来处理多用户的操作，使用了changeCredentials函数来切换不同的用户。<br />
<br />**具体调用示例：**<br />

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

<br />**函数签名：**<br />

```
   BigInteger requestRemoveGovernAccount(String credential)
```

<br />**输入参数：**<br />

- externalAccount  用户的外部账户地址。


<br />**返回参数：**<br />

- BigInteger 投票ID，用户需保存该ID便于后续的交互和其他操作。


##### 删除一个投票账户

<br />**函数签名：**<br />

```
   TransactionReceipt removeGovernAccount(BigInteger requestId, String credential)
```

<br />**输入参数：**<br />

- requestId  之前的投票请求返回的ID
- externalAccount  用户的外部账户地址。


<br />**返回参数：**<br />

- TransactionReceipt 交易回执。


#### 治理账户添加一个投票新账户

<br />参考上文提及的三个步骤：发起投票请求、投票、执行操作。此处，使用了单SDK来处理多用户的操作，使用了changeCredentials函数来切换不同的用户。<br />
<br />**具体调用示例：**<br />

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

<br />**函数签名：**<br />

```
   BigInteger requestAddGovernAccount(String credential)
```

<br />**输入参数：**<br />

- externalAccount  用户的外部账户地址。


<br />**返回参数：**<br />

- BigInteger 投票ID，用户需保存该ID便于后续的交互和其他操作。


##### 添加一个投票账户

<br />**函数签名：**<br />

```
   TransactionReceipt addGovernAccount(BigInteger requestId, String credential)
```

<br />**输入参数：**<br />

- requestId  之前的投票请求返回的ID
- externalAccount  用户的外部账户地址。


<br />**返回参数：**<br />

- TransactionReceipt 交易回执。


### 不同权重的投票制治理模式

<br />不同权重的投票制模式总体和多签制非常类似，此处不再做过多的赘述，请参考上节。<br />

#### 治理账户添加一个投票新账户

<br />参考上文提及的三个步骤：发起投票请求、投票、执行操作。此处，使用了单SDK来处理多用户的操作，使用了changeCredentials函数来切换不同的用户。<br />
<br />**具体调用示例：**<br />

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

<br />**函数签名：**<br />

```
   BigInteger requestAddGovernAccount(String credential, int weight)
```

<br />**输入参数：**<br />

- externalAccount  用户的外部账户地址。
- weight 新加入账户的权重


<br />**返回参数：**<br />

- BigInteger 投票ID，用户需保存该ID便于后续的交互和其他操作。


##### 添加一个投票账户

<br />**函数签名：**<br />

```
   TransactionReceipt addGovernAccount(BigInteger requestId, String credential, int weight)
```

<br />**输入参数：**<br />

- requestId  之前的投票请求返回的ID
- externalAccount  用户的外部账户地址。
- weight 新加入账户的权重


<br />**返回参数：**<br />

- TransactionReceipt 交易回执。


#### 其他交易

<br />其余部分颇为相似，可参考基于相同权重的投票模式<br />

## 普通账户管理模式功能说明


### 普通账户主要功能

<br />普通账户的功能均位于EndUserOperManager类中。<br />
<br />首先，注入该类：<br />

```
@Autowired
private EndUserOperManager endUserOperManager;
```

#### 创建新账户

<br />**具体调用示例：**<br />

```
    String account = endUserAdminManager.createAccount(p1Address);
```

<br />**函数签名：**<br />

```
    String createAccount(String who)
```

<br />**输入参数：**<br />

- externalAccount  待创建的账户的外部账户的私钥地址。


<br />**返回参数：**<br />

- String 新建的账户地址


#### 重置用户私钥

<br />**具体调用示例：**<br />

```
    TransactionReceipt tr = endUserAdminManager.resetAccount(p2Address);
```

<br />**函数签名：**<br />

```
    TransactionReceipt resetAccount(String newCredential)
```

<br />**输入参数：**<br />

- newCredential  待更换的私钥地址。


<br />**返回参数：**<br />

- TransactionReceipt 交易回执

#### 账户强制注销

<br />**具体调用示例：**<br />

```
    TransactionReceipt tr = endUserAdminManager.cancelAccount();
```

<br />**函数签名：**<br />

```
    TransactionReceipt cancelAccount()
```

<br />**返回参数：**<br />

- TransactionReceipt 交易回执


#### 修改普通账户的管理类型

<br />**具体调用示例：**<br />

```
    List<String> voters = Lists.newArrayList();
    voters.add(u.getAddress());
    voters.add(u1.getAddress());
    voters.add(u2.getAddress());
    TransactionReceipt tr = endUserAdminManager.modifyManagerType(voters);
```

<br />**函数签名：**<br />

```
    TransactionReceipt modifyManagerType(List<String> voters)
    //重载函数，设置为仅自己管理
    TransactionReceipt modifyManagerType()
```

<br />**输入参数：**<br />

- voters （可选） 当变更为支持社交好友投票时需要传入，且voters的大小必须为3。投票本身为2-3的规则。如果voters不传入，则默认不开启投票模式。


<br />**返回参数：**<br />

- TransactionReceipt 交易回执


#### 添加一个社交好友

<br />**具体调用示例：**<br />

```
    TransactionReceipt tr = endUserAdminManager.addRelatedAccount(userAddress);
```

<br />**函数签名：**<br />

```
    TransactionReceipt addRelatedAccount(String account)
```

<br />**输入参数：**<br />

- account  被添加好友的外部账户地址。


<br />**返回参数：**<br />

- TransactionReceipt 交易回执


#### 删除一个社交好友

<br />**具体调用示例：**<br />

```
    TransactionReceipt tr = endUserAdminManager.removeRelatedAccount(userAddress);
```

<br />**函数签名：**<br />

```
    TransactionReceipt removeRelatedAccount(String account)
```

<br />**输入参数：**<br />

- account  被删除好友的外部账户地址。


<br />**返回参数：**<br />

- TransactionReceipt 交易回执


### 使用社交好友投票重置私钥

#### 重置用户私钥

<br />参考上文提及的三个步骤：发起投票请求、投票、执行操作。此处，使用了单SDK来处理多用户的操作，使用了changeCredentials函数来切换不同的用户。<br />
<br />社交好友投票相关的操作在 SocialVoteManager 类中。<br />
<br />**具体调用示例：**<br />

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

<br />**函数签名：**<br />

```
    TransactionReceipt requestResetAccount(String newCredential, String oldCredential)
```

<br />**输入参数：**<br />

- oldCredential  用户的外部账户的原私钥地址。
- newCredential  该账户被重置后的私钥地址


<br />**返回参数：**<br />

- TransactionReceipt 交易回执。


##### 投票

<br />**函数签名：**<br />

```
    TransactionReceipt vote(String oldCredential, boolean agreed)
```

<br />**输入参数：**<br />

- oldCredential  申请变更账户的外部账户地址。
- agreed  是否同意


<br />**返回参数：**<br />

- TransactionReceipt 交易回执。


##### 重置用户私钥

<br />**函数签名：**<br />

```
    TransactionReceipt resetAccount(String newCredential, String oldCredential)
```

<br />**输入参数：**<br />

- newCredential  该账户被重置后的私钥地址
- oldCredential  用户的外部账户的原私钥地址。


<br />**返回参数：**<br />

- TransactionReceipt 交易回执。
