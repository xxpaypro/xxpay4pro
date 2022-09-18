package org.xxpay.pay.channel.gomepay.util;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;


/**
 *
 * Title: DES加密算法�?
 * Copyright: Copyright (c) 2002 yipsilon.com
 * @author yipsilon
 * @version 0.1
 */

public class DSDES {
  public static int _DES = 1;
  public static int _DESede = 2;
  public static int _Blowfish = 3;

  private Cipher p_Cipher;
  private SecretKey p_Key;
  private String p_Algorithm;

  private void selectAlgorithm(int al){
    switch(al){
      default:
      case 1:
        this.p_Algorithm = "DES";
        break;
      case 2:
        this.p_Algorithm = "DESede";
        break;
      case 3:
        this.p_Algorithm = "Blowfish";
        break;
    }
  }

  public DSDES(int algorithm) throws Exception{
    this.selectAlgorithm(algorithm);
    Security.addProvider(new com.sun.crypto.provider.SunJCE());
    this.p_Cipher = Cipher.getInstance(this.p_Algorithm);
  }

  public byte[] getKey(){
    return this.checkKey().getEncoded();
  }

  private SecretKey checkKey(){
    try{
      if(this.p_Key == null){
        KeyGenerator keygen = KeyGenerator.getInstance(this.p_Algorithm);
        this.p_Key = keygen.generateKey();
      }
    }catch(NoSuchAlgorithmException nsae){}
    return this.p_Key;
  }

  public void setKey(byte[] enckey){
    this.p_Key = new SecretKeySpec(enckey,this.p_Algorithm);
  }

  public byte[] encode(byte[] data) throws Exception{
    this.p_Cipher.init(Cipher.ENCRYPT_MODE,this.checkKey());
    return this.p_Cipher.doFinal(data);
  }

  public byte[] decode(byte[] encdata, byte[] enckey) throws Exception{
    this.setKey(enckey);
    this.p_Cipher.init(Cipher.DECRYPT_MODE,this.p_Key);
    return this.p_Cipher.doFinal(encdata);
  }

  public String byte2hex(byte[] b){
    String hs = "";
    String stmp = "";
    for(int i = 0;i < b.length;i++){
      stmp = Integer.toHexString(b[i] & 0xFF);
      if(stmp.length() == 1) hs += "0" + stmp;
      else hs += stmp;
    }
    return hs.toUpperCase();
  }

  public byte[] hex2byte(String hex) throws IllegalArgumentException{
    if(hex.length() % 2 != 0)
      throw new IllegalArgumentException();
    char[] arr = hex.toCharArray();
    byte[] b = new byte[hex.length()/2];
    for(int i = 0,j = 0,l = hex.length();i < l;i++,j++){
      String swap = "" + arr[i++] + arr[i];
      int byteint = Integer.parseInt(swap,16) & 0xFF;
      b[j] = new Integer(byteint).byteValue();
    }
    return b;
  }
  

  public static byte[] strToHex(String arg0) throws Exception{
    byte[] bit = new byte[8];
    for (int k = 0, kk = 0; k < arg0.length(); k += 2, kk++) {
      char tmp1 = arg0.charAt(k);
      char tmp2 = arg0.charAt(k + 1);
      bit[kk] = (byte) ( ( (tmp1 & 0x0f) << 4) | (tmp2 & 0x0f));
    }
    return bit;

  }

  
 
  //根据密钥生成对应的密�?
  
  public static String getWhiteData(byte[] mykey ,byte[] data) throws Exception {
	    byte[] key; //密钥文件(byte)
	    //byte[] mykey = {49, 49, 50, 50, 51, 51, 52, 52}; //初始化密钥文�?byte)
	    DSDES des = new DSDES(DSDES._DES); // 声明DES
	    des.setKey(mykey); //设置密钥文件
	    key = des.getKey(); //获取随机生成的密�?-这里获取的实际上是固定的密钥，呵�?
	    byte[] dec = des.decode(data, key); //解密文件,其中转换十六进制密钥为byte
	    return new String(dec);
	  }
  
