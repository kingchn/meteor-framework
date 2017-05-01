package cn.meteor.module.util.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * MD5工具类
 * 
 * @author shenjc
 * 
 */
public class MD5Utils {
	
//	private static final Logger logger = LoggerFactory.getLogger(MD5Utils.class);
	private static final Logger logger = LogManager.getLogger(MD5Utils.class);

//	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
	
	private final static String DEFAULT_CHARSET_NAME = "utf-8";
	
	/**
	 * MD5 摘要计算(String)
	 * @param data String
	 * @throws Exception
	 * @return String
	 */
	public static String md5Digest(String data) throws Exception {
		return md5Digest(data, DEFAULT_CHARSET_NAME);
	}
	
	/**
	 * MD5 摘要计算(String)
	 * @param data  String
	 * @param charsetName 字符集
	 * @return
	 * @throws Exception
	 */
	public static String md5Digest(String data, String charsetName) throws Exception {
		return byteArrayToHexString(md5Digest(data.getBytes(charsetName)));
	}
	
	/**
	 * MD5 摘要计算(String)
	 * @param data  String
	 * @param charsetName 字符集
	 * @return
	 * @throws Exception
	 */
	public static String md5Digest(String data, Charset charset) throws Exception {
		return byteArrayToHexString(md5Digest(data.getBytes(charset)));
	}
	
	/**
	 * MD5 摘要计算(File)
	 * @param file File
	 * @throws Exception
	 * @return String
	 */
	public static String md5Digest(File file) throws Exception {
		FileInputStream fis = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			fis = new FileInputStream(file);
			byte[] buffer = new byte[2048];
			int length = -1;
			while ((length = fis.read(buffer)) != -1) {
				md.update(buffer, 0, length);
			}
			byte[] b = md.digest();
			return byteArrayToHexString(b);
		} catch (Exception ex) {
			logger.error(ex.toString());
			return null;
		} finally {
			try {
				fis.close();
			} catch (IOException ex) {
				logger.error(ex.toString());
			}
		}
		
	}

	/**
	 * 转换字节数组为16进制字串
	 * @param b 字节数组
	 * @return 16进制字串
	 */
	public static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	/**
	 * 转换字节为16进制字串
	 * @param b 字节
	 * @return 16进制字串
	 */
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	/**
	 * MD5 摘要计算(byte[])
	 * @param data byte[]
	 * @throws Exception
	 * @return byte[] 16 bit digest
	 */
	public static byte[] md5Digest(byte[] data) throws Exception {
		java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5"); // MD5 is 16 bit message digest
		return md.digest(data);
	}



}
