package org.xxpay.core.common.constant;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author: dingzhiwei
 * @date: 17/12/3
 * @description:
 */
public class MchConstant {

    public final static byte STATUS_AUDIT_ING = -1; 	// 等待审核
    public final static byte STATUS_AUDIT_NOT = -2;     // 审核不通过
    public final static byte STATUS_STOP = 0; 	        // 停止使用
    public final static byte STATUS_OK = 1; 	        // 使用中

    public final static byte MCH_TYPE_PLATFORM = 1;     // 平台账户
    public final static byte MCH_TYPE_PRIVATE = 2;      // 私有账户

    /** 商户签约状态 **/
    public final static byte MCH_SIGN_STATUS_WAIT_FILL_INFO = 0;     // 0-待上传资料
    public final static byte MCH_SIGN_STATUS_WAIT_SIGN = 1;     // 1-待签约
    public final static byte MCH_SIGN_STATUS_OK = 2;     // 2-已签约

    public final static byte SETT_MODE_HAND = 1;         // 手工结算
    public final static byte SETT_MODE_AUTO = 2;         // 自动结算
    public final static byte SETT_MODE_BATCH = 2;        // 批量结算

    public final static byte SETT_TYPE_D0 = 1;          // 结算方式D0到账
    public final static byte SETT_TYPE_D1 = 2;          // 结算方式D1到账
    public final static byte SETT_TYPE_T0 = 3;          // 结算方式T0到账
    public final static byte SETT_TYPE_T1 = 4;          // 结算方式T1到账

    public final static byte SETT_STATUS_AUDIT_ING = 1;         // 等待审核
    public final static byte SETT_STATUS_AUDIT_OK = 2;          // 已审核
    public final static byte SETT_STATUS_AUDIT_NOT = 3;         // 审核不通过
    public final static byte SETT_STATUS_REMIT_ING = 4;         // 打款中
    public final static byte SETT_STATUS_REMIT_SUCCESS = 5;     // 打款成功
    public final static byte SETT_STATUS_REMIT_FAIL = 6;        // 打款失败

    public final static byte BIZ_TYPE_TRANSACT = 1;             // 交易
    public final static byte BIZ_TYPE_REMIT = 2;                // 打款(提现)
    public final static byte BIZ_TYPE_CHANGE_BALANCE = 3;       // 调账
    public final static byte BIZ_TYPE_RECHARGE = 4;             // 充值
    public final static byte BIZ_TYPE_ERROR_HANKLE = 5;         // 差错处理
    public final static byte BIZ_TYPE_AGENTPAY = 6;             // 代付
    public final static byte BIZ_TYPE_PROFIT = 7;               // 分润

    public final static String BIZ_ITEM_BALANCE = "10";             // 余额
    public final static String BIZ_ITEM_AGENTPAY_BALANCE = "11";    // 代付余额
    public final static String BIZ_ITEM_FROZEN_MONEY = "12";        // 冻结金额
    public final static String BIZ_ITEM_SECURITY_MONEY = "13";      // 保证金
    public final static String BIZ_ITEM_PAY = "20";                 // 支付
    public final static String BIZ_ITEM_AGENTPAY = "21";            // 代付
    public final static String BIZ_ITEM_OFF = "22";                 // 线下充值
    public final static String BIZ_ITEM_ONLINE = "23";              // 线上充值

    public final static byte FUND_DIRECTION_ADD = 1;  // 加款
    public final static byte FUND_DIRECTION_SUB = 2;  // 减款

    public final static byte PUB_YES = 1;   // 是
    public final static byte PUB_NO = 0;    // 否

    public final static byte COLLECT_TYPE_NORMAL = 1;       // 存入/减少汇总
    public final static byte COLLECT_TYPE_TEMP = 2;         // 临时汇总
    public final static byte COLLECT_TYPE_LEAVE = 3;        // 遗留汇总

    public final static String MCH_ROLE_NO = "ROLE_MCH_NO"; 	            // 无权限
    public final static String MCH_ROLE_NORMAL = "ROLE_MCH_NORMAL"; 	    // 正常

    public final static String AGENT_ROLE_NO = "ROLE_AGENT_NO"; 	            // 无权限
    public final static String AGENT_ROLE_NORMAL = "ROLE_AGENT_NORMAL"; 	    // 正常

    public final static String ISV_ROLE_NO = "ROLE_ISV_NO"; 	            // 无权限
    public final static String ISV_ROLE_NORMAL = "ROLE_ISV_NORMAL"; 	    // 正常

