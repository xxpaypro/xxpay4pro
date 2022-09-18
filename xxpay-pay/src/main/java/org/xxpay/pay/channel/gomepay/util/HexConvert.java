package org.xxpay.pay.channel.gomepay.util;

//import org.apache.log4j.Logger;

/**
 * 工具类：中文和16进制编码互转
 * @author 王博
 *
 */
public class HexConvert {
	//static Logger logger = Logger.getLogger(HexConvert.class);
	//转化字符串为十六进制编码
	public static String toHexCode(String content) 
	{ 
		String enUnicode=null;
		  for(int i=0;i<content.length();i++){
		   if(i==0){
		       enUnicode=getHexString(Integer.toHexString(content.charAt(i)).toUpperCase());
		      }else{
		       enUnicode=enUnicode+getHexString(Integer.toHexString(content.charAt(i)).toUpperCase());
		      }
		  }
		  return enUnicode;
	}
	private static String getHexString(String hexString){
	      String hexStr="";
	      for(int i=hexString.length();i<4;i++){
	       if(i==hexString.length())
	        hexStr="0";
	       else
	        hexStr=hexStr+"0";
	      }
	      return hexStr+hexString;
	 }

	
	// 转化十六进制编码为字符串 
	public static String toHexString(String content) 
	{ 
		try{
			String enUnicode=null;
			String deUnicode=null;
			for(int i=0;i<content.length();i++){
				if(enUnicode==null){
					enUnicode=String.valueOf(content.charAt(i));
				}else{
					enUnicode=enUnicode+content.charAt(i);
				}
				if(i%4==3){
					if(enUnicode!=null){
						if(deUnicode==null){
							deUnicode = String.valueOf((char)Integer.valueOf(enUnicode, 16).intValue());
						}else{
							deUnicode=deUnicode+String.valueOf((char)Integer.valueOf(enUnicode, 16).intValue());
						}
					}
					enUnicode=null;
				}
				
			}
			return deUnicode;
		}catch(Exception e){
			//logger.error("hex to string exception :",e);
			return content;
		}
	}



	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//String product = "4ECE8D2D72698F66652F4ED8";
		//String product1 = "充值";
		//System.out.println("十六进制加密:"+toHexCode(product1));
		//System.out.println(toHexString(product));
	
		
		System.out.println("十六进制解译:"+toHexString("6D890025002A002153CA768400208BA25355FF1A00310032003300390031003600200031003400310033003100320033003800330031"));
	}

}