  	public static String getBlackData( byte[] mykey ,byte[] data) throws Exception {
  		byte[] key; //密钥文件(byte)
  		//byte[] mykey = {49, 49, 50, 50, 51, 51, 52, 52}; //初始化密钥文�?byte)
  		DSDES des = new DSDES(DSDES._DES); // 声明DES
  		des.setKey(mykey); //设置密钥文件
  		key = des.getKey(); //获取随机生成的密�?-这里获取的实际上是固定的密钥，呵�?
  		byte[] enc = des.encode(data); //生成加密文件(byte)
  		String hexenc = des.byte2hex(enc); //生成十六进制加密文件
  		return hexenc;
  	}
  	//解密
  	private byte[] byteMi = null;
    private byte[] byteMing = null;
    private String strMi= "";
    private String strM= ""; 
    private Key key;
  	public  void setDesString(String strMi){  
  	   BASE64Decoder base64De = new BASE64Decoder();   
  	    try
  	    {
  	     this.byteMi = base64De.decodeBuffer(strMi);  
  	      this.byteMing = this.getDesCode(byteMi);  
  	      this.strM = new String(byteMing,"UTF8");  
  	      }  
  	    catch(Exception e)
  	     {
  	      e.printStackTrace();
  	      }  
  	    finally
  	     {
  	      base64De = null;  
  	      byteMing = null;  
  	      byteMi = null;
  	      }  
  	  
  	  }
  	private byte[] getDesCode(byte[] byteD){
  	   Cipher cipher;  
  	    byte[] byteFina=null;  
  	    try{
  	     cipher = Cipher.getInstance("DES");  
  	      cipher.init(Cipher.DECRYPT_MODE,key);  
  	      byteFina = cipher.doFinal(byteD);
  	      }
  	   catch(Exception e)
  	     {
  	      e.printStackTrace();
  	      }
  	   finally
  	     {
  	      cipher=null;
  	      }  
  	    return byteFina;
  	  } 

  
  public static void main(String[] args) throws Exception{

    //String value = "123456";
	//String value = "merchno=100000000000001&dsorderid=1406787028045&amount=0.10&currency=USD&dsyburl=http://test5.bonusepay.com/bonuspay_wk_v1.01/notifytest.jsp&dstburl=http://test5.bonusepay.com/bonuspay_wk_v1.01/returntest.jsp?id=1406787028045&product=test&productdesc=test";
	//byte[] mykey ={49,49,49,49,49,49,49,49}; 
	//byte[] bit = DES.strToHex(value);//对明文进行码制转�?
	//String  sss = StringUtil.byte2hex(DES.encrypt(mykey,value.getBytes()));
    //String key = "11111111";
    //byte[] d = DES.getMacStr(key.getBytes(),bit);
    //String tmpStr = DES.getMacStr(key.getBytes(),bit);
    //String tmpStr1 = StringUtil.parseByteStr(tmpStr);
    //System.out.println("1111111======"+sss);
    
    DSDES ds= new DSDES(1);
    //key ="88888888";
    //key ="d3b03885";
    //String kkk = ds.getBlackData(key.getBytes(), value.getBytes());
    //System.out.println("1111111======"+kkk);
	 
	String deses = DSDES.getBlackData("a73c5cec".getBytes(), "merchno=611100000310745&merchname=676D5DDE670954C14FE1606F79D1628067099650516C53F8&dsorderid=CS-160719-19688-00029&product=632A5A015C0F9C7C004E006F00720064006900630020004E00610074007500720061006C00735A745E7C513F9CD59C7C9C7C6CB95A74513F004400480041542B7EF4751F7D2000410044&productdesc=632A5A015C0F9C7C004E006F00720064006900630020004E00610074007500720061006C00735A745E7C513F9CD59C7C9C7C6CB95A74513F004400480041542B7EF4751F7D2000410044&userno=413e063409014af8802309033f057d85&mediumno=0100980023127266&cardno=9001990024665770&currency=CNY&transcurrency=CNY&amount=61.545&cardtype=01&usertype=0&banktburl=http://www.kjyoupin.com/OnLinePayment/ebcpay/return_url.aspx&dsyburl=http://www.kjyoupin.com/OnLinePayment/ebcpay/notify_url.aspx&dsusername=502A65875A1F&dsidtype=0&dsidcard=340221198409300023".getBytes("utf-8"));
	System.out.println(deses);
	  
//    String data = DSDES.getWhiteData("abbd8ca5".getBytes(), ds.hex2byte("9B897E3D6AE7178368B9C16A5A2269E515C9C61C1F36B0C3844ABFB0A466133C04AA47821D5FA099F1C3DDDFB4EE83A03A667EB209349A1F3B6C822B2ABA7FA76D62F18CC8B34AEAF9904A5B8091E5D897F488EE81C39A3B0B3227B6A4FC16BFC6D7CD7B1736897BE05869B9709E9BBB4B3E65C4B2645BBA086EA62DBB47AFD105F2A7712C9E954DD7F0C0598231CFEE5E2190AD12298F9A05F1247019A529B54F6B46D5A9CB107DE05869B9709E9BBB4B3E65C4B2645BBA086EA62DBB47AFD1891CB7FAFADBF428BA9381D0841531D3B20A756E048951838B21D581DCE9E6817D897705756B1CD0DC281961F34D7CB448E3730A8C1F27775050E77A940BF513893FEDB6EF26ADF27A35B430BF9ED46317C37319D73F89CC"));
//    System.out.println(data);
    
    
    
    
  }
}