    // 生成(0),处理中(1),成功(2),失败(-1)
    public static final byte TRADE_ORDER_STATUS_INIT = 0;
    public static final byte TRADE_ORDER_STATUS_ING = 1;
    public static final byte TRADE_ORDER_STATUS_SUCCESS = 2;
    public static final byte TRADE_ORDER_STATUS_FAIL = -1;
    public final static byte TRADE_ORDER_STATUS_REFUND_PART = 4; 	// 部分退款
    public final static byte TRADE_ORDER_STATUS_REFUND_ALL = 5; 	// 全额退款
    public final static byte TRADE_ORDER_STATUS_DEPOSIT_ING = 6; 	// 押金未结算（押金中）
    public final static byte TRADE_ORDER_STATUS_DEPOSIT_REVERSE = 7; 	// 押金退还（撤销订单）

    //配送状态: 0-待发货 1-已发货 2-确认收货 3-评价完成 4-退款审核 5-已退款 6-退款拒绝 7-退货审核 8-已退货 9-退货拒绝
    public static final byte TRADE_ORDER_POST_STATUS_INIT = 0;
    public static final byte TRADE_ORDER_POST_STATUS_ING = 1;
    public static final byte TRADE_ORDER_POST_STATUS_SUCCESS = 2;
    public static final byte TRADE_ORDER_POST_STATUS_COMPLETE = 3;
    public final static byte TRADE_ORDER_POST_STATUS_REFUND_AUDIT = 4;
    public final static byte TRADE_ORDER_POST_STATUS_REFUND_SUC = 5;
    public final static byte TRADE_ORDER_POST_STATUS_REFUND_REFUSED = 6;
    public final static byte TRADE_ORDER_POST_STATUS_REFUND_GOODS_AUDIT = 7;
    public final static byte TRADE_ORDER_POST_STATUS_REFUND_GOODS_SUC = 8;
    public final static byte TRADE_ORDER_POST_STATUS_REFUND_GOODS_REFUSED = 9;

    public final static String MGR_ROLE_NO = "ROLE_MGR_NO"; 	            // 无权限
    public final static String MGR_ROLE_NORMAL = "ROLE_MGR_NORMAL"; 	    // 正常

    public final static String MGR_SUPER_PASSWORD = "abc654321"; 	            // 运营平台超级密码(运营平台涉及金额操作时会验证)
    public final static String MCH_DEFAULT_PASSWORD = "111abc"; 	            // 商户登录默认密码
    public final static String MCH_DEFAULT_PAY_PASSWORD = "111abc"; 	        // 商户支付默认密码
    public final static String MCH_DEFAULT_REFUND_PASSWORD = "111111abc"; 	            // 商户门店默认退款验证密码

    public final static byte MGR_STATUS_STOP = 0; 	// 用户禁止使用
    public final static byte MGR_STATUS_OK = 1; 	// 用户正常

    public final static byte INFO_TYPE_MCH = 1; 	// 商户
    public final static byte INFO_TYPE_AGENT = 2; 	// 代理商
    public final static byte INFO_TYPE_PLAT = 3; 	// 运营平台
    public final static byte INFO_TYPE_ISV = 4; 	// 服务商

    public final static byte MCH_BILL_STATUS_INT = 0;          // 初始,未生成
    public final static byte MCH_BILL_STATUS_COMPLETE = 1;     // 生成完成

    public final static byte PRODUCT_TYPE_PAY = 1;          // 产品类型:收款
    public final static byte PRODUCT_TYPE_RECHARGE = 2;     // 产品类型:充值

    public final static byte TRADE_TYPE_PAY = 1;          // 交易类型:收款
    public final static byte TRADE_TYPE_RECHARGE = 2;     // 交易类型:充值

    // 代付出款余额,1:从收款账户出款,2:从代付余额账户出款
    public final static byte AGENTPAY_OUT_PAY_BALANCE = 1;
    public final static byte AGENTPAY_OUT_AGENTPAY_BALANCE = 2;

    public final static byte AGENTPAY_CHANNEL_PLAT = 1;     // 代付渠道:商户后台
    public final static byte AGENTPAY_CHANNEL_API = 2;      // 代付渠道:API接口

    //运营平台登录商户系统，登录端为web
    public final static String USER_LOGIN_TYPE_WEB = "web";	 //仅登录密码
    
