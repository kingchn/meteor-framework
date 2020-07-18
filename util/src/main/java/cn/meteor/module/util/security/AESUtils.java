package cn.meteor.module.util.security;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** 
 ** 
 * AES128 算法，加密模式为ECB，填充模式为 pkcs7（实际就是pkcs5） 
 * 
 * 
 */  
public class AESUtils {
	

	private static final Logger logger = LogManager.getLogger(AESUtils.class);
  
	/**
	 * 加密
	 * 
	 * @param content 需要加密的内容
	 * @param key  加密密码
	 * @return
	 */
	public static byte[] encrypt(String content, String key) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new java.security.SecureRandom(key.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");// 创建密码器
			byte[] byteContent = content.getBytes("UTF-8");
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);// 初始化
			byte[] result = cipher.doFinal(byteContent);
			return result; // 加密
		} catch (NoSuchAlgorithmException e) {
			logger.error("调用AESUtils.encrypt出错", e);
		} catch (NoSuchPaddingException e) {
			logger.error("调用AESUtils.encrypt出错", e);
		} catch (InvalidKeyException e) {
			logger.error("调用AESUtils.encrypt出错", e);
		} catch (UnsupportedEncodingException e) {
			logger.error("调用AESUtils.encrypt出错", e);
		} catch (IllegalBlockSizeException e) {
			logger.error("调用AESUtils.encrypt出错", e);
		} catch (BadPaddingException e) {
			logger.error("调用AESUtils.encrypt出错", e);
		}
		return null;
	}

	/**
	 * 解密
	 * 
	 * @param content 待解密内容
	 * @param key 解密密钥
	 * @return
	 */
	public static byte[] decrypt(byte[] content, String key) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new java.security.SecureRandom(key.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);// 初始化
			byte[] result = cipher.doFinal(content);
			return result; // 加密
		} catch (NoSuchAlgorithmException e) {
			logger.error("调用AESUtils.decrypt出错", e);
		} catch (NoSuchPaddingException e) {
			logger.error("调用AESUtils.decrypt出错", e);
		} catch (InvalidKeyException e) {
			logger.error("调用AESUtils.decrypt出错", e);
		} catch (IllegalBlockSizeException e) {
			logger.error("调用AESUtils.decrypt出错", e);
		} catch (BadPaddingException e) {
			logger.error("调用AESUtils.decrypt出错", e);
		}
		return null;
	}

	/**
	 * 字符串加密
	 * 
	 * @param content 要加密的字符串
	 * @param key 加密的AES Key
	 * @return
	 */
	public static String encryptString(String content, String key) {
		return byteArrayToHexString(encrypt(content, key));
	}

	/**
	 * 字符串解密
	 * 
	 * @param content 要解密的字符串
	 * @param key 解密的AES Key
	 * @return
	 */
	public static String decryptString(String content, String key) {
		byte[] decryptFrom = hexToByteArray(content);
		byte[] decryptResult = decrypt(decryptFrom, key);
		return new String(decryptResult);
	}
    
	/*=========================================================*/
    
    /*
	 * 16进制数字字符集
	 */
	private static String hexChars = "0123456789ABCDEF";
    
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
	 * 将字节数组转成16进制数字字符串
	 */
	public static String byteArrayToHexString(byte[] data) {
		StringBuilder sb = new StringBuilder(data.length * 2);
		// 将字节数组中每个字节拆解成2位16进制整数
		for (int i = 0; i < data.length; i++) {
			sb.append(hexChars.charAt((data[i] & 0xf0) >> 4));
			sb.append(hexChars.charAt((data[i] & 0x0f) >> 0));
		}
		return sb.toString();
	}	
  
    
}  