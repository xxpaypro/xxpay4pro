package org.xxpay.pay.channel.hcpay.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XStreamUtil {
 
	private static XStream xStream;
	static{
		xStream = new XStream(new DomDriver());
	}
	
	//xml转java对象
	public static Object xmlToBean(String xml){
		return xStream.fromXML(xml);
	}
	
	//java对象转xml
	public static String beanToXml(Object obj){
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + xStream.toXML(obj);
	}

}