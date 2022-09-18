package org.xxpay.pay.channel.unionpay.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.xxpay.core.common.util.MyLog;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.*;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

/**
 * 银联接口签名工具类
 * create date: 20191203
 * Author: terrfly
 */
public class UniSignUtils {

    private static final String KEYSTORE_TYPE_PKCS12 = "PKCS12";  //私钥类型
    private static final String KEYSTORE_PROVIDER_BC = "BC";  //提供商
    private static final String ALGORITHM_SHA256WITHRSA = "SHA256withRSA";  //签名算法 sha256

    private static final String CERTIFICATE_TYPE_X509 = "X.509"; //公钥证书类型


    private static final MyLog logger = MyLog.getLog(UniSignUtils.class);
    static {
        try {
            Security.addProvider(new BouncyCastleProvider());
        } catch (Exception e) {
            logger.error("addProvider Error", e);
        }
    }

    /** 签名  & 自动填充签名字段 **/
    public static void signAndFillField(JSONObject reqParams, String privateKeyFilePath, String certPwd){

        //1. 获取证书ID
        String certId = getCertId(privateKeyFilePath, certPwd);
        reqParams.put("certId", certId);

        //2. 签名
        String sign = signBy256(reqParams, privateKeyFilePath, certPwd);
        reqParams.put("signature", sign);
    }


    /** 签名
     * 注意事项： 签名需商户申请 5.1.0版本证书；
     * 文档： https://open.unionpay.com/tjweb/acproduct/list?apiSvcId=468&index=2
     * 1. 排序并拼接为[key=value]格式；
     * 2. 对原始签名串使用SHA-256算法做摘要
     * 3. 使用商户私钥做签名（使用 SHA-256）
     * 4. 进行Base64处理
     * **/
    public static String signBy256(JSONObject params, String privateKeyFilePath, String certPwd) {

        try {

            //0. 将请求参数 转换成key1=value1&key2=value2的形式
            String stringSign = convertSignString(params);

            //1. 通过SHA256进行摘要并转16进制
            byte[] signDigest = sha256X16(stringSign, "UTF-8");

            //2. /获取私钥证书的key
            PrivateKey privateKey = getSignCertPrivateKey(privateKeyFilePath, certPwd);

            //3. 使用 SHA-256算法 进行签名
            Signature st = Signature.getInstance(ALGORITHM_SHA256WITHRSA, KEYSTORE_PROVIDER_BC);
            st.initSign(privateKey);
            st.update(signDigest);
            byte[] result = st.sign();

            //4. 做base64 处理
            byte[] byteSign = Base64.encodeBase64(result);
            return new String(byteSign);

        } catch (Exception e) {
            logger.error("银联签名失败", e);
            return null;
        }
    }

