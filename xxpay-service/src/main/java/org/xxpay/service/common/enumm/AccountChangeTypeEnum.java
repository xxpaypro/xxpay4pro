package org.xxpay.service.common.enumm;

/**
 * Created with IntelliJ IDEA.
 * User: zhanglei
 * Date: 18/3/28 上午9:43
 * Description: 账户变动类型枚举类
 */
public enum AccountChangeTypeEnum {

    ACCOUNT_CHANGE_TYPE_IN((short) 0, "转入"),
    ACCOUNT_CHANGE_TYPE_OUT((short) 1, "转出"),
    ACCOUNT_CHANGE_TYPE_INIT((short) 2, "初始化");

    // 操作类型
    private Short type;

    // 描述
    private String desc;

    AccountChangeTypeEnum(Short type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Short getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static AccountChangeTypeEnum getAccountChangeTypeEnum(Short type) {
        if (type == null) {
            return null;
        }

        AccountChangeTypeEnum[] values = AccountChangeTypeEnum.values();
        for (AccountChangeTypeEnum e : values) {
            if (e.getType().equals(type)) {
                return e;
            }
        }
        return null;
    }

}
