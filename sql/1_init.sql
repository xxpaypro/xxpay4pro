/* 支付中心相关表结构 */

-- 设置提交符为 ;
delimiter ;

-- 商户基础信息表
DROP TABLE IF EXISTS t_mch_info;
CREATE TABLE `t_mch_info` (
  `MchId` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商户ID',
  `MchName` varchar(30) NOT NULL COMMENT '商户全称',
  `MchShortName` varchar(20) NOT NULL COMMENT '商户简称',
  `ContactRealName` varchar(20) DEFAULT NULL COMMENT '联系人真实姓名',
  `LoginUserName` varchar(50) NOT NULL COMMENT '登录用户名',
  `LoginMobile` varchar(12) NOT NULL COMMENT '登录手机号（联系人手机号）',
  `LoginEmail` varchar(32) COMMENT '登录邮箱（联系人邮箱）',
  `RegisterPassword` varchar(100) DEFAULT NULL COMMENT '注册密码，仅在商户注册时使用',
  `Type` tinyint(6) NOT NULL DEFAULT 1 COMMENT '类型:1-平台账户,2-私有账户',
  `PrivateKey` varchar(128) DEFAULT NULL COMMENT '私钥',
  `ProvinceCode` int NOT NULL COMMENT '行政地区编号，省',
  `CityCode` int NOT NULL COMMENT '行政地区编号， 市',
  `AreaCode` int NOT NULL COMMENT '行政地区编号， 县',
  `AreaInfo` varchar(128) NOT NULL COMMENT '省市县名称描述',
  `Address` varchar(64) NOT NULL COMMENT '商户地址',   -- 商户信息

  `AgentId` bigint(20) COMMENT '代理商ID',
  `MchLevel` int NOT NULL DEFAULT '0' COMMENT '商户级别， 0-服务商直接商户',
  `IsvId` bigint(20) NOT NULL COMMENT '服务商ID',
  `Status` tinyint(6) NOT NULL COMMENT '商户状态: -2审核不通过, -1-待审核, 0-停用, 1-正常',
  `SignStatus` tinyint(6) NOT NULL COMMENT '签约状态: 0-待补充资料, 1-待签约, 2-已签约',
  `DepositModeStatus` tinyint(6) DEFAULT '0' COMMENT '是否支持押金模式:0-否,1-是',
  `AuditInfo` varchar(256) COMMENT '审核信息',
  `Remark` varchar(128) DEFAULT NULL COMMENT '商户备注',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`MchId`),
  UNIQUE KEY `Uni_LoginUserName` (`LoginUserName`),
  UNIQUE KEY `Uni_Mobile` (`LoginMobile`),
  UNIQUE KEY `Uni_Email` (`LoginEmail`)
) ENGINE=InnoDB AUTO_INCREMENT=20000000 DEFAULT CHARSET=utf8mb4 COMMENT='商户基础信息表';