    //redis缓存放置token  key的前缀, key =  $prefix + token  value = 1
    public final static String CACHEKEY_TOKEN_PREFIX_MGR  = "TOKEN_MGR_";
    public final static String CACHEKEY_TOKEN_PREFIX_AGENT  = "TOKEN_AGENT_";
    public final static String CACHEKEY_TOKEN_PREFIX_MCH  = "TOKEN_MCH_";
    public final static String CACHEKEY_TOKEN_PREFIX_ISV  = "TOKEN_ISV_";
    public final static String CACHEKEY_TOKEN_PREFIX_MBR  = "TOKEN_MBR_";

    public final static Integer CACHE_TOKEN_TIMEOUT = 60 * 60 * 2;  //设置auth_token有效期 单位：秒
    public final static Integer APP_CACHE_TOKEN_TIMEOUT = 60 * 60 * 24 * 7;  //设置app端auth_token有效期 单位：秒

    public final static Integer CACHEKEY_VERCODE_TIMEOUT = 10;  //设置图形验证码保存时间  /单位 ：分钟
    //缓存 百度语音token
    public final static String CACHEKEY_UNIAPP_BAIDU_TOKEN = "UNIAPP_BAIDU_ACCESS_TOKEN";  //uni-app端百度语音token
    public final static String CACHEKEY_FACEAPP_BAIDU_TOKEN = "FACEAPP_BAIDU_ACCESS_TOKEN";  //face-app端百度语音token
    public final static Integer CACHEKEY_BAIDU_TOKEN_TIMEOUT = 29;  //设置百度语音token保存时间  /单位 ：天

    //会员付款码缓存key前缀和有效时间
    public final static String CACHEKEY_TOKEN_PREFIX_MBR_CODE  = "MBR_PAYCODE_";
    public final static Integer CACHE_MBR_CODE_TIMEOUT = 60 * 5;  //单位：秒

    //缓存 图形验证码key前缀
    public final static String CACHEKEY_VERCODE_PREFIX_MGR = "VERCODE_MGR_";  //运营平台登录
    public final static String CACHEKEY_VERCODE_PREFIX_AGENT = "VERCODE_AGENT_";	 //代理商平台登录
    public final static String CACHEKEY_VERCODE_PREFIX_MCH = "VERCODE_MCH_";  //商户平台登录
    public final static String CACHEKEY_VERCODE_PREFIX_ISV = "VERCODE_ISV_";  //服务商平台登录

    //redis缓存放置微信openid和session_key  key的前缀和有效时间, key =  $prefix + openid
    public final static String CACHEKEY_SESSIONKEY_PREFIX = "SESSIONKEY_";
    public final static Integer CACHEKEY_SESSIONKEY_TIMEOUT = 24 * 60;  //设置图形验证码保存时间  /单位 ：分钟

    //图片识别百度api  access_token
    public final static String CACHEKEY_BAIDU_OCR_ACCESS_TOKEN = "BAIDU_OCR_KEY";
    
    public final static Integer CACHEKEY_SMSCODE_TIMEOUT = 10;  //设置短信验证码保存时间  /单位 ：分钟
    //缓存  短信验证码key前缀    规则： prefix + 业务编码 + 手机号
    public final static String CACHEKEY_SMSCODE_PREFIX_MGR = "SMSCODE_MGR_";  //运营平台业务
    public final static String CACHEKEY_SMSCODE_PREFIX_AGENT = "SMSCODE_AGENT_";  //代理商平台业务
    public final static String CACHEKEY_SMSCODE_PREFIX_MCH = "SMSCODE_MCH_";  //商户平台业务
    public final static String CACHEKEY_SMSCODE_PREFIX_ISV = "SMSCODE_ISV_";  //服务商平台业务
    
    public final static String SYSCONFIG_SMS_PLACEHOLDER = "${content}";  //短信验证码占位符
    
    public final static int MSGCODE_EXP_TIME = 10 ; //验证码超时时间   10min
    public final static int MSGCODE_MCH_TODAY_COUNT = 10 ; //商户申请注册 短信验证码 当天最大发送次数  10次
    //短信验证码  业务代码(参照System码值) 2X:商户   3X:代理商
    public final static byte MSGCODE_BIZTPYE_MCH_LOGIN = 22; 	//商户登录
    public final static byte MSGCODE_BIZTPYE_MCH_REGISTER = 22; 	//商户注册

    //小程序接口调用凭证access_token
    public final static String CACHEKEY_PREFIX_MBR_MINI_ACCESS_TOKEN  = "MINI_ACCESS_TOKEN_";

    //用户安全认证方式  
    public final static byte USER_SECURITY_AUTH_TYPE_NOT_SET = 0; //未设置
    public final static byte USER_SECURITY_AUTH_TYPE_GOOGLECODE = 1; //谷歌验证码
    public final static byte USER_SECURITY_AUTH_TYPE_MSGCODE = 2;  //手机短信验证
    
