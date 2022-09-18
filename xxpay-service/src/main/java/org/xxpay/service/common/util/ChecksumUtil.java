package org.xxpay.service.common.util;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.Key;

/**
 * Created with IntelliJ IDEA.
 * User: zhanglei
 * Date: 18/3/1 下午6:03
 * Description: 生成checksum工具类
 */
public class ChecksumUtil {

    // checksumkey(xxpay_checksum_key)
    // 算法名称:desede,加密模式:,ECB 电子密码本模式,填充方式:PKCS5Padding
    private static final String CHECKSUM_KEY = "pd7DdqNPZK0tN/4Ng3Eo25jrP7KetQjA";

    private static DesUtil desUtil = new DesUtil();

    private static ChecksumUtil checksumUtil = new ChecksumUtil();

    private ChecksumUtil(){
    }

    public static ChecksumUtil getInstance(){
        return checksumUtil;
    }

    /**
     * 生成checksum， 用DES加密后，再用MD5加密
     * @param userID
     * @param money
     * @param timeMills
     * @return
     */
    public String generateChecksum(String userID, long money, long timeMills){
        StringBuilder builder = new StringBuilder();
        builder.append(userID).append(",").append(money).append(",").append(timeMills);
        String data = builder.toString();
        String secretData = null;
        try {
            secretData =  desUtil.encrypt(data, CHECKSUM_KEY);
        } catch (Exception e) {
        }
        if(secretData != null){
            return MD5Util.string2MD5(secretData);
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        String userID = "1";
        long leftCount = 2L;
        long timeMills = 1520575850727L;
        String checksum = ChecksumUtil.getInstance().generateChecksum(userID, leftCount, timeMills);
        System.out.println(checksum);
    }

    public static class DesUtil {
        // 算法名称
        private static final String KEY_ALGORITHM = "DES";

        // 算法名称/加密模式/填充方式
        private static final String CIPHER_ALGORITHM = "DES/ECB/PKCS5Padding";

        /**
         * 加密
         */
        public static String encrypt(String plainData, String secretKey) throws Exception {
            Key key = keyGenerator(secretKey);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] buf = cipher.doFinal(plainData.getBytes("utf-8"));
            return new String(Hex.encodeHex(buf));
        }

        /**
         * 解密
         */
        public static String decrypt(String secretData, String secretKey) throws Exception {
            Key key = keyGenerator(new String(secretKey));
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] buf = cipher.doFinal(Hex.decodeHex(secretData.toCharArray()));
            return new String(buf, "utf-8");
        }

        /**
         * 生成密钥key对象
         */
        private static Key keyGenerator(String secretKey) throws Exception {
            byte[] input = secretKey.getBytes("utf-8");
            DESKeySpec desKey = new DESKeySpec(input);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
            return keyFactory.generateSecret(desKey);
        }
    }

}
