package cn.meteor.module.util.security;

import java.io.ByteArrayOutputStream;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;

public class RSAUtils {

	/**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";
    
    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;
    
    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;
	
	/**
     * <p>
     * 生成密钥对(公钥和私钥)
     * </p>
     * 
     * @return
     */
    public static KeyPair genKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024, new SecureRandom());
        KeyPair keyPair = keyPairGen.generateKeyPair();
        return keyPair;
    }
    
    /**
     * 从密钥对中获取Base64字符串公钥
     * @param keyPair
     * @return Base64字符串公钥
     */
    public static String getBase64StringPublicKey(KeyPair keyPair) {
    	RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
    	System.out.println("toString: "+ rsaPublicKey.toString());
    	System.out.println("getFormat: "+ rsaPublicKey.getFormat());
    	String base64StringPublicKey = Base64.encodeBase64String(rsaPublicKey.getEncoded());//二进制数据编码为BASE64字符串
    	return base64StringPublicKey;
    }
    
    /**
     * 从密钥对中获取Base64字符串私钥
     * @param keyPair
     * @return Base64字符串私钥
     */
    public static String getBase64StringPrivateKey(KeyPair keyPair) {
    	RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
    	String base64StringPrivateKey = Base64.encodeBase64String(rsaPrivateKey.getEncoded());//二进制数据编码为BASE64字符串
    	return base64StringPrivateKey;
    }
    
    
    /** 
     * 由字符串转化得到的公钥对象 
     * @param base64StringPublicKey 公钥数据字符串 
     * @throws Exception 加载公钥时产生的异常 
     */  
    public static RSAPublicKey getRSAPublicKeyBybase64StringKey(String base64StringPublicKey) throws Exception {  
        try {  
//            BASE64Decoder base64Decoder= new BASE64Decoder();  
//            byte[] keyBytes= base64Decoder.decodeBuffer(publicKeyString);
            byte[] keyBytes= Base64.decodeBase64(base64StringPublicKey);
            KeyFactory keyFactory= KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec= new X509EncodedKeySpec(keyBytes);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {  
            throw new Exception("无此算法"+ e.getMessage());  
        } catch (InvalidKeySpecException e) {  
            throw new Exception("公钥非法"+ e.getMessage());  
        } catch (NullPointerException e) {  
            throw new Exception("公钥数据为空"+ e.getMessage());  
        }  
    }
    
    /**
     * 由字符串转化得到的私钥对象
     * @param base64StringPrivateKey
     * @return
     * @throws Exception
     */
    public static RSAPrivateKey getRSAPrivateKeyBybase64StringKey(String base64StringPrivateKey) throws Exception{
        try {  
//            BASE64Decoder base64Decoder= new BASE64Decoder();  
//            byte[] keyBytes= base64Decoder.decodeBuffer(base64StringPrivateKey);
            byte[] keyBytes= Base64.decodeBase64(base64StringPrivateKey);
            PKCS8EncodedKeySpec keySpec= new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory= KeyFactory.getInstance("RSA");  
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);  
        } catch (NoSuchAlgorithmException e) {  
            throw new Exception("无此算法"+ e.getMessage());  
        } catch (InvalidKeySpecException e) {  
            throw new Exception("私钥非法"+ e.getMessage());  
        } catch (NullPointerException e) {  
            throw new Exception("私钥数据为空"+ e.getMessage());  
        }  
    }
    
    
    
    /** 
     * 加密过程 
     * @param rsaPublicKey 公钥 
     * @param plainTextBytes 明文数据 
     * @return 
     * @throws Exception 加密过程中的异常信息 
     */  
    public static byte[] encrypt(RSAPublicKey rsaPublicKey, byte[] plainTextBytes) throws Exception{
        if(rsaPublicKey== null){  
            throw new Exception("加密公钥为空, 请设置");  
        }  
        Cipher cipher= null;  
        try {  
//            cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());
//        	cipher= Cipher.getInstance("RSA");//不使用Provider
        	cipher= Cipher.getInstance("RSA/ECB/PKCS1Padding");//不使用Provider
            cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey);  
//            byte[] output= cipher.doFinal(plainTextBytes);  
//            return output;
            int inputLen = plainTextBytes.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(plainTextBytes, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(plainTextBytes, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            out.close();
            return encryptedData;
        } catch (NoSuchAlgorithmException e) {  
            throw new Exception("无此加密算法"+ e.getMessage());  
        } catch (NoSuchPaddingException e) {  
            e.printStackTrace();  
            return null;  
        }catch (InvalidKeyException e) {  
            throw new Exception("加密公钥非法,请检查"+ e.getMessage());  
        } catch (IllegalBlockSizeException e) {  
            throw new Exception("明文长度非法"+ e.getMessage());  
        } catch (BadPaddingException e) {  
            throw new Exception("明文数据已损坏"+ e.getMessage());  
        }  
    }
    
    /**
     * 使用Base64字符串公钥进行加密
     * @param base64StringPublicKey Base64字符串公钥
     * @param plainTextBytes
     * @return
     * @throws Exception
     */
    public static byte[] encryptWithBase64StringPublicKey(String base64StringPublicKey, byte[] plainTextBytes) throws Exception{
    	RSAPublicKey rsaPublicKey = getRSAPublicKeyBybase64StringKey(base64StringPublicKey);//由字符串转化得到的公钥对象
    	return encrypt(rsaPublicKey, plainTextBytes);
    }
    
    
    /** 
     * 解密过程 
     * @param rsaPrivateKey 私钥 
     * @param cipherBytes 密文数据 
     * @return 明文 
     * @throws Exception 解密过程中的异常信息 
     */  
    public static byte[] decrypt(RSAPrivateKey rsaPrivateKey, byte[] cipherBytes) throws Exception{  
        if (rsaPrivateKey== null){  
            throw new Exception("解密私钥为空, 请设置");  
        }  
        Cipher cipher= null;  
        try {  
//            cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());
//            cipher= Cipher.getInstance("RSA");//不使用Provider
        	cipher= Cipher.getInstance("RSA/ECB/PKCS1Padding");//不使用Provider
            cipher.init(Cipher.DECRYPT_MODE, rsaPrivateKey);  
//            byte[] output= cipher.doFinal(cipherBytes);  
//            return output;  
            int inputLen = cipherBytes.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(cipherBytes, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(cipherBytes, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return decryptedData;
        } catch (NoSuchAlgorithmException e) {  
            throw new Exception("无此解密算法"+ e.getMessage());  
        } catch (NoSuchPaddingException e) {  
            e.printStackTrace();  
            return null;  
        }catch (InvalidKeyException e) {  
            throw new Exception("解密私钥非法,请检查"+ e.getMessage());  
        } catch (IllegalBlockSizeException e) {  
            throw new Exception("密文长度非法"+ e.getMessage());  
        } catch (BadPaddingException e) {  
            throw new Exception("密文数据已损坏"+ e.getMessage());  
        }         
    }
    
    /**
     * 使用Base64字符串私钥进行解密
     * @param base64StringPrivateKey Base64字符串私钥
     * @param cipherBytes
     * @return
     * @throws Exception
     */
    public static byte[] decryptWithBase64StringPrivateKey(String base64StringPrivateKey, byte[] cipherBytes) throws Exception{
    	RSAPrivateKey rsaPrivateKey = getRSAPrivateKeyBybase64StringKey(base64StringPrivateKey);
    	return decrypt(rsaPrivateKey, cipherBytes);
    }
    
    
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