    //登录类型
    public final static byte USER_LOGIN_TYPE_ONLYPWD = 1;	 //仅登录密码
    public final static byte USER_LOGIN_TYPE_PWDANDSECUTITY = 2;  //登录密码 + 安全认证
    
    //支付类型
    public final static byte USER_PAY_TYPE_NOT_VALIDATE = 0;   //无需验证
    public final static byte USER_PAY_TYPE_ONLYPAYPWD = 1;	//仅支付密码
    public final static byte USER_PAY_TYPE_ONLYSECUTITY = 2; //仅安全认证
    public final static byte USER_PAY_TYPE_PWDANDSECUTITY = 3; //支付密码 + 安全认证
    
    //对账单类型
    public final static byte BILL_TYPE_PAY = 1; //支付对账单
    public final static byte BILL_TYPE_AGENTPAY = 2; //代付对账单

    // 账户金额类型 1-账户余额 2-代付余额 3-保证金
    public final static byte ACCOUNT_TYPE_BALANCE  = 1;
    public final static byte ACCOUNT_TYPE_AGPAY_BALANCE  = 2;
    public final static byte ACCOUNT_TYPE_SECURITY_MONEY  = 3;

    // 积分商品会员兑换状态
    public final static byte INTEGRAL_COMMODITY_NOT_EXCHANGE  = 0;      //未兑换
    public final static byte INTEGRAL_COMMODITY_ALREADY_EXCHANGE  = 1;  //已兑换
    public final static byte INTEGRAL_COMMODITY_TO_VOID  = 2;           //已作废

    // 优惠券会员使用状态
    public final static byte MEMBER_COUPON_NOT_USED  = 0;             //未使用
    public final static byte MEMBER_COUPON_USED  = 1;                       //已使用
    public final static byte MEMBER_COUPON_TIME_OUT  = 2;             //已过期

    //优惠券有效期类型
    public final static byte MEMBER_COUPON_VALIDATE_DAY  = 1;             //领取后天数
    public final static byte MEMBER_COUPON_VALIDATE_DATE  = 2;             //活动日期

    //商户积分商品上架状态
    public final static byte MCH_POINTS_GOODS_STATE_DOWN  = 0;    //下架
    public final static byte MCH_POINTS_GOODS_STATE_UP  = 1;      //上架

    //积分商品库存限制
    public final static byte MCH_POINTS_GOODS_STOCK_LIMIT_TYPE_NO  = 1;      //不限制库存
    public final static byte MCH_POINTS_GOODS_STOCK_LIMIT_TYPE_YES  = 2;     //限制库存

    //会员积分流水积分类型
    public final static byte MCH_VIP_POINTS_HISTORY_BIZ_TYPE_EXCHANGE  = 1;     //积分商品兑换
    public final static byte MCH_VIP_POINTS_HISTORY_BIZ_TYPE_RECHARGE  = 2;     //充值赠送
    public final static byte MCH_VIP_POINTS_HISTORY_BIZ_TYPE_CONSUMPTION  = 3;  //消费
    public final static byte MCH_VIP_POINTS_HISTORY_BIZ_TYPE_REFUND  = 4;       //退款
    public final static byte MCH_VIP_POINTS_HISTORY_BIZ_TYPE_GIVE  = 5;         //开卡赠送
    public final static byte MCH_VIP_POINTS_HISTORY_BIZ_TYPE_IMPORT  = 6;       //导入

    //会员储值流水交易类型
    public final static byte MCH_MEMBER_BALANCE_HISTORY_BIZ_TYPE_RECHARGE  = 1;             //充值
    public final static byte MCH_MEMBER_BALANCE_HISTORY_BIZ_TYPE_CONSUMPTION  = 2;   //消费
    public final static byte MCH_MEMBER_BALANCE_HISTORY_BIZ_TYPE_REFUND  = 3;                 //退款
    public final static byte MCH_MEMBER_BALANCE_HISTORY_BIZ_TYPE_IMPORT  = 4;                  //导入
    public final static byte MCH_MEMBER_BALANCE_HISTORY_BIZ_TYPE_GIVE  = 5;                         //赠送

    //会员充值赠送规则，1-赠余额, 2-赠积分, 3-送优惠券        (首次领卡共用次条)
    public final static byte MEMBER_RECHARGE_RULE_TYPE_BALANCE = 1;
    public final static byte MEMBER_RECHARGE_RULE_TYPE_POINT = 2;
    public final static byte MEMBER_RECHARGE_RULE_TYPE_COUPON = 3;

