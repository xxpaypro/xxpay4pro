package org.xxpay.service.common.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import org.jasypt.util.text.BasicTextEncryptor;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.util.MyLog;


public class EncryptDruidDataSource extends DruidDataSource {

    private static final MyLog logger = MyLog.getLog(EncryptDruidDataSource.class);

    private static final BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();
    static {
        basicTextEncryptor.setPassword(MchConstant.DB_PASSWORD_DECRYPT_KEY);
    }

    @Override
    public void setUsername(String username) {
        try {
            super.setUsername(basicTextEncryptor.decrypt(username));
        } catch (Exception e) {
            logger.error("数据库[账号]解密失败，请检查加密因子是否正确！");
            throw e;
        }
    }

    @Override
    public void setPassword(String password) {
        try {
            super.setPassword(basicTextEncryptor.decrypt(password));
        } catch (Exception e) {
            logger.error("数据库[密码]解密失败，请检查加密因子是否正确！");
            throw e;
        }
    }
}