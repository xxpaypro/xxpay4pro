
# 声明
源码只供学习使用，如用于商业活动与本人无关，请勿将系统用于非法业务

# 在线体验


### XXPayPro版本演示地址：

运营端：[https://mgr.xxpaypro.com](https://mgr.xxpaypro.com) 账号:xxpaymgr 登录密码：111111abc 

代理端：[https://agent.xxpaypro.com](https://agent.xxpaypro.com) 账号：test001 登录密码：a123123123 支付密码：a123123123

商户端：[https://mch.xxpaypro.com](https://mch.xxpaypro.com) 账号：test 登录密码：a123123123 支付密码：a123123123

核销端：[https://writeoff.xxpaypro.com](https://writeoff.xxpaypro.com) 账号：hexiao111 登录密码：a123123123 支付密码：a123123123

测试账号密码会定期更改，如不对请联系客服发新的账号密码

### 开发文档
[https://www.xxpaypro.com](https://www.xxpaypro.com) 

### XXPayPlus版本演示地址：

运营端：[https://mgr.xxpayplus.com](https://mgr.xxpayplus.com) 帐号：jeepay 登录密码：jeepay123

代理端：[https://agent.xxpayplus.com](https://agent.xxpayplus.com) 帐号：agenttest 登录密码：agent123123

商户端：[https://mch.xxpayplus.com](https://mch.xxpayplus.com) 帐号：mchtest 登录密码：mch123123


系统采用JAVA语言开发，会java的技术人员可以自行二次开发


XxPayPro是一套开箱即用、适合拿来直接运营的聚合支付系统。系统适合有技术团队的企业购买，我司可提供程序源码、技术文档和售后技术支持服务。

程序源码和文档包括哪些？
源码包括：所有Java服务端源码和Layui前端源码，可二次开发，想怎么改就怎么改，So Easy !

文档包括：开发说明、系统部署、通道对接、API接口、线上运维、系统业务等。

# 技术支持有哪些服务？
针对每个购买的客户，我司会单独创建群，至少指定一名技术支持人员单独提供售后技术支持。

技术支持内容包括：系统部署指导、二次开发指导、反馈Bug的修复、需求的收集等。

注：不提供软件开发环境搭建、不提供java基础辅导、仅限该系统业务技术交流。




### 如需要最新完整商业版本请联系 飞机(Telegram)：@xxpaypro



# 系统描述
xxpay4pro为xxpay pro版，使用spring boot + dubbo架构开发。包括运营平台、代理商系统、商户系统、支付系统，结算系统、对账系统等。

# 模块说明
xxpay-service 所有核心业务方法封装，供其他模块引用后调用

xxpay-core 核心包，包括dubbo服务接口以及实体bean,以及公用引用及常用工具类等

xxpay-manage 运营平台（接口和管理界面，前后端分离）

xxpay-merchant 商户系统（接口和管理界面，前后端分离）

xxpay-agent 代理商系统（接口和管理界面，前后端分离）

xxpay-pay 支付网关，提供商户访问的支付接口及对接所有支付通道实现

xxpay-task定时任务，包括对账服务、结算服务，部署时需单节点部署

xxpay-writeoff，核销端、提供核销API，和核销商提交话费，电费，油卡等核销户号，部署时需单节点部署

xxpay-z-api-base 支付接口的基础包

<table>
<thead>
<tr>
<th>项目</th>
<th>端口</th>
<th>描述</th>
</tr>
</thead>
<tbody><tr>
<td>xxpay-core</td>
<td></td>
<td>公共方法,实体Bean,API接口定义</td>
</tr>
<tr>
<td>xxpay-z-api-base</td>
<td></td>
<td>支付接口的基础包</td>
</tr>
<tr>
<td>xxpay-manage</td>
<td>56701</td>
<td>运营平台接口</td>
</tr>
<tr>
<td>xxpay-agent</td>
<td>56702</td>
<td>代理商系统接口</td>
</tr>
<tr>
<td>xxpay-merchant</td>
<td>56703</td>
<td>商户系统接口</td>
</tr>
<tr>
<td>xxpay-pay</td>
<td>56700</td>
<td>支付核心系统</td>
</tr>
<tr>
<td>xxpay-service</td>
<td></td>
<td>业务接口</td>
</tr>
<tr>
<td>xxpay-task</td>
<td>56705</td>
<td>定时任务,包括对账和结算服务</td>
</tr>
<tr>
<td>xxpay-writeoff</td>
<td>56706</td>
<td>核销商系统接口</td>
</tr>
</tbody></table>


# xxpay4pro
聚合支付系统
四方支付系统
第四方支付平台
三方平台，核销平台，核销系统
码商系统
xxpaypro 聚合支付系统 四方支付系统 第四方支付平台 三方平台，核销平台，核销系统 码商系统 石化核销 中石油核销 电卡核销 费话核销