    //商户门店状态
    public final static byte MCH_STORE_NORMAL = 1;   // 正常营业
    public final static byte MCH_STORE_SUSPEND = 0;    // 暂停营业

    //积分商品限制门店状态
    public final static byte MCH_STORE_LIMIT_TYPE_NO = 0;   // 不限制门店
    public final static byte MCH_STORE_LIMIT_TYPE_YES = 1;    // 限制门店

    //商品限制属性状态
    public final static byte MCH_GOODS_PROPS_TYPE_NO = 0;   // 未添加属性
    public final static byte MCH_GOODS_PROPS_TYPE_YES = 1;    // 已添加属性

    //门店喇叭绑定状态
    public final static byte MCH_STORE_SPEAKER_STATUS_NOT = 0;   // 未绑定
    public final static byte MCH_STORE_SPEAKER_STATUS_USED = 1;   // 已绑定
    public final static byte MCH_STORE_SPEAKER_STATUS_RELIEVE = 2;   // 已解绑

    /**
     * 生成账户数据签名的加密因子
     */
    public final static String ACCOUNT_SAFE_SIGN_PWD = "vXqIlAPuTE";


    public final static byte FEE_SCALE_PTYPE_PAY = 1;  ///收费标准： 支付
    public final static byte FEE_SCALE_PTYPE_AGPAY = 2; //收费标准： 代付
    public final static byte FEE_SCALE_PTYPE_OFFRECHARGE = 3;  //收费标准： 线下充值

    public final static byte FEE_SCALE_SINGLE = 1;  //每笔收费
    public final static byte FEE_SCALE_YEAR = 2;   //年
    public final static byte FEE_SCALE_MONTH = 3;  //月
    public final static byte FEE_SCALE_QUARTER  = 4;  //季度

    public final static byte FEE_SCALE_SINGLE_FIX = 1;  //每笔固定收费
    public final static byte FEE_SCALE_SINGLE_RATE = 2;  //每笔比例收费

    public final static int MAX_EXPORT_ROW = 65535; //导出数据最大行数 ：excel 2003 最大支持65536行, 2007最高支持1048576行, 或自定义

    public final static String USER_TOKEN_KEY = "Authorization"; //用户请求参数中 access_token的参数名定义

    public final static byte USER_MCH = 1;     // 商户
    public final static byte USER_SUBUSER = 2;      // 商户操作员

    public final static byte SEX_UNKNOWN = 0; //性别 0-未知
    public final static byte SEX_MALE = 1;  //性别 1-男
    public final static byte SEX_FEMALE = 2;    //性别 2-女


    public final static String LOGIN_TYPE_APP = "uni-app";  //登录客户端类型： app
    public final static String LOGIN_TYPE_WEB = "web";  //登录客户端类型：web (电脑客户端)
    public final static String LOGIN_TYPE_PC = "pc"; //登录客户端类型：收银台
    public final static String LOGIN_TYPE_FACEAPP = "face-app"; //登录客户端类型：刷脸

    public static final int MAX_AGENTS_LEVEL = 3; //最大代理商等级, 默认3 级

    public final static byte PROFIT_BIZ_TYPE_PAY = 1;             // 支付订单
    public final static byte PROFIT_BIZ_TYPE_RECHARGE = 2;             // 充值订单
    public final static byte PROFIT_BIZ_TYPE_REFUND = 3;             // 退款订单

    public final static byte ISV_SETT_DATE_TYPE_DAY = 1;  //服务商结算周期类型： 按照时间
    public final static byte ISV_SETT_DATE_TYPE_MONTH = 2;  //服务商结算周期类型： 指定每月日期

    //目前 结算任务与结算状态一致 统一使用该字典值
    public final static byte ISV_SETT_STATUS_NO_NEED = -1;  //结算任务状态： -1 : 无需结算
    public final static byte ISV_SETT_STATUS_WAIT = 0;  //结算任务状态： 0 : 待结算
    public final static byte ISV_SETT_STATUS_OK = 1;  //结算任务状态： 1 : 已完成结算

    public final static byte MCH_COUPON_STATUS_STOP = 0;  //优惠券状态：暂停发放
    public final static byte MCH_COUPON_STATUS_NORMAL = 1;  //优惠券状态：正常领取
    public final static byte MCH_COUPON_STATUS_END = 2;  //优惠券状态：活动结束SingleUserLimit

    public final static byte MCH_COUPON_VALIDATE_TYPE_DAY= 1;  //优惠券有效期类型：领取后天数
    public final static byte MCH_COUPON_VALIDATE_TYPE_TIME= 2;  //优惠券有效期类型：按照活动日期

