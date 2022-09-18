package org.xxpay.service.common.enumm;

/**
 * Created with IntelliJ IDEA.
 * User: zhanglei
 * Date: 18/3/28 上午9:42
 * Description: 账户类型枚举类
 */
public enum AccountTypeEnum {

    ACCOUNT_TYPE_TOTAL((short) 0, "总账户"),
    ACCOUNT_TYPE_USEABLE((short) 1, "可用账户");

    // 操作类型
    private Short type;

    // 描述
    private String desc;

    AccountTypeEnum(Short type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Short getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static AccountTypeEnum getAccountTypeEnum(Short type) {
        if (type == null) {
            return null;
        }

        AccountTypeEnum[] values = AccountTypeEnum.values();
        for (AccountTypeEnum e : values) {
            if (e.getType().equals(type)) {
                return e;
            }
        }
        return null;
    }

}
