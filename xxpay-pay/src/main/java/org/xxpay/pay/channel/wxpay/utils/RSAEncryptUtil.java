package org.xxpay.pay.channel.wxpay.utils;

import com.alibaba.fastjson.JSONObject;
import org.xxpay.core.common.util.MyLog;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.security.PublicKey;
import java.util.Base64;

public class RSAEncryptUtil {

    private static final MyLog logger = MyLog.getLog(RSAEncryptUtil.class);

    private static final String CIPHER_PROVIDER = "SunJCE";
    private static final String TRANSFORMATION_PKCS1Paddiing = "RSA/ECB/PKCS1Padding";
    private static final String TRANSFORMATION_OAEPWithSHA = "RSA/ECB/OAEPWithSHA-1AndMGF1Padding";
    private static final String CHAR_ENCODING = "UTF-8";//固定值，无须修改


    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT = 128;

    /** 获取微信证书信息 **/
    public static String getWXCert(JSONObject certJSON, String mchApiV3Key) {
        //敏感信息证书生成所需参数
        JSONObject encryptCertificateJSON = certJSON.getJSONObject("encrypt_certificate");
        String nonce = encryptCertificateJSON.getString("nonce");
        String associatedData = encryptCertificateJSON.getString("associated_data");
        //要被解密的证书字符串
        String cipherText = encryptCertificateJSON.getString("ciphertext");
        String wechatpayCert = "";
        try {
            final Cipher cipher = Cipher.getInstance(ALGORITHM, "SunJCE");
            SecretKeySpec key = new SecretKeySpec(mchApiV3Key.getBytes(), "AES");
            GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, nonce.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, key, spec);
            cipher.updateAAD(associatedData.getBytes());
            return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)));

        } catch (Exception e) {
            logger.error("getWXCertError:", e);
            return wechatpayCert;
        }
    }


    /** 敏感信息加密 **/
    public static String rsaEncrypt(String Content, X509Certificate certificate)throws IllegalBlockSizeException, IOException {
        PublicKey publicKey = certificate.getPublicKey();
        try {

            Cipher cipher = Cipher.getInstance(TRANSFORMATION_OAEPWithSHA);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] data = Content.getBytes(CHAR_ENCODING);
            byte[] cipherdata = cipher.doFinal(data);
            return Base64.getEncoder().encodeToString(cipherdata);
        }  catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("当前Java环境不支持RSA v1.5/OAEP", e);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("无效的证书", e);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new IllegalBlockSizeException("加密原串的长度不能超过214字节");
        }
    }
}