-- 商户扩展信息表（进件）
DROP TABLE IF EXISTS t_mch_info_ext;
CREATE TABLE `t_mch_info_ext` (
  `MchId` bigint(20) NOT NULL COMMENT '商户ID',  -- 商户信息
  `AgentId` bigint(20) COMMENT '代理商ID',
  `IsvId` bigint(20) NOT NULL COMMENT '服务商ID',
  `LegalPersonName` varchar(30) COMMENT '法人姓名',
  `LegalPersonPhone` varchar(30) COMMENT '法人电话',
  `LegalPersonIdCardNo` varchar(32) COMMENT '法人身份证号码',
  `LegalPersonIdCardDate` varchar(32) COMMENT '法人身份证有效期: 格式为：[yyyy-MM-dd_yyyy-MM-dd] 0_0表示长期有效',
  `BusinessLicenseNo` varchar(32) COMMENT '营业执照编号',
  `BusinessLicenseDate` varchar(32) COMMENT '营业执照有效期: 格式为：[yyyy-MM-dd_yyyy-MM-dd] 0_0表示长期有效',
  `ServicePhone` varchar(30) COMMENT '客服电话',
  `IndustryCode` int COMMENT '行业编码',

  `SettAccAttr` tinyint(6) DEFAULT '0' COMMENT '账户属性:0-对私,1-对公,默认对私',  -- 结算账号信息
  `SettAccNo` varchar(30) COMMENT '银行卡号',
  `SettAccName` varchar(30) COMMENT '开户名称(持卡人姓名)',
  `SettBankName` varchar(64) COMMENT '银行名称',
  `SettBankNetName` varchar(64) COMMENT '支行名称',
  `SettBankProvinceCode` int COMMENT '开户地区编号，省',
  `SettBankCityCode` int COMMENT '开户地区编号， 市',
  `SettBankAreaCode` int COMMENT '开户地区编号， 县',
  `SettBankAreaInfo` varchar(128) COMMENT '开户省市县名称描述',

  `StoreOuterImg` varchar(256) COMMENT '门头照',  -- 照片附件信息
  `StoreInnerImg` varchar(256) COMMENT '经营场所照',
  `CashierImg` varchar(256) COMMENT '收银台照',
  `LegalIdCard1Img` varchar(256) COMMENT '法人身份证正面',
  `LegalIdCard2Img` varchar(256) COMMENT '法人身份证反面',
  `OpenAccountImg` varchar(256) COMMENT '开户许可证照',
  `BusinessLicenseImg` varchar(256) COMMENT '营业执照',
  `Qualifications` varchar(1024) COMMENT '特殊资质',
  `SettIdCard1Img` varchar(256) COMMENT '结算人身份证正面',
  `SettIdCard2Img` varchar(256) COMMENT '结算人身份证反面',
  `SettIdCard3Img` varchar(256) COMMENT '手持身份证照片',
  `AuthorizeImg` varchar(256) COMMENT '非法人对私授权函',
  `LeaseImg` varchar(256) COMMENT '租房合同',
  `BankCardImg` varchar(256) COMMENT '银行卡照',
  `Other1Img` varchar(256) COMMENT '其他资料-1',
  `Other2Img` varchar(256) COMMENT '其他资料-2',

  `ApplyRateInfo` varchar(256) COMMENT '申请费率',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`MchId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户扩展信息表（进件）';

DROP TABLE IF EXISTS t_mch_app;
CREATE TABLE `t_mch_app` (
  `AppId` varchar(32) NOT NULL COMMENT '应用ID',
  `AppName` varchar(128) NOT NULL DEFAULT '' COMMENT '应用名称',
  `MchId` bigint(20) NOT NULL COMMENT '商户ID',
  `MchType` tinyint(6) NOT NULL COMMENT '商户类型:1-平台账户,2-私有账户',
  `Status` tinyint(6) NOT NULL COMMENT '应用状态,0-停止使用,1-使用中',
  `Remark` varchar(128) DEFAULT NULL COMMENT '备注',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`AppId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户应用表';

DROP TABLE IF EXISTS t_pay_order;
CREATE TABLE `t_pay_order` (
  `PayOrderId` varchar(30) NOT NULL COMMENT '支付订单号',
  `MchId` bigint(20) NOT NULL COMMENT '商户ID',
  `MchType` tinyint(6) NOT NULL COMMENT '商户类型:1-平台账户,2-私有账户',
  `DepositMode` tinyint(6) DEFAULT '0' COMMENT '是否押金模式:0-否,1-是',
  `AppId` varchar(32) DEFAULT NULL COMMENT '应用ID',
  `MchOrderNo` varchar(30) NOT NULL COMMENT '商户订单号',
  `AgentId` bigint(20) DEFAULT NULL COMMENT '商户所属代理商ID',
  `IsvId` bigint(20) COMMENT '商户所属服务商ID',
  `ProductId` int(11) DEFAULT NULL COMMENT '支付产品ID',
  `MchPassageId` int(11) DEFAULT NULL COMMENT '商户通道ID',
  `IsvPassageId` int(11) DEFAULT NULL COMMENT '服务商通道ID',
  `ChannelType` varchar(30) NOT NULL COMMENT '渠道类型,对接支付接口类型代码',
  `ChannelId` varchar(24) NOT NULL COMMENT '渠道ID,对应支付接口代码',
  `Amount` bigint(20) NOT NULL COMMENT '支付金额,单位分',
  `DepositAmount` bigint(20) DEFAULT NULL COMMENT '押金金额,单位分',
  `Currency` varchar(3) NOT NULL DEFAULT 'cny' COMMENT '三位货币代码,人民币:cny',
  `Status` tinyint(6) NOT NULL DEFAULT '0' COMMENT '支付状态,0-订单生成,1-支付中,2-支付成功, 4-已退款, 5-订单关闭, 6-押金未结算, 7-押金退还',
  `ClientIp` varchar(32) DEFAULT NULL COMMENT '客户端IP',
  `Device` varchar(64) DEFAULT NULL COMMENT '设备',
  `Subject` varchar(64) NOT NULL COMMENT '商品标题',
  `Body` varchar(256) NOT NULL COMMENT '商品描述信息',
  `Extra` varchar(512) DEFAULT NULL COMMENT '特定渠道发起额外参数',
  `ChannelUser` varchar(32) DEFAULT NULL COMMENT '渠道用户标识,如微信openId,支付宝账号',
  `ChannelMchId` varchar(32) NOT NULL COMMENT '渠道商户ID',
  `ChannelOrderNo` varchar(64) DEFAULT NULL COMMENT '渠道订单号',
  `ChannelAttach` varchar(1024) DEFAULT NULL COMMENT '渠道数据包',
  `IsRefund` tinyint(6) NOT NULL DEFAULT '0' COMMENT '是否退款,0-未退款,1-退款',
  `RefundTimes` int NULL COMMENT '退款次数',
  `SuccessRefundAmount` bigint(20) NULL COMMENT '成功退款金额,单位分',
  `ErrCode` varchar(64) DEFAULT NULL COMMENT '渠道支付错误码',
  `ErrMsg` varchar(128) DEFAULT NULL COMMENT '渠道支付错误描述',
  `Param1` varchar(64) DEFAULT NULL COMMENT '扩展参数1',
  `Param2` varchar(64) DEFAULT NULL COMMENT '扩展参数2',
  `NotifyUrl` varchar(128) NOT NULL COMMENT '通知地址',
  `ReturnUrl` varchar(128) DEFAULT '' COMMENT '跳转地址',
  `ExpireTime` datetime DEFAULT NULL COMMENT '订单失效时间',
  `PaySuccTime` datetime DEFAULT NULL COMMENT '订单支付成功时间',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `ProductType` tinyint(6) DEFAULT 1 COMMENT '产品类型:1-收款,2-充值',
  PRIMARY KEY (`PayOrderId`),
  UNIQUE KEY `IDX_MchId_MchOrderNo` (`MchId`, MchOrderNo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付订单表';

-- 支付订单扩展表
DROP TABLE IF EXISTS t_pay_order_ext;
CREATE TABLE `t_pay_order_ext` (
  `PayOrderId` varchar(30) NOT NULL COMMENT '支付订单号',
  `MchId` bigint(20) NOT NULL COMMENT '商户ID',
  `MchOrderNo` varchar(30) NOT NULL COMMENT '商户订单号',
  `retData` varchar(4096) NOT NULL COMMENT '返回商户接口数据',
  PRIMARY KEY (`PayOrderId`),
  UNIQUE KEY `IDX_MchId_MchOrderNo` (`MchId`, `MchOrderNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付订单扩展表';



DROP TABLE IF EXISTS t_iap_receipt;
CREATE TABLE `t_iap_receipt` (
  `PayOrderId` varchar(30) NOT NULL COMMENT '支付订单号',
  `MchId` bigint(20) NOT NULL COMMENT '商户ID',
  `AppId` varchar(32) NOT NULL COMMENT '应用ID',
  `TransactionId` varchar(24) NOT NULL COMMENT 'IAP业务号',
  `ReceiptData` TEXT NOT NULL COMMENT '渠道ID',
  `Status` tinyint(6) NOT NULL DEFAULT '0' COMMENT '处理状态:0-未处理,1-处理成功,-1-处理失败',
  `HandleCount` tinyint(6) NOT NULL DEFAULT 0 COMMENT '处理次数',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`PayOrderId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='苹果支付凭据表';

drop table if exists `t_trans_order` ;
CREATE TABLE `t_trans_order` (
  `TransOrderId` varchar(30) NOT NULL COMMENT '转账订单号',
  `InfoId` bigint(20) NOT NULL COMMENT '代理商或商户ID',
  `InfoType` tinyint(6) NOT NULL COMMENT '账号类型:1-商户 2-代理商 3-平台',
  `MchType` tinyint(6) COMMENT '商户类型:1-平台账户,2-私有账户',
  `AppId` varchar(32) DEFAULT NULL COMMENT '应用ID',
  `MchTransNo` varchar(30) NOT NULL COMMENT '商户转账单号',
  `ChannelType` varchar(30) NOT NULL COMMENT '渠道类型,对接支付接口类型代码',
  `ChannelId` varchar(24) NOT NULL COMMENT '渠道ID,对应支付接口代码',
  `PassageId` int(11) DEFAULT NULL COMMENT '通道ID',
  `PassageAccountId` int(11) DEFAULT NULL COMMENT '通道账户ID',
  `Amount` bigint(20) NOT NULL COMMENT '转账金额,单位分',
  `Currency` varchar(3) NOT NULL DEFAULT 'cny' COMMENT '三位货币代码,人民币:cny',
  `Status` tinyint(6) NOT NULL DEFAULT '0' COMMENT '转账状态:0-订单生成,1-转账中,2-转账成功,3-转账失败',
  `Result` tinyint(6) NOT NULL DEFAULT '0' COMMENT '转账结果:0-不确认结果,1-等待手动处理,2-确认成功,3-确认失败',
  `ClientIp` varchar(32) DEFAULT NULL COMMENT '客户端IP',
  `Device` varchar(64) DEFAULT NULL COMMENT '设备',
  `RemarkInfo` varchar(256) DEFAULT NULL COMMENT '备注',
  `ChannelUser` varchar(32) DEFAULT NULL COMMENT '渠道用户标识,如微信openId,支付宝账号',
  `AccountAttr` tinyint(6) DEFAULT '0' COMMENT '账户属性:0-对私,1-对公,默认对私',
  `AccountType` tinyint(6) DEFAULT NULL COMMENT '账户类型:1-银行卡转账,2-微信转账,3-支付宝转账',
  `AccountName` varchar(64) DEFAULT NULL COMMENT '账户名',
  `AccountNo` varchar(64) DEFAULT NULL COMMENT '账户号',
  `Province` varchar(32) DEFAULT NULL COMMENT '开户行所在省',
  `City` varchar(32) DEFAULT NULL COMMENT '开户行所在市',
  `BankName` varchar(128) DEFAULT NULL COMMENT '开户行名称',
  `BankType` bigint(20) DEFAULT NULL COMMENT '联行号',
  `BankCode` varchar(32) DEFAULT NULL COMMENT '银行代码',
  `ChannelMchId` varchar(32) NOT NULL COMMENT '渠道商户ID',
  `ChannelRate` decimal(20,6) DEFAULT NULL COMMENT '渠道费率',
  `channelFeeEvery` bigint(20) DEFAULT NULL COMMENT '渠道每笔费用,单位分',
  `ChannelCost` bigint(20) DEFAULT NULL COMMENT '渠道成本,单位分',
  `ChannelOrderNo` varchar(32) DEFAULT NULL COMMENT '渠道订单号',
  `ChannelErrCode` varchar(128) DEFAULT NULL COMMENT '渠道错误码',
  `ChannelErrMsg` varchar(128) DEFAULT NULL COMMENT '渠道错误描述',
  `Extra` varchar(512) DEFAULT NULL COMMENT '特定渠道发起时额外参数',
  `NotifyUrl` varchar(128) DEFAULT NULL COMMENT '通知地址',
  `Param1` varchar(64) DEFAULT NULL COMMENT '扩展参数1',
  `Param2` varchar(64) DEFAULT NULL COMMENT '扩展参数2',
  `ExpireTime` datetime DEFAULT NULL COMMENT '订单失效时间',
  `TransSuccTime` datetime DEFAULT NULL COMMENT '订单转账成功时间',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`TransOrderId`),
  UNIQUE KEY `IDX_MchId_MchOrderNo` (`InfoId`,`InfoType`, `MchTransNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='转账订单表';

DROP TABLE IF EXISTS t_refund_order;
CREATE TABLE `t_refund_order` (
  `RefundOrderId` varchar(30) NOT NULL COMMENT '退款订单号',
  `PayOrderId` varchar(30) NOT NULL COMMENT '支付订单号',
  `ChannelPayOrderNo` varchar(64) DEFAULT NULL COMMENT '渠道支付单号',
  `MchId` bigint(20) NOT NULL COMMENT '商户ID',
  `MchType` tinyint(6) NOT NULL COMMENT '商户类型:1-平台账户,2-私有账户',
  `AppId` varchar(32) NOT NULL COMMENT '应用ID',
  `MchRefundNo` varchar(30) NOT NULL COMMENT '商户退款单号',
  `ChannelType` varchar(30) NOT NULL COMMENT '渠道类型,WX:微信,ALIPAY:支付宝',
  `ChannelId` varchar(24) NOT NULL COMMENT '渠道ID',
  `PassageId` int(11) DEFAULT NULL COMMENT '通道ID',
  `PayAmount` bigint(20) NOT NULL COMMENT '支付金额,单位分',
  `RefundAmount` bigint(20) NOT NULL COMMENT '退款金额,单位分',
  `Currency` varchar(3) NOT NULL DEFAULT 'cny' COMMENT '三位货币代码,人民币:cny',
  `Status` tinyint(6) NOT NULL DEFAULT '0' COMMENT '退款状态:0-订单生成,1-退款中,2-退款成功,3-退款失败',
  `Result` tinyint(6) NOT NULL DEFAULT '0' COMMENT '退款结果:0-不确认结果,1-等待手动处理,2-确认成功,3-确认失败',
  `ClientIp` varchar(32) DEFAULT NULL COMMENT '客户端IP',
  `Device` varchar(64) DEFAULT NULL COMMENT '设备',
  `RemarkInfo` varchar(256) DEFAULT NULL COMMENT '备注',
  `ChannelUser` varchar(32) DEFAULT NULL COMMENT '渠道用户标识,如微信openId,支付宝账号',
  `UserName` varchar(24) DEFAULT NULL COMMENT '用户姓名',
  `ChannelMchId` varchar(32) NOT NULL COMMENT '渠道商户ID',
  `ChannelOrderNo` varchar(32) DEFAULT NULL COMMENT '渠道订单号',
  `ChannelErrCode` varchar(128) DEFAULT NULL COMMENT '渠道错误码',
  `ChannelErrMsg` varchar(128) DEFAULT NULL COMMENT '渠道错误描述',
  `Extra` varchar(512) DEFAULT NULL COMMENT '特定渠道发起时额外参数',
  `NotifyUrl` varchar(128) DEFAULT NULL COMMENT '通知地址',
  `Param1` varchar(64) DEFAULT NULL COMMENT '扩展参数1',
  `Param2` varchar(64) DEFAULT NULL COMMENT '扩展参数2',
  `ExpireTime` datetime DEFAULT NULL COMMENT '订单失效时间',
  `RefundSuccTime` datetime DEFAULT NULL COMMENT '订单退款成功时间',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`RefundOrderId`),
  UNIQUE KEY `IDX_MchId_MchOrderNo` (`MchId`, MchRefundNo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='退款订单表';

DROP TABLE IF EXISTS t_mch_notify;
CREATE TABLE `t_mch_notify` (
  `OrderId` varchar(24) NOT NULL COMMENT '订单ID',
  `MchId` bigint(20) NOT NULL COMMENT '商户ID',
  `IsvId` bigint(20) DEFAULT NULL COMMENT '服务商ID',
  `AppId` varchar(32) DEFAULT NULL COMMENT '应用ID',
  `MchOrderNo` varchar(30) NOT NULL COMMENT '商户订单号',
  `OrderType` varchar(8) NOT NULL COMMENT '订单类型:1-支付,2-转账,3-退款',
  `NotifyUrl` varchar(2048) NOT NULL COMMENT '通知地址',
  `NotifyCount` tinyint(6) NOT NULL DEFAULT 0 COMMENT '通知次数',
  `Result` varchar(2048) DEFAULT NULL COMMENT '通知响应结果',
  `Status` tinyint(6) NOT NULL DEFAULT '1' COMMENT '通知状态,1-通知中,2-通知成功,3-通知失败',
  `LastNotifyTime` datetime DEFAULT NULL COMMENT '最后一次通知时间',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`OrderId`),
  UNIQUE KEY `IDX_MchId_OrderType_MchOrderNo` (`MchId`, `OrderType`, `MchOrderNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户通知表';

DROP TABLE IF EXISTS t_mch_sett_batch_record;
CREATE TABLE `t_mch_sett_batch_record` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `MchId` bigint(20) NOT NULL COMMENT '商户ID',
  `SettRecordId` bigint(20) NOT NULL COMMENT '结算记录ID',
  `BankAccountName` varchar(128) DEFAULT NULL COMMENT '收款银行户名',
  `BankAccountNumber` varchar(128) DEFAULT NULL COMMENT '收款银行账号',
  `BankName` varchar(128) DEFAULT NULL COMMENT '开户银行',
  `BankNetName` varchar(128) DEFAULT NULL COMMENT '开户网点名称',
  `BankProvince` varchar(128) DEFAULT NULL COMMENT '开户行所在省',
  `BankCity` varchar(128) DEFAULT NULL COMMENT '开户行所在市',
  `Amount` bigint(20) DEFAULT NULL COMMENT '金额',
  `PublicFlag` tinyint(6) NOT NULL COMMENT '对公私标识',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='批量结算明细表';

drop table if exists t_sett_bank_account;
CREATE TABLE `t_sett_bank_account` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `InfoId` bigint(20) NOT NULL COMMENT '代理商或商户ID',
  `InfoType` tinyint(6) NOT NULL COMMENT '账号类型:1-商户 2-代理商 3-平台',
  `Name` varchar(30) NOT NULL DEFAULT '' COMMENT '名称',
  `AccountType` tinyint(6) NOT NULL COMMENT '账号类型:1-银行账号,2-微信,3-支付宝',
  `BankName` varchar(64) DEFAULT '' COMMENT '银行名称',
  `BankNetName` varchar(128) DEFAULT NULL COMMENT '开户网点名称',
  `Province` varchar(32) DEFAULT NULL COMMENT '开户行所在省',
  `City` varchar(32) DEFAULT NULL COMMENT '开户行所在市',
  `AccountName` varchar(128) NOT NULL COMMENT '开户人',
  `AccountNo` varchar(64) NOT NULL COMMENT '账号',
  `AccountAttr` tinyint(6) DEFAULT '0' COMMENT '账户属性:0-对私,1-对公,默认对私',
  `IsDefault` tinyint(6) NOT NULL DEFAULT '0' COMMENT '是否默认:0-不是默认,1-默认',
  `Remark` varchar(128) DEFAULT NULL COMMENT '备注',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `IDX_AccountNo` (`AccountNo`,`InfoType`,`AccountType`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='银行账号表';

drop table if exists `t_agentpay_record`;
CREATE TABLE `t_agentpay_record` (
  `AgentpayOrderId` varchar(30) NOT NULL COMMENT '代付单号',
  `InfoId` bigint(20) NOT NULL COMMENT '代理商或商户ID',
  `InfoType` tinyint(6) NOT NULL COMMENT '账号类型:1-商户 2-代理商 3-平台',
  `MchType` tinyint(6) COMMENT '商户类型:1-平台账户,2-私有账户',
  `Amount` bigint(20) DEFAULT NULL COMMENT '申请代付金额',
  `Fee` bigint(20) DEFAULT NULL COMMENT '代付手续费',
  `RemitAmount` bigint(20) DEFAULT NULL COMMENT '打款金额',
  `SubAmount` bigint(20) DEFAULT NULL COMMENT '扣减账户金额',
  `SubAmountFrom` tinyint(6) NOT NULL COMMENT '扣减资金来源:1:从收款账户出款,2:从代付余额账户出款',
  `AccountAttr` tinyint(6) DEFAULT '0' COMMENT '账户属性:0-对私,1-对公,默认对私',
  `AccountType` tinyint(6) DEFAULT '1' COMMENT '账户类型:1-银行卡转账,2-微信转账,3-支付宝转账',
  `AccountName` varchar(64) DEFAULT NULL COMMENT '账户名',
  `AccountNo` varchar(64) DEFAULT NULL COMMENT '账户号',
  `Province` varchar(32) DEFAULT NULL COMMENT '开户行所在省',
  `City` varchar(32) DEFAULT NULL COMMENT '开户行所在市',
  `BankName` varchar(128) DEFAULT NULL COMMENT '开户行名称',
  `BankNetName` varchar(128) DEFAULT NULL COMMENT '开户网点名称',
  `BankNumber` varchar(64) DEFAULT NULL COMMENT '联行号',
  `BankCode` varchar(32) DEFAULT NULL COMMENT '银行代码',
  `Status` tinyint(6) DEFAULT '0' COMMENT '状态:0-待处理,1-处理中,2-成功,3-失败',
  `TransOrderId` varchar(30) DEFAULT NULL COMMENT '转账订单号',
  `TransMsg` varchar(128) DEFAULT NULL COMMENT '转账返回消息',
  `ChannelId` varchar(24) DEFAULT NULL COMMENT '渠道ID',
  `PassageId` int(11) DEFAULT NULL COMMENT '通道ID',
  `Remark` varchar(128) DEFAULT NULL COMMENT '备注',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `ClientIp` varchar(32) DEFAULT NULL COMMENT '客户端IP',
  `Device` varchar(64) DEFAULT NULL COMMENT '设备',
  `BatchNo` varchar(64) DEFAULT NULL COMMENT '代付批次号',
  `AgentpayChannel` tinyint(6) DEFAULT '1' COMMENT '代付渠道:1-平台,2-API接口',
  `MchOrderNo` varchar(64) DEFAULT NULL COMMENT '商户单号',
  `NotifyUrl` varchar(128) DEFAULT NULL COMMENT '通知地址',
  `Extra` varchar(128) DEFAULT NULL COMMENT '扩展域',
  PRIMARY KEY (`AgentpayOrderId`),
  UNIQUE KEY `IDX_TransOrderId` (`TransOrderId`),
  UNIQUE KEY `IDX_MchId_MchOrderNo` (`InfoId`, `InfoType`, `MchOrderNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='代付记录表';

DROP TABLE IF EXISTS `t_mch_trade_order`;
CREATE TABLE `t_mch_trade_order` (
  `TradeOrderId` varchar(30) NOT NULL COMMENT '交易单号',
  `TradeType` tinyint(6) DEFAULT NULL COMMENT '交易类型:1-收款,2-充值',
  `DepositMode` tinyint(6) DEFAULT '0' COMMENT '是否押金模式:0-否,1-是',
  `MchId` bigint(20) NOT NULL COMMENT '商户ID',
  `AgentId` bigint(20) COMMENT '代理商ID',
  `IsvId` bigint(20) COMMENT '服务商ID',
  `AppId` varchar(32) DEFAULT NULL COMMENT '应用ID',
  `ClientIp` varchar(32) DEFAULT NULL COMMENT '客户端IP',
  `Device` varchar(64) DEFAULT NULL COMMENT '设备',
  `GoodsId` varchar(30) DEFAULT NULL COMMENT '商品ID',
  `Subject` varchar(64) NOT NULL COMMENT '商品标题',
  `Body` varchar(256) NOT NULL COMMENT '商品描述信息',
  `OrderAmount` bigint(20) NOT NULL COMMENT '订单金额,单位分',
  `DiscountAmount` bigint(20) NOT NULL COMMENT '优惠金额,单位分',
  `Amount` bigint(20) NOT NULL COMMENT '实际支付金额,单位分',
  `DepositAmount` bigint(20) DEFAULT NULL COMMENT '押金金额,单位分',
  `MchIncome` bigint(20) DEFAULT NULL COMMENT '商户入账,单位分',
  `UserId` varchar(30) DEFAULT NULL COMMENT '用户ID',
  `ChannelUserId` varchar(64) DEFAULT NULL COMMENT '支付渠道用户ID(微信openID或支付宝账号等第三方支付账号)',
  `Status` tinyint(6) NOT NULL DEFAULT '0' COMMENT '订单状态,生成(0),处理中(1),支付成功(2),失败(-1),部分退款(4),全额退款(5),押金未结算(6),押金退还(7)',
  `PayOrderId` varchar(30) DEFAULT NULL COMMENT '支付订单号',
  `ProductId` int COMMENT '产品ID',
  `ProductType` tinyint(6) DEFAULT NULL COMMENT '支付产品类型: 1-现金收款, 2-会员卡支付, 3-微信支付, 4-支付宝支付',
  `MchCouponId` bigint(20) COMMENT '商户优惠券ID',
  `MemberCouponNo` varchar(32) COMMENT '会员优惠券核销码',
  `StoreId` bigint(20) COMMENT '门店ID',
  `StoreNo` varchar(20) COMMENT '门店编号',
  `StoreName` varchar(50) COMMENT '门店名称',
  `OperatorId` varchar(32) COMMENT '操作员ID',
  `OperatorName` varchar(32) COMMENT '操作员名称',
  `MemberId` bigint(20) COMMENT '会员ID',
  `MemberTel` varchar(32) COMMENT '会员手机号',
  `RuleId` bigint(20) COMMENT '充值赠送规则ID',
  `GivePoints` bigint(20) COMMENT '消费赠送积分',
  `RefundTotalAmount` bigint(20) NOT NULL DEFAULT '0' COMMENT '退款总金额',
  `TradeSuccTime` datetime DEFAULT NULL COMMENT '交易成功时间',
  `SettTaskStatus` tinyint(6) NOT NULL DEFAULT '-1' COMMENT '结算任务状态: -1无需执行结算任务, 0-待执行结算任务, 1-已完成结算任务',
  `IsvDeviceNo` varchar(200) COMMENT '硬件设备编号',
  `IndustryType` tinyint(6) DEFAULT NULL COMMENT '所属行业 1-餐饮 2-电商',
  `PostType` tinyint(6) DEFAULT NULL COMMENT '1-堂食 2-外卖',
  `StoreAreaId` bigint(20) DEFAULT NULL COMMENT '餐桌号ID',
  `AreaName` varchar(64) DEFAULT NULL COMMENT '区域名',
  `AppointmentTime` datetime DEFAULT NULL COMMENT '预约时间',
  `AddressId` bigint(20) DEFAULT NULL COMMENT '收货地址ID',
  `ProvinceCode` int DEFAULT NULL COMMENT '行政地区编号，省',
  `CityCode` int DEFAULT NULL COMMENT '行政地区编号， 市',
  `AreaCode` int DEFAULT NULL COMMENT '行政地区编号， 县',
  `AreaInfo` varchar(130) DEFAULT NULL COMMENT '省市县名称描述',
  `Address` varchar(128) DEFAULT NULL COMMENT '详细地址',
  `ReceiveTel` varchar(32) DEFAULT NULL COMMENT '收货人手机号',
  `ReceiveName` varchar(64) DEFAULT NULL COMMENT '收货人姓名',
  `PostId` bigint(20) DEFAULT NULL COMMENT '发货快递ID',
  `TransportNo` varchar(32) DEFAULT NULL COMMENT '运单号',
  `PostStatus` tinyint(6) DEFAULT NULL COMMENT '发货状态：0-待发货 1-已发货 2-确认收货 3-评价完成 4-退款审核 5-已退款 6-退款拒绝 7-退货审核 8-已退货 9-退货拒绝',
  `Evaluation` varchar(500) DEFAULT NULL COMMENT '订单评价',
  `EvaluationImage` varchar(1024) DEFAULT NULL COMMENT '订单评价图片',
  `DeleteFlag` tinyint(6) NOT NULL DEFAULT 0 COMMENT '会员端订单删除标识：0-未删除 1-删除',
  `ReturnReasonId` bigint(20) DEFAULT NULL COMMENT '退货原因ID',
  `ReturnDesc` varchar(256) DEFAULT NULL COMMENT '退货详情',
  `GoodsDesc` text COMMENT '商品信息，商品ID、对应数量和属性',
  `ImgPathMain` varchar(256) DEFAULT NULL COMMENT '主图图片路径',
  `Remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `PostPrice` bigint(20) DEFAULT NULL COMMENT '运费',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`TradeOrderId`),
  UNIQUE KEY `IDX_PayOrderId` (`PayOrderId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户交易表';


-- 商户退款表
DROP TABLE IF EXISTS t_mch_refund_order;
CREATE TABLE `t_mch_refund_order` (
  `MchRefundOrderId` varchar(30) NOT NULL COMMENT '商户退款订单号',
  `TradeOrderId` varchar(30) NOT NULL COMMENT '商户交易表',
  `ChannelRefundNo` varchar(30) COMMENT '渠道退款单号',
  `MchId` bigint(20) NOT NULL COMMENT '商户ID',
  `PayAmount` bigint(20) NOT NULL COMMENT '支付金额,单位分',
  `RefundAmount` bigint(20) NOT NULL COMMENT '本次退款金额,单位分',
  `Currency` varchar(3) NOT NULL DEFAULT 'cny' COMMENT '三位货币代码,人民币:cny',
  `Status` tinyint(6) NOT NULL DEFAULT '0' COMMENT '退款状态:0-订单生成,1-退款中,2-退款成功,3-退款失败',
  `RefundDesc` varchar(256) COMMENT '退款原因',
  `Remark` varchar(256) COMMENT '备注',
  `RefundSuccTime` datetime COMMENT '订单退款成功时间',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`MchRefundOrderId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户退款表';

-- 商户二维码表
DROP TABLE IF EXISTS t_mch_qr_code;
CREATE TABLE `t_mch_qr_code` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `MchId` bigint(20) NOT NULL COMMENT '商户ID',
  `Status` tinyint(6) NOT NULL DEFAULT '1' COMMENT '状态,启用(1),停止(0)',
  `CodeName` varchar(24) NOT NULL COMMENT '统一扫码名称',
  `PayAmount` bigint(20) COMMENT '二维码支付金额, 单位：分',
  `StoreId` bigint(20) COMMENT '绑定门店ID',
  `OperatorId` varchar(32) COMMENT '绑定操作员ID',
  `Remark` varchar(256) COMMENT '备注信息',
  `CreateOperatorId` varchar(32) NOT NULL COMMENT '创建人ID',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户统一扫码配置';

DROP TABLE IF EXISTS t_sys_message;
CREATE TABLE `t_sys_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `title` varchar(120) NOT NULL COMMENT '消息名称',
  `message` text COMMENT '消息内容',
  `status` tinyint DEFAULT 1 COMMENT '状态 0：隐藏, 1：显示',
  `createUserId` bigint(20) COMMENT '创建者ID',
  `isvStatus` tinyint DEFAULT 0 COMMENT '服务商消息状态：0：隐藏, 1：显示',
  `agentStatus` tinyint DEFAULT 0 COMMENT '代理商消息状态：0：隐藏, 1：显示',
  `mchStatus` tinyint DEFAULT 0 COMMENT '商户消息状态：0：隐藏, 1：显示',
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统消息表';

DROP TABLE IF EXISTS t_mgr_sys_user;
CREATE TABLE `t_mgr_sys_user` (
  `UserId` bigint NOT NULL AUTO_INCREMENT,
  `NickName` varchar(100) NOT NULL COMMENT '用户昵称, 仅做展示',
  `LoginUserName` varchar(50) NOT NULL COMMENT '用户登录名',
  `Email` varchar(32) COMMENT '登录邮箱',
  `Mobile` varchar(12) NOT NULL COMMENT '登录手机号',
  `LoginPassword` varchar(100) NOT NULL COMMENT '登录密码',
  `Status` tinyint NOT NULL DEFAULT 1 COMMENT '状态 0：禁用, 1：正常',
  `IsSuperAdmin` tinyint NOT NULL DEFAULT 0 COMMENT '是否超级管理员/是否主角色（商户） 0：否, 1：是',
  `BelongInfoId` bigint(20) NOT NULL COMMENT '所属角色ID 商户ID / 代理商ID / 0(平台)',
  `BelongInfoType` tinyint(6) NOT NULL COMMENT '所属角色类型:1-商户 2-代理商 3-平台 4-服务商',
  `Avatar` varchar(256) COMMENT '头像路径',
  `Sex` tinyint(6) COMMENT '性别:0-未知 1-男 2-女',
  `StoreId` bigint(20) COMMENT '所属门店ID',
  `WorkStatus` tinyint not null default '0' COMMENT '0-未工作, 1-工作中',
  `WorkStartTime` datetime COMMENT '工作开始时间',
  `CreateUserId` bigint(20) COMMENT '创建者ID',
  `LastLoginTime` datetime COMMENT '最后一次登录时间',
  `LastLoginIp` varchar(32) COMMENT '最后一次登录IP',
  `LastPasswordResetTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后一次重置密码时间',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`UserId`),
  UNIQUE KEY `IDX_UserName` (`BelongInfoType`, `LoginUserName`),
  UNIQUE KEY `IDX_Mobile` (`BelongInfoType`,`Mobile`),
  UNIQUE KEY `IDX_Email` (`BelongInfoType`,`Email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统用户表';

DROP TABLE IF EXISTS t_mgr_sys_role;
CREATE TABLE `t_mgr_sys_role` (
  `roleId` bigint NOT NULL AUTO_INCREMENT,
  `roleName` varchar(100) COMMENT '角色名称',
  `createUserId` bigint(20) COMMENT '创建者ID',
  `BelongInfoId` bigint(20) NOT NULL COMMENT '所属角色ID 商户ID / 代理商ID / 0(平台)',
  `BelongInfoType` tinyint(6) NOT NULL COMMENT '所属角色类型:1-商户 2-代理商 3-平台',
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`roleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

DROP TABLE IF EXISTS t_mgr_sys_user_role;
CREATE TABLE `t_mgr_sys_user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `userId` bigint COMMENT '用户ID',
  `roleId` bigint COMMENT '角色ID',
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户与角色对应关系表';

DROP TABLE IF EXISTS t_mgr_sys_resource;
CREATE TABLE `t_mgr_sys_resource` (
  `resourceId` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COMMENT '资源名称',
  `title` varchar(50) COMMENT '资源标题',
  `jump` varchar(200) COMMENT '跳转URL',
  `permName` varchar(500) COMMENT '授权名,以ROLE_开头,如ROLE_MCH',
  `permUrl` varchar(500) COMMENT '授权URL,如:mch_info/**',
  `type` tinyint DEFAULT 1 COMMENT '资源类型 1：菜单, 2：按钮',
  `system` tinyint DEFAULT 1 COMMENT '所属系统: 1-商户 2-代理商 3-平台 4-服务商',
  `icon` varchar(50) COMMENT '菜单图标',
  `orderNum` int COMMENT '排序',
  `parentId` bigint DEFAULT 0 COMMENT '父资源ID，一级为0',
  `status` tinyint DEFAULT 1 COMMENT '状态 0：禁用, 1：正常',
  `property` varchar(50) DEFAULT '' COMMENT '属性,为空都可见.否则对应商户类型,如1 表示平台账户可见, 1,2 表示平台账户和私有账户都可见',
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`resourceId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资源表';

DROP TABLE IF EXISTS t_mgr_sys_permission;
CREATE TABLE `t_mgr_sys_permission` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `roleId` bigint COMMENT '角色ID',
  `resourceId` bigint COMMENT '资源ID',
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='授权表';

###  对账相关表
DROP TABLE IF EXISTS t_check_batch;
CREATE TABLE `t_check_batch` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `batchNo` varchar(32) NOT NULL COMMENT '对账批次号',
  `billDate` date DEFAULT NULL COMMENT '账单时间(账单交易发生时间)',
  `billType` tinyint(6) NOT NULL COMMENT '账单类型(默认全部是交易成功)',
  `handleStatus` tinyint(6) NOT NULL DEFAULT 1 COMMENT '类型:0-未处理,1-已处理',
  `bankType` varchar(30) NOT NULL COMMENT '银行类型,wxpay:微信,alipay:支付宝',
  `channelMchId` varchar(32) DEFAULT NULL COMMENT '渠道商户ID',
  `mistakeCount` int DEFAULT NULL COMMENT '所有差错总单数',
  `unhandleMistakeCount` int DEFAULT NULL COMMENT '待处理的差错总单数',
  `tradeCount` int DEFAULT NULL COMMENT '平台总交易单数',
  `bankTradeCount` int DEFAULT NULL COMMENT '银行总交易单数',
  `tradeAmount` bigint(20) DEFAULT NULL COMMENT '平台交易总金额',
  `bankTradeAmount` bigint(20) DEFAULT NULL COMMENT '银行交易总金额',
  `refundAmount` bigint(20) DEFAULT NULL COMMENT '平台退款总金额',
  `bankRefundAmount` bigint(20) DEFAULT NULL COMMENT '银行退款总金额',
  `fee` bigint(20) DEFAULT NULL COMMENT '平台总手续费',
  `bankFee` bigint(20) DEFAULT NULL COMMENT '银行总手续费',
  `orgCheckFilePath` varchar(128) DEFAULT NULL COMMENT '原始对账文件存放地址',
  `releaseCheckFilePath` varchar(128) DEFAULT NULL COMMENT '解析后文件存放地址',
  `releaseStatus` tinyint(6) NOT NULL DEFAULT 1 COMMENT '解析状态:0-失败,1-成功',
  `checkFailMsg` varchar(128) DEFAULT NULL COMMENT '解析检查失败的描述信息',
  `bankErrMsg` varchar(128) DEFAULT NULL COMMENT '银行返回的错误信息',
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `IDX_batchNo` (`batchNo`)
) ENGINE=InnoDB AUTO_INCREMENT=0000000 DEFAULT CHARSET=utf8mb4 COMMENT='对账批次表';

DROP TABLE IF EXISTS t_check_mistake;
CREATE TABLE `t_check_mistake` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `batchNo` varchar(32) NOT NULL COMMENT '对账批次号',
  `billDate` date DEFAULT NULL COMMENT '账单时间(账单交易发生时间)',
  `billType` tinyint(6) DEFAULT NULL COMMENT '账单类型(默认全部是交易成功)',
  `bankType` varchar(30) DEFAULT NULL COMMENT '银行类型,wxpay:微信,alipay:支付宝',
  `channelMchId` varchar(32) DEFAULT NULL COMMENT '渠道商户ID',
  `orderTime` datetime DEFAULT NULL COMMENT '下单时间',
  `mchId` bigint(20) DEFAULT NULL COMMENT '商户ID',
  `mchName` varchar(30) DEFAULT NULL DEFAULT '' COMMENT '商户名称',
  `mchOrderNo` varchar(30) DEFAULT NULL COMMENT '商户订单号',
  `tradeTime` datetime DEFAULT NULL COMMENT '平台交易时间',
  `orderId` varchar(24) DEFAULT NULL COMMENT '平台订单ID',
  `orderAmount` bigint(20) DEFAULT NULL COMMENT '平台交易金额',
  `refundAmount` bigint(20) DEFAULT NULL COMMENT '平台退款金额',
  `orderStatus` tinyint(6) DEFAULT NULL DEFAULT 0 COMMENT '平台订单状态',
  `fee` bigint(20) DEFAULT NULL COMMENT '平台手续费',
  `bankTradeTime` datetime DEFAULT NULL COMMENT '银行交易时间',
  `bankOrderNo` varchar(40) DEFAULT NULL COMMENT '银行订单号',
  `bankOrderStatus` varchar(30) DEFAULT NULL DEFAULT 0 COMMENT '银行订单状态',
  `bankAmount` bigint(20) DEFAULT NULL COMMENT '银行交易金额',
  `bankRefundAmount` bigint(20) DEFAULT NULL COMMENT '银行退款金额',
  `bankFee` bigint(20) DEFAULT NULL COMMENT '银行手续费',
  `errType` tinyint(6) DEFAULT NULL COMMENT '差错类型:1',
  `handleStatus` tinyint(6) DEFAULT NULL DEFAULT 0 COMMENT '类型:0-未处理,1-已处理',
  `handleValue` varchar(30) DEFAULT NULL COMMENT '处理结果',
  `handleRemark` varchar(128) DEFAULT NULL COMMENT '处理备注',
  `operatorName` varchar(30) DEFAULT NULL COMMENT '操作人姓名',
  `operatorUserId` varchar(30) DEFAULT NULL COMMENT '操作人用户ID',
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='对账差错表';

DROP TABLE IF EXISTS t_check_mistake_pool;
CREATE TABLE `t_check_mistake_pool` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `productame` varchar(50) DEFAULT NULL COMMENT '商品名称',
  `mchOrderNo` varchar(30) NOT NULL COMMENT '商户订单号',
  `orderId` varchar(24) NOT NULL COMMENT '平台订单ID',
  `bankOrderNo` varchar(40) DEFAULT NULL COMMENT '银行订单号',
  `orderAmount` bigint(20) DEFAULT NULL COMMENT '订单金额',
  `platIncome` bigint(20) DEFAULT NULL COMMENT '平台收入',
  `feeate` decimal(20,6) DEFAULT NULL COMMENT '平台费率',
  `platCost` bigint(20) DEFAULT NULL COMMENT '平台成本',
  `platProfit` bigint(20) DEFAULT NULL COMMENT '平台利润',
  `status` tinyint(6) DEFAULT NULL DEFAULT 0 COMMENT '状态',
  `channelId` varchar(24) DEFAULT NULL COMMENT '渠道ID',
  `channelType` varchar(30) DEFAULT NULL COMMENT '渠道类型,WX:微信,ALIPAY:支付宝',
  `paySuccessTime` datetime DEFAULT NULL COMMENT '支付成功时间',
  `completeTime` datetime DEFAULT NULL COMMENT '完成时间',
  `isRefund` tinyint(6) NOT NULL DEFAULT 0 COMMENT '类型:0-否,1-是',
  `refundTimes` int DEFAULT NULL COMMENT '退款次数',
  `successRefundAmount` bigint(20) DEFAULT NULL COMMENT '成功退款总金额',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注',
  `batchNo` varchar(32) NOT NULL COMMENT '对账批次号',
  `billDate` date DEFAULT NULL COMMENT '账单时间(账单交易发生时间)',
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='对账差错缓冲池表';

-- 对账表
drop table if exists `t_bill` ;
CREATE TABLE `t_bill` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `InfoId` bigint(20) NOT NULL COMMENT '代理商或商户ID',
  `InfoType` tinyint(6) NOT NULL COMMENT '账号类型:1-商户 2-代理商 3-平台',
  `InfoName` varchar(30) NOT NULL COMMENT '名称',
  `mchType` tinyint(6) COMMENT '类型:1-平台账户,2-私有账户',
  `billType` tinyint(6) NOT NULL COMMENT '账单类型:1-支付对账单,2-代付对账单',
  `billDate` date DEFAULT NULL COMMENT '账单时间',
  `Status` tinyint(6) NOT NULL DEFAULT '0' COMMENT '账单状态:0-未生成,1--已生成',
  `billPath` varchar(128) DEFAULT NULL COMMENT '账单地址',
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `IDX_bill_BillDate` (`InfoId`, `InfoType`, `billDate`,`billType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='对账单';

### 用户账户表
DROP TABLE IF EXISTS t_user_account;
CREATE TABLE `t_user_account` (
  `UserId` varchar(64) NOT NULL COMMENT '用户ID',
  `MchId` bigint(20) NOT NULL COMMENT '商户ID',
  `Balance` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '账户余额',
  `CheckSum` varchar(32) NOT NULL COMMENT '用户账户校验码',
  `UpdateTimeJava` bigint(20) NOT NULL COMMENT '账户更新时间',
  `TotalRollIn` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户累计转入金额',
  `TotalRollOut` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户累计转出金额',
  `UseableBalance` bigint(20) NOT NULL DEFAULT '0' COMMENT '账户可用余额',
  `UseableCheckSum` varchar(32) NOT NULL COMMENT '用户可用账户校验码',
  `UseableUpdateTimeJava` bigint(20) NOT NULL COMMENT '可用账户更新时间',
  `UseableRollIn` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户累计转入可用金额',
  `UseableRollOut` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户累计转出可用金额',
  `State` smallint(6) NOT NULL DEFAULT '1' COMMENT '状态.0表示账户冻结.1表示正常',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`UserId`,`MchId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户账户表';

DROP TABLE IF EXISTS t_user_account_log;
CREATE TABLE `t_user_account_log` (
  `LogId` bigint(20) NOT NULL AUTO_INCREMENT,
  `UserId` varchar(64) NOT NULL COMMENT '用户ID',
  `MchId` bigint(20) NOT NULL COMMENT '商户ID',
  `ChangeAmount` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '变动金额',
  `Type` smallint(6) NOT NULL DEFAULT '1' COMMENT '账户变动类型.0表示转入.1表示转出.2表示初始化',
  `AccountType` smallint(6) NOT NULL DEFAULT '1' COMMENT '账户类型 0:总账户 1:可用账户',
  `OldBalance` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '变动前账户余额',
  `OldUpdateTimeJava` bigint(20) NOT NULL COMMENT '变动前账户更新时间',
  `OldCheckSum` varchar(32) NOT NULL COMMENT '变动前用户账户校验码',
  `NewBalance` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '变动后账户余额',
  `NewUpdateTimeJava` bigint(20) NOT NULL COMMENT '变动后账户更新时间',
  `NewCheckSum` varchar(32) NOT NULL COMMENT '变动后用户账户校验码',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`LogId`),
  KEY `IDX_UserId` (`UserId`),
  KEY `IDX_UserId_MchId` (`UserId`,`MchId`)
) ENGINE=InnoDB AUTO_INCREMENT=121 DEFAULT CHARSET=utf8mb4 COMMENT='用户账户日志表';

DROP TABLE IF EXISTS t_user_account_change_detail;
CREATE TABLE `t_user_account_change_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `UserId` varchar(64) NOT NULL COMMENT '用户ID',
  `MchId` bigint(20) NOT NULL COMMENT '商户ID',
  `ChangeDay` int(11) NOT NULL COMMENT '收入日期，格式：yyyymmdd',
  `ChangeType` smallint(6) NOT NULL DEFAULT '0' COMMENT '账户变动类型.0表示转入.1表示转出',
  `AccountType` smallint(6) NOT NULL DEFAULT '1' COMMENT '变动账户类型 0:账户 1:可用账户',
  `ChangeAmount` bigint(20) NOT NULL COMMENT '账户变动金额',
  `ChangeLogId` bigint(20) DEFAULT NULL COMMENT '变动日志记录ID',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `IDX_UserId_MchId` (`UserId`,`MchId`),
  KEY `IDX_UserId_MchId_Day` (`UserId`,`MchId`,`ChangeDay`),
  KEY `IDX_UserId_MchId_Type` (`UserId`,`MchId`,`ChangeType`),
  KEY `IDX_UserId_MchId_Day_Type` (`UserId`,`MchId`,`ChangeDay`,`ChangeType`)
) ENGINE=InnoDB AUTO_INCREMENT=89 DEFAULT CHARSET=utf8mb4 COMMENT='用户账户变动记录表';

### 代理商相关表
DROP TABLE IF EXISTS t_agent_info;
CREATE TABLE `t_agent_info` (
  `AgentId` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '代理商ID',  -- 代理商基本信息
  `AgentName` varchar(30) NOT NULL COMMENT '代理商名称',

  `LoginUserName` varchar(50) NOT NULL COMMENT '登录用户名',   -- 代理商联系人信息及地址信息
  `Mobile` varchar(12) NOT NULL COMMENT '登录手机号（联系人手机号）',
  `Email` varchar(32) NOT NULL COMMENT '登录邮箱（联系人邮箱）',
  `RealName` varchar(30) COMMENT '联系人真实姓名',
  `IdCard` varchar(32) DEFAULT NULL COMMENT '联系人身份证号',
  `Qq` varchar(32) DEFAULT NULL COMMENT '联系人QQ号码',
  `Address` varchar(128) DEFAULT NULL COMMENT '代理商通讯地址',

  `ProfitRate` decimal(5,2) DEFAULT 0 COMMENT '返佣百分比值,支持两位小数, 最高100% ',
  `Status` tinyint(6) NOT NULL COMMENT '状态: -2审核不通过, -1待审核, 0-暂停使用, 1-正常 ',
  `IsvId` bigint(20) NOT NULL COMMENT '服务商ID' ,
  `Pid` bigint(20) DEFAULT 0 COMMENT '上级代理商ID' ,
  `AgentLevel` int NOT NULL DEFAULT '1' COMMENT '代理商等级：运营平台新增为一级代理 以此类推',
  `AllowAddSubAgent` tinyint(6) NOT NULL DEFAULT '0' COMMENT '是否可以发展子代理商 0-不允许, 1-允许' ,
  `AllowAddMch` tinyint(6) NOT NULL DEFAULT '0' COMMENT '是否可以发展商户 0-不允许, 1-允许',
  `Remark` varchar(128) DEFAULT NULL COMMENT '备注',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`AgentId`),
  UNIQUE KEY `Uni_LoginUserName` (`LoginUserName`),
  UNIQUE KEY `Uni_Mobile` (`Mobile`),
  UNIQUE KEY `Uni_Email` (`Email`)
) ENGINE=InnoDB AUTO_INCREMENT=10000000 DEFAULT CHARSET=utf8mb4 COMMENT='代理商信息表';

### 支付通道重构相关表
DROP TABLE IF EXISTS t_pay_type;
CREATE TABLE `t_pay_type` (
  `payTypeCode` varchar(32) NOT NULL COMMENT '支付类型编码',
  `payTypeName` varchar(64) NOT NULL COMMENT '支付类型名称',
  `Type` varchar(2) NOT NULL DEFAULT '1' COMMENT '类型,1:支付,2:代付',
  `Status` tinyint(6) NOT NULL DEFAULT '1' COMMENT '状态,0-关闭,1-开启',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`payTypeCode`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COMMENT='支付类型表';

DROP TABLE IF EXISTS t_pay_interface_type;
CREATE TABLE `t_pay_interface_type` (
  `IfTypeCode` varchar(30) NOT NULL COMMENT '接口类型代码',
  `IfTypeName` varchar(30) NOT NULL COMMENT '接口类型名称',
  `Status` tinyint(6) NOT NULL DEFAULT '1' COMMENT '状态,0-关闭,1-开启',
  `IsvParam` varchar(4096) NOT NULL COMMENT 'ISV接口配置定义描述,json字符串',
  `MchParam` varchar(4096) NOT NULL COMMENT '商户接口配置定义描述,json字符串',
  `PrivateMchParam` varchar(4096) NOT NULL COMMENT '私有商户接口配置定义描述,json字符串',
  `Remark` varchar(128) DEFAULT NULL COMMENT '备注',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`IfTypeCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付接口类型表';

DROP TABLE IF EXISTS t_pay_interface;
CREATE TABLE `t_pay_interface` (
  `IfCode` varchar(30) NOT NULL COMMENT '接口代码',
  `IfName` varchar(30) NOT NULL COMMENT '接口名称',
  `IfTypeCode` varchar(30) NOT NULL COMMENT '接口类型代码',
  `PayType` varchar(2) NOT NULL COMMENT '支付类型',
  `Scene` tinyint(6) DEFAULT NULL COMMENT '应用场景,1:移动APP,2:移动网页,3:PC网页,4:微信公众平台,5:手机扫码',
  `Status` tinyint(6) NOT NULL DEFAULT '1' COMMENT '接口状态,0-关闭,1-开启',
  `Param` varchar(4096) DEFAULT NULL COMMENT '配置参数,json字符串',
  `Remark` varchar(128) DEFAULT NULL COMMENT '备注',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `Extra` varchar(1024) DEFAULT NULL COMMENT '扩展参数',
  PRIMARY KEY (`IfCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付接口表';

DROP TABLE IF EXISTS t_pay_product;
CREATE TABLE `t_pay_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '产品ID',
  `ProductName` varchar(64) NOT NULL COMMENT '产品名称',
  `PayType` varchar(2) NOT NULL COMMENT '支付类型',
  `AgentRate` decimal(20,6) DEFAULT NULL COMMENT '代理商费率,百分比',
  `MchRate` decimal(20,6) DEFAULT NULL COMMENT '商户费率,百分比',
  `IfMode` tinyint(6) NOT NULL DEFAULT 1 COMMENT '接口模式,1-单独,2-轮询',
  `PayPassageId` int(11) DEFAULT NULL COMMENT '支付通道ID',
  `PayPassageAccountId` int(11) DEFAULT NULL COMMENT '支付通道账户ID',
  `PollParam` varchar(4096) DEFAULT NULL COMMENT '轮询配置参数,json字符串',
  `Status` tinyint(6) NOT NULL DEFAULT '1' COMMENT '状态,0-关闭,1-开启',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `ProductType` tinyint(6) DEFAULT 1 COMMENT '产品类型:1-收款,2-充值',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=8000 DEFAULT CHARSET=utf8mb4 COMMENT='支付产品表';

DROP TABLE IF EXISTS t_agentpay_passage;
CREATE TABLE `t_agentpay_passage` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '代付通道ID',
  `BelongInfoId` bigint(20) NOT NULL COMMENT '所属角色ID 商户ID / 0(平台)',
  `BelongInfoType` tinyint(6) NOT NULL COMMENT '所属角色类型:1-商户 2-代理商 3-平台',
  `PassageName` varchar(30) NOT NULL COMMENT '通道名称',
  `IfCode` varchar(30) NOT NULL COMMENT '接口代码',
  `IfTypeCode` varchar(30) NOT NULL COMMENT '接口类型代码',
  `Status` tinyint(6) NOT NULL DEFAULT '1' COMMENT '通道状态,0-关闭,1-开启',
  `FeeType` tinyint(6) NOT NULL DEFAULT 1 COMMENT '手续费类型,1-百分比收费,2-固定收费',
  `FeeRate` decimal(20,6) DEFAULT NULL COMMENT '手续费费率,百分比',
  `FeeEvery` bigint(20) DEFAULT NULL COMMENT '手续费每笔金额,单位分',
  `MaxDayAmount` bigint(20) DEFAULT NULL COMMENT '当天交易金额,单位分',
  `TradeStartTime` varchar(20) DEFAULT NULL COMMENT '交易开始时间',
  `TradeEndTime` varchar(20) DEFAULT NULL COMMENT '交易结束时间',
  `RiskStatus` tinyint(6) NOT NULL DEFAULT '0' COMMENT '风控状态,0-关闭,1-开启',
  `IsDefault` tinyint(6) NOT NULL DEFAULT '0' COMMENT '是否默认,0-否,1-是',
  `Remark` varchar(128) DEFAULT NULL COMMENT '备注',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2000 DEFAULT CHARSET=utf8mb4 COMMENT='代付通道表';

DROP TABLE IF EXISTS t_agentpay_passage_account;
CREATE TABLE `t_agentpay_passage_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '账户ID',
  `AccountName` varchar(30) NOT NULL COMMENT '账户名称',
  `AgentpayPassageId` int(11) NOT NULL COMMENT '代付通道ID',
  `IfCode` varchar(30) NOT NULL COMMENT '接口代码',
  `IfTypeCode` varchar(30) NOT NULL COMMENT '接口类型代码',
  `Param` varchar(4096) NOT NULL COMMENT '账户配置参数,json字符串',
  `Status` tinyint(6) NOT NULL DEFAULT '1' COMMENT '账户状态,0-停止,1-开启',
  `PollWeight` INT NOT NULL DEFAULT '1' COMMENT '轮询权重',
  `PassageMchId` varchar(64) NOT NULL COMMENT '通道商户ID',
  `RiskMode` tinyint(6) NOT NULL DEFAULT '1' COMMENT '风控模式,1-继承,2-自定义',
  `MaxDayAmount` bigint(20) DEFAULT NULL COMMENT '当天交易金额,单位分',
  `TradeStartTime` varchar(20) DEFAULT NULL COMMENT '交易开始时间',
  `TradeEndTime` varchar(20) DEFAULT NULL COMMENT '交易结束时间',
  `RiskStatus` tinyint(6) NOT NULL DEFAULT '0' COMMENT '风控状态,0-关闭,1-开启',
  `Remark` varchar(128) DEFAULT NULL COMMENT '备注',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `IDX_AgentpayPassageId_PassageMchId` (`AgentpayPassageId`, `PassageMchId`)
) ENGINE=InnoDB AUTO_INCREMENT=2000100 DEFAULT CHARSET=utf8mb4 COMMENT='代付通道账户表';

### 结算记录表
DROP TABLE IF EXISTS t_sett_record;
CREATE TABLE `t_sett_record` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `InfoId` bigint(20) NOT NULL COMMENT '代理商或商户ID',
  `InfoType` tinyint(6) NOT NULL COMMENT '结算商类型:1-商户 2-代理商 3-平台',
  `SettType` tinyint(6) NOT NULL COMMENT '结算发起类型:1-手工结算,2-自动结算',
  `SettDate` date NOT NULL COMMENT '结算日期',
  `SettAmount` bigint(20) NOT NULL COMMENT '申请结算金额',
  `SettFee` bigint(20) NOT NULL COMMENT '结算手续费',
  `RemitAmount` bigint(20) NOT NULL COMMENT '结算打款金额',
  `AccountAttr` tinyint(6) DEFAULT 0 COMMENT '账户属性:0-对私,1-对公,默认对私',
  `AccountType` tinyint(6) DEFAULT 1 COMMENT '账户类型:1-银行卡转账,2-微信转账,3-支付宝转账',
  `BankName` varchar(128) DEFAULT NULL COMMENT '开户行名称',
  `BankNetName` varchar(128) DEFAULT NULL COMMENT '开户网点名称',
  `AccountName` varchar(64) DEFAULT NULL COMMENT '账户名',
  `AccountNo` varchar(64) DEFAULT NULL COMMENT '账户号',
  `Province` varchar(32) DEFAULT NULL COMMENT '开户行所在省',
  `City` varchar(32) DEFAULT NULL COMMENT '开户行所在市',
  `SettStatus` tinyint(6) NOT NULL COMMENT '结算状态:1-等待审核,2-已审核,3-审核不通过,4-打款中,5-打款成功,6-打款失败',
  `Remark` varchar(128) DEFAULT NULL COMMENT '备注',
  `RemitRemark` varchar(128) DEFAULT NULL COMMENT '打款备注',
  `OperatorId` bigint(20) DEFAULT NULL COMMENT '操作用户ID',
  `SettOrderId` varchar(30) DEFAULT NULL COMMENT '结算单号',
  `TransOrderId` varchar(30) DEFAULT NULL COMMENT '转账订单号',
  `TransMsg` varchar(128) DEFAULT NULL COMMENT '转账返回消息',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `IDX_SettOrderId` (`SettOrderId`),
  UNIQUE KEY `IDX_TransOrderId` (`TransOrderId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='结算记录表';

### 银行卡Bin信息
DROP TABLE IF EXISTS t_bank_card_bin;
CREATE TABLE `t_bank_card_bin` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `CardBin` varchar(8) NOT NULL COMMENT '卡bin',
  `BankName` varchar(50) DEFAULT NULL COMMENT '银行名称',
  `BankCode` varchar(10) DEFAULT NULL COMMENT '银行编码',
  `CardName` varchar(50) DEFAULT NULL COMMENT '卡名',
  `CardType` varchar(15) DEFAULT NULL COMMENT '银行卡类型',
  `CardLength` int(11) DEFAULT NULL COMMENT '卡号长度',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `IDX_CardBin` (`CardBin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='银行卡Bin信息';

### 系统配置表
DROP TABLE IF EXISTS t_sys_config;
CREATE TABLE `t_sys_config` (
  `code` varchar(64) NOT NULL COMMENT '配置主键',
  `name` varchar(64) NOT NULL COMMENT '配置名称',
  `value` varchar(4096) NOT NULL COMMENT '配置内容',
  `remark` varchar(128) NOT NULL COMMENT '配置备注',
  `type` varchar(128) NOT NULL COMMENT '配置类别',
  `orderNum` float DEFAULT 1.0 COMMENT '排序值',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';


### 订单分润明细表
drop table if exists `t_order_profit_detail`;
CREATE TABLE `t_order_profit_detail` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `BizType` tinyint(6) NOT NULL COMMENT '业务类型,1-支付, 2-充值, 3-退款',
  `BizOrderId` varchar(30) NOT NULL COMMENT '业务关联单号',
  `BizOrderPayAmount` bigint(20) NOT NULL COMMENT '业务订单实际支付金额',
  `BizOrderCreateDate` timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '业务订单创建日期，便于查询统计',
  `ProductId` int NOT NULL COMMENT '产品ID',
  `ReferInfoType` tinyint(6) NOT NULL COMMENT '计算订单分润参考对象：1-商户 2-代理商 3-平台 4-服务商',
  `ReferInfoId` bigint(20) NOT NULL COMMENT '订单分润参考对象关联ID',
  `IsvId` bigint(20) NOT NULL COMMENT '所属服务商ID',
  `IncomeAmount` bigint(20) NOT NULL COMMENT '收入金额,单位分; 商户入账金额|代理商返佣|服务商返佣',
  `FeeAmount` bigint(20) NOT NULL COMMENT '支出金额,单位分; 商户费率费用|代理商退款退还返佣|服务商退款退还返佣',
  `FeeRateSnapshot` varchar(30) DEFAULT NULL COMMENT '费率/费用快照, 商家费率|代理商返佣比例|服务商基础费率',
  `SettTaskStatus` tinyint(6) NOT NULL DEFAULT '-1' COMMENT '结算任务状态: -1无需执行结算任务, 0-待执行结算任务, 1-已完成结算任务',
  `SettTaskId` bigint(20) COMMENT '结算任务ID',
  `SettTime` datetime COMMENT '结算时间',
  `CreatedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '分润明细创建时间',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `IDX_Order_Profit_Detail_No` (`BizType`, `BizOrderId`, `ReferInfoType`, `ReferInfoId`)
) ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8mb4 COMMENT='订单分润明细表';

-- 系统短信验证码表
DROP TABLE IF EXISTS t_mgr_msgcode;
CREATE TABLE `t_mgr_msgcode` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `PhoneNo` varchar(30) NOT NULL DEFAULT '' COMMENT '手机号',
  `Code` varchar(30) NOT NULL DEFAULT '' COMMENT '码值',
  `bizType` tinyint(6) NOT NULL DEFAULT 0 COMMENT '发送类型:20-商户注册, 21-商户登录 ,31-代理商登录',
  `ExpTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '失效时间',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8mb4 COMMENT='系统短信验证码表';

-- 接口类型配置参数模板
DROP TABLE IF EXISTS t_pay_interface_type_template;
CREATE TABLE `t_pay_interface_type_template` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `TemplateName` varchar(64) NOT NULL COMMENT '模板名称',
  `IfTypeCode` varchar(30) NOT NULL COMMENT '接口类型代码',
  `Status` tinyint(6) NOT NULL DEFAULT '1' COMMENT '状态,0-关闭,1-开启',
  `PassageMchIdTemplate` varchar(64) NOT NULL COMMENT '通道商户ID模板 不影响实际配置参数',
  `ParamTemplate` varchar(8192) DEFAULT NULL COMMENT '账户配置参数模板 不影响实际配置参数,json字符串',
  `Remark` varchar(128) DEFAULT NULL COMMENT '备注',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8mb4 COMMENT='支付接口类型参数模板表';

-- 账户余额表
DROP TABLE IF EXISTS t_account_balance;
CREATE TABLE `t_account_balance` (
	`Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `InfoId` bigint(20) NOT NULL COMMENT '代理商或商户ID',
  `InfoType` tinyint(6) NOT NULL COMMENT '类型:1-商户 2-代理商 3-平台',
  `InfoName` varchar(30) NOT NULL DEFAULT '' COMMENT '名称',
	`AccountType` tinyint(6) NOT NULL COMMENT '账户金额类型 1-账户余额 2-代付余额 3-保证金',
  `Amount` bigint(20) NOT NULL COMMENT '金额 单位:分',
  `UnAmount` bigint(20) NOT NULL COMMENT '不可用金额 单位:分（业务中间状态时产生的金额）',
	`FrozenAmount` bigint(20) DEFAULT 0 COMMENT '平台冻结金额 单位:分',
	`SettAmount` bigint(20) DEFAULT 0 COMMENT '可提现金额 单位:分',
	`TotalAddAmount` bigint(20) DEFAULT 0 COMMENT '针对Amount字段的累计增加金额（用于统计，仅支持累加操作，不支持扣减修改） 单位:分',
	`TotalSubAmount` bigint(20) DEFAULT 0 COMMENT '针对Amount字段的累计减少金额（用于统计，仅支持累加操作，不支持扣减修改） 单位:分',
  `Status` tinyint(6) NOT NULL DEFAULT '1' COMMENT '账户状态,1-可用,0-停止使用',
  `SafeKey` varchar(128) NOT NULL COMMENT '数据安全保护秘钥 & 数据版本号',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `IDX_Account_Balance_No` (`InfoId`, `InfoType`, `AccountType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账户/余额表';

-- 资金账户流水表
drop table if exists `t_account_history`;
CREATE TABLE `t_account_history` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `InfoId` bigint(20) NOT NULL COMMENT '代理商或商户ID',
  `InfoType` tinyint(6) NOT NULL COMMENT '类型: 1-商户 2-代理商 3-平台',
  `ChangeAmount` bigint(20) NOT NULL COMMENT '变动金额',
  `Balance` bigint(20) NOT NULL COMMENT '变更前账户余额',
  `AfterBalance` bigint(20) NOT NULL COMMENT '变更后账户余额',
  `BizOrderId` varchar(64) COMMENT '平台订单号',
  `BizChannelOrderNo` varchar(32) COMMENT '渠道订单号',
  `BizOrderAmount` bigint(20) COMMENT '订单金额',
  `BizOrderFee` bigint(20) COMMENT '手续费',
  `FundDirection` tinyint(6) NOT NULL COMMENT '资金变动方向,1-加款,2-减款',
  `BizType` tinyint(6) NOT NULL COMMENT '业务类型,1-支付,2-提现,3-调账,4-充值,5-差错处理,6-代付 7-分润',
  `BizItem` varchar(2) COMMENT '业务类目:10-余额,11-代付余额,12-冻结金额,13-保证金,20-支付,21-代付,22-线下充值,23-线上充值',
  `ChangeAccountType` tinyint(6) NOT NULL COMMENT '变更账户类型 1-账户余额 2-代付余额 3-保证金',
  `Remark` varchar(128) DEFAULT NULL COMMENT '备注',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资金账户流水表';

-- 收费标准表
DROP TABLE IF EXISTS t_fee_scale;
CREATE TABLE `t_fee_scale` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `InfoId` bigint(20) NOT NULL COMMENT '代理商或商户ID',
  `InfoType` tinyint(6) NOT NULL COMMENT '类型: 1-商户 2-代理商 3-平台 4-服务商',
  `ProductType` tinyint(6) NOT NULL COMMENT '收费产品类型 1-支付 2-代付 3-线下充值',
  `refProductId` int(11) NOT NULL COMMENT '关联来源产品ID, 支付类关联产品表；代付类关联代付通道表',
  `FeeScale` tinyint(6) NOT NULL COMMENT '收费标准 1-每笔 2-年 3-月 4-季度',
  `FeeScaleStep` int(11) COMMENT '收费标准梯度',
  `SingleFeeType`  tinyint(6) COMMENT '按每笔收费标准的收费方式 1-按固定金额 2-按比例',
  `Fee` decimal(20,2) DEFAULT NULL COMMENT '收费金额或比例 金额单位：分， 比例单位：%',
  `ExtConfig` varchar(4096) DEFAULT NULL COMMENT '扩展配置 可设置获取支付子账户的获取方式等参数' ,
  `Status` tinyint(6) NOT NULL DEFAULT '1' COMMENT '状态,0-关闭,1-开启',
  `IsDefault` tinyint(6) NOT NULL DEFAULT '0' COMMENT '是否默认,0-否,1-是',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `IDX_Fee_Scale_Product` (`infoType`, `infoId`, `ProductType`, `refProductId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收费标准表';

-- 计费风控表   t_fee_scale的扩展表
DROP TABLE IF EXISTS t_fee_risk_config;
CREATE TABLE `t_fee_risk_config` (
  `FeeScaleId` int(11) NOT NULL COMMENT 'FeeScale ID',
  `MaxDayAmount` bigint(20) COMMENT '日累计金额：单位分',
  `MaxEveryAmount` bigint(20) COMMENT '单笔最大限额,单位分',
  `MinEveryAmount` bigint(20) COMMENT '单笔最小限额,单位分',
  `TradeStartTime` varchar(20) COMMENT '交易开始时间 格式: HH:mm:ss',
  `TradeEndTime` varchar(20) COMMENT '交易结束时间 格式: HH:mm:ss',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`FeeScaleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='计费风控表';

-- 系统操作日志表
DROP TABLE IF EXISTS t_mgr_sys_log;
CREATE TABLE `t_mgr_sys_log` (
	 `Id` INT (11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
	 `UserId` BIGINT NOT NULL COMMENT '用户ID',
	 `UserName` VARCHAR (50) NOT NULL COMMENT '用户名',
   `UserIp` VARCHAR (64) NOT NULL DEFAULT '' COMMENT '用户IP',
   `System` tinyint(6) NOT NULL COMMENT '系统: 1-商户 2-代理商 3-平台 4-服务商',
	 `MethodName` VARCHAR (128) NOT NULL DEFAULT '' COMMENT '方法名',
	 `MethodRemark` VARCHAR (128) NOT NULL DEFAULT '' COMMENT '方法描述',
	 `OptReqParam` VARCHAR (512) NOT NULL DEFAULT '' COMMENT '操作请求参数',
	 `OptResInfo` VARCHAR (512) NOT NULL DEFAULT '' COMMENT '操作响应结果',
	 `CreateTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	 `UpdateTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	 PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '系统操作日志表';

# 初始化系统用户
INSERT INTO t_mgr_sys_user (LoginUserName, LoginPassword, nickName, email, mobile, status, createUserId, lastPasswordResetTime, createTime, updateTime, isSuperAdmin, BelongInfoId, BelongInfoType) VALUES ('xxpaymgr', '$2a$10$kFafDqYtRcifYbXujyzxnetsKJAQw5oofIYzugwWD34aQmrp3/AN6', '超级管理员', 'xxpay@126.com', '18866666666', 1, null, '2018-01-08 11:38:25', '2018-01-02 22:30:45', '2018-01-08 11:38:25', 1, 0, 3);

# 初始化系统资源

-- 运营平台系统
INSERT INTO `t_mgr_sys_resource` VALUES (10, 'home', '首页', '', '', '', 1, 3, 'layui-icon-home', 50, 0, 1, '', '2019-08-29', '2019-08-29');
INSERT INTO `t_mgr_sys_resource` VALUES (11, 'index', '首页', '/', 'ROLE_MENU', '/home/**', 1, 3, '', 100, 10, 1, '', '2019-08-29', '2019-08-29');

INSERT INTO `t_mgr_sys_resource` VALUES (12, 'isv', '服务商管理', '', '', '', 1, 3, 'layui-icon-user', 100, null, 1, '', '2019-08-27 16:00:00', '2019-08-27 16:00:00');
INSERT INTO `t_mgr_sys_resource` VALUES (13, 'list', '服务商管理', '', 'ROLE_ISV', '/isv_inf/**', 1, 3, '', 100, 12, 1, '', '2019-08-27 16:00:00', '2019-08-27 16:00:00');

INSERT INTO `t_mgr_sys_resource` VALUES (14, 'sys', '系统管理', '', '', '', 1, 3, 'layui-icon-set', 150, null, 1, '', '2018-01-23 23:16:41', '2018-01-23 23:16:41');
INSERT INTO `t_mgr_sys_resource` VALUES (15, 'permissionConfig', '权限管理', '', '', '', 1, 3, '', 100, 14, 1, '', '2018-10-31 15:24:44', '2018-10-31 15:24:44');
INSERT INTO `t_mgr_sys_resource` VALUES (16, 'user', '用户管理', '/sys/user/config', 'ROLE_SYS_USER', '/sys/user/**', 1, 3, 'layui-icon-user', 100, 15, 1, '', '2018-01-23 23:18:24', '2018-02-10 03:45:20');
INSERT INTO `t_mgr_sys_resource` VALUES (17, 'role', '角色管理', '/sys/role/config', 'ROLE_SYS_ROLE', '/sys/role/**', 1, 3, '', 110, 15, 1, '', '2018-01-23 23:18:59', '2018-01-23 23:18:59');
INSERT INTO `t_mgr_sys_resource` VALUES (18, 'resource', '资源管理', '/sys/resource/config', 'ROLE_SYS_RESOURCE', '/sys/resource/**', 1, 3, '', 120, 15, 1, '', '2018-01-23 23:19:29', '2018-02-27 18:39:37');
INSERT INTO `t_mgr_sys_resource` VALUES (19,'systemConfig', '系统设置', '', '', '', 1, 3, '', 110, 14, 1, '', '2018-10-31 15:24:44', '2018-10-31 15:24:44');
INSERT INTO `t_mgr_sys_resource` VALUES (20, 'smsConfig', '短信设置', '/sys/smsConfig/config', 'ROLE_SYS_SMSCONFIG', '/sys/smsConfig/**', 1, 3, '', 100, 19, 1, '', '2018-10-31 15:24:44', '2018-10-31 15:24:44');
INSERT INTO `t_mgr_sys_resource` VALUES (21, 'otherConfig', '其他设置', '/sys/otherConfig/config', 'ROLE_SYS_SYSTEMCONFIG', '/sys/systemConfig/**', 1, 3, '', 110, 19, 1, '', '2018-10-31 15:24:44', '2018-10-31 15:24:44');
INSERT INTO `t_mgr_sys_resource` VALUES (22, 'message', '消息管理', '', 'ROLE_SYS_MESSAGE', '/sys/message/**', 1, 3, '', 120, 14, 1, '', '2018-01-23 23:20:21', '2018-01-23 23:20:21');
INSERT INTO `t_mgr_sys_resource` VALUES (23, 'syslog', '操作日志', '', 'ROLE_SYS_SYSLOG', '/sys/syslog/**', 1, 3, '', 130, 14, 1, '', '2018-01-23 23:20:21', '2018-01-23 23:20:21');

INSERT INTO `t_mgr_sys_resource` VALUES (87, 'clientVersion', '版本管理', '', 'ROLE_CLIENT_VERSION', '/sys/clientVersion/**', 1, 3, '', 131, 14, 1, '', '2019-10-14 13:59:21', '2019-10-14 13:59:21');


-- 服务商系统
INSERT INTO `t_mgr_sys_resource` VALUES (24, 'home', '首页', '', '', '', 1, 4, 'layui-icon-home', 50, 0, 1, '', '2019-08-29', '2019-08-29');
INSERT INTO `t_mgr_sys_resource` VALUES (25, 'index', '首页', '/', 'ROLE_MENU', '/home/**', 1, 4, '', 100, 24, 1, '', '2019-08-29', '2019-08-29');

INSERT INTO `t_mgr_sys_resource` VALUES (26, 'account', '服务商', '', '', '', 1, 4, 'layui-icon-username', 100, 0, 1, '', '2019-08-29', '2019-08-29');
INSERT INTO `t_mgr_sys_resource` VALUES (27, 'info', '基本信息', '', 'ROLE_BASICINFO', '/isv/get,/account/get', 1, 4, '', 110, 26, 1, '', '2019-08-29', '2019-08-29');
INSERT INTO `t_mgr_sys_resource` VALUES (28, 'pay_passage', '通道配置', '', 'ROLE_MY_PASSAGE', '/pay_passage/**', 1, 4, '', 120, 26, 1, '', '2019-08-29', '2019-08-29');
INSERT INTO `t_mgr_sys_resource` VALUES (29, 'pay_product', '费率配置', '', 'ROLE_MY_PRODUCT', '/pay_product/**', 1, 4, '', 130, 26, 1, '', '2019-09-02', '2019-09-02');

INSERT INTO `t_mgr_sys_resource` VALUES (30, 'commission', '佣金结算', '', '', '', 1, 4, 'layui-icon-rmb', 150, 0, 1, '', '2019-08-29', '2019-08-29');
INSERT INTO `t_mgr_sys_resource` VALUES (31, 'settlement', '佣金结算', '', 'ROLE_COMMISSION', '/commission/**', 1, 4, '', 100, 30, 1, '', '2019-08-29', '2019-08-29');

INSERT INTO `t_mgr_sys_resource` VALUES (32, 'agent', '代理商管理', '', '', '', 1, 4, 'layui-icon-username', 200, 0, 1, '', '2019-08-29', '2019-08-29');
INSERT INTO `t_mgr_sys_resource` VALUES (33, 'audit', '待审核的代理', '', 'ROLE_AUDIT', '/audit/**', 1, 4, '', 100, 32, 1, '', '2019-08-29', '2019-08-29');
INSERT INTO `t_mgr_sys_resource` VALUES (34, 'signing', '已签约代理', '', 'ROLE_SIGNING', '/signing/**', 1, 4, '', 110, 32, 1, '', '2019-08-29', '2019-08-29');
INSERT INTO `t_mgr_sys_resource` VALUES (35, 'list', '所有代理', '', 'ROLE_ISV_AGENT', '/list/**', 1, 4, '', 120, 32, 1, '', '2019-08-29', '2019-08-29');

INSERT INTO `t_mgr_sys_resource` VALUES (36, 'biz', '商户管理', '', '', '', 1, 4, 'layui-icon-group', 250, 0, 1, '', '2019-08-29', '2019-08-29');
INSERT INTO `t_mgr_sys_resource` VALUES (37, 'merchant', '商户列表', '', 'ROLE_MCHINFO', '/merchant/**', 1, 4, '', 100, 36, 1, '', '2019-08-29', '2019-08-29');
INSERT INTO `t_mgr_sys_resource` VALUES (38, 'device', '进件列表', '', 'ROLE_DEVICE', '/device/**', 1, 4, '', 110, 36, 1, '', '2019-08-29', '2019-08-29');

INSERT INTO `t_mgr_sys_resource` VALUES (39, 'order', '订单管理', '', '', '', 1, 4, 'layui-icon-rmb', 450, 0, 1, '', '2019-08-29', '2019-08-29');
INSERT INTO `t_mgr_sys_resource` VALUES (40, 'pay', '支付订单', '', 'ROLE_PAY_ORDER', '/pay/**', 1, 4, '', 100, 39, 1, '', '2019-08-29', '2019-08-29');
INSERT INTO `t_mgr_sys_resource` VALUES (41, 'trade', '交易订单', '', 'ROLE_TRADE_ORDER', '/trade/**', 1, 4, '', 110, 39, 1, '', '2019-08-29', '2019-08-29');
INSERT INTO `t_mgr_sys_resource` VALUES (42, 'notify', '商户通知', '', 'ROLE_MCH_NOTIFY', '/notify/**', 1, 4, '', 120, 39, 1, '', '2019-08-29', '2019-08-29');

INSERT INTO `t_mgr_sys_resource` VALUES (43, 'device_config', '设备配置', '', '', '', 1, 4, 'layui-icon-app', 500, 0, 1, '', '2019-08-29', '2019-08-29');
INSERT INTO `t_mgr_sys_resource` VALUES (44, 'speaker_config', '云喇叭配置', '', 'ROLE_SPEAKER_SET', '/speaker_config/*', 1, 4, '', 100, 43, 1, '', '2019-09-02', '2019-09-02');
INSERT INTO `t_mgr_sys_resource` VALUES (45, 'printer_config', '打印机配置', '', 'ROLE_PRINTER_CONFIG', '/printer_config/*', 1, 4, '', 110, 43, 1, '', '2019-09-02', '2019-09-02');
INSERT INTO `t_mgr_sys_resource` VALUES (93, 'device_config', '硬件设备管理', '', 'ROLE_DEVICE_CONFIG', '/device_config/*', 1, 4, '', 111, 43, 1, '', '2019-10-31', '2019-10-31');

INSERT INTO `t_mgr_sys_resource` VALUES (46, 'advert', '广告管理', '', '', '', 1, 4, 'layui-icon-layouts', 550, 0, 1, '', '2019-08-29', '2019-08-29');
INSERT INTO `t_mgr_sys_resource` VALUES (47, 'list', '广告管理', '', 'ROLE_ADVERT', '/list/*', 1, 4, '', 100, 46, 1, '', '2019-08-29', '2019-08-29');

INSERT INTO `t_mgr_sys_resource` VALUES (48, 'data', '数据中心', '', '', '', 1, 4, 'layui-icon-console', 600, 0, 1, '', '2019-08-29', '2019-08-29');
INSERT INTO `t_mgr_sys_resource` VALUES (49, 'history', '流水概况', '', 'ROLE_DATA_HISTORY', '/history/**', 1, 4, '', 100, 48, 1, '', '2019-08-29', '2019-08-29');

INSERT INTO `t_mgr_sys_resource` VALUES (50, 'sub_user', '系统管理', '', '', '', 1, 4, 'layui-icon-set', 450, 0, 1, '', '2019-08-29', '2019-08-29');
INSERT INTO `t_mgr_sys_resource` VALUES (51, 'user', '用户管理', '', 'ROLE_SUBUSER_USER', '/sub_user/user/**', 1, 4, '', 100, 50, 1, '', '2019-08-29', '2019-08-29');
INSERT INTO `t_mgr_sys_resource` VALUES (52, 'role', '角色管理', '', 'ROLE_SUBUSER_ROLE', '/sub_user/role/**', 1, 4, '', 110, 50, 1, '', '2019-08-29', '2019-08-29');

-- 代理商系统
INSERT INTO `t_mgr_sys_resource` VALUES (53, 'home', '首页', '', '', '', 1, 2, 'layui-icon-home', 100, 0, 1, '', '2019-08-29', '2019-08-29');
INSERT INTO `t_mgr_sys_resource` VALUES (54, 'index', '首页', '/', 'ROLE_MENU', '/index/**', 1, 2, '', 100, 53, 1, '', '2019-08-29', '2019-08-29');

INSERT INTO `t_mgr_sys_resource` VALUES (55, 'commission', '佣金结算', '', '', '', 1, 2, 'layui-icon-rmb', 100, 0, 1, '', '2019-08-29', '2019-08-29');
INSERT INTO `t_mgr_sys_resource` VALUES (56, 'settlement', '佣金结算', '', 'ROLE_AGENT_COMMISSION', '/settlement/**', 1, 2, '', 100, 55, 1, '', '2019-08-29', '2019-08-29');

INSERT INTO `t_mgr_sys_resource` VALUES (57, 'biz', '商户管理', '', '', '', 1, 2, 'layui-icon-group', 150, 0, 1, '', '2019-08-29', '2019-08-29');
INSERT INTO `t_mgr_sys_resource` VALUES (58, 'merchant', '商户列表', '', 'ROLE_MCHINFO', '/merchant/**', 1, 2, '', 100, 57, 1, '', '2019-08-29', '2019-08-29');
INSERT INTO `t_mgr_sys_resource` VALUES (59, 'device', '进件列表', '', 'ROLE_AGENT_DEVICE', '/device/**', 1, 2, '', 110, 57, 1, '', '2019-08-29', '2019-08-29');

INSERT INTO `t_mgr_sys_resource` VALUES (60, 'sub_user', '系统管理', '', '', '', 1, 2, 'layui-icon-set', 200, 0, 1, '', '2019-08-29', '2019-08-29');
INSERT INTO `t_mgr_sys_resource` VALUES (61, 'user', '用户管理', '', 'ROLE_SUBUSER_USER', '/sub_user/user/**', 1, 2, '', 100, 60, 1, '', '2019-08-29', '2019-08-29');
INSERT INTO `t_mgr_sys_resource` VALUES (62, 'role', '角色管理', '', 'ROLE_SUBUSER_ROLE', '/sub_user/role/**', 1, 2, '', 110, 60, 1, '', '2019-08-29', '2019-08-29');

-- 商户系统
INSERT INTO `t_mgr_sys_resource` VALUES (63, 'home', '首页', '', '', '', 1, 1, 'layui-icon-home', 100, 0, 1, '', '2019-08-29', '2019-08-29');
INSERT INTO `t_mgr_sys_resource` VALUES (64, 'index', '首页', '/', 'ROLE_MENU', '/index/**', 1, 1, '', 100, 63, 1, '', '2019-08-29', '2019-08-29');

INSERT INTO `t_mgr_sys_resource` VALUES (65, 'account', '会员管理', '', '', '', 1, 1, 'layui-icon-user', 150, 0, 1, '', '2018-04-24 11:11:46', '2018-04-24 15:40:59');
INSERT INTO `t_mgr_sys_resource` VALUES (66, 'member', '会员列表', '', 'ROLE_MY_MEMBER', '/member/**', 1, 1, '', 100, 65, 1, '', '2018-04-24 11:24:20', '2018-04-24 15:38:04');
INSERT INTO `t_mgr_sys_resource` VALUES (67, 'card', '会员卡设置', '', 'ROLE_MEMBER_CARD', '/card/**', 1, 1, '', 120, 65, 1, '', '2018-04-24 11:27:09', '2018-06-10 22:34:22');
INSERT INTO `t_mgr_sys_resource` VALUES (68, 'rule', '储值规则', '', 'ROLE_RECHARGE_RULE', '/rule/**', 1, 1, '', 130, 65, 1, '', '2018-04-24 11:27:09', '2018-06-10 22:34:22');
INSERT INTO `t_mgr_sys_resource` VALUES (69, 'coupon', '卡券营销', '', 'ROLE_MY_COUPON', '/coupon/**', 1, 1, '', 140, 65, 1, '', '2018-04-24 11:24:20', '2018-04-24 15:38:04');
INSERT INTO `t_mgr_sys_resource` VALUES (70, 'points_goods', '积分商品', '', 'ROLE_MY_POINTS_GOODS', '/points_goods/**', 1, 1, '', 150, 65, 1, '', '2018-04-24 11:24:20', '2018-04-24 15:38:04');

INSERT INTO `t_mgr_sys_resource` VALUES (71, 'order', '支付管理', '', '', '', 1, 1, 'layui-icon-rmb', 200, 0, 1, '', '2018-04-24 11:11:46', '2018-04-24 15:40:59');
INSERT INTO `t_mgr_sys_resource` VALUES (72, 'trade', '交易流水', '', 'ROLE_MY_ORDER_HISTORY', '/trade/**', 1, 1, '', 100, 71, 1, '', '2018-04-24 11:24:20', '2018-04-24 15:38:04');
INSERT INTO `t_mgr_sys_resource` VALUES (73, 'qrcode', '二维码管理', '', 'ROLE_MY_QRCODE', '/qrcode/**', 1, 1, '', 110, 71, 1, '', '2018-04-24 11:24:20', '2018-04-24 15:38:04');

INSERT INTO `t_mgr_sys_resource` VALUES (74, 'store', '门店管理', '', '', '', 1, 1, 'layui-icon-group', 250, 0, 1, '', '2018-04-24 11:11:46', '2018-04-24 15:40:59');
INSERT INTO `t_mgr_sys_resource` VALUES (75, 'list', '门店列表', '', 'ROLE_MY_POINTS_GOODS', '/store/**', 1, 1, '', 100, 74, 1, '', '2018-04-24 11:24:20', '2018-04-24 15:38:04');

INSERT INTO `t_mgr_sys_resource` VALUES (76, 'data', '数据中心', '', '', '', 1, 1, 'layui-icon-console', 300, 0, 1, '', '2018-04-24 11:11:46', '2018-04-24 15:40:59');
INSERT INTO `t_mgr_sys_resource` VALUES (77, 'history', '流水概览', '', 'ROLE_DATA_HISTORY', '/history/**', 1, 1, '', 100, 76, 1, '', '2018-04-24 11:24:20', '2018-04-24 15:38:04');

INSERT INTO `t_mgr_sys_resource` VALUES (78, 'sub_user', '系统管理', '', '', '', 1, 1, 'layui-icon-set', 350, 0, 1, '', '2019-08-29', '2019-08-29');
INSERT INTO `t_mgr_sys_resource` VALUES (79, 'user', '用户管理', '', 'ROLE_SUBUSER_USER', '/sub_user/user/**', 1, 1, '', 100, 78, 1, '', '2019-08-29', '2019-08-29');
INSERT INTO `t_mgr_sys_resource` VALUES (80, 'role', '角色管理', '', 'ROLE_SUBUSER_ROLE', '/sub_user/role/**', 1, 1, '', 110, 78, 1, '', '2019-08-29', '2019-08-29');

-- 运营平台增加支付配置
INSERT INTO `t_mgr_sys_resource` VALUES (81, 'config', '支付配置', '', '', '', 1, 3, 'layui-icon-component', 125, 0, 1, '', '2019-08-29', '2019-08-29');
INSERT INTO `t_mgr_sys_resource` VALUES (82, 'pay_interface_type', '接口类型', '', 'ROLE_PAY_INTERFACE_TYPE', '/config/pay_interface_type/**', 1, 3, '', 100,81, 1, '', '2018-01-23 23:20:21', '2018-01-23 23:20:21');
INSERT INTO `t_mgr_sys_resource` VALUES (83, 'pay_interface', '支付接口', '', 'ROLE_PAY_INTERFACE', '/config/pay_interface/**', 1, 3, '', 110,81, 1, '', '2018-01-23 23:20:21', '2018-01-23 23:20:21');
INSERT INTO `t_mgr_sys_resource` VALUES (84, 'pay_product', '支付产品', '', 'ROLE_PAY_PRODUCT', '/config/pay_product/**', 1, 3, '', 120,81, 1, '', '2018-01-23 23:20:21', '2018-01-23 23:20:21');

-- 代理商增加代理商管理
INSERT INTO `t_mgr_sys_resource` VALUES (85, 'agent', '代理商管理', '', '', '', 1, 2, 'layui-icon-username', 120, 0, 1, '', '2019-08-29', '2019-08-29');
INSERT INTO `t_mgr_sys_resource` VALUES (86, 'sub_agent', '下级代理商', '', 'ROLE_AGENT_SUB_AGENT', '/sub_agent/**', 1, 2, '', 100, 85, 1, '', '2019-08-29', '2019-08-29');

-- 添加代理商的 数据中心菜单
INSERT INTO `t_mgr_sys_resource` VALUES (88, 'data', '数据中心', '', '', '', 1, 2, 'layui-icon-console', 160, 0, 1, '', '2019-10-16', '2019-10-16');
INSERT INTO `t_mgr_sys_resource` VALUES (89, 'history', '流水概况', '', 'ROLE_DATA_HISTORY', '/history/**', 1, 2, '', 100, 88, 1, '', '2019-10-16', '2019-10-16');

-- 服务商[第三方配置]
INSERT INTO `t_mgr_sys_resource` VALUES (90, 'component_config', '第三方配置', '', 'ROLE_COMPONENTS', '/components/**', 1, 4, '', 131, 26, 1, '', '2019-10-23', '2019-10-23');

-- 商户[交班管理]
INSERT INTO `t_mgr_sys_resource` VALUES (91, 'handover', '交班管理', '', '', '', 1, 1, 'layui-icon-survey', 310, 0, 1, '', '2019-10-29', '2019-10-29');
INSERT INTO `t_mgr_sys_resource` VALUES (92, 'history', '交班记录', '', 'ROLE_HANDOVER_HISTORY', '/handover_history/**', 1, 1, '', 100, 91, 1, '', '2019-10-29', '2019-10-29');

# 初始化角色表
INSERT INTO t_mgr_sys_role (roleId, roleName, createUserId, BelongInfoId, BelongInfoType, createTime, updateTime) VALUES (6, '运营管理', null, 0, 3, '2018-01-23 22:03:15', '2018-01-23 22:03:15');
INSERT INTO t_mgr_sys_role (roleId, roleName, createUserId, BelongInfoId, BelongInfoType, createTime, updateTime) VALUES (7, '财务管理', null, 0, 3, '2018-01-23 22:03:24', '2018-01-23 22:03:24');
INSERT INTO t_mgr_sys_role (roleId, roleName, createUserId, BelongInfoId, BelongInfoType, createTime, updateTime) VALUES (8, '系统管理', null, 0, 3, '2018-01-23 22:03:31', '2018-01-23 22:03:31');
#初始化化权限
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (482, 9, 10, '2018-08-08 21:52:48', '2018-08-08 21:52:48');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (483, 9, 11, '2018-08-08 21:52:48', '2018-08-08 21:52:48');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (484, 9, 12, '2018-08-08 21:52:48', '2018-08-08 21:52:48');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (485, 9, 38, '2018-08-08 21:52:48', '2018-08-08 21:52:48');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (486, 9, 95, '2018-08-08 21:52:48', '2018-08-08 21:52:48');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (487, 9, 26, '2018-08-08 21:52:48', '2018-08-08 21:52:48');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (488, 9, 96, '2018-08-08 21:52:48', '2018-08-08 21:52:48');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (489, 9, 16, '2018-08-08 21:52:48', '2018-08-08 21:52:48');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (490, 9, 17, '2018-08-08 21:52:48', '2018-08-08 21:52:48');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (491, 9, 18, '2018-08-08 21:52:48', '2018-08-08 21:52:48');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (492, 9, 21, '2018-08-08 21:52:48', '2018-08-08 21:52:48');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (493, 9, 22, '2018-08-08 21:52:48', '2018-08-08 21:52:48');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (494, 9, 23, '2018-08-08 21:52:48', '2018-08-08 21:52:48');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (495, 9, 24, '2018-08-08 21:52:48', '2018-08-08 21:52:48');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (496, 9, 25, '2018-08-08 21:52:48', '2018-08-08 21:52:48');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (497, 9, 27, '2018-08-08 21:52:48', '2018-08-08 21:52:48');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (498, 9, 39, '2018-08-08 21:52:48', '2018-08-08 21:52:48');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (499, 9, 31, '2018-08-08 21:52:48', '2018-08-08 21:52:48');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (500, 9, 35, '2018-08-08 21:52:48', '2018-08-08 21:52:48');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (501, 8, 10, '2018-08-08 21:53:03', '2018-08-08 21:53:03');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (502, 8, 11, '2018-08-08 21:53:03', '2018-08-08 21:53:03');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (503, 8, 12, '2018-08-08 21:53:03', '2018-08-08 21:53:03');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (504, 8, 38, '2018-08-08 21:53:03', '2018-08-08 21:53:03');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (505, 8, 95, '2018-08-08 21:53:03', '2018-08-08 21:53:03');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (506, 8, 26, '2018-08-08 21:53:03', '2018-08-08 21:53:03');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (507, 8, 96, '2018-08-08 21:53:03', '2018-08-08 21:53:03');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (508, 8, 67, '2018-08-08 21:53:03', '2018-08-08 21:53:03');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (509, 8, 68, '2018-08-08 21:53:03', '2018-08-08 21:53:03');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (510, 8, 97, '2018-08-08 21:53:03', '2018-08-08 21:53:03');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (511, 8, 16, '2018-08-08 21:53:03', '2018-08-08 21:53:03');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (512, 8, 17, '2018-08-08 21:53:03', '2018-08-08 21:53:03');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (513, 8, 18, '2018-08-08 21:53:03', '2018-08-08 21:53:03');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (514, 8, 21, '2018-08-08 21:53:03', '2018-08-08 21:53:03');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (515, 8, 41, '2018-08-08 21:53:03', '2018-08-08 21:53:03');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (516, 8, 82, '2018-08-08 21:53:03', '2018-08-08 21:53:03');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (517, 8, 83, '2018-08-08 21:53:03', '2018-08-08 21:53:03');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (518, 8, 84, '2018-08-08 21:53:03', '2018-08-08 21:53:03');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (519, 8, 85, '2018-08-08 21:53:03', '2018-08-08 21:53:03');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (520, 8, 112, '2018-08-08 21:53:03', '2018-08-08 21:53:03');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (521, 8, 22, '2018-08-08 21:53:03', '2018-08-08 21:53:03');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (522, 8, 23, '2018-08-08 21:53:03', '2018-08-08 21:53:03');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (523, 8, 24, '2018-08-08 21:53:03', '2018-08-08 21:53:03');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (524, 8, 25, '2018-08-08 21:53:03', '2018-08-08 21:53:03');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (525, 8, 93, '2018-08-08 21:53:03', '2018-08-08 21:53:03');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (526, 8, 94, '2018-08-08 21:53:03', '2018-08-08 21:53:03');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (527, 8, 98, '2018-08-08 21:53:03', '2018-08-08 21:53:03');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (528, 8, 99, '2018-08-08 21:53:03', '2018-08-08 21:53:03');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (529, 8, 100, '2018-08-08 21:53:03', '2018-08-08 21:53:03');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (530, 8, 101, '2018-08-08 21:53:03', '2018-08-08 21:53:03');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (531, 8, 102, '2018-08-08 21:53:03', '2018-08-08 21:53:03');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (532, 8, 27, '2018-08-08 21:53:03', '2018-08-08 21:53:03');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (533, 8, 39, '2018-08-08 21:53:03', '2018-08-08 21:53:03');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (534, 8, 31, '2018-08-08 21:53:03', '2018-08-08 21:53:03');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (535, 8, 32, '2018-08-08 21:53:03', '2018-08-08 21:53:03');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (536, 8, 33, '2018-08-08 21:53:03', '2018-08-08 21:53:03');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (537, 8, 34, '2018-08-08 21:53:03', '2018-08-08 21:53:03');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (538, 8, 35, '2018-08-08 21:53:03', '2018-08-08 21:53:03');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (539, 7, 10, '2018-08-08 21:53:24', '2018-08-08 21:53:24');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (540, 7, 38, '2018-08-08 21:53:24', '2018-08-08 21:53:24');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (541, 7, 26, '2018-08-08 21:53:24', '2018-08-08 21:53:24');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (542, 7, 67, '2018-08-08 21:53:24', '2018-08-08 21:53:24');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (543, 7, 97, '2018-08-08 21:53:24', '2018-08-08 21:53:24');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (544, 7, 16, '2018-08-08 21:53:24', '2018-08-08 21:53:24');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (545, 7, 17, '2018-08-08 21:53:24', '2018-08-08 21:53:24');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (546, 7, 18, '2018-08-08 21:53:24', '2018-08-08 21:53:24');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (547, 7, 21, '2018-08-08 21:53:24', '2018-08-08 21:53:24');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (548, 7, 22, '2018-08-08 21:53:24', '2018-08-08 21:53:24');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (549, 7, 23, '2018-08-08 21:53:24', '2018-08-08 21:53:24');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (550, 7, 24, '2018-08-08 21:53:24', '2018-08-08 21:53:24');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (551, 7, 25, '2018-08-08 21:53:24', '2018-08-08 21:53:24');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (552, 7, 93, '2018-08-08 21:53:24', '2018-08-08 21:53:24');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (553, 7, 94, '2018-08-08 21:53:24', '2018-08-08 21:53:24');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (554, 7, 99, '2018-08-08 21:53:24', '2018-08-08 21:53:24');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (555, 7, 100, '2018-08-08 21:53:24', '2018-08-08 21:53:24');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (556, 7, 101, '2018-08-08 21:53:24', '2018-08-08 21:53:24');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (557, 7, 102, '2018-08-08 21:53:24', '2018-08-08 21:53:24');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (558, 7, 27, '2018-08-08 21:53:24', '2018-08-08 21:53:24');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (559, 7, 39, '2018-08-08 21:53:24', '2018-08-08 21:53:24');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (560, 6, 10, '2018-08-08 21:53:35', '2018-08-08 21:53:35');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (561, 6, 11, '2018-08-08 21:53:35', '2018-08-08 21:53:35');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (562, 6, 12, '2018-08-08 21:53:35', '2018-08-08 21:53:35');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (563, 6, 38, '2018-08-08 21:53:35', '2018-08-08 21:53:35');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (564, 6, 95, '2018-08-08 21:53:35', '2018-08-08 21:53:35');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (565, 6, 16, '2018-08-08 21:53:35', '2018-08-08 21:53:35');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (566, 6, 17, '2018-08-08 21:53:35', '2018-08-08 21:53:35');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (567, 6, 18, '2018-08-08 21:53:35', '2018-08-08 21:53:35');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (568, 6, 21, '2018-08-08 21:53:35', '2018-08-08 21:53:35');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (569, 6, 31, '2018-08-08 21:53:35', '2018-08-08 21:53:35');
INSERT INTO t_mgr_sys_permission (id, roleId, resourceId, createTime, updateTime) VALUES (570, 6, 35, '2018-08-08 21:53:35', '2018-08-08 21:53:35');
# 初始化支付类型
INSERT INTO t_pay_type (payTypeCode, payTypeName, Type, Status, CreateTime, UpdateTime) VALUES ('10', '网银支付', '1', 1, '2018-08-29 00:05:10', '2018-08-29 00:05:10');
INSERT INTO t_pay_type (payTypeCode, payTypeName, Type, Status, CreateTime, UpdateTime) VALUES ('11', '快捷支付', '1', 1, '2018-08-29 00:05:10', '2018-08-29 00:05:10');
INSERT INTO t_pay_type (payTypeCode, payTypeName, Type, Status, CreateTime, UpdateTime) VALUES ('12', '微信扫码支付', '1', 1, '2018-08-29 00:05:10', '2018-08-29 00:05:10');
INSERT INTO t_pay_type (payTypeCode, payTypeName, Type, Status, CreateTime, UpdateTime) VALUES ('13', '微信H5支付', '1', 1, '2018-08-29 00:05:10', '2018-08-29 00:05:10');
INSERT INTO t_pay_type (payTypeCode, payTypeName, Type, Status, CreateTime, UpdateTime) VALUES ('14', '微信公众号支付', '1', 1, '2018-08-29 00:05:10', '2018-08-29 00:05:10');
INSERT INTO t_pay_type (payTypeCode, payTypeName, Type, Status, CreateTime, UpdateTime) VALUES ('15', '微信小程序支付', '1', 1, '2018-08-29 00:05:10', '2018-08-29 00:05:10');
INSERT INTO t_pay_type (payTypeCode, payTypeName, Type, Status, CreateTime, UpdateTime) VALUES ('16', '支付宝扫码支付', '1', 1, '2018-08-29 00:05:10', '2018-08-29 00:05:10');
INSERT INTO t_pay_type (payTypeCode, payTypeName, Type, Status, CreateTime, UpdateTime) VALUES ('17', '支付宝H5支付', '1', 1, '2018-08-29 00:05:10', '2018-08-29 00:05:10');
INSERT INTO t_pay_type (payTypeCode, payTypeName, Type, Status, CreateTime, UpdateTime) VALUES ('18', '支付宝服务窗支付', '1', 1, '2018-08-29 00:05:10', '2018-08-29 00:05:10');
INSERT INTO t_pay_type (payTypeCode, payTypeName, Type, Status, CreateTime, UpdateTime) VALUES ('19', 'QQ钱包扫码', '1', 1, '2018-08-29 00:05:10', '2018-08-29 00:05:10');
INSERT INTO t_pay_type (payTypeCode, payTypeName, Type, Status, CreateTime, UpdateTime) VALUES ('20', 'QQ钱包H5支付', '1', 1, '2018-08-29 00:05:10', '2018-08-29 00:05:10');
INSERT INTO t_pay_type (payTypeCode, payTypeName, Type, Status, CreateTime, UpdateTime) VALUES ('21', '京东扫码支付', '1', 1, '2018-08-29 00:05:10', '2018-08-29 00:05:10');
INSERT INTO t_pay_type (payTypeCode, payTypeName, Type, Status, CreateTime, UpdateTime) VALUES ('22', '京东H5支付', '1', 1, '2018-08-29 00:05:10', '2018-08-29 00:05:10');
INSERT INTO t_pay_type (payTypeCode, payTypeName, Type, Status, CreateTime, UpdateTime) VALUES ('23', '百度钱包', '1', 1, '2018-08-29 00:05:10', '2018-08-29 00:05:10');
INSERT INTO t_pay_type (payTypeCode, payTypeName, Type, Status, CreateTime, UpdateTime) VALUES ('24', '银联二维码', '1', 1, '2018-08-29 00:05:10', '2018-08-29 00:05:10');
INSERT INTO t_pay_type (payTypeCode, payTypeName, Type, Status, CreateTime, UpdateTime) VALUES ('25', '充值卡支付', '1', 1, '2018-08-29 00:05:10', '2018-08-29 00:05:10');
INSERT INTO t_pay_type (payTypeCode, payTypeName, Type, Status, CreateTime, UpdateTime) VALUES ('90', '转账', '2', 1, '2018-08-29 00:05:10', '2018-08-29 00:05:10');
INSERT INTO t_pay_type (payTypeCode, payTypeName, Type, Status) VALUES ('27', '微信条码支付', '1', 1);
INSERT INTO t_pay_type (payTypeCode, payTypeName, Type, Status) VALUES ('28', '支付宝条码支付', '1', 1);
INSERT INTO t_pay_type (payTypeCode, payTypeName, Type, Status) VALUES ('29', '云闪付条码支付', '1', 1);
# 初始化支付接口类型表
INSERT INTO t_pay_interface_type (IfTypeCode, IfTypeName, Status, IsvParam, MchParam, PrivateMchParam, Remark, CreateTime, UpdateTime) VALUES ('alipay', '支付宝支付官方', 1,
'[{"name":"pid","desc":"合作伙伴身份（PID）","type":"text","verify":"required"},{"name":"appId","desc":"应用App ID","type":"text","verify":"required"},{"name":"privateKey", "desc":"应用私钥", "type": "textarea","verify":"required"},{"name":"alipayPublicKey", "desc":"支付宝公钥", "type": "textarea","verify":"required"},{"name":"encryptionType","desc":"接口加密方式","type":"radio","verify":"","values":"key,cert","titles":"公钥（无需证书文件）,公钥证书（请务必上传证书文件）"},{"name":"appPublicCert","desc":"应用公钥证书（.crt格式）","type":"file","verify":""},{"name":"alipayPublicCert","desc":"支付宝公钥证书（.crt格式）","type":"file","verify":""},{"name":"alipayRootCert","desc":"支付宝根证书（.crt格式）","type":"file","verify":""}]',
'[{"name":"appAuthToken", "desc":"子商户app_auth_token", "type": "text","readonly":"readonly"},{"name":"refreshToken", "desc":"子商户刷新token", "type": "hidden","readonly":"readonly"},{"name":"expireTimestamp", "desc":"authToken有效期（13位时间戳）", "type": "hidden","readonly":"readonly"}]',
'[{"name":"pid","desc":"合作伙伴身份（PID）","type":"text","verify":"required"},{"name":"appId","desc":"应用App ID","type":"text","verify":"required"},{"name":"privateKey", "desc":"应用私钥", "type": "textarea","verify":"required"},{"name":"alipayPublicKey", "desc":"支付宝公钥", "type": "textarea","verify":"required"}]',
 '支付宝官方支付', '2018-05-08 19:38:05', '2018-05-08 19:38:05');

INSERT INTO t_pay_interface_type (IfTypeCode, IfTypeName, Status, IsvParam, MchParam, PrivateMchParam, Remark, CreateTime, UpdateTime) VALUES ('wxpay', '微信支付官方', 1,
'[{"name":"appId","desc":"应用App ID","type":"text","verify":"required"},{"name":"appSecret","desc":"应用AppSecret","type":"text","verify":"required"},{"name":"mchId", "desc":"微信支付商户号", "type": "text","verify":"required"},{"name":"key", "desc":"API密钥", "type": "textarea","verify":"required"},{"name":"apiV3Key", "desc":"API V3秘钥", "type": "textarea","verify":"required"},{"name":"serialNo", "desc":"序列号", "type": "textarea","verify":"required"},{"name":"cert", "desc":"API证书(.p12格式)", "type": "file","verify":"required"},{"name":"apiClientKey", "desc":"私钥文件(.pem格式)", "type": "file","verify":"required"}]',
'[{"name":"subMchId","desc":"子商户ID","type":"text","verify":"required"},{"name":"subMchAppId","desc":"子账户appID(线上支付必填)","type":"text","verify":""}]',
 '[{"name":"appId","desc":"应用App ID","type":"text","verify":"required"},{"name":"appSecret","desc":"应用AppSecret","type":"text","verify":"required"},{"name":"mchId", "desc":"微信支付商户号", "type": "text","verify":"required"},{"name":"key", "desc":"API密钥", "type": "textarea","verify":"required"},{"name":"certLocalPath", "desc":"API证书路径", "type": "text","verify":""}]',
 '微信官方支付接口', '2018-05-08 19:37:19', '2018-05-08 19:37:19');

-- 银联支付接口
INSERT INTO t_pay_interface_type (IfTypeCode, IfTypeName, Status, IsvParam, MchParam, PrivateMchParam, Remark, CreateTime, UpdateTime) VALUES ('unionpay', '银联支付', 1,
'[{"name":"merId","desc":"商户编号","type":"text","verify":"required"},{"name":"mchPrivateCertFile","desc":"商户私钥文件[版本5.1.0]（.pfx格式）","type":"file","verify":"required"},{"name":"mchPrivateCertPwd", "desc":"商户私钥文件密码", "type": "text","verify":"required"},{"name":"unionpayRootCertFile", "desc":"银联根证书（.cer格式）", "type": "file","verify":"required"},{"name":"unionpayMiddleCertFile", "desc":"银联中级证书", "type": "file","verify":"required"}]',
 '[]', '[]',
 '银联支付接口', '2019-12-04', '2019-12-04');

-- INSERT INTO t_pay_interface_type (IfTypeCode, IfTypeName, Status, IsvParam, MchParam, PrivateMchParam, Remark, CreateTime, UpdateTime) VALUES ('swiftpay', '威富通支付', 1, '[]', '[]', '[{"name":"mchId","desc":"商户号","type":"text","verify":"required"},{"name":"key","desc":"密钥","type":"text","verify":"required"},{"name":"reqUrl", "desc":"接口地址", "type": "text","verify":"required"}]', '威富通支付', '2018-05-08 19:38:59', '2018-05-08 19:38:59');
# 初始化支付接口表
INSERT INTO t_pay_interface (IfCode, IfName, IfTypeCode, PayType, Scene, Status, Param, Remark, CreateTime, UpdateTime) VALUES ('alipay_pc', '支付宝PC支付', 'alipay', '10', 3, 1, null, '', '2018-05-08 19:49:04', '2018-05-08 19:49:04');
INSERT INTO t_pay_interface (IfCode, IfName, IfTypeCode, PayType, Scene, Status, Param, Remark, CreateTime, UpdateTime) VALUES ('alipay_jsapi', '支付宝服务窗支付', 'alipay', '18', 2, 1, null, '', '2019-09-09', '2019-09-09');
INSERT INTO t_pay_interface (IfCode, IfName, IfTypeCode, PayType, Scene, Status, Param, Remark, CreateTime, UpdateTime) VALUES ('alipay_wap', '支付宝H5支付', 'alipay', '17', 2, 1, null, '', '2018-05-08 19:46:58', '2018-05-08 19:46:58');
-- INSERT INTO t_pay_interface (IfCode, IfName, IfTypeCode, PayType, Scene, Status, Param, Remark, CreateTime, UpdateTime) VALUES ('swiftpay_alipay_native', '威富通支付宝扫码', 'swiftpay', '16', 3, 1, null, '', '2018-05-08 19:45:12', '2018-05-08 19:45:12');
-- INSERT INTO t_pay_interface (IfCode, IfName, IfTypeCode, PayType, Scene, Status, Param, Remark, CreateTime, UpdateTime) VALUES ('swiftpay_wxpay_native', '威富通微信扫码', 'swiftpay', '12', 3, 1, null, '', '2018-05-08 19:44:25', '2018-05-08 19:44:25');
INSERT INTO t_pay_interface (IfCode, IfName, IfTypeCode, PayType, Scene, Status, Param, Remark, CreateTime, UpdateTime) VALUES ('wxpay_jsapi', '微信公众号支付', 'wxpay', '14', 4, 1, null, '', '2018-05-08 19:49:40', '2018-05-08 19:49:40');
INSERT INTO t_pay_interface (IfCode, IfName, IfTypeCode, PayType, Scene, Status, Param, Remark, CreateTime, UpdateTime) VALUES ('wxpay_mweb', '微信H5支付', 'wxpay', '13', 2, 1, null, '', '2018-05-08 19:50:40', '2018-05-08 19:50:40');
INSERT INTO t_pay_interface (IfCode, IfName, IfTypeCode, PayType, Scene, Status, Param, Remark, CreateTime, UpdateTime) VALUES ('wxpay_native', '微信扫码支付', 'wxpay', '12', 3, 1, null, '', '2018-05-08 19:50:05', '2018-05-08 19:50:05');
INSERT INTO t_pay_interface (IfCode, IfName, IfTypeCode, PayType, Scene, Status, Param, Remark) VALUES ('wxpay_bar', '微信条码支付', 'wxpay', '27', 5, 1, null, '');
INSERT INTO t_pay_interface (IfCode, IfName, IfTypeCode, PayType, Scene, Status, Param, Remark) VALUES ('alipay_bar', '支付宝条码支付', 'alipay', '28', 5, 1, null, '');

INSERT INTO t_pay_interface (IfCode, IfName, IfTypeCode, PayType, Scene, Status, Param, Remark) VALUES ('unionpay_bar', '云闪付条码支付', 'unionpay', '29', 5, 1, null, '');
INSERT INTO t_pay_interface (IfCode, IfName, IfTypeCode, PayType, Scene, Status, Param, Remark) VALUES ('wxpay_mini', '微信小程序支付', 'wxpay', '15', 2, 1, null, '');


# 初始化支付产品
-- INSERT INTO t_pay_product (id, ProductName, PayType, Status) VALUES (8000, '网银支付', '10', 1);
-- INSERT INTO t_pay_product (id, ProductName, PayType, Status) VALUES (8001, '快捷支付', '11', 1);
INSERT INTO t_pay_product (id, ProductName, PayType, Status) VALUES (8002, '微信扫码支付', '12', 1);
-- INSERT INTO t_pay_product (id, ProductName, PayType, Status) VALUES (8003, '微信H5支付', '13', 1);
INSERT INTO t_pay_product (id, ProductName, PayType, Status) VALUES (8004, '微信公众号支付', '14', 1);
-- INSERT INTO t_pay_product (id, ProductName, PayType, Status) VALUES (8005, '微信小程序支付', '15', 1);
-- INSERT INTO t_pay_product (id, ProductName, PayType, Status) VALUES (8006, '支付宝扫码支付', '16', 1);
-- INSERT INTO t_pay_product (id, ProductName, PayType, Status) VALUES (8007, '支付宝H5支付', '17', 1);
INSERT INTO t_pay_product (id, ProductName, PayType, Status) VALUES (8008, '支付宝服务窗支付', '18', 1);
-- INSERT INTO t_pay_product (id, ProductName, PayType, Status) VALUES (8009, 'QQ钱包扫码', '19', 1);
-- INSERT INTO t_pay_product (id, ProductName, PayType, Status) VALUES (8010, 'QQ钱包H5支付', '20', 1);
-- INSERT INTO t_pay_product (id, ProductName, PayType, Status) VALUES (8011, '京东扫码支付', '21', 1);
INSERT INTO t_pay_product (id, ProductName, PayType, Status) VALUES (8012, '京东H5支付', '22', 1);
-- INSERT INTO t_pay_product (id, ProductName, PayType, Status) VALUES (8013, '百度钱包', '23', 1);
-- INSERT INTO t_pay_product (id, ProductName, PayType, Status) VALUES (8014, '银联二维码', '24', 1);
INSERT INTO t_pay_product (id, ProductName, PayType, Status) VALUES (8020, '微信条码支付', '27', 1);
INSERT INTO t_pay_product (id, ProductName, PayType, Status) VALUES (8021, '支付宝条码支付', '28', 1);

INSERT INTO t_pay_product (id, ProductName, PayType, Status) VALUES (8022, '云闪付条码支付', '29', 1);
INSERT INTO t_pay_product (id, ProductName, PayType, Status) VALUES (8023, '微信小程序支付', '15', 1);

# 初始化系统配置
INSERT INTO t_sys_config (code, name, value, remark, type, orderNum) VALUES ('drawFlag', '提现开关', '1', '提现开关:0-关闭,1-开启', 'sett', 1.0);
INSERT INTO t_sys_config (code, name, value, remark, type, orderNum) VALUES ('allowDrawWeekDay', '允许星期几', '1,2,3,4,5,6,7', '每周周几允许提现,数字表示,多个逗号分隔', 'sett', 1.1);
INSERT INTO t_sys_config (code, name, value, remark, type, orderNum) VALUES ('drawDayStartTime', '开始时间','00:00:00', '每天提现开始时间', 'sett', 1.2);
INSERT INTO t_sys_config (code, name, value, remark, type, orderNum) VALUES ('drawDayEndTime', '结束时间', '23:59:59', '每天提现结束时间', 'sett', 1.3);
INSERT INTO t_sys_config (code, name, value, remark, type, orderNum) VALUES ('dayDrawTimes', '提现次数', '10', '每日提现次数', 'sett', 1.4);
INSERT INTO t_sys_config (code, name, value, remark, type, orderNum) VALUES ('drawMaxDayAmount', '日提现最大金额' ,'50000000', '每天提现最大金额,单位分', 'sett', 1.5);
INSERT INTO t_sys_config (code, name, value, remark, type, orderNum) VALUES ('maxDrawAmount', '单笔最大金额', '5000000', '单笔最大金额,单位分', 'sett', 1.6);
INSERT INTO t_sys_config (code, name, value, remark, type, orderNum) VALUES ('minDrawAmount', '单笔最小金额', '100', '单笔最小金额,单位分', 'sett', 1.7);
INSERT INTO t_sys_config (code, name, value, remark, type, orderNum) VALUES ('feeType', '手续费类型', '2', '手续费类型,1-百分比收费,2-固定收费', 'sett', 1.8);
INSERT INTO t_sys_config (code, name, value, remark, type, orderNum) VALUES ('feeRate', '手续费百分比值','1', '手续费百分比值', 'sett', 1.9);
INSERT INTO t_sys_config (code, name, value, remark, type, orderNum) VALUES ('feeLevel', '每笔手续费','100', '每笔提现手续费', 'sett', 2.0);
INSERT INTO t_sys_config (code, name, value, remark, type, orderNum) VALUES ('drawFeeLimit', '手续费上限','1000', '每笔提现手续费上限,单位分', 'sett', 2.1);
INSERT INTO t_sys_config (code, name, value, remark, type, orderNum) VALUES ('settType', '结算类型', '0', '结算类型,0-手动提现,1-自动结算', 'sett', 2.2);
INSERT INTO t_sys_config (code, name, value, remark, type, orderNum) VALUES ('settMode', '结算周期类型', '1', '结算周期类型,1- D类型,2-T类型', 'sett', 2.3);
INSERT INTO t_sys_config (code, name, value, remark, type, orderNum) VALUES ('settRiskDay', '结算风险预存期', '1', '结算风险预存期，单位：天数', 'sett', 2.4);
-- 最大代理商级别配置信息 默认5级
INSERT INTO t_sys_config (code, name, value, remark, type, orderNum) VALUES ('agentMaxlevel', '代理商等级限制', '5', '平台允许的最大代理商等级', 'agent', 1);

INSERT INTO t_sys_config (code, name, value, remark, type, orderNum) VALUES ('smsConfig', '短信配置',
"{accessKeyId:\"LTAIf1oqGNYOG2CT\", accessKeySecret : \"WjC582APyOTEnQDwxmMpWzUMJBnIdp\", signName :\"XxPay聚合支付\", templats:
[{bizType:22, bizName:'找回密码', templateCode:'SMS_127164863', templateParam: \"{code:'${content}'}\"}
]}"
, '短信配置信息', 'sms', 1.0);
INSERT INTO t_sys_config (code, name, value, remark, type, orderNum) VALUES ('appVoice', '收款到账语音播报', '1', '收款到账语音播报：0-关闭 1-开启', 'app', 1);
INSERT INTO t_sys_config (code, name, value, remark, type, orderNum) VALUES ('appPush', '新消息通知', '1', '新消息通知：0-关闭 1-开启', 'app', 1);

-- 上传方式配置
INSERT INTO t_sys_config (code, name, value, remark, type, orderNum) VALUES ('uploadFileSaveType', '上传文件存储方式', '1', '上传文件存储方式: 1-本地, 2-阿里云oss', 'uploadFile', 1.0);
INSERT INTO t_sys_config (code, name, value, remark, type, orderNum) VALUES ('ossEndpoint', 'EndPoint地域节点,(不包含http://和https://)', '', 'EndPoint地域节点,(不包含http://和https://)', 'aliyunOss', 1.0);
INSERT INTO t_sys_config (code, name, value, remark, type, orderNum) VALUES ('ossBucketName', 'BucketName', '', 'BucketName', 'aliyunOss', 4.0);
INSERT INTO t_sys_config (code, name, value, remark, type, orderNum) VALUES ('ossAccessKeyId', 'accessKeyId', '', 'accessKeyId', 'aliyunOss', 2.0);
INSERT INTO t_sys_config (code, name, value, remark, type, orderNum) VALUES ('ossAccessKeySecret', 'accessKeySecret', '', 'accessKeySecret', 'aliyunOss', 3.0);

-- 百度orc设置
INSERT INTO t_sys_config (code, name, value, remark, type, orderNum) VALUES ('baiduOcrClientId', '百度OCR [API Key]', '', '百度OCR clientId[API Key]', 'baiduOCR', 1.0);
INSERT INTO t_sys_config (code, name, value, remark, type, orderNum) VALUES ('baiduOcrClientSecret', '百度OCR [Secret Key]', '', '百度OCR clientSecret[Secret Key]', 'baiduOCR', 2.0);
INSERT INTO t_sys_config (code, name, value, remark, type, orderNum) VALUES ('baiduOcrType', '百度OCR类型', '', '1-普通接口, 2-高精度接口', 'baiduOCR', 3.0);

-- 百度语音设置（商户APP）
INSERT INTO t_sys_config (code, name, value, remark, type, orderNum) VALUES ('baiduBceAppId2Mch', '商户APP: 百度语音AppId', '', '商户APP: 百度语音AppId', 'baiduBce2Mch', 1.0);
INSERT INTO t_sys_config (code, name, value, remark, type, orderNum) VALUES ('baiduBceAppKey2Mch', '商户APP: 百度语音AppKey', '', '商户APP: 百度语音AppKey', 'baiduBce2Mch', 2.0);
INSERT INTO t_sys_config (code, name, value, remark, type, orderNum) VALUES ('baiduBceAppSecret2Mch', '商户APP: 百度语音AppSecret', '', '商户APP: 百度语音AppSecret', 'baiduBce2Mch', 3.0);

-- 百度语音设置（刷脸APP）
INSERT INTO t_sys_config (code, name, value, remark, type, orderNum) VALUES ('baiduBceAppId2Face', '刷脸APP: 百度语音AppId', '', '刷脸APP: 百度语音AppId', 'baiduBce2Face', 1.0);
INSERT INTO t_sys_config (code, name, value, remark, type, orderNum) VALUES ('baiduBceAppKey2Face', '刷脸APP: 百度语音AppKey', '', '刷脸APP: 百度语音AppKey', 'baiduBce2Face', 2.0);
INSERT INTO t_sys_config (code, name, value, remark, type, orderNum) VALUES ('baiduBceAppSecret2Face', '刷脸APP: 百度语音AppSecret', '', '刷脸APP: 百度语音AppSecret', 'baiduBce2Face', 3.0);

-- uniPush推送设置
INSERT INTO t_sys_config (code, name, value, remark, type, orderNum) VALUES ('uniPushAppId', 'uniPush [AppId]', '', 'uniPush消息推送 appId', 'uniPush', 1.0);
INSERT INTO t_sys_config (code, name, value, remark, type, orderNum) VALUES ('uniPushAppKey', 'uniPush [AppKey]', '', 'uniPush消息推送 appKey', 'uniPush', 2.0);
INSERT INTO t_sys_config (code, name, value, remark, type, orderNum) VALUES ('uniPushMasterSecret', 'uniPush [MasterSecret]', '', 'uniPush消息推送 masterSecret', 'uniPush', 3.0);


-- 新增存储过程(查询)： 根据代理商ID, 遍历所有代理商分佣比例
-- 返回格式：  代理商ID   分佣比例  等级    pid
drop procedure if exists proc_s_agents_profit_rate;

-- 设置提交符为 //
delimiter //
create procedure proc_s_agents_profit_rate(in agentId INT)
BEGIN

	  set @agentIdArr = concat(agentId); -- 最终需要查询的代理商ID字符串 使用,拼接方式

		set @pid = (select ai.pid from t_agent_info ai where ai.AgentId = agentId); -- 查询代理商的上级代理ID
		while (@pid is not null and  @pid <> '0' ) DO
			  set @agentIdArr = CONCAT_WS(',',@agentIdArr, @pid);
			  set @pid = (select ai.pid from t_agent_info ai where ai.AgentId = @pid);
		end while;


		set @sql = "select tai.AgentId, tai.AgentLevel, tai.Pid, tai.ProfitRate
					from t_agent_info tai
					where tai.AgentId in (#agentIdArr#) order by tai.AgentLevel desc";


		-- 存储过程无数组格式，in函数传入字符串会出现查询异常，使用sql拼接方式.
		set @sql = REPLACE(@sql, '#agentIdArr#', @agentIdArr);

		PREPARE stmt from @sql;
		EXECUTE stmt;

end//
-- 设置提交符为 ;
delimiter ;

-- 小新聚合支付 新增表结构

-- 1. 商户门店表
DROP TABLE IF EXISTS `t_mch_store`;
CREATE TABLE `t_mch_store` (
  `StoreId` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '门店ID',
  `StoreNo` varchar(50) NOT NULL COMMENT '门店编号',
  `StoreName` varchar(50) NOT NULL COMMENT '门店名称',
  `RefundPassword` varchar(100) DEFAULT NULL COMMENT '退款密码',
  `ProvinceCode` int(11) NOT NULL COMMENT '行政地区编号，省',
  `CityCode` int(11) NOT NULL COMMENT '行政地区编号， 市',
  `AreaCode` int(11) NOT NULL COMMENT '行政地区编号， 县',
  `AreaInfo` varchar(128) NOT NULL COMMENT '省市县名称描述',
  `Address` varchar(64) NOT NULL COMMENT '具体位置',
  `Lot` varchar(50) COMMENT '经度',
  `Lat` varchar(50) COMMENT '纬度',
  `Status` tinyint(6) NOT NULL COMMENT '状态:0-暂停营业,1-正常营业',
  `Tel` varchar(32) COMMENT '门店电话',
  `MchId` bigint(20) NOT NULL COMMENT '所属商户ID',
  `Remark` varchar(128) COMMENT '备注',
  `StoreImgPath` varchar(128) COMMENT '门店图片, 本地相对路径',
  `MiniImgPath` varchar(128) DEFAULT NULL COMMENT '小程序首页顶部背景图, 本地相对路径',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`StoreId`),
  UNIQUE KEY(`StoreNo`,`MchId`)
) ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8mb4 COMMENT='商户门店表';

-- 2. 商户会员表
DROP TABLE IF EXISTS `t_member`;
CREATE TABLE `t_member` (
  `MemberId` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '会员ID',
  `MemberNo` varchar(32) DEFAULT NULL COMMENT '卡号',
  `MemberName` varchar(32) NOT NULL COMMENT '会员名称',
  `Tel` varchar(32) COMMENT '手机号',
  `Sex` tinyint(6) COMMENT '性别:0-女 1-男',
  `Status` tinyint(6) NOT NULL COMMENT '状态:0-停用 1-启用 2-未绑定手机号',
  `Birthday` date COMMENT '生日',
  `Remark` varchar(128) COMMENT '备注',
  `MchId` bigint(20) NOT NULL COMMENT '所属商户ID',
  `WxOpenId` varchar(128) COMMENT '微信OpenId',
  `AlipayUserId` varchar(128) COMMENT '支付宝UserId',
  `Avatar` varchar(256) DEFAULT NULL COMMENT '头像路径',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`MemberId`),
  UNIQUE KEY `Uni_MemberId_No` (`MchId`, `MemberNo`),
  UNIQUE KEY `Uni_MemberId_Tel` (`MchId`, `Tel`),
  UNIQUE KEY `Uni_MchId_WxOpenId` (`MchId`, `WxOpenId`),
  UNIQUE KEY `Uni_MchId_AlipayUserId` (`MchId`, `AlipayUserId`)
) ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8mb4 COMMENT='商户会员表';

-- 3. 商户会员储值账户表
DROP TABLE IF EXISTS `t_member_balance`;
CREATE TABLE `t_member_balance` (
  `MemberId` bigint(20) NOT NULL COMMENT '会员ID',
  `MemberNo` varchar(32) NOT NULL COMMENT '会员卡号',
  `Balance` bigint(20) NOT NULL DEFAULT '0' COMMENT '账户可用余额',
  `TotalRechargeAmount` bigint(20) NOT NULL DEFAULT '0' COMMENT '实际储值金额',
  `TotalGiveAmount` bigint(20) NOT NULL DEFAULT '0' COMMENT '系统赠送金额总额',
  `TotalConsumeAmount` bigint(20) NOT NULL DEFAULT '0' COMMENT '总消费金额',
  `TotalRefundAmount` bigint(20) NOT NULL DEFAULT '0' COMMENT '总消费退款金额',
  PRIMARY KEY (`MemberId`),
  UNIQUE KEY `Uni_MemberNo` (`MemberNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户会员储值账户表';

-- 4. 商户会员积分表
DROP TABLE IF EXISTS `t_member_points`;
CREATE TABLE `t_member_points` (
  `MemberId` bigint(20) NOT NULL COMMENT '会员ID',
  `MemberNo` varchar(32) NOT NULL COMMENT '会员卡号',
  `Points` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '账户可用积分',
  `TotalAddPoints` bigint(20) NOT NULL DEFAULT '0' COMMENT '实际总累计积分',
  `TotalGivePoints` bigint(20) NOT NULL DEFAULT '0' COMMENT '系统赠送积分总额',
  `TotalConsumePoints` bigint(20) NOT NULL DEFAULT '0' COMMENT '总支出积分',
  `TotalRefundPoints` bigint(20) NOT NULL DEFAULT '0' COMMENT '总消费退款积分',
  PRIMARY KEY (`MemberId`),
  UNIQUE KEY `Uni_MemberNo` (`MemberNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户会员积分表';


-- 5. 商户会员储值流水表
DROP TABLE IF EXISTS `t_member_balance_history`;
CREATE TABLE `t_member_balance_history` (
  `BalanceHistoryId` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '账户流水ID',
  `MemberId` bigint(20) NOT NULL COMMENT '会员ID',
  `MemberNo` varchar(32) NOT NULL COMMENT '卡号',
  `ChangeAmount` bigint(20) NOT NULL COMMENT '变动金额, +增加, -减少',
  `Balance` bigint(20) unsigned NOT NULL COMMENT '变更前账户余额',
  `AfterBalance` bigint(20) unsigned NOT NULL COMMENT '变更后账户余额',
  `GiveAmount` bigint(20) unsigned NOT NULL COMMENT '赠送金额（仅做记录）',
  `MchId` bigint(20) NOT NULL COMMENT '所属商户ID',
  `BizOrderId` varchar(64) COMMENT '平台订单号',
  `BizType` tinyint(6) NOT NULL COMMENT '交易类型:1-充值, 2-消费, 3-退款, 4-导入, 5-赠送',
  `PayType` tinyint(6) DEFAULT NULL COMMENT '支付方式:1-微信, 2-支付宝, 3-储值卡, 4-导入',
  `PageOrigin` tinyint(6) DEFAULT NULL COMMENT '来源 1-PC, 2-ANDROID, 3-IOS, 4-H5',
  `OperatorId` varchar(32) COMMENT '操作员ID',
  `OperatorName` varchar(32) COMMENT '操作员名称',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`BalanceHistoryId`)
) ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8mb4 COMMENT='商户会员储值流水表';

-- 6. 商户会员积分流水表
DROP TABLE IF EXISTS `t_member_points_history`;
CREATE TABLE `t_member_points_history` (
  `PointsHistoryId` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '积分流水ID',
  `MemberId` bigint(20) NOT NULL COMMENT '会员ID',
  `MemberNo` varchar(32) NOT NULL COMMENT '卡号',
  `ChangePoints` bigint(20) NOT NULL COMMENT '变动积分, +增加, -减少',
  `Points` bigint(20) unsigned NOT NULL COMMENT '变更前积分',
  `AfterPoints` bigint(20) unsigned NOT NULL COMMENT '变更后积分',
  `MchId` bigint(20) NOT NULL COMMENT '所属商户ID',
  `BizOrderId` varchar(64) COMMENT '平台订单号',
  `BizType` tinyint(6) NOT NULL COMMENT '积分类型:1-积分商品兑换, 2-充值赠送, 3-消费, 4-退款, 5-开卡赠送, 6-导入',
  `OperatorId` varchar(32) COMMENT '操作员ID',
  `OperatorName` varchar(32) COMMENT '操作员名称',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`PointsHistoryId`)
) ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8mb4 COMMENT='商户会员积分流水表';

-- 7. 商户积分商品表
DROP TABLE IF EXISTS `t_mch_points_goods`;
CREATE TABLE `t_mch_points_goods` (
  `GoodsId` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '积分商品ID',
  `GoodsName` varchar(32) NOT NULL COMMENT '商品名称',
  `Points` bigint(20) NOT NULL COMMENT '所需积分',
  `GoodsPrice` bigint(20) NOT NULL COMMENT '商品价格，单位：分',
  `ImgPath` varchar(128) COMMENT '图片路径',
  `Status` tinyint(6) NOT NULL COMMENT '状态 0-下架 1-上架',
  `StockLimitType` tinyint(6) NOT NULL COMMENT '是否限制库存 1-不限库存 2-限制',
  `StockNum` bigint(20) COMMENT '库存数量',
  `SingleMemberLimit` int NOT NULL COMMENT '单用户兑换限制数量 -1不限制',
  `BeginTime` datetime COMMENT '活动开始时间',
  `EndTime` datetime COMMENT '活动结束时间',
  `MchId` bigint(20) NOT NULL COMMENT '所属商户ID',
  `StoreLimitType` tinyint(6) NOT NULL COMMENT '是否限制门店 0-不限门店 1-限制门店,详见门店关联表',
  `Remark` varchar(128) COMMENT '备注',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`GoodsId`)
) ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8mb4 COMMENT='商户积分商品表';

-- 8. 商户积分商品与门店关联表
DROP TABLE IF EXISTS `t_mch_points_goods_store_rela`;
CREATE TABLE `t_mch_points_goods_store_rela` (
  `GoodsId` bigint(20) NOT NULL COMMENT '积分商品ID',
  `StoreId` bigint(20) NOT NULL COMMENT '门店ID',
  PRIMARY KEY (`GoodsId`, `StoreId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户积分商品与门店关联表';

-- 9. 积分商品会员兑换表
DROP TABLE IF EXISTS `t_member_goods_exchange`;
CREATE TABLE `t_member_goods_exchange` (
  `ExchangeId` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '兑换ID',
  `ExchangeNo` varchar(32) NOT NULL COMMENT '提货码',
  `MemberId` bigint(20) NOT NULL COMMENT '会员ID',
  `MemberNo` varchar(32) NOT NULL COMMENT '会员卡号',
  `MchId` bigint(20) NOT NULL COMMENT '所属商户ID',
  `GoodsId` bigint(20) NOT NULL COMMENT '积分商品ID',
  `GoodsName` varchar(32) NOT NULL COMMENT '商品名称',
  `Points` bigint(20) NOT NULL COMMENT '使用积分',
  `Status` tinyint(6) NOT NULL COMMENT '状态:0-未兑换 1-已兑换, 2-已作废',
  `Remark` varchar(128) COMMENT '备注',
  `OperatorId` varchar(32) COMMENT '操作员ID',
  `OperatorName` varchar(32) COMMENT '操作员名称',
  `ExchangeTime` datetime COMMENT '兑换时间',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`ExchangeId`),
  UNIQUE KEY `Uni_ExchangeNo` (`ExchangeNo`)
) ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8mb4 COMMENT='积分商品会员兑换表';

-- 10. 商户优惠券表
DROP TABLE IF EXISTS `t_mch_coupon`;
CREATE TABLE `t_mch_coupon` (
  `CouponId` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '优惠券ID',
  `CouponName` varchar(32) NOT NULL COMMENT '优惠券名称',
  `CouponColor` varchar(32) NOT NULL COMMENT '优惠券配色',
  `LogoImgPath` varchar(256) NOT NULL COMMENT '优惠券logo路径',
  `CouponAmount` bigint(20) NOT NULL COMMENT '优惠券面值, 单位分',
  `PayAmountLimit` bigint(20) NOT NULL COMMENT '最低消费金额',
  `Status` tinyint(6) NOT NULL COMMENT '状态:0-暂停发放 1-正常发放 2-活动已结束',
  `ValidateType` tinyint(6) NOT NULL COMMENT '有效期类型:1-领取后天数, 2-按照活动日期',
  `ValidateDay` int COMMENT '领取后有效期天数',
  `BeginTime` datetime NOT NULL COMMENT '活动开始时间',
  `EndTime` datetime NOT NULL COMMENT '活动结束时间',
  `TotalNum` int NOT NULL COMMENT '总发放数量',
  `ReceiveNum` int NOT NULL COMMENT '已领取数量',
  `UseTimeConfig` varchar(128) NOT NULL COMMENT '优惠券使用限制，例如时段,周,时间, json格式',
  `SingleUserLimit` int NOT NULL COMMENT '单用户领取数量限制, -1不限制',
  `StoreLimitType` tinyint(6) NOT NULL COMMENT '是否限制门店 0-不限门店 1-限制门店,详见门店关联表',
  `ExpiredWarningTime` int NOT NULL COMMENT '过期提醒提前天数：-1不提醒',
  `SyncWX` tinyint(6) NOT NULL COMMENT '是否同步到微信卡包 1-是, 0-否',
  `MchId` bigint(20) NOT NULL COMMENT '所属商户ID',
  `Remark` varchar(128) COMMENT '备注',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`CouponId`)
) ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8mb4 COMMENT='商户优惠券表';

-- 11. 商户优惠券与门店关联表
DROP TABLE IF EXISTS `t_mch_coupon_store_rela`;
CREATE TABLE `t_mch_coupon_store_rela` (
  `CouponId` bigint(20) NOT NULL COMMENT '优惠券ID',
  `StoreId` bigint(20) NOT NULL COMMENT '门店ID',
  PRIMARY KEY (`CouponId`, `StoreId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户优惠券-门店关联表';

-- 12. 会员优惠券领取记录表
DROP TABLE IF EXISTS `t_member_coupon`;
CREATE TABLE `t_member_coupon` (
  `CouponNo` varchar(32) NOT NULL COMMENT '优惠卷核销码',
  `MemberId` bigint(20) NOT NULL COMMENT '会员ID',
  `CouponId` bigint(20) NOT NULL COMMENT '优惠券ID',
  `Status` tinyint(6) NOT NULL COMMENT '状态:0-未使用 1-已使用 2-已过期',
  `MchId` bigint(20) NOT NULL COMMENT '所属商户ID',
  `Remark` varchar(128) COMMENT '备注',
  `ValidateEnd` datetime NOT NULL COMMENT '有效期至',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`CouponNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员优惠券领取记录表';


-- 13. 商户会员优惠配置信息
DROP TABLE IF EXISTS `t_mch_member_config`;
CREATE TABLE `t_mch_member_config` (
  `MchId` bigint(20) NOT NULL COMMENT 'MchId',
  `ConsumeAmount` bigint(20) NOT NULL COMMENT '消费金额',
  `ConsumeGivePoints` bigint(20) NOT NULL COMMENT '消费金额满足时赠送积分',
  `BalancePointsType` tinyint(6) NOT NULL COMMENT '储值消费是否可积分 0-否, 1-是',
  `NewMemberType` tinyint(6) NOT NULL COMMENT '会员卡领取方式, 1-免激活领取, 2-人工录入',
  `NewMemberRule` tinyint(6)  NOT NULL COMMENT '首次领取赠送规则, 0-无优惠, 1-赠余额, 2-赠积分',
  `NewMemberGivePoints` bigint(20) NOT NULL COMMENT '首次领取返点, 余额/积分',
  `MemberCardColor` varchar(128) NOT NULL COMMENT '会员卡封面背景颜色',
  `MemberCardName` varchar(128) NOT NULL COMMENT '会员卡名称',
  `MemberCardValidTime` varchar(128) NOT NULL COMMENT '会员卡期限',
  `MemberCardTel` varchar(128) NOT NULL COMMENT '会员卡联系电话',
  `MemberCardRoleDesc` varchar(128) NOT NULL COMMENT '会员卡特权说明',
  `MemberCardUseDesc` varchar(128) NOT NULL COMMENT '会员卡使用须知',
  `Remark` varchar(128) COMMENT '备注',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`MchId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户会员优惠配置信息';

-- 14. 商户会员充值规则表
DROP TABLE IF EXISTS `t_mch_member_recharge_rule`;
CREATE TABLE `t_mch_member_recharge_rule` (
  `RuleId` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '规则ID',
  `MchId` bigint(20) NOT NULL COMMENT 'MchId',
  `RechargeAmount` bigint(20) NOT NULL COMMENT '充值金额',
  `RuleType` tinyint(6) NOT NULL COMMENT '赠送规则, 1-赠余额, 2-赠积分, 3-送优惠券',
  `GivePoints` bigint(20) COMMENT '赠送返点, 余额/积分',
  `GiveCouponId` bigint(20) COMMENT '赠送优惠券ID',
  `FirstFlag` tinyint(6) NOT NULL COMMENT '推荐标识: 0-否, 1-是',
  `Status` tinyint(6) NOT NULL COMMENT '状态:0-暂停使用 1-正常 ',
  `Remark` varchar(128) COMMENT '备注',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`RuleId`)
) ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8mb4 COMMENT='商户会员充值规则表';

-- 15. 商户粉丝表
DROP TABLE IF EXISTS `t_mch_fans`;
CREATE TABLE `t_mch_fans` (
  `MchId` bigint(20) NOT NULL COMMENT 'MchId',
  `WxOpenId` varchar(128) NOT NULL COMMENT '微信openId',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`MchId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户粉丝表';


-- 新支付通道表
DROP TABLE IF EXISTS t_pay_passage;
CREATE TABLE `t_pay_passage` (
                               `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '支付通道ID',
                               `BelongInfoId` bigint(20) NOT NULL COMMENT '所属角色ID',
                               `BelongInfoType` tinyint(6) NOT NULL COMMENT '所属角色类型:1-商户 2-代理商 3-平台 4-服务商',
                               `PassageName` varchar(30) NOT NULL COMMENT '通道名称',
                               `IfTypeCode` varchar(30) NOT NULL COMMENT '接口类型代码',
                               `Status` tinyint(6) NOT NULL DEFAULT '1' COMMENT '通道状态,0-关闭,1-开启',
                               `MaxDayAmount` bigint(20) DEFAULT NULL COMMENT '当天交易金额,单位分',
                               `MaxEveryAmount` bigint(20) DEFAULT NULL COMMENT '单笔最大金额,单位分',
                               `MinEveryAmount` bigint(20) DEFAULT NULL COMMENT '单笔最小金额,单位分',
                               `TradeStartTime` varchar(20) DEFAULT NULL COMMENT '交易开始时间',
                               `TradeEndTime` varchar(20) DEFAULT NULL COMMENT '交易结束时间',
                               `RiskStatus` tinyint(6) NOT NULL DEFAULT '0' COMMENT '风控状态,0-关闭,1-开启',
                               `ContractStatus` tinyint(6) NOT NULL DEFAULT '0' COMMENT '签约状态,0-未开通,1-待审核,2-审核不通过,3-已签约',
                               `IsvParam` varchar(4096) COMMENT 'isv账户配置参数,json字符串',
                               `MchParam` varchar(4096) COMMENT '商户账户配置参数,json字符串',
                               `Remark` varchar(128) DEFAULT NULL COMMENT '备注',
                               `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                               PRIMARY KEY (`ID`),
                               UNIQUE KEY `Uni_IfTypeCode` (`BelongInfoId`, `BelongInfoType`, `IfTypeCode`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4 COMMENT='支付通道表';

-- 服务商表
DROP TABLE IF EXISTS t_isv_info;
CREATE TABLE `t_isv_info` (
                            `IsvId` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '服务商ID',  -- 服务商基本信息
                            `IsvName` varchar(30) NOT NULL COMMENT '服务商名称',
                            `LoginUserName` varchar(50) NOT NULL COMMENT '登录用户名',   -- 服务商联系人信息及地址等信息
                            `Mobile` varchar(12) NOT NULL COMMENT '登录手机号（联系人手机号）',
                            `Email` varchar(32) NOT NULL COMMENT '登录邮箱（联系人邮箱）',
                            `RegisterPassword` varchar(100) DEFAULT NULL COMMENT '注册密码，仅在服务商注册时使用',
                            `RealName` varchar(30) NOT NULL COMMENT '用户真实姓名',
                            `IdCard` varchar(32) NOT NULL COMMENT '联系人身份证号',
                            `Qq` varchar(32) COMMENT '联系人QQ号码',
                            `Address` varchar(128) NOT NULL COMMENT '服务商通讯地址',
                            `Domain` varchar(128) COMMENT '域名',
                            `SystemTitle` varchar(30) COMMENT '系统名称',
                            `SupportName` varchar(30) COMMENT '技术支持姓名',
                            `SupportTel` varchar(32) COMMENT '技术支持电话',
                            `Status` tinyint(6) NOT NULL COMMENT '状态:0-停止使用,1-使用中, -1 等待审核, -2 审核不通过',
                            `Remark` varchar(128) DEFAULT NULL COMMENT '备注',
                            `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            PRIMARY KEY (`IsvId`),
                            UNIQUE KEY `Uni_LoginUserName` (`LoginUserName`),
                            UNIQUE KEY `Uni_Mobile` (`Mobile`),
                            UNIQUE KEY `Uni_Email` (`Email`)
) ENGINE=InnoDB AUTO_INCREMENT=40000000 DEFAULT CHARSET=utf8mb4 COMMENT='服务商信息表';


-- 商户用户登录设备信息（用户客户端消息推送）
DROP TABLE IF EXISTS t_mch_device;
CREATE TABLE `t_mch_device` (
                            `Cid` varchar(64) NOT NULL COMMENT '设备号CID',
                            `DeviceInfo` varchar(512) DEFAULT NULL COMMENT '设备信息',
                            `UserId` bigint(20) NOT NULL COMMENT '用户ID',
                            `LoginTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
                            PRIMARY KEY (`Cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户用户登录设备信息';

-- 门店与云喇叭关联表
DROP TABLE IF EXISTS `t_mch_store_speaker`;
CREATE TABLE `t_mch_store_speaker` (
  `StoreId` BIGINT(20) NOT NULL COMMENT '门店ID',
  `SpeakerId` BIGINT(10) NOT NULL COMMENT '云喇叭ID',
  `Status` TINYINT(6) NOT NULL COMMENT '状态,0-未绑定,1-已绑定, 2-已解绑',
  `MoneyCode` BIGINT(20) DEFAULT NULL COMMENT '设备验证金额',
  `CreateTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`StoreId`, `SpeakerId`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='门店与云喇叭关联表';

-- 服务商云喇叭配置表
DROP TABLE IF EXISTS `t_isv_speaker_config`;
CREATE TABLE `t_isv_speaker_config` (
  `ID` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `IsvId` BIGINT(20) NOT NULL COMMENT '服务商ID',
  `UserId` VARCHAR(32) NOT NULL COMMENT '客户编号',
  `UserPassword` VARCHAR(32) NOT NULL COMMENT '客户密码',
  `Status` TINYINT(6) NOT NULL COMMENT '状态,0-关闭，1-启用',
  `Token` VARCHAR(128) DEFAULT NULL COMMENT '携带token',
  `TokenExpire` int(11) DEFAULT '0' COMMENT 'token过期时间 分钟',
  `Manufacturer` VARCHAR(32) DEFAULT NULL COMMENT '厂商',
  `CreateTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`ID`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='服务商云喇叭配置表';

-- 服务商与打印机配置表
DROP TABLE IF EXISTS `t_isv_printer_config`;
CREATE TABLE `t_isv_printer_config` (
  `IsvId` BIGINT(20) NOT NULL COMMENT '服务商ID',
  `AccessKey` VARCHAR(128) DEFAULT NULL COMMENT '打印机key',
  `AccessSecret` VARCHAR(128) DEFAULT NULL COMMENT '打印机秘钥',
  `Status` TINYINT(6) NOT NULL COMMENT '状态,0-关闭,1-开启',
  `Token` VARCHAR(128) DEFAULT NULL COMMENT '携带token',
  `Manufacturer` VARCHAR(32) DEFAULT NULL COMMENT '厂商',
  `TokenExpire` DATETIME DEFAULT NULL COMMENT 'token失效时间',
  `CreateTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`IsvId`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='服务商与打印机配置表';

-- 门店与打印机关联表
DROP TABLE IF EXISTS `t_mch_store_printer`;
CREATE TABLE `t_mch_store_printer` (
  `StoreId` BIGINT(20) NOT NULL COMMENT '门店ID',
  `PrinterId` VARCHAR(32) DEFAULT NULL COMMENT '打印机ID',
  `Status` TINYINT(6) NOT NULL COMMENT '状态,0-关闭,1-开启',
  `CreateTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`StoreId`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='门店与打印机关联表';

-- 商户用户配置表（APP配置）
DROP TABLE IF EXISTS t_mch_app_config;
CREATE TABLE `t_mch_app_config` (
	`UserId` bigint(20) NOT NULL COMMENT '用户ID',
	`AppPush` tinyint(6) NOT NULL DEFAULT '1' COMMENT 'app通知(0-关闭,1-开启)',
	`AppVoice` tinyint(6) NOT NULL DEFAULT '1' COMMENT 'app语音提醒(0-关闭,1-开启)',
	`CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户用户配置表';


-- 服务商结算配置表
DROP TABLE IF EXISTS t_isv_sett_config;
CREATE TABLE `t_isv_sett_config` (
	`IsvId` bigint(20) NOT NULL COMMENT 'IsvId',
	`SettDateType` tinyint(6) NOT NULL COMMENT '结算周期类型 1-按照时间, 2-指定每月日期',
	`SettSetDay` int NOT NULL COMMENT '结算设置天数',
	`PrevSettDate` datetime COMMENT '上次跑批时间',
	`NextSettDate` datetime COMMENT '下次跑批时间',
	`CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (`IsvId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务商结算配置表';

-- 服务商结算跑批任务表
DROP TABLE IF EXISTS t_order_profit_task;
CREATE TABLE `t_order_profit_task` (
    `TaskId` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'BatchId, 跑批任务ID',
	`IsvId` bigint(20) NOT NULL COMMENT 'IsvId',
	`AgentId` bigint(20) COMMENT '结算代理商ID, null为服务商本身分佣统计',
	`AgentPid` bigint(20) COMMENT '结算代理商父ID, null为服务商本身分佣统计',
	`AgentName` varchar(64) COMMENT '代理商名称',
	`TradeFirstDate` datetime COMMENT '交易最早时间',
	`TradeLastDate` datetime COMMENT '交易最晚时间',
    `SettConfigSnapshot` varchar(64) COMMENT '服务商结算配置快照',
    `AllTradeCount` bigint(20) NOT NULL COMMENT '全部交易总笔数',
    `AllTradeAmount` bigint(20) NOT NULL COMMENT '全部交易总金额,单位分',
    `AllTradeProfitAmount` bigint(20) NOT NULL COMMENT '全部交易总返佣金额,单位分',
    `WxTradeCount` bigint(20) NOT NULL COMMENT '微信交易总笔数',
    `WxTradeAmount` bigint(20) NOT NULL COMMENT '微信交易总金额,单位分',
    `WxTradeProfitAmount` bigint(20) NOT NULL COMMENT '微信交易总返佣金额,单位分',
    `AlipayTradeCount` bigint(20) NOT NULL COMMENT '支付宝交易总笔数',
    `AlipayTradeAmount` bigint(20) NOT NULL COMMENT '支付宝交易总金额,单位分',
    `AlipayTradeProfitAmount` bigint(20) NOT NULL COMMENT '支付宝交易总返佣金额,单位分',
    `SettStatus` tinyint(6) NOT NULL COMMENT '结算状态 -1 无需结算 0-待结算 1-已结算',
    `SettDate` datetime COMMENT '结算时间',
    `SettImg` varchar(256) COMMENT '结算支付凭证图片',
	`CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (`TaskId`)
) ENGINE=InnoDB AUTO_INCREMENT=100001 DEFAULT CHARSET=utf8mb4 COMMENT='服务商结算跑批任务表';


-- 地区编码表（区/县 级别）
DROP TABLE IF EXISTS `t_sys_area_code`;
CREATE TABLE `t_sys_area_code` (
  `AreaCode` int NOT NULL COMMENT '地区编码',
  `AreaName` varchar(30) NOT NULL COMMENT '地区名称',
  `ProvinceCode` int NOT NULL COMMENT '省编码',
  `CityCode` int NOT NULL COMMENT '市编码',
  PRIMARY KEY (`AreaCode`),
  KEY `KEY_CityCode` (`CityCode`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COMMENT='地区编码表（区/县 级别）';

-- 地区编码表（市级别）
DROP TABLE IF EXISTS `t_sys_city_code`;
CREATE TABLE `t_sys_city_code` (
  `CityCode` int NOT NULL COMMENT '市编码',
  `CityName` varchar(30) NOT NULL COMMENT '市名称',
  `ProvinceCode` int NOT NULL COMMENT '省编码',
  PRIMARY KEY (`CityCode`),
  KEY `KEY_provinceCode` (`ProvinceCode`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COMMENT='地区编码表（市级别）';

-- 地区编码表（省级别）
DROP TABLE IF EXISTS `t_sys_province_code`;
CREATE TABLE `t_sys_province_code` (
  `ProvinceCode` int NOT NULL COMMENT '省编码',
  `provinceName` varchar(30) NOT NULL COMMENT '省名称',
  PRIMARY KEY (`ProvinceCode`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COMMENT='地区编码表（省级别）';

-- 行业编码表
DROP TABLE IF EXISTS `t_sys_industry_code`;
CREATE TABLE `t_sys_industry_code` (
  `IndustryCode` int NOT NULL COMMENT '行业编码',
  `IndustryName` varchar(30) NOT NULL COMMENT '行业名称',
  `IndustryDesc` varchar(100) NOT NULL COMMENT '行业描述',
  `ParentCode` int NOT NULL COMMENT '上级编码Code, 等级编码父ID为0',
  PRIMARY KEY (`IndustryCode`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COMMENT='行业编码表';

-- 服务商广告配置表
DROP TABLE IF EXISTS `t_isv_advert_config`;
CREATE TABLE `t_isv_advert_config` (
  `ID` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `IsvId` BIGINT(20) NOT NULL COMMENT '服务商ID',
  `AdvertName` VARCHAR(32) NOT NULL COMMENT '广告名称',
  `ShowType` TINYINT(6) NOT NULL COMMENT '显示位置：1-移动端支付成功页, 2-刷脸设备支付成功页, 3-商家APP轮播图片, 4-刷脸设备投屏广告',
  `Status` TINYINT(6) NOT NULL COMMENT '状态,0-关闭，1-启用',
  `PutMch` TINYINT(6) NOT NULL COMMENT '投放商户,-1:所有商户，1-一级子商户，2-二级子商户，3-三级子商户',
  `ProvinceCode` int NOT NULL COMMENT '投放省 -1全部省',
  `CityCode` int NOT NULL COMMENT '投放市 -1全部市',
  `AreaCode` int NOT NULL COMMENT '投放县 -1全部县',
  `MediaType` TINYINT(6) NOT NULL COMMENT '1-图片, 2-视频',
  `MediaPath` varchar(128) NOT NULL COMMENT '资源路径',
  `AdvertUrl` varchar(128) COMMENT '广告跳转链接',
  `BeginTime` datetime NOT NULL COMMENT '开始时间',
  `EndTime` datetime NOT NULL COMMENT '结束时间',
  `SortNum` int NOT NULL default 0 COMMENT '排序数值, 降序顺序',
  `Remark` varchar(128) COMMENT '备注',
  `CreateTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务商广告配置表';

-- 客户端更新表
DROP TABLE IF EXISTS t_sys_client_version;
CREATE TABLE `t_sys_client_version` (
	 `Vid` INT (11) NOT NULL AUTO_INCREMENT COMMENT 'version ID',
	 `VersionName` VARCHAR(50) NOT NULL COMMENT '版本名称',
	 `versionSN` VARCHAR(50) NOT NULL COMMENT '版本序列号',
	 `versionDesc` VARCHAR(200) NOT NULL COMMENT '版本描述信息',
	 `ForceUpdate` TINYINT(6) NOT NULL COMMENT '是否需要强制更新 1-是, 0-否',
	 `DownloadUrl` VARCHAR(200) NOT NULL COMMENT '下载地址',
	 `FileSize` VARCHAR(10) NOT NULL COMMENT '文件大小，单位：M ',
	 `ClientType` VARCHAR(20) NOT NULL COMMENT '客户端类型：FACE_CLIENT: 刷脸设备, MCH_APP: 商家手机客户端, PC_PLUGIN: 商家PC收银插件',
     `CreateTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	 `UpdateTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	 PRIMARY KEY (`Vid`),
	 UNIQUE KEY `Uni_Client_VersionSN` (`versionSN`, `ClientType`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '客户端更新表';

-- 服务商第三方配置表
DROP TABLE IF EXISTS `t_isv_wx3rd_info`;
CREATE TABLE `t_isv_wx3rd_info` (
  `IsvId` bigint(20) NOT NULL COMMENT 'IsvId',
  `ConfigAuthHost` varchar(100) NOT NULL COMMENT '服务商配置项：授权发起页域名',
  `ConfigTestMpAccount` varchar(100) NOT NULL COMMENT '服务商配置项：授权测试公众号列表',
  `ConfigAuthMsgUrl` varchar(100) NOT NULL COMMENT '服务商配置项：授权事件接收URL',
  `ConfigMsgToken` varchar(100) NOT NULL COMMENT '服务商配置项：消息校验Token',
  `ConfigAesKey` varchar(100) NOT NULL COMMENT '服务商配置项：消息加解密Key',
  `ConfigNormalMsgUrl` varchar(100) NOT NULL COMMENT '服务商配置项：消息与事件接收URL',
  `ConfigMiniHost` varchar(100) NOT NULL COMMENT '服务商配置项：小程序服务器域名',
  `ConfigMiniBizHost` varchar(100) NOT NULL COMMENT '服务商配置项：小程序业务域名',
  `ConfigWhiteIp` varchar(100) NOT NULL COMMENT '服务商配置项：白名单IP地址列表',
  `ConfigWxCheckFileName` varchar(100) COMMENT '服务商配置项：服务商上传的微信校验文件名',
  `ConfigWxCheckFileValue` varchar(100) COMMENT '服务商配置项：服务商上传的微信校验文件内容',
  `ComponentAppId` varchar(100) COMMENT '开放平台appId',
  `ComponentAppSecret` varchar(100) COMMENT '开放平台appSecret',
  `ComponentVerifyTicket` varchar(256) COMMENT '开放平台最新有效的ticket',
  `Status` tinyint(6) NOT NULL COMMENT '状态:0-暂停使用, 1-待录入信息, 2-账号信息待验证, 3-验证通过, 4-验证错误',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`IsvId`),
  UNIQUE KEY `Wx_Check_Key` (`ConfigWxCheckFileName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务商第三方配置表';

-- 商户第三方授权信息表
DROP TABLE IF EXISTS `t_mch_wxauth_info`;
CREATE TABLE `t_mch_wxauth_info` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `MchId` bigint(20) NOT NULL COMMENT 'MchId',
  `IsvId` bigint(20) NOT NULL COMMENT 'IsvId',
  `AuthAppId` varchar(100) DEFAULT NULL COMMENT '公众号/小程序授权appId',
  `AuthRefreshToken` varchar(256) DEFAULT NULL COMMENT '公众号/小程序授权刷新accessToken',
  `AuthFuncInfo` varchar(300) DEFAULT NULL COMMENT '公众号/小程序授权集合列表 格式： [1,2,3]',
  `AuthStatus` tinyint(6) NOT NULL COMMENT '公众号/小程序授权状态：0-已取消授权, 1-已授权, 3-授权不符合要求，4-已注册',
  `AuthFrom` tinyint(6) NOT NULL COMMENT '公众号/小程序授权来源，1-点餐小程序',
  `AuthType` tinyint(6) NOT NULL COMMENT '授权账号类型：1-公众号，2-小程序',
  `OpenAppId` varchar(100) DEFAULT NULL COMMENT '开放平台AppId',
  `LegalPersonaName` varchar(32) DEFAULT NULL COMMENT '法人姓名',
  `LegalPersonaWechat` varchar(64) DEFAULT NULL COMMENT '法人微信',
  `BussinessName` varchar(64) DEFAULT NULL COMMENT '企业名称',
  `BussinessCode` varchar(64) DEFAULT NULL COMMENT '信用代码',
  `WxCheckFileName` varchar(100) DEFAULT NULL COMMENT '配置二维码规则，微信校验文件名',
  `WxCheckFileValue` varchar(100) DEFAULT NULL COMMENT '配置二维码规则，微信校验文件内容',
  `Prefix` varchar(100) DEFAULT NULL COMMENT '二维码规则',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `IDX_MchId_AuthFrom` (`MchId`,`AuthFrom`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户第三方授权信息表';


-- 新增商户授权小程序版本管理记录表
DROP TABLE IF EXISTS `t_mch_mini_version`;
CREATE TABLE `t_mch_mini_version` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `MchId` bigint(20) NOT NULL COMMENT '商户Id',
  `IsvId` bigint(20) NOT NULL COMMENT '服务商Id',
  `AuthFrom` tinyint(6) NOT NULL COMMENT '小程序授权来源，1-点餐小程序',
  `AuthAppId` varchar(100) NOT NULL COMMENT '授权小程序AppId',
  `MiniVersion` varchar(20) NOT NULL COMMENT '版本号',
  `MiniDesc` varchar(200) DEFAULT NULL COMMENT '版本描述',
  `Develop` varchar(32) DEFAULT NULL COMMENT '开发者',
  `CommitTime` datetime DEFAULT NULL COMMENT '代码提交时间',
  `AuditTime` datetime DEFAULT NULL COMMENT '提交审核时间',
  `ReleaseTime` datetime DEFAULT NULL COMMENT '发布时间',
  `AuditId` varchar(64) DEFAULT NULL COMMENT '审核编号',
  `VersionStatus` tinyint(6) NOT NULL COMMENT '小程序状态:  1-开发版本 2-审核版本 3-线上版本',
  `AuditStatus` tinyint(6) DEFAULT NULL COMMENT '审核状态:  0-审核成功 1-审核被拒绝 2-审核中 3-已撤回 4-审核延后',
  `RefusedReason` varchar(1024) DEFAULT NULL COMMENT '拒绝/延后原因',
  `RefusedScreenShot` varchar(512) DEFAULT NULL COMMENT '审核失败的小程序截图示例',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `IDX_MchId_AuthFrom_AuthAppId_VersionStatus` (`MchId`, `AuthFrom`, `AuthAppId`, `VersionStatus`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户授权小程序版本管理记录表';


###  商户操作员交班表
DROP TABLE IF EXISTS t_mch_operator_handover;
CREATE TABLE `t_mch_operator_handover` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `UserId` bigint(20) NOT NULL COMMENT '操作员ID',
  `UserName` varchar(100) NOT NULL COMMENT '操作员名称',
  `StoreId` bigint(20) NOT NULL COMMENT '门店ID',
  `MchId` bigint(20) NOT NULL COMMENT '商户ID',
  `WorkStartTime` datetime NOT NULL COMMENT '上班时间',
  `WorkEndTime` datetime NOT NULL COMMENT '交班时间',
  `CountTotalOrder` bigint(20) NOT NULL COMMENT '订单总数量',
  `SumRechargeAmount` bigint(20) NOT NULL COMMENT '充值金额, 单位: 分',
  `SumDiscountAmount` bigint(20) NOT NULL COMMENT '优惠金额, 单位: 分',
  `SumCashAmount` bigint(20) NOT NULL COMMENT '实收现金, 单位: 分',
  `SumRefundAmount` bigint(20) NOT NULL COMMENT '退款金额, 单位: 分',
  `SumRealAmount` bigint(20) NOT NULL COMMENT '实际收款, 单位: 分',
  `Remark` varchar(200) COMMENT '备注',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8mb4 COMMENT='商户操作员交班记录表';



###  服务商硬件设备表
DROP TABLE IF EXISTS t_isv_device;
CREATE TABLE `t_isv_device` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `DeviceName` varchar(200) NOT NULL COMMENT '设备名称',
  `DeviceNo` varchar(200) NOT NULL COMMENT '设备编号',
  `DeviceType` tinyint(6) NOT NULL COMMENT '设备类型 1-微信 2-支付宝',
  `MchId` bigint(20) NOT NULL COMMENT '绑定商户ID， 不绑定为0',
  `MchName` varchar(100) COMMENT '商户名称',
  `StoreId` bigint(20) NOT NULL COMMENT '绑定门店ID， 不绑定为0',
  `StoreName` varchar(100) COMMENT '门店名称',
  `IsvId` bigint(20) NOT NULL COMMENT '服务商ID',
  `Status` tinyint(6) NOT NULL COMMENT '状态:0-设备停用, 1-设备启用',
  `Remark` varchar(200) COMMENT '备注',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `Uni_DeviceNo` (`IsvId`, `DeviceType`, `DeviceNo`)
) ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8mb4 COMMENT='服务商硬件设备表';




-- 微信商户入驻快照信息
DROP TABLE IF EXISTS t_wx_mch_snapshot;
CREATE TABLE `t_wx_mch_snapshot` (
  `ApplyNo` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '申请编号',
  `MchId` bigint(20) NOT NULL COMMENT '商户ID',
  `IsvId` bigint(20) NOT NULL COMMENT '服务商ID',
  `ApplyStatus` tinyint(6) NOT NULL DEFAULT 0 COMMENT '小微商户状态：0-未入驻, 1-小微商户审核中, 2-小微商户审核通过, 3-待商户签约, 4-请求异常/微信驳回, 5-待账户验证, 6-已作废, 7-编辑中',
  `AuditInfo` varchar(512) COMMENT '小微商户审核信息',
  `ApplyType` tinyint(6) NOT NULL COMMENT '进件商户类型：1-特约商户进件，2-收付通小微进件',
  `IdCardCopy` varchar(256) NOT NULL COMMENT '身份证人像面照片',
  `IdCardNational` varchar(256) NOT NULL COMMENT '身份证国徽面照片',
  `IdCardName` varchar(30) NOT NULL COMMENT '身份证姓名',
  `IdCardNumber` varchar(32) NOT NULL COMMENT '身份证号码',
  `AccountName` varchar(30) NOT NULL COMMENT '开户名称',
  `AccountBank` varchar(64) NOT NULL COMMENT '开户银行',
  `BankAddressCode` varchar(30) NOT NULL COMMENT '开户银行省市编码',
  `BankName` varchar(30) NOT NULL COMMENT '开户银行全称（含支行）',
  `AccountNumber` varchar(30) NOT NULL COMMENT '银行账号',
  `StoreName` varchar(30) NOT NULL COMMENT '门店名称',
  `StoreUrl` varchar(256) COMMENT '店铺链接',
  `StoreAddressCode` varchar(30) DEFAULT NULL COMMENT '门店省市编码',
  `StoreStreet` varchar(64) DEFAULT NULL COMMENT '门店街道名称',
  `StoreEntrancePic` varchar(256) DEFAULT NULL COMMENT '门店门口照片',
  `IndoorPic` varchar(256) DEFAULT NULL COMMENT '店内环境照片',
  `MerchantShortName` varchar(30) NOT NULL COMMENT '商户简称',
  `ServicePhone` varchar(30) DEFAULT NULL COMMENT '客服电话',
  `BusinessAdditionDesc` varchar(64) DEFAULT NULL COMMENT '补充说明',
  `BusinessAdditionPics` varchar(256) DEFAULT NULL COMMENT '补充材料',
  `ContactType` varchar(30) COMMENT '超级管理员类型',
  `Contact` varchar(30) NOT NULL COMMENT '超级管理员姓名',
  `ContactIdCardNo` varchar(30) COMMENT '超级管理员身份证号',
  `ContactPhone` varchar(30) NOT NULL COMMENT '手机号码',
  `ContactEmail` varchar(30) COMMENT '联系邮箱',
  `WxApplymentId` varchar(30) COMMENT '微信返回申请单号',
  `WxApplymentMchQrImg` varchar(256) COMMENT '微信返回商户进件二维码图片地址',
  `WxMchId` varchar(30) COMMENT '微信返回商户编号',
  `OrganizationType` varchar(512) COMMENT '主体类型 SUBJECT_TYPE_INDIVIDUAL（个体户） SUBJECT_TYPE_ENTERPRISE（企业）',
  `BusinessLicenseCopy` varchar(512) COMMENT '营业执照图片',
  `BusinessLicenseNumber` varchar(100) COMMENT '营业执照注册号',
  `BusinessMerchantName` varchar(64) COMMENT '营业执照上的商户名称',
  `LegalPerson` varchar(64) COMMENT '经营者姓名/法定代表人',
  `SalesSceneType` varchar(100) COMMENT '经营场景类型 线下门店：SALES_SCENES_STORE',
  `MpAppid` varchar(256) COMMENT '公众号APPID',
  `SettlementId` varchar(64) COMMENT '入驻结算规则ID',
  `QualificationType` varchar(256) COMMENT '所属行业',
  `BankAccountType` varchar(100) COMMENT '账户类型 BANK_ACCOUNT_TYPE_CORPORATE：对公银行账户  BANK_ACCOUNT_TYPE_PERSONAL：经营者个人银行卡',
  `IdCardValidStartTime` varchar(64) COMMENT '身份证有效期开始时间',
  `IdCardValidEndTime` varchar(64) COMMENT '身份证有效期结束时间',
  `Qualifications` varchar(1024) COMMENT '特殊资质， 图片集合 []',
  PRIMARY KEY (`ApplyNo`),
  UNIQUE KEY(`MchId`)
) ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8mb4 COMMENT='微信商户入驻快照信息';



-- 微信商户升级快照
DROP TABLE IF EXISTS t_wx_mch_upgrade_snapshot;
CREATE TABLE `t_wx_mch_upgrade_snapshot` (
  `ApplyNo` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '申请编号',
  `MchId` bigint(20) NOT NULL COMMENT '商户ID',
  `IsvId` bigint(20) NOT NULL COMMENT '服务商ID',
  `ApplyStatus` tinyint(6) NOT NULL DEFAULT 0 COMMENT '普通商户状态：0-未升级, 1-微信审核中, 2-普通商户升级完成, 3-待签约, 4-待账户验证, 5-请求异常/微信驳回',
  `AuditInfo` varchar(512) COMMENT '普通商户审核信息',
  `SubMchId` varchar(512) COMMENT '小微商户号',
  `OrganizationType` varchar(512) COMMENT '主体类型 2-企业 4-个体工商户 3-党政、机关及事业单位  1708-其他组织',
  `BusinessLicenseCopy` varchar(512) COMMENT '营业执照扫描件图片',
  `BusinessLicenseNumber` varchar(100) COMMENT '营业执照注册号',
  `MerchantName` varchar(64) COMMENT '商户名称',
  `CompanyAddress` varchar(128) COMMENT '注册地址',
  `LegalPerson` varchar(64) COMMENT '经营者姓名/法定代表人',
  `BusinessTime` varchar(32) COMMENT '营业期限',
  `BusinessLicenceType` varchar(10) COMMENT '营业执照类型 1762-已三证合一 1763-未三证合一',
  `OrganizationCopy` varchar(256) COMMENT '组织机构代码证照片（未三证合一必填）',
  `OrganizationNumber` varchar(64) COMMENT '组织机构代码（未三证合一必填）',
  `OrganizationTime` varchar(64) COMMENT '组织机构代码有效期限（未三证合一必填）',
  `AccountName` varchar(64) COMMENT '对公账户开户名称',
  `AccountBank` varchar(64) COMMENT '对公账户开户银行',
  `BankAddressCode` varchar(64) COMMENT '开户银行省市编码',
  `BankName` varchar(128) COMMENT '开户银行全称 （含支行）',
  `AccountNumber` varchar(64) COMMENT '银行卡号',
  `MerchantShortname` varchar(64) COMMENT '商户简称',
  `Business` varchar(10) COMMENT '费率结算规则ID',
  `Qualifications` varchar(1024) COMMENT '特殊资质， 图片集合 []',
  `BusinessScene` varchar(100) COMMENT '经营场景 1721-线下  1837-公众号  1838-小程序  1724-APP  1840-PC网站',
  `BusinessAdditionDesc` varchar(512) COMMENT '补充说明',
  `BusinessAdditionPics` varchar(1024) COMMENT '补充材料 图片集合 []',
  `ContactEmail` varchar(64) COMMENT '联系邮箱',
  `MpAppid` varchar(256) COMMENT '公众号APPID',
  `MpAppScreenShots` varchar(1024) COMMENT '公众号页面截图 图片集合 []',
  `MiniprogramAppid` varchar(256) COMMENT '小程序APPID',
  `MiniprogramScreenShots` varchar(1024) COMMENT '小程序页面截图 图片集合 []',
  `AppAppid` varchar(256) COMMENT '应用APPID',
  `AppScreenShots` varchar(1024) COMMENT 'APP截图 图片集合 []',
  `AppDownloadUrl` varchar(256) COMMENT 'APP下载链接',
  `WebUrl` varchar(256) COMMENT 'PC网站域名',
  `WebAuthoriationLetter` varchar(512) COMMENT '网站授权函 图片',
  `WebAppid` varchar(512) COMMENT 'PC网站对应的公众号APPID',
  `WxApplymentMchQrImg` varchar(256) COMMENT '微信返回商户签约二维码图片地址',
  `AccountVerifyInfo` varchar(1024) COMMENT '账户验证信息',
  `WxMchId` varchar(30) COMMENT '微信返回商户编号',
  PRIMARY KEY (`ApplyNo`),
  UNIQUE KEY(`MchId`)
) ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8mb4 COMMENT='微信商户升级快照';

-- 支付宝商户入驻快照信息
DROP TABLE IF EXISTS t_alipay_mch_snapshot;
CREATE TABLE `t_alipay_mch_snapshot` (
  `ApplyNo` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '申请编号',
  `MchId` bigint(20) NOT NULL COMMENT '商户ID',
  `IsvId` bigint(20) NOT NULL COMMENT '服务商ID',
  `ApplyStatus` tinyint(6) NOT NULL DEFAULT 0 COMMENT '支付宝申请状态: 0-初始态, 1-暂存, 2-支付宝审核中, 3-待商户确认, 4-商户确认成功, 5-商户超时未确认, 6-审核失败或商户拒绝',
  `AuditInfo` varchar(512) COMMENT '支付宝审核信息',
  `AlipayAccount` varchar(64) COMMENT '支付宝账号',
  `ContactName` varchar(30) NOT NULL COMMENT '联系人姓名',
  `ContactMobile` varchar(30) NOT NULL COMMENT '联系人手机号码',
  `ContactEmail` varchar(30) COMMENT '联系人邮箱',
  `MccCode` varchar(30) COMMENT 'mcc编号',
  `Rate` varchar(10) NOT NULL COMMENT '费率',
  `BusinessLicenseNo` varchar(32) COMMENT '营业执照编号',
  `DateLimitation` varchar(32) COMMENT '营业执照截止日期: 格式为：yyyy-MM-dd',
  `LongTerm` tinyint(6) COMMENT '是否长期有效0-否, 1-是',
  `BusinessLicensePic` varchar(256) COMMENT '营业执照图片',
  `SpecialLicensePic` varchar(256) COMMENT '企业特殊资质图片',
  `ShopSignBoardPic` varchar(256) COMMENT '门店门口照片',
  `BatchNo` varchar(64) COMMENT '支付宝返回的事务ID',
  `UserId` varchar(30) COMMENT '支付宝返回的商户UID',
  `AuthAppId` varchar(64) COMMENT '支付宝返回的商户appid',
  `AppAuthToken` varchar(128) COMMENT '支付宝返回的商户appAuthToken',
  `AppRefreshToken` varchar(128) COMMENT '支付宝返回的商户刷新令牌',
  `ExpiresIn` varchar(30) COMMENT '支付宝应用授权有效期',
  `ReExpiresIn` varchar(30) COMMENT '支付宝刷新令牌有效期',
  `AgentAppId` varchar(64) COMMENT '支付宝代理创建的appId',
  `MerchantPid` varchar(64) COMMENT '支付宝返回的商户pid',
  `ConfirmUrl` varchar(256) COMMENT '支付宝确认签约链接',
  PRIMARY KEY (`ApplyNo`),
  UNIQUE KEY(`MchId`)
) ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8mb4 COMMENT='支付宝商户入驻快照信息';

-- 支付宝类目编码表
DROP TABLE IF EXISTS t_sys_alipay_industry_code;
CREATE TABLE `t_sys_alipay_industry_code` (
  `Id` int(11) NOT NULL COMMENT 'ID',
  `IndustryName` varchar(30) NOT NULL COMMENT '行业名称',
  `IndustryDesc` varchar(100) DEFAULT NULL COMMENT '行业描述',
  `ParentCode` int(11) DEFAULT 0 COMMENT '上级编码ID',
  `MccCode` varchar(100) DEFAULT NULL COMMENT '经营类目编码',
  PRIMARY KEY (`Id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COMMENT='支付宝类目编码表';

DROP TABLE IF EXISTS t_sys_wxpay_industry_code;
CREATE TABLE `t_sys_wxpay_industry_code` (
  `Id` int(11) NOT NULL COMMENT 'ID',
  `IndustryName` varchar(30) NOT NULL COMMENT '行业名称',
  `IndustryDesc` varchar(512) DEFAULT NULL COMMENT '特殊资质要求',
  `Rate` varchar(30) DEFAULT NULL COMMENT '费率',
  `SettRule` varchar(100) DEFAULT NULL COMMENT '结算规则',
  `ParentCode` int(11) DEFAULT 0 COMMENT '上级规则ID',
  `CategoryCode` varchar(100) DEFAULT NULL COMMENT '结算规则ID',
  PRIMARY KEY (`Id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COMMENT='微信行业费率表';


-- ##########################################################################################
-- 商城、点餐小程序表结构

-- 商城订单详情表
DROP TABLE IF EXISTS `t_mch_trade_order_detail`;
CREATE TABLE `t_mch_trade_order_detail` (
    `OrderId` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单详情ID',
	`TradeOrderId` varchar(30) NOT NULL COMMENT '交易单号',
	`MemberId` bigint(20) NOT NULL COMMENT '会员ID',
	`GoodsId` bigint(20) NOT NULL COMMENT '商品ID',
	`GoodsName` varchar(128) NOT NULL COMMENT '商品名称',
	`ImgPathMain` varchar(256) DEFAULT NULL COMMENT '主图图片路径',
	`GoodsDesc` text DEFAULT NULL COMMENT '商品描述',
	`CellingPrice` bigint(20) NOT NULL COMMENT '商品售价，单位：分',
	`BuyingPrice` bigint(20) DEFAULT NULL COMMENT '商品进价，单位：分',
	`MemberPrice` bigint(20) DEFAULT NULL COMMENT '商品会员价，单位：分',
	`GoodsNum` int(11) NOT NULL COMMENT '商品数量',
	`GoodsProps` text DEFAULT NULL COMMENT '选择的属性ID集合',
	`GoodsPropsName` text DEFAULT NULL COMMENT '选择的属性分类名称',
	`GoodsPropsValue` text DEFAULT NULL COMMENT '选择的属性值',
  `MchId` bigint(20) NOT NULL COMMENT '所属商户ID',
	`IsvId` bigint(20) NOT NULL COMMENT '服务商ID',
	`CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`OrderId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商城订单详情表';


-- 商户商品表
DROP TABLE IF EXISTS `t_mch_goods`;
CREATE TABLE `t_mch_goods` (
  `GoodsId` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `GoodsName` varchar(128) NOT NULL COMMENT '商品名称',
	`CategoryId` bigint(6) NOT NULL COMMENT '商品分类ID',
	`CategoryName` varchar(64) NOT NULL COMMENT '商品分类名称',
	`SubGoodsId` varchar(1024) DEFAULT NULL COMMENT '子商品ID',
	`IndustryType` tinyint(6) NOT NULL COMMENT '所属行业 1-餐饮商品 2-电商商品',
	`GoodsType` tinyint(6) NOT NULL COMMENT '商品类型 1-单一商品 2-组合商品',
	`GoodsModule` varchar(256) NOT NULL COMMENT '商品所属模块，0-普通商品 1-新品 2-热卖 3-促销 4-限时抢购 5-会员专享',
	`GoodsDesc` text DEFAULT NULL COMMENT '商品描述',
	`GraphicDesc` text DEFAULT NULL COMMENT '图文详情',
	`CellingPrice` bigint(20) NOT NULL COMMENT '商品售价，单位：分',
	`BuyingPrice` bigint(20) DEFAULT NULL COMMENT '商品进价，单位：分',
	`MemberPrice` bigint(20) DEFAULT NULL COMMENT '商品会员价，单位：分',
	`Unit` varchar(20) DEFAULT NULL COMMENT '单位',
	`StockLimitType` tinyint(6) NOT NULL COMMENT '是否限制库存 1-不限库存 2-限制',
	`StockNum` bigint(20) DEFAULT NULL COMMENT '库存数量',
  `ActualSaleNum` bigint(20) NOT NULL DEFAULT 0 COMMENT '实际销量',
	`VirtualSaleNum` bigint(20) DEFAULT NULL COMMENT '虚拟数量',
	`BrowseNumber` int(11) NOT NULL DEFAULT 0 COMMENT '浏览量',
	`EvaluationNumber` int(11) NOT NULL DEFAULT 0 COMMENT '评价数',
	`ProducedBeginTime` datetime DEFAULT NULL COMMENT '生产日期',
	`Expiration` int(11) DEFAULT NULL COMMENT '保质期，单位：天',
	`Supplier` varchar(64) DEFAULT NULL COMMENT '供应商',
	`ImgPathMain` varchar(256) DEFAULT NULL COMMENT '主图图片路径',
	`ImgPathMore` varchar(1024) DEFAULT NULL COMMENT '其他图片路径,最多四张',
  `Status` tinyint(6) NOT NULL COMMENT '状态 0-下架 1-上架 2-售罄',
	`Brand` varchar(64) DEFAULT NULL COMMENT '品牌',
	`GoodsTag` varchar(128) COMMENT '商品标签',
	`BarCode` varchar(32) COMMENT '商品条码',
    `IsRecommend` tinyint(6) NOT NULL DEFAULT 0 COMMENT '是否为精品推荐商品，0-否 1-是',
	`GoodsPropsType` tinyint(6) NOT NULL COMMENT '是否添加属性 0-未添加 1-已添加,详见属性关联表',
	`StoreLimitType` tinyint(6) NOT NULL COMMENT '是否限制门店 0-不限门店 1-限制门店,详见门店关联表',
	`MiniGoodsId` bigint(20) DEFAULT NULL COMMENT '小程序商品库的商品ID',
    `MiniAuditId` bigint(20) DEFAULT NULL COMMENT '小程序商品库的商品审核单ID',
  `MchId` bigint(20) NOT NULL COMMENT '所属商户ID',
	`IsvId` bigint(20) NOT NULL COMMENT '服务商ID',
	`CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`GoodsId`)
) ENGINE=InnoDB AUTO_INCREMENT=100000 DEFAULT CHARSET=utf8mb4 COMMENT='商户商品表';

-- 商户商品属性分类表
DROP TABLE IF EXISTS `t_mch_goods_props_category`;
CREATE TABLE `t_mch_goods_props_category` (
  `PropsCategoryId` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品属性分类ID',
  `PropsCategoryName` varchar(64) NOT NULL COMMENT '商品属性分类名称',
	`MchId` bigint(20) NOT NULL COMMENT '所属商户ID',
	`IsvId` bigint(20) NOT NULL COMMENT '服务商ID',
	`CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`PropsCategoryId`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4 COMMENT='商户商品属性分类表';

-- 商户商品属性表
DROP TABLE IF EXISTS `t_mch_goods_props`;
CREATE TABLE `t_mch_goods_props` (
    `PropsId` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品属性ID',
    `PropsName` varchar(64) NOT NULL COMMENT '商品属性名称',
    `PropsValue` varchar(64) NOT NULL COMMENT '商品属性值',
	`PropsCategoryId` bigint(20) NOT NULL COMMENT '商品属性分类ID',
	`MchId` bigint(20) NOT NULL COMMENT '所属商户ID',
	`IsvId` bigint(20) NOT NULL COMMENT '服务商ID',
	`CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`PropsId`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4 COMMENT='商户商品属性表';

-- 商户商品与属性分类关联表
DROP TABLE IF EXISTS `t_mch_goods_props_category_rela`;
CREATE TABLE `t_mch_goods_props_category_rela` (
  `GoodsId` bigint(20) NOT NULL COMMENT '商品ID',
  `PropsCategoryId` bigint(20) NOT NULL COMMENT '属性分类ID',
  PRIMARY KEY (`GoodsId`, `PropsCategoryId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户商品与属性分类关联表';

-- 商户商品与门店关联表
DROP TABLE IF EXISTS `t_mch_goods_store_rela`;
CREATE TABLE `t_mch_goods_store_rela` (
  `GoodsId` bigint(20) NOT NULL COMMENT '商品ID',
  `StoreId` bigint(20) NOT NULL COMMENT '门店ID',
  PRIMARY KEY (`GoodsId`, `StoreId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户商品与门店关联表';

-- 商品分类表
DROP TABLE IF EXISTS `t_mch_goods_category`;
CREATE TABLE `t_mch_goods_category` (
  `CategoryId` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品分类ID',
	`CategoryName` varchar(64) NOT NULL COMMENT '商品分类名称',
	`CategoryImg` varchar(128) COMMENT '分类页广告图',
    `JumpUrl` varchar(128) DEFAULT NULL COMMENT '跳转链接',
	`CategoryIcon` varchar(128) COMMENT '分类图标',
	`ParentCategoryId` bigint(20) NOT NULL DEFAULT 0 COMMENT '父分类ID，0表示一级分类',
	`AuthFrom` tinyint(6) NOT NULL COMMENT '所属小程序，1-餐饮 2-电商',
	`MchId` bigint(20) NOT NULL COMMENT '所属商户ID',
	`IsvId` bigint(20) NOT NULL COMMENT '服务商ID',
	`CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`CategoryId`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- 购物车表
DROP TABLE IF EXISTS `t_mch_shopping_cart`;
CREATE TABLE `t_mch_shopping_cart` (
    `CartId` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '购物车ID',
	`MemberId` bigint(20) NOT NULL COMMENT '会员ID',
	`GoodsId` bigint(20) NOT NULL COMMENT '商品ID',
	`IsGoodsSelected` tinyint(6) NOT NULL COMMENT '购物车商品是否选中 0-否 1-是',
	`GoodsNum` int(11) NOT NULL COMMENT '商品数量',
	`GoodsProps` text DEFAULT NULL COMMENT '选择的属性ID',
    `IndustryType` tinyint(6) NOT NULL COMMENT '所属行业 1-餐饮 2-电商',
	`MchId` bigint(20) NOT NULL COMMENT '所属商户ID',
	`IsvId` bigint(20) NOT NULL COMMENT '服务商ID',
	`CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`CartId`),
    UNIQUE KEY `IDX_MemberId_GoodsId_GoodsProps` (`MemberId`, `GoodsId`, `GoodsProps`(100))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

-- 收货地址表
DROP TABLE IF EXISTS `t_mch_receive_address`;
CREATE TABLE `t_mch_receive_address` (
  `AddressId` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '收货地址ID',
	`MemberId` bigint(20) NOT NULL COMMENT '会员ID',
	`ContactName` varchar(64) NOT NULL COMMENT '联系人姓名',
	`Tel` varchar(32) NOT NULL COMMENT '手机号',
  `ProvinceCode` int(11) NOT NULL COMMENT '行政地区编号，省',
  `CityCode` int(11) NOT NULL COMMENT '行政地区编号， 市',
  `AreaCode` int(11) NOT NULL COMMENT '行政地区编号， 县',
  `AreaInfo` varchar(128) NOT NULL COMMENT '省市县名称描述',
	`Address` varchar(128) NOT NULL COMMENT '详细地址',
	`isDefaultAddress` tinyint(6) NOT NULL COMMENT '是否为默认地址 0-否 1-是',
	`MchId` bigint(20) NOT NULL COMMENT '所属商户ID',
	`IsvId` bigint(20) NOT NULL COMMENT '服务商ID',
	`CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`AddressId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收货地址表';

-- 商户快递配置表
DROP TABLE IF EXISTS `t_mch_post_config`;
CREATE TABLE `t_mch_post_config` (
  `PostId` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '快递ID',
	`PostName` bigint(20) NOT NULL COMMENT '快递名称',
	`MchId` bigint(20) NOT NULL COMMENT '所属商户ID',
	`IsvId` bigint(20) NOT NULL COMMENT '服务商ID',
	`CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`PostId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户快递配置表';

-- 商户运费模版表
DROP TABLE IF EXISTS `t_mch_freight_template`;
CREATE TABLE `t_mch_freight_template` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '运费模版ID',
	`MchId` bigint(20) NOT NULL COMMENT '所属商户ID',
	`IsvId` bigint(20) NOT NULL COMMENT '服务商ID',
	`TemplateName` varchar(64) NOT NULL COMMENT '模版名称',
	`ValuationType` tinyint(6) NOT NULL COMMENT '计价方式 1-统一收费 2-按体积收费 3-按重量收费',
	`Valuation` bigint(20) DEFAULT NULL COMMENT '运费价格，单位：分',
	`FirstNum` int(11) DEFAULT NULL COMMENT '初始体积',
	`FirstNumValuation` bigint(20) DEFAULT NULL COMMENT '初始体积收费，单位：分',
	`OtherNum` int(11) DEFAULT NULL COMMENT '超出体积',
	`OtherNumValuation` bigint(20) DEFAULT NULL COMMENT '超出体积收费，单位：分',
	`FirstWeight` int(11) DEFAULT NULL COMMENT '首重重量',
	`FirstWeightValuation` bigint(20) DEFAULT NULL COMMENT '首重收费，单位：分',
	`OtherWeight` int(11) DEFAULT NULL COMMENT '续重重量',
	`OtherWeightValuation` bigint(20) DEFAULT NULL COMMENT '续重收费，单位：分',
	`CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户运费模版表';

-- 商户餐饮店区域管理表
DROP TABLE IF EXISTS `t_mch_store_area_manage`;
CREATE TABLE `t_mch_store_area_manage` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '区域ID',
	`MchId` bigint(20) NOT NULL COMMENT '所属商户ID',
	`IsvId` bigint(20) NOT NULL COMMENT '服务商ID',
	`StoreId` bigint(20) NOT NULL COMMENT '门店ID',
	`AreaName` varchar(64) NOT NULL COMMENT '区域名',
	`CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户餐饮店区域管理表';

-- 商户餐饮店营业及配送信息表
DROP TABLE IF EXISTS `t_mch_store_takeout`;
CREATE TABLE `t_mch_store_takeout` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
	`MchId` bigint(20) NOT NULL COMMENT '所属商户ID',
	`IsvId` bigint(20) NOT NULL COMMENT '服务商ID',
	`IsOpen` tinyint(6) NOT NULL COMMENT '是否营业 0-否 1-是',
	`ShopOpenTime` varchar(256) DEFAULT NULL COMMENT '营业时间',
	`ServiceItem` tinyint(6) NOT NULL COMMENT '服务项目，0-堂食&外卖 1-堂食 2-外卖',
	`DeliveryCost` bigint(20) NOT NULL DEFAULT 0 COMMENT '起送费用，单位：分',
	`DistributionCost` bigint(20) NOT NULL DEFAULT 0 COMMENT '配送费用，单位：分',
	`FreeDistribution` bigint(20) DEFAULT NULL COMMENT '免费配送所需订单金额，单位：分',
	`DistributionScope` varchar(32) DEFAULT NULL COMMENT '配送范围，最大半径 单位：KM',
	`DistributionTime` varchar(32) DEFAULT NULL COMMENT '送达时间，单位：分钟',
	`DistributioPlatform` tinyint(6) NOT NULL DEFAULT 1 COMMENT '配送平台,1-自配送',
	`CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
   PRIMARY KEY (`Id`),
   UNIQUE KEY `MchId_IDX` (`MchId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户餐饮店营业及配送信息表';

-- 商户退货原因表
DROP TABLE IF EXISTS `t_mch_return_reason`;
CREATE TABLE `t_mch_return_reason` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '退货原因ID',
	`MchId` bigint(20) NOT NULL COMMENT '所属商户ID',
	`IsvId` bigint(20) NOT NULL COMMENT '服务商ID',
	`Content` varchar(256) NOT NULL COMMENT '名称',
	`Status` tinyint(6) NOT NULL COMMENT '状态 0-禁用 1-启用',
	`CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户退货原因表';

-- 商户常见问题表
DROP TABLE IF EXISTS `t_mch_question`;
CREATE TABLE `t_mch_question` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '常见问题ID',
	`MchId` bigint(20) NOT NULL COMMENT '所属商户ID',
	`IsvId` bigint(20) NOT NULL COMMENT '服务商ID',
	`Title` varchar(64) NOT NULL COMMENT '标题',
	`Content` varchar(2048) DEFAULT NULL COMMENT '正文',
	`Status` tinyint(6) NOT NULL COMMENT '状态 0-禁用 1-启用',
	`CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户常见问题表';

-- 会员与微信openid关联表
DROP TABLE IF EXISTS `t_member_openid_rela`;
CREATE TABLE `t_member_openid_rela` (
  `MemberId` bigint(20) NOT NULL COMMENT '会员ID',
  `WxOpenId` varchar(128) NOT NULL COMMENT '微信OpenId',
  `WxOpenIdFrom` tinyint(6) NOT NULL COMMENT '微信OpenId来源，1-餐饮小程序 ',
  `MchId` bigint(20) NOT NULL COMMENT '商户ID',
  PRIMARY KEY (`MemberId`, `WxOpenId`, `WxOpenIdFrom`, `MchId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员与微信openid关联表';

-- 新增商户小程序轮播图配置表
DROP TABLE IF EXISTS `t_mch_store_banner`;
CREATE TABLE `t_mch_store_banner` (
  `BannerId` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `MchId` bigint(20) NOT NULL COMMENT '商户ID',
  `BannerName` varchar(32) NOT NULL COMMENT '轮播图名称',
  `AuthFrom` tinyint(6) NOT NULL COMMENT '所属小程序：1-点餐  2-商城',
  `ShowType` tinyint(6) NOT NULL COMMENT '显示位置：1-商户小程序首页',
  `Status` tinyint(6) NOT NULL COMMENT '状态,0-关闭，1-启用',
  `PutStoreLimitType` tinyint(6) NOT NULL COMMENT '投放门店 0-全部门店 1-部分门店投放，详见门店关联表',
  `MediaPath` varchar(128) NOT NULL COMMENT '资源路径',
  `AdvertUrl` varchar(128) DEFAULT NULL COMMENT '跳转链接',
  `SortNum` int(11) NOT NULL DEFAULT '0' COMMENT '排序数值, 降序顺序',
  `Remark` varchar(128) DEFAULT NULL COMMENT '备注',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`BannerId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户小程序轮播图配置表';

-- 新增商户小程序轮播图-门店关联表
DROP TABLE IF EXISTS `t_mch_store_banner_rela`;
CREATE TABLE `t_mch_store_banner_rela` (
  `BannerId` bigint(20) NOT NULL COMMENT '轮播图ID',
  `StoreId` bigint(20) NOT NULL COMMENT '门店ID',
  PRIMARY KEY (`BannerId`,`StoreId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户小程序轮播图-门店关联表';

-- 新增商户小程序配置表
DROP TABLE IF EXISTS `t_mch_mini_config`;
CREATE TABLE `t_mch_mini_config` (
  `ConfigId` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `ConfigCode` varchar(20) NOT NULL COMMENT '配置键名',
  `ConfigName` varchar(32) NOT NULL COMMENT '配置名称',
  `Icon` varchar(128) DEFAULT NULL COMMENT '图标',
  `Value` longtext NOT NULL COMMENT '配置值',
  `MchId` bigint(20) NOT NULL COMMENT '所属商户ID',
  `AuthFrom` tinyint(6) NOT NULL COMMENT '所属小程序：1-点餐  2-商城',
  `Status` tinyint(6) NOT NULL COMMENT '状态,0-关闭，1-启用',
  `SortNum` int(11) NOT NULL DEFAULT '0' COMMENT '排序数值',
  `IsDraft` tinyint(6) DEFAULT NULL COMMENT '存储的值是否为草稿，0-正式  1-草稿',
  `Remark` varchar(128) DEFAULT NULL COMMENT '备注',
  `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`ConfigId`),
  UNIQUE KEY `IDX_ConfigCode_MchId_AuthFrom_IsDraft` (`ConfigCode`,`MchId`,`AuthFrom`,`IsDraft`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户小程序配置表';

-- 商城订单售后表
DROP TABLE IF EXISTS `t_mch_trade_order_after_sale`;
CREATE TABLE `t_mch_trade_order_after_sale` (
    `AfterSaleId` bigint(20) NOT NULL COMMENT '订单详情ID',
	`TradeOrderId` varchar(30) NOT NULL COMMENT '交易单号',
	`MemberId` bigint(20) NOT NULL COMMENT '会员ID',
	`GoodsDesc` text NOT NULL COMMENT '商品信息',
	`AfterSaleAmount` bigint(20) NOT NULL COMMENT '申请退款商品总金额',
	`AfterSaleType` tinyint(6) NOT NULL COMMENT '售后类型：1-退款 2-换货 3-维修',
	`AuthFrom` tinyint(6) NOT NULL COMMENT '所属小程序：1-点餐 2-商城',
	`Status` tinyint(6) NOT NULL COMMENT '售后状态：-2-审核不通过，-1-申请待审核，1-审核通过，2-待发货，3-已发货，4-商家收货并处理，5-商家寄回买家，6-售后单完成，7-原物寄回',
	`AuditTime` datetime DEFAULT NULL COMMENT '审核时间',
	`AuditRemark` varchar(512) DEFAULT NULL COMMENT '审核描述',
	`Reason` varchar(30) NOT NULL COMMENT '申请原因',
	`AfterSaleDesc` varchar(512) DEFAULT NULL COMMENT '售后申请描述',
	`AfterSaleImg` varchar(1200) DEFAULT NULL COMMENT '售后申请凭证',
	`AddressId` bigint(20) DEFAULT NULL COMMENT '地址信息ID',
    `AddressInfo` text DEFAULT NULL COMMENT '地址信息',
    `BackTransportNo` varchar(32) DEFAULT NULL COMMENT '退回商家运单号',
    `BackTransportTime` datetime DEFAULT NULL COMMENT '商家收到退货时间',
    `TransportNo` varchar(32) DEFAULT NULL COMMENT '换货/维修完成，发往用户运单号',
    `TransportTime` datetime DEFAULT NULL COMMENT '商家发回货物时间',
    `CompleteTime` datetime DEFAULT NULL COMMENT '售后完成时间',
    `RefundAmount` bigint(20) DEFAULT NULL COMMENT '退款金额，单位分',
    `MchId` bigint(20) NOT NULL COMMENT '所属商户ID',
	`IsvId` bigint(20) NOT NULL COMMENT '服务商ID',
	`CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`AfterSaleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商城订单售后表';

-- 小程序直播表
DROP TABLE IF EXISTS `t_mch_mini_live`;
CREATE TABLE `t_mch_mini_live` (
    `RoomId` bigint(20) NOT NULL COMMENT '订单详情ID',
	`Name` varchar(32) NOT NULL COMMENT '直播间名字',
	`CoverImg` varchar(32) NOT NULL COMMENT '直播间背景图mediaID',
	`StartTime` varchar(20) NOT NULL COMMENT '直播计划开始时间，时间戳',
	`EndTime` varchar(20) NOT NULL COMMENT '直播计划结束时间，时间戳',
	`AnchorName` varchar(32) NOT NULL COMMENT '主播昵称，最短2个汉字，最长15个汉字',
	`AnchorWechat` varchar(64) NOT NULL COMMENT '主播微信号，需要通过“小程序直播”小程序的实名验证',
	`AnchorImg` varchar(32) NOT NULL COMMENT '直播间分享图mediaID，图片规则：建议像素1080*1920，大小不超过2M',
	`Type` tinyint(6) NOT NULL COMMENT '直播间类型，1: 推流，0：手机直播',
	`ScreenType` tinyint(6) NOT NULL COMMENT '横屏、竖屏，1：横屏，0：竖屏',
	`CloseLike` tinyint(6) NOT NULL COMMENT '是否关闭点赞，0：开启，1：关闭',
	`CloseGoods` tinyint(6) NOT NULL COMMENT '是否关闭货架，0：开启，1：关闭',
	`CloseComment` tinyint(6) NOT NULL COMMENT '是否关闭评论，0：开启，1：关闭',
	`AuthFrom` tinyint(6) NOT NULL COMMENT '所属小程序：1-点餐 2-商城',
    `MchId` bigint(20) NOT NULL COMMENT '所属商户ID',
	`IsvId` bigint(20) NOT NULL COMMENT '服务商ID',
	`CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`RoomId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='小程序直播表';


-- 临时测试数据
-- INSERT INTO t_isv_info (IsvId, IsvName, UserName, RealName, Password, PayPassword, IdCard, Mobile, Tel, Qq, Email, Address, Status, LoginType, PayType, SecurityAuthType, GoogleAuthSecretKey, Remark, DrawFeeLimit, LastLoginIp, LastLoginTime, LastPasswordResetTime, CreateTime, UpdateTime, PrivateKey) VALUES (40000000, '骏易科技', 'junyikeji', '骏易科技', '$2a$10$M.HuKNFNsc0Fs/TTmclyP.r3hqi7al.teo7mfQ.5MTBP4X57BFMqS', 'a390113400778be79bc2b853a4e808ca', '2201212312312313', 13211112121, '010-86463331', '7694354334', 'jmd@126.com', '北京昌平', 1, 1, 1, 0, null, null, null, null, null, '2019-09-04 04:10:42', '2019-09-04 17:10:41', '2019-09-04 17:10:41', 'Q2KM4D56IV5ACKZ2BL1I5RMAWZJQSDVJK18OUNXXSACJXSRBA6XB9QA3UL7E5T6AUO5WPZLF3DO81C9HDANRAJI6PUOUS3GBOD1LPMGK2BOGMYHRC2MILFK0VP8NIME7');
-- INSERT INTO t_mch_info (MchId, Name, userName, realName, Type, AgentId, IsvId, Mobile, Email, idCard, qq, siteName, siteUrl, Password, PayPassword, Role, MchRate, PrivateKey, Address, SettConfigMode, DrawFlag, AllowDrawWeekDay, DrawDayStartTime, DrawDayEndTime, DrawMaxDayAmount, FeeType, FeeRate, FeeLevel, DrawFeeLimit, MaxDrawAmount, MinDrawAmount, DayDrawTimes, SettType, SettMode, SettRiskDay, Status, LoginType, PayType, SecurityAuthType, GoogleAuthSecretKey, Remark, Tag, LastLoginIp, LastLoginTime, LastPasswordResetTime, LoginWhiteIp, LoginBlackIp, PayWhiteIp, PayBlackIp, AgentpayWhiteIp, AgentpayBlackIp, CreateTime, UpdateTime) VALUES (20000000, '张三9234189809', 'test', '张三', 1, null, 40000000, 13089238078, '19234189809@qq.com', null, null, null, null, '$2a$10$ZV2lCKJg4Mvty6CBbiGv7.FthDrkECbWCSgtlyasPY.cEtvHItYYC', 'a390113400778be79bc2b853a4e808ca', 'ROLE_MCH_NORMAL', null, 'YO1ZSFR70EBIWCDQJNDCJAZ1LQM7B0ZDV1639AVOFIYGK94SILBQ5ZQGXTUX2CFY3PVACOR619710HAUM3B5AY1UPLCMKCO4ATGDMCQA3UHSVBEO9YHVDBDJMXVEFXBJ', '河北省沧州市运河区御河路9234189809号', 1, 1, null, null, null, null, 2, 0.000000, '0', null, null, null, 10, 0, 1, 1, 1, 1, 1, 0, null, null, null, '0:0:0:0:0:0:0:1', '2019-09-04 05:11:00', '2019-09-04 04:36:27', null, null, null, null, null, null, '2019-09-04 17:36:26', '2019-09-04 18:10:59');
-- INSERT INTO t_account_balance (Id, InfoId, InfoType, InfoName, AccountType, Amount, UnAmount, FrozenAmount, SettAmount, TotalAddAmount, TotalSubAmount, Status, SafeKey, UpdateTime) VALUES (1, 20000000, 1, '张三9234189809', 1, 0, 0, 0, 0, 0, 0, 1, '1efd02975026f17ecb371ffbd372cb0f', '2019-09-04 17:36:26');
-- INSERT INTO t_account_balance (Id, InfoId, InfoType, InfoName, AccountType, Amount, UnAmount, FrozenAmount, SettAmount, TotalAddAmount, TotalSubAmount, Status, SafeKey, UpdateTime) VALUES (2, 20000000, 1, '张三9234189809', 2, 0, 0, 0, 0, 0, 0, 1, 'e3a803d9fc372d9e70c4c9f1e0aa5022', '2019-09-04 17:36:26');
-- INSERT INTO t_account_balance (Id, InfoId, InfoType, InfoName, AccountType, Amount, UnAmount, FrozenAmount, SettAmount, TotalAddAmount, TotalSubAmount, Status, SafeKey, UpdateTime) VALUES (3, 20000000, 1, '张三9234189809', 3, 0, 0, 0, 0, 0, 0, 1, '12c25f7f5fbe6dfbc4b4d46f99ceb1ed', '2019-09-04 17:36:26');

-- --------
-- 随行付
INSERT INTO t_pay_interface_type (IfTypeCode, IfTypeName, Status, IsvParam, MchParam, PrivateMchParam, Remark, CreateTime, UpdateTime) VALUES
 ('suixingpay', '随行付', 1,
'[{"name":"wxAuthAppId","desc":"[微信授权参数]appId","type":"text","verify":"required"}, {"name":"wxAuthAppSecret","desc":"[微信授权参数]appSecret","type":"text","verify":"required"}, {"name":"alipayAuthAppId","desc":"[支付宝授权参数]appId","type":"text","verify":"required"}, {"name":"alipayAuthPrivateKey","desc":"[支付宝授权参数]应用私钥","type":"textarea","verify":"required"}, {"name":"alipayAuthPublicKey","desc":"[支付宝授权参数]支付宝公钥","type":"textarea","verify":"required"}, {"name":"orgId","desc":"合作机构编号","type":"text","verify":"required"}, {"name":"privateKey","desc":"合作机构私钥","type":"textarea","verify":"required"}, {"name":"sxfPublic","desc":"随行付公钥","type":"textarea","verify":"required"}]',
'[{"name":"mno","desc":"商户编号","type":"text","verify":"required"}]',
'[]',
 '随行付', '2020-08-24 17:37:05', '2020-08-24 17:37:05');

-- 随行付 接口
INSERT INTO `t_pay_interface` VALUES ('suixingpay_wx_bar', '随行付微信条码支付', 'suixingpay', '27', 2, 1, NULL, '', '2020-8-24 17:39:54', '2020-8-25 11:39:13', '');
INSERT INTO `t_pay_interface` VALUES ('suixingpay_wx_jsapi', '随行付微信服务窗支付', 'suixingpay', '14', 2, 1, NULL, '', '2020-8-24 17:39:54', '2020-8-25 11:39:13', '');
INSERT INTO `t_pay_interface` VALUES ('suixingpay_alipay_bar', '随行付支付宝条码支付', 'suixingpay', '28', 2, 1, NULL, '', '2020-8-24 17:39:54', '2020-8-25 11:39:13', '');
INSERT INTO `t_pay_interface` VALUES ('suixingpay_alipay_jsapi', '随行付支付宝服务窗支付', 'suixingpay', '18', 2, 1, NULL, '', '2020-8-24 17:39:54', '2020-8-25 11:39:13', '');

-- 哆啦宝
INSERT INTO t_pay_interface_type (IfTypeCode, IfTypeName, Status, IsvParam, MchParam, PrivateMchParam, Remark, CreateTime, UpdateTime) VALUES
 ('dlbpay', '哆啦宝支付', 1,
'[{"name":"wxAuthAppId","desc":"[微信授权参数]appId","type":"text","verify":"required"}, {"name":"wxAuthAppSecret","desc":"[微信授权参数]appSecret","type":"text","verify":"required"}, {"name":"alipayAuthAppId","desc":"[支付宝授权参数]appId","type":"text","verify":"required"}, {"name":"alipayAuthPrivateKey","desc":"[支付宝授权参数]应用私钥","type":"textarea","verify":"required"}, {"name":"alipayAuthPublicKey","desc":"[支付宝授权参数]支付宝公钥","type":"textarea","verify":"required"}, {"name":"agentNum","desc":"代理商编号","type":"text","verify":"required"}, {"name":"accessKey","desc":"代理商密钥","type":"textarea","verify":"required"}, {"name":"secretKey","desc":"secretKey","type":"textarea","verify":"required"}]',
'[{"name":"customerNum","desc":"商户编号","type":"text","verify":"required"},{"name":"shopNum","desc":"店铺编号","type":"text","verify":"required"}]',
'[]',
 '哆啦宝支付', '2020-09-14 15:35:00', '2020-09-14 15:35:00');

 -- 哆啦宝 接口
INSERT INTO `t_pay_interface` VALUES ('dlbpay_wx_bar', '哆啦宝微信条码支付', 'dlbpay', '27', 2, 1, NULL, '', '2020-09-14 15:35:00', '2020-09-14 15:35:00', '');
INSERT INTO `t_pay_interface` VALUES ('dlbpay_wx_jsapi', '哆啦宝微信服务窗支付', 'dlbpay', '14', 2, 1, NULL, '', '2020-09-14 15:35:00', '2020-09-14 15:35:00', '');
INSERT INTO `t_pay_interface` VALUES ('dlbpay_alipay_bar', '哆啦宝支付宝条码支付', 'dlbpay', '28', 2, 1, NULL, '', '2020-09-14 15:35:00', '2020-09-14 15:35:00', '');
INSERT INTO `t_pay_interface` VALUES ('dlbpay_alipay_jsapi', '哆啦宝支付宝服务窗支付', 'dlbpay', '18', 2, 1, NULL, '', '2020-09-14 15:35:00', '2020-09-14 15:35:00', '');

INSERT INTO `t_pay_interface` VALUES ('dlbpay_jd_h5', '哆啦宝京东H5支付', 'dlbpay', '22', 5, 1, NULL, '', '2020-09-27 10:25:00', '2020-09-27 10:25:00', '');


-- 文章表
DROP TABLE IF EXISTS t_sys_article;
CREATE TABLE `t_sys_article` (
     `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
     `ArticleType` tinyint(6) DEFAULT 1 COMMENT '文章栏目:1-服务协议&隐私政策   2-支付安全  ',
     `Title` varchar(128) NOT NULL COMMENT '文章标题',
     `Content` text NOT NULL COMMENT '文章内容',
     `MchId` bigint(20) NOT NULL COMMENT '所属商户ID',
	 `IsvId` bigint(20) NOT NULL COMMENT '服务商ID',
     `CreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `UpdateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章表';

-- 随行付进件快照
DROP TABLE IF EXISTS t_sxf_mch_snapshot;
CREATE TABLE `t_sxf_mch_snapshot` (
  `ApplyNo` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '申请编号',
  `MchId` bigint(20) NOT NULL COMMENT '商户ID',
  `IsvId` bigint(20) NOT NULL COMMENT '服务商ID',
  `MecDisNm` varchar(30) NOT NULL COMMENT '商户简称',
  `MblNo` varchar(30) NOT NULL COMMENT '商户联系电话',
  `CsTelNo` varchar(30) NOT NULL COMMENT '客服联系电话',
  `IdentityName` varchar(30) NOT NULL COMMENT '法人/负责人',
  `IdentityTyp` varchar(6) NOT NULL DEFAULT '00' COMMENT '00-身份证，03-军人证，04-警察证，05-港澳通行证，06-台湾通行证，07护照，98-单位证件，99-其他',
  `IdentityNo` varchar(32) NOT NULL COMMENT '法人/负责人证件号码',
  `LegalPersonidPositivePic` varchar(256) DEFAULT NULL COMMENT '法人身份证正面',
  `LegalPersonidOppositePic` varchar(256) DEFAULT NULL COMMENT '法人身份证反面',
  `OperationalType` varchar(6) NOT NULL DEFAULT '01' COMMENT '经营类型：01-线下，02-线上，03非盈利类，04-缴费类，05-保险类，06-私立院校',
  `HaveLicenseNo` varchar(6) NOT NULL DEFAULT '01' COMMENT '资质类型：01-自然人，02-个体户，03-企业（线上商户必须为03）',
  `MecTypeFlag` varchar(6) NOT NULL DEFAULT '00' COMMENT '商户类型：00-普通单店商户，01-连锁总店，02-连锁分店，03-1+n总店，04-1+n分店',
  `ParentMno` varchar(30) DEFAULT NULL COMMENT '所属总店商户编号（商户类型为01/02必填）',
  `QrcodeList` varchar(256) NOT NULL COMMENT '二维码费率 json形式传入 01-微信，02-支付宝，06-银联单笔小于等于1000,07银联单笔大于1000',
  `OnlineType` varchar(6) NOT NULL DEFAULT '01' COMMENT '线上产品类型：01-APP，02-网站，03-公众号/小程序（线上类型商户必传）',
  `OnlineName` varchar(128) DEFAULT NULL COMMENT '线上产品名称',
  `OnlineTypeInfo` varchar(256) DEFAULT NULL COMMENT 'app下载地址及账号信息',
  `CprRegNmCn` varchar(244) DEFAULT NULL COMMENT '营业执照注册名称（企业、个体户必传）',
  `RegistCode` varchar(32) DEFAULT NULL COMMENT '营业执照注册号（企业、个体户必传）',
  `LicenseMatch` varchar(6) NOT NULL DEFAULT '00' COMMENT '是否三证合一：00-是，01-否（企业必传）',
  `CprRegAddr` varchar(128) NOT NULL COMMENT '商户经营详细地址',
  `StorePic` varchar(256) DEFAULT NULL COMMENT '门头照',
  `InsideScenePic` varchar(256) DEFAULT NULL COMMENT '经营场所照',
  `RegProvCd` varchar(30) NOT NULL COMMENT '商户经营地址省份（编码）',
  `RegCityCd` varchar(30) NOT NULL COMMENT '商户经营地址市（编码）',
  `RegDistCd` varchar(30) NOT NULL COMMENT '商户经营地址区（编码）',
  `MccCd` varchar(30) NOT NULL COMMENT '经营类目',
  `ActTyp` tinyint(6) DEFAULT 1 COMMENT '账户类型:0-对公,1-对私,默认对私(对应随行付00：对公，01：对私)',
  `ActNm` varchar(30) DEFAULT NULL COMMENT '结算账户名',
  `ActNo` varchar(30) DEFAULT NULL COMMENT '银行卡号',
  `DepoBank` varchar(30) DEFAULT NULL COMMENT '结算银行名称',
  `BankCardPositivePic` varchar(256) DEFAULT NULL COMMENT '银行卡正面照',
  `DepoProvCd` int(11) DEFAULT NULL COMMENT '开户地区编号，省',
  `DepoCityCd` int(11) DEFAULT NULL COMMENT '开户地区编号， 市',
  `LbnkNo` varchar(30) NOT NULL COMMENT '开户支行联行行号',
  `StmManIdNo` varchar(32) NOT NULL COMMENT '结算人身份证号码（对私必传）',
  `SettlePersonIdcardPositive` varchar(256) DEFAULT NULL COMMENT '结算人身份证正面',
  `SettlePersonIdcardOpposite` varchar(256) DEFAULT NULL COMMENT '结算人身份证反面',
  `LicensePic` varchar(256) DEFAULT NULL COMMENT '营业执照',
  `TaxRegistLicensePic` varchar(256) DEFAULT NULL COMMENT '税务登记证照片（非三合一证必传）',
  `OrgCodePic` varchar(256) DEFAULT NULL COMMENT '组织机构代码证（非三合一证必传）',
  `OpeningAccountLicensePic` varchar(256) NOT NULL COMMENT '开户许可证（对公结算必传）',
  `IcpLicence` varchar(256) DEFAULT NULL COMMENT 'ICP许可证或公众号主体信息截图（线上网站类、公众号商户必传）',
  `LetterOfAuthPic` varchar(256) DEFAULT NULL COMMENT '非法人对私授权函',
  `OtherMaterialPictureOne` varchar(256) DEFAULT NULL COMMENT '其他资料-1',
  `OtherMaterialPictureTwo` varchar(256) DEFAULT NULL COMMENT '其他资料-2',
  `ApplyStatus` tinyint(6) NOT NULL DEFAULT '-1' COMMENT '商户入驻状态：-1-未入住，0-入驻审核中, 1-入驻通过, 2-入驻驳回, 3-入驻图片驳回',
  `ApplicationId` varchar(32) DEFAULT NULL COMMENT '随行付返回进件申请ID',
  `AuditInfo` varchar(512) DEFAULT NULL COMMENT '返回审核信息',
  PRIMARY KEY (`ApplyNo`),
  UNIQUE KEY `MchId` (`MchId`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COMMENT='随行付商户入驻快照信息';


-- 点餐、商城页面路径配置
INSERT INTO `t_mch_mini_config` VALUES (1,'pagePath', '小程序页面路径', null, '[{"name":"商品详情","value":"/pages/index/dishdat/dishdat","api":"/mchGoods/list","queryValue":"goodsName","query":[["goodsId","id"]],"placeholder":"商品名称"},{"name":"优惠券列表","value":"/packageUser/user/dicout/dicout"},{"name":"充值活动","value":"/packageUser/user/rech/rech"}]', '-1', '1', '1', '0', null, null, '2020-08-21 18:40:21', '2020-08-24 11:24:13');
INSERT INTO `t_mch_mini_config` VALUES (2,'pagePath', '小程序页面路径', null, '[{"name":"直播列表","value":"/pages/index/directRoom/directRoom"},{"name":"我的优惠券列表页","value":"/packageUser/user/mydicout/mydicout"},{"name":"商品列表","value":"/pages/classify/productDisplay/productDisplay","api":"/mchGoods/category/list","queryValue":"categoryName","query":[["categoryName","title"],"categoryId","goodsModule"],"placeholder":"分类名称"},{"name":"商品详情","value":"/pages/classify/productDetails/productDetails","api":"/mchGoods/list","queryValue":"goodsName","query":["goodsId"],"placeholder":"商品名称"},{"name":"优惠券详情页","value":"/packageUser/user/dicout/dicdat/dicdat","api":"/coupon/list","queryValue":"couponName","query":["couponId"],"placeholder":"优惠券名称"},{"name":"充值优惠活动页","value":"/packageUser/user/mywallet/rech/rech"}]', '-1', '2', '1', '0', null, null, '2020-08-21 18:40:21', '2020-08-24 11:24:13');

