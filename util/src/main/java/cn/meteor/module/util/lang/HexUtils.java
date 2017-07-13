package cn.meteor.module.util.lang;

import java.io.ByteArrayOutputStream;

/**
 * 16进制转换工具类
 * @author shenjc
 *
 */
public class HexUtils {

	/*
	 * 16进制数字字符集
	 */
	private static String hexChars = "0123456789ABCDEF";
	
	/*
	 * 将字节数组转成16进制数字字符串
	 */
	public static String byteArrayToHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		// 将字节数组中每个字节拆解成2位16进制整数
		for (int i = 0; i < bytes.length; i++) {
			sb.append(hexChars.charAt((bytes[i] & 0xf0) >> 4));
			sb.append(hexChars.charAt((bytes[i] & 0x0f) >> 0));
		}
		return sb.toString();
	}
	
	/*
	 * 将字符串转成16进制数字字符串
	 */
	public static String stringToHex(String string) {
		return byteArrayToHex(string.getBytes());
	}


	/*
	 * 将16进制数字字符串转成字节数组
	 */
	public static byte[] hexToByteArray(String hexString) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(hexString.length() / 2);
		// 将每2位16进制整数组装成一个字节
		for (int i = 0; i < hexString.length(); i += 2)
			baos.write((hexChars.indexOf(hexString.charAt(i)) << 4 | hexChars.indexOf(hexString.charAt(i + 1))));
		return baos.toByteArray();
	}
	
	/*
	 * 将16进制数字字符串转成字符串
	 */
	public static String hexToString(String hexString) {		
		return new String(hexToByteArray(hexString));
	}
	

	
}