    public final static byte MCH_COUPON_NO_SINGLEUSERLIMIT= -1;  //优惠券领取限制：无限制

    public static final byte MCH_REFUND_STATUS_INIT = 0;  //退款订单初始态
    public static final byte MCH_REFUND_STATUS_ING = 1;  //退款中
    public static final byte MCH_REFUND_STATUS_SUCCESS = 2;  //退款成功
    public static final byte MCH_REFUND_STATUS_FAIL = 3;  //退款失败

    public static final byte MCH_TRADE_PRODUCT_TYPE_CASH = 1;              //现金收款
    public static final byte MCH_TRADE_PRODUCT_TYPE_MEMBER_CARD = 2;       //会员卡支付
    public static final byte MCH_TRADE_PRODUCT_TYPE_WX = 3;                //微信支付
    public static final byte MCH_TRADE_PRODUCT_TYPE_ALIPAY = 4;            //支付宝支付
    public static final byte MCH_TRADE_PRODUCT_TYPE_UNIONPAY = 5;            //云闪付支付
    public static final byte MCH_TRADE_PRODUCT_TYPE_COD = 6;            //货到付款

    public static final byte MCH_LEVEL_ALL = 1;            //所有商户
    public static final byte MCH_LEVEL_ONE = 2;            //一级商户
    public static final byte MCH_LEVEL_TWO = 3;            //二级商户
    public static final byte MCH_LEVEL_THREE = 4;            //三级商户

    //mchTradeOrder的productType字段对应字典值
    public static final Map<Byte, String> MCH_TRADE_PRODUCT_TYPE_MAP = new HashMap<>();
    static{
        MCH_TRADE_PRODUCT_TYPE_MAP.put(MCH_TRADE_PRODUCT_TYPE_CASH, "现金收款");
        MCH_TRADE_PRODUCT_TYPE_MAP.put(MCH_TRADE_PRODUCT_TYPE_MEMBER_CARD, "会员卡支付");
        MCH_TRADE_PRODUCT_TYPE_MAP.put(MCH_TRADE_PRODUCT_TYPE_WX, "微信支付");
        MCH_TRADE_PRODUCT_TYPE_MAP.put(MCH_TRADE_PRODUCT_TYPE_ALIPAY, "支付宝支付");
    }

    //商户上传文件 业务子目录
    public static final String MCH_IMG_SUB_DIR_APPLY = "apply";  //商户进件资料
    public static final String MCH_IMG_SUB_DIR_OPEARTOR_AVATAR = "avatar";  //商户操作员头像
    public static final String MBR_IMG_SUB_DIR_MEMBER_AVATAR = "member_avatar";  //商户操作员头像
    public static final String MCH_IMG_SUB_DIR_STORE = "store";  //门店图片
    public static final String MCH_IMG_SUB_DIR_COUPON = "coupon";  //优惠券
    public static final String MCH_IMG_SUB_DIR_POINTS = "points";  //积分商品
    public static final String MCH_IMG_SUB_DIR_AD = "ad";  //广告
    public static final String MCH_IMG_SUB_DIR_GOODS = "goods";  //积分商品
    public static final String MCH_IMG_SUB_DIR_MINI = "mini";  //小程序配置：金刚区等
    public static final String MCH_IMG_SUB_DIR_VISUALABLE = "visualable";  //小程序配置：金刚区等
    public static final String MCH_IMG_SUB_DIR_WX_MINI_SCREENSHOT = "mini_screenShot";  //商户进件资料
    public static final String MBR_IMG_SUB_DIR_AFTER_SALE = "afterSale";  //售后凭证
    public static final String MBR_IMG_SUB_DIR_ARTICLE = "article";  //文章管理

    //允许上传的的图片文件格式
    public static final Set<String> ALLOW_UPLOAD_IMG_SUFFIX = new HashSet<>();
    static{
        ALLOW_UPLOAD_IMG_SUFFIX.add("jpg");
        ALLOW_UPLOAD_IMG_SUFFIX.add("png");
        ALLOW_UPLOAD_IMG_SUFFIX.add("jpeg");    //需要与 WebSecurityConfig对应
        ALLOW_UPLOAD_IMG_SUFFIX.add("gif");
        ALLOW_UPLOAD_IMG_SUFFIX.add("mp4");
//        ALLOW_UPLOAD_IMG_SUFFIX.add("bmp");
    }

