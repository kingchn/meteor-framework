package cn.meteor.module.util.security;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.meteor.module.util.lang.HexUtils;

/**
 * 3DES加密工具类
 * 
 * @author shenjc
 *
 */
public class TripleDESUtils {
	
//	private static final Logger logger = LoggerFactory.getLogger(TripleDESUtils.class);
	private static final Logger logger = LogManager.getLogger(TripleDESUtils.class);
	
	private static final String Algorithm = "DESede"; // 定义 加密算法,可用 DES,DESede,Blowfish	

	/**
	 * 加密
	 * @param key 加密密钥，长度为24字节
	 * @param data 待加密字符串
	 * @return 密文
	 */
	public static String encrypt(byte[] keyBytes, String data) {
		byte[] dataBytes = data.getBytes();
		byte[] encryptBytes = encryptMode(keyBytes, dataBytes);
		String encryptString = HexUtils.byteArrayToHex(encryptBytes);
		return encryptString;
	}
	
	/**
	 * 解密
	 * @param key 加密密钥，长度为24字节
	 * @param data 待解密字符串
	 * @return 明文
	 */
	public static String decrypt(byte[] keyBytes, String data) {
		byte[] dataBytes = HexUtils.hexToByteArray(data);
		byte[] decryptBytes = decryptMode(keyBytes, dataBytes);
		String decryptString = new String(decryptBytes);
		return decryptString;
	}
	
	public static String encryptWithIV(byte[] keyBytes, String data, byte[] iv) {
		byte[] dataBytes = data.getBytes();
		byte[] encryptBytes = encryptModeWithIV(keyBytes, dataBytes, iv);
		String encryptString = HexUtils.byteArrayToHex(encryptBytes);
		return encryptString;
	}
	
	public static String decryptWithIV(byte[] keyBytes, String data, byte[] iv) {
		byte[] dataBytes = HexUtils.hexToByteArray(data);
		byte[] decryptBytes = decryptModeWithIV(keyBytes, dataBytes, iv);
		String decryptString = new String(decryptBytes);
		return decryptString;
	}


	/**
	 * @param keyByte 加密密钥，长度为24字节
	 * @param data 被加密的数据缓冲区（源）
	 * @return
	 */
	private static byte[] encryptMode(byte[] keyByte, byte[] data) {
		try {
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(keyByte, Algorithm);
			// 加密
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.ENCRYPT_MODE, deskey);
			return c1.doFinal(data);
//			return new sun.misc.BASE64Encoder().encode(c1.doFinal(src)).getBytes();	//加上Base64编码
		} catch (java.security.NoSuchAlgorithmException e1) {
			logger.error(e1.getMessage());
		} catch (javax.crypto.NoSuchPaddingException e2) {
			logger.error(e2.getMessage());
		} catch (java.lang.Exception e3) {
			logger.error(e3.getMessage());
		}
		return null;
	}

	/**
	 * @param keyByte 加密密钥，长度为24字节
	 * @param dataBytes 加密后的缓冲区
	 * @return
	 */
	private static byte[] decryptMode(byte[] keyByte, byte[] dataBytes) {
		try {
//			data = new sun.misc.BASE64Decoder().decodeBuffer(new String(data));	//加上Base64编码
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(keyByte, Algorithm);
			// 解密
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.DECRYPT_MODE, deskey);
			return c1.doFinal(dataBytes);
		} catch (java.security.NoSuchAlgorithmException e1) {
			logger.error(e1.getMessage());
		} catch (javax.crypto.NoSuchPaddingException e2) {
			logger.error(e2.getMessage());
		} catch (java.lang.Exception e3) {
			logger.error(e3.getMessage());
		}
		return null;
	}
	
	private static byte[] encryptModeWithIV(byte[] keyBytes, byte[] dataBytes, byte[] iv) {
		try {
			DESedeKeySpec dks = new DESedeKeySpec(keyBytes);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
			SecretKey securekey = keyFactory.generateSecret(dks);
			IvParameterSpec ips = new IvParameterSpec(iv);
			Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, securekey, ips);
			return cipher.doFinal(dataBytes);
		} catch (InvalidKeyException e) {
			logger.error(e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage());
		} catch (InvalidKeySpecException e) {
			logger.error(e.getMessage());
		} catch (NoSuchPaddingException e) {
			logger.error(e.getMessage());
		} catch (InvalidAlgorithmParameterException e) {
			logger.error(e.getMessage());
		} catch (IllegalBlockSizeException e) {
			logger.error(e.getMessage());
		} catch (BadPaddingException e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	
	private static byte[] decryptModeWithIV(byte[] keyBytes, byte[] dataBytes, byte[] iv) {
			try {
				Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
				SecretKeyFactory skf = SecretKeyFactory.getInstance("DESede");

				SecretKey sk = skf.generateSecret(new DESedeKeySpec(keyBytes));
				IvParameterSpec ips = new IvParameterSpec(iv);
				cipher.init(javax.crypto.Cipher.DECRYPT_MODE, sk, ips);
				
				return cipher.doFinal(dataBytes);
			} catch (NoSuchAlgorithmException e) {
				logger.error(e.getMessage());
			} catch (NoSuchPaddingException e) {
				logger.error(e.getMessage());
			} catch (InvalidKeyException e) {
				logger.error(e.getMessage());
			} catch (InvalidKeySpecException e) {
				logger.error(e.getMessage());
			} catch (InvalidAlgorithmParameterException e) {
				logger.error(e.getMessage());
			} catch (IllegalBlockSizeException e) {
				logger.error(e.getMessage());
			} catch (BadPaddingException e) {
				logger.error(e.getMessage());
			}
			return null;
	}
	
	
//	/**
//	 * 二进制转十六进制字符串。每一个字节转为两位十六进制字符串。
//	 * 
//	 * @param b
//	 * @return
//	 */
//	public static String byte2hex(byte[] b) {
//		String hs = "";
//		String stmp = "";
//		for (int i = 0; i < b.length; i++) {
//			stmp = Integer.toHexString(b[i] & 0XFF);
//			if (stmp.length() == 1) {
//				hs = hs + "0" + stmp;
//			} else {
//				hs = hs + stmp;
//			}
//		}
//		return hs.toUpperCase();
//	}
//	
//	/**
//	 * 十六进制转字节数组
//	 * 
//	 * @param inputStr
//	 * @return
//	 */
//	public static byte[] hex2byte(String hex) throws IllegalArgumentException {
//		if (hex.length() % 2 != 0) {
//			throw new IllegalArgumentException();
//		}
//		if (hex.startsWith("0x")) {
//			hex = hex.substring(2);
//		}
//		char[] arr = hex.toCharArray();
//		byte[] b = new byte[hex.length() / 2];
//		for (int i = 0, j = 0, l = hex.length(); i < l; i++, j++) {
//			String swap = "" + arr[i++] + arr[i];
//			int byteint = Integer.parseInt(swap, 16) & 0xFF;
//			b[j] = new Integer(byteint).byteValue();
//		}
//		return b;
//	}

}