    /** 获取证书ID **/
    public static String getCertId(String privateKeyFilePath, String certPwd) {

        try {
            KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE_PKCS12, KEYSTORE_PROVIDER_BC);
            FileInputStream fis  = new FileInputStream(privateKeyFilePath);
            char[] nPassword = null == certPwd || "".equals(certPwd.trim()) ? null: certPwd.toCharArray();
            keyStore.load(fis, nPassword);

            Enumeration<String> aliasenum = keyStore.aliases();
            String keyAlias = null;
            if (aliasenum.hasMoreElements()) {
                keyAlias = aliasenum.nextElement();
            }
            X509Certificate cert = (X509Certificate) keyStore.getCertificate(keyAlias);
            return cert.getSerialNumber().toString();
        } catch (Exception e) {
            logger.error("银联获取证书ID失败！", e);
            return null;
        }
    }

    /** 验签 **/
    public static boolean validate(JSONObject params, String unionpayRootCertPath, String unionpayMiddleCertPath){


        //将字符串转换为X509Certificate对象.
        X509Certificate x509Cert = genCertificateByStr(params.getString("signPubKeyCert"));
        if(x509Cert == null) {
            return false;
        }

        //检验证书链
        if(!verifyCertificate(x509Cert, unionpayRootCertPath, unionpayMiddleCertPath)){
            return false;
        }

        // 3.验签
        String stringSign = params.getString("signature");

        // 将请求参数信息转换成key1=value1&key2=value2的形式
        String stringData = convertSignString(params);
        try {
            Signature st = Signature.getInstance(ALGORITHM_SHA256WITHRSA, KEYSTORE_PROVIDER_BC);
            st.initVerify(x509Cert.getPublicKey());
            st.update(sha256X16(stringData, "UTF-8"));
            return st.verify(Base64.decodeBase64(stringSign.getBytes("UTF-8")));

            // 验证签名需要用银联发给商户的公钥证书.
        } catch (Exception e) {
            logger.error("验签失败！", e);
        }

        return false;
    }

    /** 将JSON中的数据转换成key1=value1&key2=value2的形式, 忽略空内容 和 signature字段 **/
    private static String convertSignString(JSONObject params) {
        TreeMap<String, Object> tree = new TreeMap<>();

        //1. 所有参数进行排序
        params.keySet().stream().forEach( key -> tree.put(key, params.get(key)));

        //2. 拼接为 key=value&形式
        StringBuffer stringBuffer = new StringBuffer();
        tree.keySet().stream().forEach( key -> {

            if(StringUtils.isAnyEmpty(key, tree.get(key).toString())){ //空值， 不参与签名
                return ;
            }
            if("signature".equals(key)){ //签名串， 不参与签名
                return ;
            }

            stringBuffer.append(key).append("=").append(tree.get(key).toString()).append("&");
        });

        //3. 去掉最后一个&
        return stringBuffer.substring(0, stringBuffer.length() - 1);
    }

    /** 通过SHA256进行摘要并转16进制  **/
    private static byte[] sha256X16(String data, String encoding) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.reset();
        md.update(data.getBytes(encoding));
        byte[] bytes = md.digest();

        StringBuilder sha256StrBuff = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            if (Integer.toHexString(0xFF & bytes[i]).length() == 1) {
                sha256StrBuff.append("0").append(Integer.toHexString(0xFF & bytes[i]));
            } else {
                sha256StrBuff.append(Integer.toHexString(0xFF & bytes[i]));
            }
        }
        return sha256StrBuff.toString().getBytes(encoding);
    }

    /** 获取证书私钥 **/
    private static PrivateKey getSignCertPrivateKey(String pfxkeyfile, String keypwd) {
        FileInputStream fis = null;

        try {
            KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE_PKCS12, KEYSTORE_PROVIDER_BC);
            fis = new FileInputStream(pfxkeyfile);
            char[] nPassword = null == keypwd || "".equals(keypwd.trim()) ? null: keypwd.toCharArray();
            if (null != keyStore) {
                keyStore.load(fis, nPassword);
            }
            Enumeration<String> aliasenum = keyStore.aliases();
            String keyAlias = null;
            if (aliasenum.hasMoreElements()) {
                keyAlias = aliasenum.nextElement();
            }
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(keyAlias, keypwd.toCharArray());
            return privateKey;
        } catch (Exception e) {
            logger.error("获取证书私钥失败！", e);
            return null;
        }finally {
            if(null!=fis) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /** 证书链验证 **/
    private static boolean verifyCertificate(X509Certificate cert, String rootCertPath, String middleCertPath) {

        if (null == cert) return false;

        try {
            cert.checkValidity();//验证有效期

            X509Certificate middleCert = getPublicCert(middleCertPath);  //获取中级证书
            if (null == middleCert) {
                return false;
            }

            X509Certificate rootCert = getPublicCert(rootCertPath);   //获取根证书
            if (null == rootCert) {
                return false;
            }

            X509CertSelector selector = new X509CertSelector();
            selector.setCertificate(cert);

            Set<TrustAnchor> trustAnchors = new HashSet<>();
            trustAnchors.add(new TrustAnchor(rootCert, null));
            PKIXBuilderParameters pkixParams = new PKIXBuilderParameters(trustAnchors, selector);
            Set<X509Certificate> intermediateCerts = new HashSet<>();
            intermediateCerts.add(rootCert);
            intermediateCerts.add(middleCert);
            intermediateCerts.add(cert);

            pkixParams.setRevocationEnabled(false);

            CertStore intermediateCertStore = CertStore.getInstance("Collection",
                    new CollectionCertStoreParameters(intermediateCerts), KEYSTORE_PROVIDER_BC);
            pkixParams.addCertStore(intermediateCertStore);

            CertPathBuilder builder = CertPathBuilder.getInstance("PKIX", KEYSTORE_PROVIDER_BC);

            @SuppressWarnings("unused")
            PKIXCertPathBuilderResult result = (PKIXCertPathBuilderResult) builder.build(pkixParams);
            return true;
        } catch (Exception e) {
            logger.error("验证证书失败！", e);
            return false;
        }

    }

    /** 通过证书路径初始化为公钥证书 **/
    private static X509Certificate getPublicCert(String path){
        X509Certificate encryptCertTemp = null;
        FileInputStream in = null;
        try {
            CertificateFactory cf = CertificateFactory.getInstance(CERTIFICATE_TYPE_X509, KEYSTORE_PROVIDER_BC);
            in = new FileInputStream(path);
            encryptCertTemp = (X509Certificate) cf.generateCertificate(in);
        } catch (Exception e) {
            logger.error("获取公钥失败！", e);
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        return encryptCertTemp;
    }

    /** 将字符串转换为X509Certificate对象.  */
    public static X509Certificate genCertificateByStr(String x509CertString) {
        X509Certificate x509Cert = null;
        try {
            CertificateFactory cf = CertificateFactory.getInstance(CERTIFICATE_TYPE_X509, KEYSTORE_PROVIDER_BC);
            InputStream tIn = new ByteArrayInputStream(x509CertString.getBytes("ISO-8859-1"));
            x509Cert = (X509Certificate) cf.generateCertificate(tIn);
        } catch (Exception e) {
            logger.error("证书转换异常", e);
        }
        return x509Cert;
    }

}
