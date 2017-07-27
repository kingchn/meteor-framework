package cn.meteor.module.util.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * SHA1工具类
 * 
 * @author shenjc
 *
 */
public class SHA1Utils {
	
	private static final String Algorithm = "SHA-1"; // 定义 加密算法,可用	
	
	/**
	 * SHA1摘要计算
	 * @param data
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String sha1Digest(String data) throws NoSuchAlgorithmException, UnsupportedEncodingException { 
		return bytes2HexString(sha1DigestUtf8(data));	}
	
	
	public static byte[] sha1DigestUtf8(String data) throws NoSuchAlgorithmException,
			UnsupportedEncodingException {
		return sha1Digest(data.getBytes("UTF-8"));
	}

	private static byte[] sha1Digest(byte[] data) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance(Algorithm);
		md.update(data);
		byte[] output = md.digest();
		return output;

	}

	private static String bytes2HexString(byte[] b) {
		String ret = "";
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			ret += hex;
		}
		return ret;
	}
}
