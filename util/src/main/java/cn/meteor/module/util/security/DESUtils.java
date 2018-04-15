package cn.meteor.module.util.security;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DESUtils {

	private static final Logger logger = LogManager.getLogger(DESUtils.class);

	public static byte[] encrypt(byte[] dataBytes, byte[] desKeyBytes) {
		try {
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			DESKeySpec desKeySpec = new DESKeySpec(desKeyBytes);
			SecretKey key = keyFactory.generateSecret(desKeySpec);
			SecureRandom secureRandom = new SecureRandom();
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, key, secureRandom);
			return cipher.doFinal(dataBytes);
		} catch (Exception e) {
			logger.error("DES加密出错：", e);
			return null;
		}
	}
	
	public static byte[] decrypt(byte[] dataBytes, byte[] desKeyBytes) {
		try {
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			DESKeySpec desKeySpec = new DESKeySpec(desKeyBytes);
			SecretKey key = keyFactory.generateSecret(desKeySpec);
			SecureRandom secureRandom = new SecureRandom();
			Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
			cipher.init(Cipher.DECRYPT_MODE, key, secureRandom);
			return cipher.doFinal(dataBytes);
		} catch (Exception e) {
			logger.error("DES加密出错：", e);
			return null;
		}
	}
	
	public static String encryptWithBase64(String data, String desKey) throws UnsupportedEncodingException {
		byte[] dataBytes= data.getBytes("UTF-8");
		byte[] desKeyBytes = desKey.getBytes("UTF-8");
		byte[] encryptBytes = encrypt(dataBytes, desKeyBytes);
		String encryptBase64String = Base64.encodeBase64String(encryptBytes);
		return encryptBase64String;
	}
	
	public static String decryptWithBase64(String data, String desKey) throws UnsupportedEncodingException {
		byte[] dataBytes = Base64.decodeBase64(data);
		byte[] desKeyBytes = desKey.getBytes("UTF-8");
		byte[] plainDataBytes = decrypt(dataBytes, desKeyBytes);
		String plainDataString = new String(plainDataBytes, "UTF-8").trim();
		return plainDataString;
	}
	
	
}