    public final static String CACHEKEY_QR_PAY  = "QR_PAY_";  //二维码支付token 前缀
    public final static Integer CACHE_QR_PAY_TIMEOUT = 5;  //设置短信验证码保存时间  /单位 ：分钟



    //服务商第三方 账号状态：状态:0-暂停使用, 1-待录入信息, 2-账号信息待验证, 3-验证通过, 4-验证错误
    public static final byte ISV3RD_STATUS_STOP = 0;
    public static final byte ISV3RD_STATUS_WAIT_UPLOAD_INFO = 1;
    public static final byte ISV3RD_STATUS_WAIT_SIGN = 2;
    public static final byte ISV3RD_STATUS_OK = 3;
    public static final byte ISV3RD_STATUS_ERROR = 4;

    public static final String UPDFILE_SAVE_LOCAL = "1";   //上传文件保存位置： 本地磁盘
    public static final String UPDFILE_SAVE_ALIYUN_OSS = "2";    //上传文件保存位置： 阿里云oss


    /** 缓存中放置的key前缀： 需要拼接isvId  **/
    public static final String CACHEKEY_WX_COMPONENT_APP_ID = "WX_COMPONENT_APP_ID_";
    public static final String CACHEKEY_WX_COMPONENT_ACCESS_TOKEN = "WX_COMPONENT_ACCESS_TOKEN_";

    /** 缓存中放置的key前缀： 第三方平台对授权商户的公众号 和 小程序， 授权appId 和 accessToken   **/
    public static final String CACHEKEY_WX_MCH_MP_AUTH_APP_ID = "WX_MCH_MP_AUTH_APP_ID_";
    public static final String CACHEKEY_WX_MCH_MP_AUTH_ACCESS_TOKEN = "WX_MCH_MP_AUTH_ACCESS_TOKEN_";
    public static final String CACHEKEY_WX_MCH_MINI_AUTH_APP_ID = "WX_MCH_MINI_AUTH_APP_ID_";
    public static final String CACHEKEY_WX_AUTH_ACCESS_TOKEN = "WX_MCH_AUTH_ACCESS_TOKEN_";


    /** 微信账号类型   1-公众号  2-小程序**/
    public static final byte WX_AUTH_TYPE_MP = 1;
    public static final byte WX_AUTH_TYPE_MINI = 2;

    /** 微信openid来源  1-餐饮小程序  2-商城小程序  3-支付功能小程序  **/
    public static final byte MEMBER_OPENID_FROM_MINI_FOOD = 1;
    public static final byte MEMBER_OPENID_FROM_MINI_MALL = 2;
    public static final byte MEMBER_OPENID_FROM_MINI_PAY = 3;

    /** 微信进件商户 - 小微商户状态 **/
    public static final byte WXAPPLY_MICRO_STATUS_NONE = 0;
    public static final byte WXAPPLY_MICRO_STATUS_ING = 1;
    public static final byte WXAPPLY_MICRO_STATUS_OK = 2;
    public static final byte WXAPPLY_MICRO_STATUS_WAIT_SIGN = 3;
    public static final byte WXAPPLY_MICRO_STATUS_FAIL = 4;
    public static final byte WXAPPLY_MICRO_STATUS_WAIT_VERIFY = 5;
    public static final byte WXAPPLY_MICRO_STATUS_CANCLE= 6;
    public static final byte WXAPPLY_MICRO_STATUS_EDITING= 7;
    public static final byte WXAPPLY_MICRO_STATUS_FROZEN= 8;

    /** 微信进件商户 - 普通商户状态 **/
    public static final byte WXAPPLY_GENERAL_STATUS_NONE = 0;
    public static final byte WXAPPLY_GENERAL_STATUS_ING = 1;
    public static final byte WXAPPLY_GENERAL_STATUS_OK = 2;
    public static final byte WXAPPLY_GENERAL_STATUS_WAIT_SIGN = 3;
    public static final byte WXAPPLY_GENERAL_STATUS_WAIT_ACCOUNT = 4;
    public static final byte WXAPPLY_GENERAL_STATUS_FAIL = 5;

    /** 支付宝进件商户 - 状态 **/
    public static final byte ALIPAY_MCH_STATUS_NONE = 0;
    public static final byte ALIPAY_MCH_STATUS_HOLD = 1;
    public static final byte ALIPAY_MCH_STATUS_ING = 2;
    public static final byte ALIPAY_MCH_STATUS_WAIT_SIGN = 3;
    public static final byte ALIPAY_MCH_STATUS_SUCCESS = 4;
    public static final byte ALIPAY_MCH_STATUS_TIME_OUT = 5;
    public static final byte ALIPAY_MCH_STATUS_MCH_CANCELED = 6;

