package org.xxpay.core.common.constant;

/**
 * Created by admin on 2016/4/27.
 */
public class Constant {

    // 账户业务模块流水号前缀(account)
    public static final String AC_BIZ_SEQUENCE_NO_PREFIX = "ac";
    // 账户业务模块流水号前缀(config)
    public static final String CF_BIZ_SEQUENCE_NO_PREFIX = "cf";

    public final static byte PUB_YES = 1;   // 是
    public final static byte PUB_NO = 0;    // 否

    // 随机通讯码不重复的时间间隔(ms)
    public static final long RPC_SEQ_NO_NOT_REPEAT_INTERVAL = 5 * 1000;

    // 服务端返回map中业务数据结果对应的key名称
    public static final String BIZ_RESULT_KEY = "bizResult";

    public static final String MCH_CONTROLLER_ROOT_PATH = "/api";
    public static final String AGENT_CONTROLLER_ROOT_PATH = "/api";
    public static final String MGR_CONTROLLER_ROOT_PATH = "/api";
    public static final String ISV_CONTROLLER_ROOT_PATH = "/api";
    public static final String MBR_CONTROLLER_ROOT_PATH = "/api";

    public static final int DUBBO_RETRIES = -1; // dubbo 重试次数
    public static final int DUBBO_TIMEOUT = 10 * 1000; // dubbo 超时时间,毫秒
    public static final int DUBBO_TIMEOUT_LONG = 60 * 1000; // dubbo 超时时间,毫秒  1分钟时长
    public static final String XXPAY_SERVICE_VERSION = "1.0.0"; // XxPay业务版本号
}
