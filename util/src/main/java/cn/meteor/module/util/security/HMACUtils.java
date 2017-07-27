package cn.meteor.module.util.security;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class HMACUtils {
	
	private final static String DEFAULT_CHARSET_NAME = "utf-8";

	
	public static String hmacDigest(String data, String key) throws UnsupportedEncodingException, Exception {
		return hmacDigest(data, key, DEFAULT_CHARSET_NAME);
	}
	
	public static String hmacDigest(String data, String key, String charsetName) throws UnsupportedEncodingException, Exception {
		return byte2hex(hmacDigest(data.toString().getBytes(charsetName), key, charsetName));
	}
	
	public static String hmacDigest(String data, String key, Charset charset) throws UnsupportedEncodingException, Exception {
		return byte2hex(hmacDigest(data.toString().getBytes(charset), key, charset));
	}
	
	/**
	* HMAC加密算法
	*
	* @param data
	* @param key
	* @return
	* @throws Exception
	*/
	public static byte[] hmacDigest(byte[] data, String key) throws Exception {
	     return hmacDigest(data, key, DEFAULT_CHARSET_NAME);
	}
	
	public static byte[] hmacDigest(byte[] data, String key, String charsetName) throws Exception {
	     SecretKey secretKey = new SecretKeySpec(key.getBytes(charsetName), "HmacMD5");
	     Mac mac = Mac.getInstance(secretKey.getAlgorithm());
	     mac.init(secretKey);
	     return mac.doFinal(data);
	}
	
	public static byte[] hmacDigest(byte[] data, String key, Charset charset) throws Exception {
	     SecretKey secretKey = new SecretKeySpec(key.getBytes(charset), "HmacMD5");
	     Mac mac = Mac.getInstance(secretKey.getAlgorithm());
	     mac.init(secretKey);
	     return mac.doFinal(data);
	}
	
	/**
	* 二行制转字符串
	*/
	private static String byte2hex(byte[] b) {
	    	 StringBuffer hs = new StringBuffer();
	    	 String stmp = "";
	    	 for (int n = 0; n < b.length; n++) {
	        	 stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
	        	 if (stmp.length() == 1)
	        	 hs.append("0").append(stmp);
	        	 else
	        	 hs.append(stmp);
	    	 }
	    	 return hs.toString();
	}
}