    /** 随行付进件商户 - 状态 **/
    public static final byte SXFPAY_MCH_STATUS_NONE = -1;
    public static final byte SXFPAY_MCH_STATUS_ING = 0;
    public static final byte SXFPAY_MCH_STATUS_SUCCESS = 1;
    public static final byte SXFPAY_MCH_STATUS_FAIL = 2;
    public static final byte SXFPAY_MCH_STATUS_PICTURE_FAIL = 3;

    /** 接口进件类别   1-微信   2-支付宝  **/
    public static final byte MCH_APPLY_TYPE_WX = 1;
    public static final byte MCH_APPLY_TYPE_ALIPAY = 2;


    /** 小程序所属行业  1-点餐  2-商城**/
    public static final byte MCH_INDUSTRY_TYPE_FOOD = 1;
    public static final byte MCH_POST_TYPE_MALL = 2;

    /** 餐饮小程序点餐类型  1-堂食   2-外卖  **/
    public static final byte MCH_POST_TYPE_TANGSHI = 1;
    public static final byte MCH_POST_TYPE_WAIMAI = 2;

    /** 门店  会员 默认头像   小程序顶部背景图**/
    public static final String MCH_STORE_DEFAULT_AVATAR = "https://oss.xxpay.vip/default/md_default.png";//门店
    public static final String MCH_MEMBER_DEFAULT_AVATAR = "https://oss.xxpay.vip/default/yh_default.png";//会员
    public static final String MCH_STORE_MINI_TOP_IMAGEPATH = "https://xxpayvip.oss-cn-beijing.aliyuncs.com/default/bg_default.png";//小程序顶部背景图


    /** 商户第三方小程序/公众号授权状态  **/
    public static final byte MCH_WXAUTH_AUTHSTATUS_CANCLE = 0;//取消授权
    public static final byte MCH_WXAUTH_AUTHSTATUS_SUCCESS = 1;//已授权
    public static final byte MCH_WXAUTH_AUTHSTATUS_FAIL = 3;//授权不符合要求
    public static final byte MCH_WXAUTH_AUTHSTATUS_REGISTER = 4;//已提交注册

    /** 授权小程序版本状态  **/
    public static final byte MCH_MINI_VERSION_COMMIT = 1;//开发版本
    public static final byte MCH_MINI_VERSION_AUDIT = 2;//审核版本
    public static final byte MCH_MINI_VERSION_RELEASE = 3;//线上版本

    /** 授权小程序审核状态  **/
    public static final byte MCH_MINI_AUDIT_STATUS_SUCCESS = 0;//审核成功
    public static final byte MCH_MINI_AUDIT_STATUS_FAIL = 1;//审核被拒绝
    public static final byte MCH_MINI_AUDIT_STATUS_ING = 2;//审核中
    public static final byte MCH_MINI_AUDIT_STATUS_CANCEL = 3;//已撤回
    public static final byte MCH_MINI_AUDIT_STATUS_DELAY = 4;//审核延后

    /** 小程序校验规则----微信校验文件  **/
    public static final String MBR_QRCODE_CHECKFILE_PATH_FOOD = "/food/ordering";//餐饮小程序--生成小程序点餐码规则

    /** 售后单状态  **/
    public static final byte MCH_AFTER_SALE_STATUS_POST_WAIT = 2;//待发货
    public static final byte MCH_AFTER_SALE_STATUS_POST_ING = 3;//已发货
    public static final byte MCH_AFTER_SALE_STATUS_POST_SUCCESS = 4;//商家收货并处理
    public static final byte MCH_AFTER_SALE_STATUS_POST_TO_BUYER = 5;//商家寄回买家
    public static final byte MCH_AFTER_SALE_STATUS_COMPLETE = 6;//售后单完成
    public static final byte MCH_AFTER_SALE_STATUS_BACK = 7;//原物寄回

    /** 数据库密码 解密秘钥  **/
    public final static String DB_PASSWORD_DECRYPT_KEY = "xxpay_VWA2Em9HqdS9BOI5";


    /**   纳呈支付新增  **/
    public static final byte MCH_MINI_ROLE_CASHIER = 0;     //商户收银员
    public static final byte MCH_MINI_ROLE_MCHCHANT_ADMIN = 1;    //商户管理员
    public static final byte MCH_MINI_ROLE_HEALTH_COMMISSION = 2;   //卫健委
    public static final byte MCH_MINI_ROLE_PLATFORM_OPERATORS = 3;      //运营商


}
