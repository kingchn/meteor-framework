package cn.meteor.module.util.security;

import java.io.UnsupportedEncodingException;
import java.security.KeyPair;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import cn.meteor.module.util.time.DateUtils;

public class RSAUtilsTest {
	
	@Test
	public void base64Test() throws UnsupportedEncodingException {		
		String inputString = "785B849137535E3F503B7E4A9CACCA73D5355C05844258157B72B0F5F65C0F56F9E3DACA3CF58C5727B7EE4F1014113FA270B6FE2A26CCAB4CD7020792F12F58B6B1DFBBB9EBAA1BBCD1DB22829EDB14003872273E6CA8EF9F3CDFC81D03BFFD3B8FDA887AEB3E05DC711AAE3545A469080B4B8C421E43BE1CF3A12B9BE7A29B";
//		byte[] binaryData = RSAUtils.hexToByteArray(inputString);
		byte[] binaryData =inputString.getBytes("UTF-8");
		String base64String = Base64.encodeBase64String(binaryData);
		System.out.println("Base64: "+base64String);
	}

	@Test
	public void rsaUtilTest() throws Exception {//指定字符串公钥私钥加密解密
		System.out.println(DateUtils.parseDateToString(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
		
		//测试字符串  
//        String testString = "{\"result\":\"成功\",\"reason\":\"00\",\"sessionInfo\":{\"sessionId\":\"47dcd0dc151d44d1af4316d7acb35b3b-1361066802848\",\"pubKey\":\"WoFk5k7KIE2goNDdDOU9NCUw\",\"priKey\":\"OqdNp3UwMCG7OJCsBysx9OTs\",\"deviceNo\":\"1\",\"userId\":\"3807\",\"userLevel\":\"2\",\"userPhone\":\"13316089112\",\"provCode\":\"440000\",\"cityCode\":\"440100\"}}";  
		String testString = "tttest123";
//		KeyPair keyPair = RSAUtils.genKeyPair();
		
//		String base64StringPublicKey = RSAUtils.getBase64StringPublicKey(keyPair);
//		String base64StringPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDEDWzOuUVKBKWdTmkBG++kuJM" +
//				"s9TTxZEXkMd80DtK1Nm7ifWyz/HyDXrxTXy/M15b56IcJ4XrrmJK5Cs+58GL9dTu" +
//				"tbH1wU7QLYsgCRnps/EWe7ScKHkE657KdGOUctoSrpyrAT5Bejm16/G1Dt22EE8b" +
//				"jJt7FhiuKRGIk0W24wIDAQAB";
		String base64StringPublicKey = RSAUtilsTestConstants.BASE64_STRING_PUBLIC_KEY;
		
		System.out.println("公钥字符串： " + base64StringPublicKey);
		
//		String base64StringPrivateKey = RSAUtils.getBase64StringPrivateKey(keyPair);
//		String base64StringPrivateKey =  "MIICXQIBAAKBgQDDEDWzOuUVKBKWdTmkBG++kuJMs9TTxZEXkMd80DtK1Nm7ifWy" +
//				"z/HyDXrxTXy/M15b56IcJ4XrrmJK5Cs+58GL9dTutbH1wU7QLYsgCRnps/EWe7Sc" +
//				"KHkE657KdGOUctoSrpyrAT5Bejm16/G1Dt22EE8bjJt7FhiuKRGIk0W24wIDAQAB" +
//				"AoGAGX+p9WbygUR+wYpLYaRu0xOyX2J2pMRDxjfXy/oQdiTC3aAYLSq/ruuSMa72" +
//				"aqwVXqA18cY83GUsVivjd1KmWyGXIAwGsORmiVFOWRXrAD5rs9FXcIONGHzf/tXA" +
//				"0xTKnqtVHzty8kqUo5MgtFjg7yBxxZnlX1Z04v0e2Y7tjVkCQQD6aseubZFBeeQ0" +
//				"M/6EFLezPYuYKEkZFYnfflJqqCbeNbZmDx95H7CnMpWxNpLliVrxWh+RJeVU1JAc" +
//				"DtrJQCEtAkEAx2mBgcMBaFTip81VNSw3bb4kwCFSKfcoJ8Da1fTlcwDzjE1IGcJq" +
//				"xMayB/ua1nzn4lvtQqOn/Pa9QzEJHZGiTwJALn9vTnMn2pmQhyT0aIAUUCLobbtl" +
//				"P1Qhgq9JDGbmuLIOiUrblDkPKyyYYQx6IEbt7QzH+cKon6TMkXkspNvUjQJBALUf" +
//				"SXgZLUAcSbpsMEaZoMbW+/5exYhEa36gFqRbScUJSi1XTHPJOLz+VFoX1+2VifD5" +
//				"2ii7CSwk2IcdFNB8pNUCQQDRtH2tBh9vkAUVjF9LS2JV829g/Lmer4ZuhDr4Ddyi" +
//				"+1MBVHiB7w6YFLK+5XY8tV1o5wUb1ayjOSvX5Ku0LTcq";
		String base64StringPrivateKey = RSAUtilsTestConstants.BASE64_STRING_PRIVATE_KEY;
//		String base64StringPrivateKeyPKCS8 =  "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMMQNbM65RUoEpZ1" +
//				"OaQEb76S4kyz1NPFkReQx3zQO0rU2buJ9bLP8fINevFNfL8zXlvnohwnheuuYkrk" +
//				"Kz7nwYv11O61sfXBTtAtiyAJGemz8RZ7tJwoeQTrnsp0Y5Ry2hKunKsBPkF6ObXr" +
//				"8bUO3bYQTxuMm3sWGK4pEYiTRbbjAgMBAAECgYAZf6n1ZvKBRH7BikthpG7TE7Jf" +
//				"YnakxEPGN9fL+hB2JMLdoBgtKr+u65IxrvZqrBVeoDXxxjzcZSxWK+N3UqZbIZcg" +
//				"DAaw5GaJUU5ZFesAPmuz0Vdwg40YfN/+1cDTFMqeq1UfO3LySpSjkyC0WODvIHHF" +
//				"meVfVnTi/R7Zju2NWQJBAPpqx65tkUF55DQz/oQUt7M9i5goSRkVid9+UmqoJt41" +
//				"tmYPH3kfsKcylbE2kuWJWvFaH5El5VTUkBwO2slAIS0CQQDHaYGBwwFoVOKnzVU1" +
//				"LDdtviTAIVIp9ygnwNrV9OVzAPOMTUgZwmrExrIH+5rWfOfiW+1Co6f89r1DMQkd" +
//				"kaJPAkAuf29OcyfamZCHJPRogBRQIuhtu2U/VCGCr0kMZua4sg6JStuUOQ8rLJhh" +
//				"DHogRu3tDMf5wqifpMyReSyk29SNAkEAtR9JeBktQBxJumwwRpmgxtb7/l7FiERr" +
//				"fqAWpFtJxQlKLVdMc8k4vP5UWhfX7ZWJ8PnaKLsJLCTYhx0U0Hyk1QJBANG0fa0G" +
//				"H2+QBRWMX0tLYlXzb2D8uZ6vhm6EOvgN3KL7UwFUeIHvDpgUsr7ldjy1XWjnBRvV" +
//				"rKM5K9fkq7QtNyo=";
		String base64StringPrivateKeyPKCS8 = RSAUtilsTestConstants.BASE64_STRING_PRIVATE_KEY_PKCS8;
		System.out.println("私钥字符串1： " + base64StringPrivateKey);
		System.out.println("私钥字符串2： " + base64StringPrivateKeyPKCS8);
		
		//加密
		byte[] cipherBytes = RSAUtils.encryptWithBase64StringPublicKey(base64StringPublicKey, testString.getBytes("UTF-8"));
		System.out.println("加密后的字节数组的长度: "+cipherBytes.length);
		
//		String cipherString = new String(cipherBytes);//会乱码
		String cipherString = RSAUtils.byteArrayToHexString(cipherBytes);//将字节数组转成16进制数字字符串
		System.out.println("加密后的字符串的长度: "+cipherString.length());		
		System.out.println("加密后的密文: "+cipherString);
		
//		String cipherBase64String = Base64.encodeBase64String(cipherBytes);
//		System.out.println("加密后的Base64密文: "+cipherBase64String);
		
		//解密
//		String cipherBase64String="fQfNWubHkA9ZlUcx4ayp5+4mH41+/uh5BJhQQwtpixptiRCkwSYudFBCxk9EWFbOhdr/TQMhG9W7IWbjHFelcbo8dR28UGWuWh00cror0+CTHBHrkdtwM43cvwXMpUlku72p+IR8wqljOy4CO4zMah+XtWudOzULec2zar1yhwg=";
//		byte[] cipherBytes2 = Base64.decodeBase64(cipherBase64String);
//		cipherString="1E798B2D4286F566BF64C37458B3F6D5BAF2108613FD6487E847BC789C729A4F47B93769C2F818A31DB39481F4A229D63389167FA778A82BF7C4C0444D19A955CB749256919125C3F394C0CC4882B93FD7CB76659959B4798EFDCACDF9DF6A86EDD20DBC8549756C926CC4D765D32A2A5BA4E19A2D9E887577D6C670853C0866";
		byte[] cipherBytes2 = RSAUtils.hexToByteArray(cipherString);
        byte[] plainTextBytes = RSAUtils.decryptWithBase64StringPrivateKey(base64StringPrivateKeyPKCS8, cipherBytes2);
        
        String plainText = new String(plainTextBytes);
        System.out.println("解密结果: "+plainText);  
        
        System.out.println(DateUtils.parseDateToString(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
	}
	
	@Test
	public void rsaHexStringBase64Test() throws Exception {//指定字符串公钥私钥加密解密
		System.out.println(DateUtils.parseDateToString(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
		
		//测试字符串  
//        String testString = "{\"result\":\"成功\",\"reason\":\"00\",\"sessionInfo\":{\"sessionId\":\"47dcd0dc151d44d1af4316d7acb35b3b-1361066802848\",\"pubKey\":\"WoFk5k7KIE2goNDdDOU9NCUw\",\"priKey\":\"OqdNp3UwMCG7OJCsBysx9OTs\",\"deviceNo\":\"1\",\"userId\":\"3807\",\"userLevel\":\"2\",\"userPhone\":\"13316089112\",\"provCode\":\"440000\",\"cityCode\":\"440100\"}}";  
		String testString = "tttest12300888";
		String base64StringPublicKey = RSAUtilsTestConstants.BASE64_STRING_PUBLIC_KEY;		
		System.out.println("公钥字符串： " + base64StringPublicKey);
		String base64StringPrivateKey = RSAUtilsTestConstants.BASE64_STRING_PRIVATE_KEY;
		String base64StringPrivateKeyPKCS8 = RSAUtilsTestConstants.BASE64_STRING_PRIVATE_KEY_PKCS8;
		System.out.println("私钥字符串： " + base64StringPrivateKey);
		System.out.println("私钥字符串(PKCS8)： " + base64StringPrivateKeyPKCS8);
		
		//加密
		byte[] cipherBytes = RSAUtils.encryptWithBase64StringPublicKey(base64StringPublicKey, testString.getBytes("UTF-8"));
		System.out.println("加密后的字节数组的长度: "+cipherBytes.length);
		
//		String cipherString = new String(cipherBytes);//会乱码
		String cipherHexString = RSAUtils.byteArrayToHexString(cipherBytes);//将字节数组转成16进制数字字符串
		System.out.println("加密后的密文(16进制字符串)的长度: "+cipherHexString.length());		
		System.out.println("加密后的密文(16进制字符串): "+cipherHexString);
		
		//再对16进制字符串做base64
		byte[] cipherHexStringBinaryData =cipherHexString.getBytes("UTF-8");
		String cipherHexStringBase64String = Base64.encodeBase64String(cipherHexStringBinaryData);
		System.out.println("加密后的密文(16进制字符串)再做base64编码: "+cipherHexStringBase64String);
		
//		cipherHexStringBase64String="OEM2QjE1MjVCRTZBQjdBRkUyODM4RkIzN0EwMTdFNTBGMEJEOEUxNEU1REMxRUE1RTA0RjYzMUFBMzM0NzIyQ0U4NkM4QTYyNjRFNDNCODk5QzU3RTA2QkVGMEJDNjlCMkFFRjNCRTdDMjhDOTVDOTg2NzY0RThBMTk1QUQ3RTIwMDVGRDJCMzk4ODBEMTkzRENEQ0E3OEVDNkJCQjI3QkZFMDQ3MzA0MDhDRUZCNDlBMkZDNzZDNzlDN0Y1NzJEREVGRTk4QUUwMTIwNjM0RjAzQ0Y1MDI2RjQ4OTdBRDdFQjdCRjVDNEVBNzI1QzEwOTk4MTAxNjFCRTU5QTVGNA==";
		
		String cipherDirectBase64String = Base64.encodeBase64String(cipherBytes);
		System.out.println("加密后的密文(直接)再做base64编码: "+cipherDirectBase64String);
		
		//===========================================================
		//解密
		byte[] cipherHexStringBase64DecodeBinaryData = Base64.decodeBase64(cipherHexStringBase64String);
		String cipherHexStringBase64DecodeString = new String(cipherHexStringBase64DecodeBinaryData);//与cipherString相同，为16进制字符串
		System.out.println("base64解码后的密文(16进制字符串): "+cipherHexStringBase64DecodeString);
		
//		String cipherBase64String="fQfNWubHkA9ZlUcx4ayp5+4mH41+/uh5BJhQQwtpixptiRCkwSYudFBCxk9EWFbOhdr/TQMhG9W7IWbjHFelcbo8dR28UGWuWh00cror0+CTHBHrkdtwM43cvwXMpUlku72p+IR8wqljOy4CO4zMah+XtWudOzULec2zar1yhwg=";
//		byte[] cipherBytes2 = Base64.decodeBase64(cipherBase64String);
//		cipherString="1E798B2D4286F566BF64C37458B3F6D5BAF2108613FD6487E847BC789C729A4F47B93769C2F818A31DB39481F4A229D63389167FA778A82BF7C4C0444D19A955CB749256919125C3F394C0CC4882B93FD7CB76659959B4798EFDCACDF9DF6A86EDD20DBC8549756C926CC4D765D32A2A5BA4E19A2D9E887577D6C670853C0866";
		byte[] cipherBytes2 = RSAUtils.hexToByteArray(cipherHexStringBase64DecodeString);
        byte[] plainTextBytes = RSAUtils.decryptWithBase64StringPrivateKey(base64StringPrivateKeyPKCS8, cipherBytes2);
        
        String plainText = new String(plainTextBytes);
        System.out.println("解密结果: "+plainText);  
        
        System.out.println(DateUtils.parseDateToString(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
	}
	
	
	
	
	//  public static void main(String[] args) throws Exception {
	//	System.out.println(DateUtils.parseDateToString(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
	//	
	//	//测试字符串  
	//    String testString = "{\"result\":\"成功\",\"reason\":\"00\",\"sessionInfo\":{\"sessionId\":\"47dcd0dc151d44d1af4316d7acb35b3b-1361066802848\",\"pubKey\":\"WoFk5k7KIE2goNDdDOU9NCUw\",\"priKey\":\"OqdNp3UwMCG7OJCsBysx9OTs\",\"deviceNo\":\"1\",\"userId\":\"3807\",\"userLevel\":\"2\",\"userPhone\":\"13300000000\",\"provCode\":\"440000\",\"cityCode\":\"440100\"}}";  
	////	String testString = "tttest123";
	//	KeyPair keyPair = genKeyPair();
	//	RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
	//	RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
	//	
	//	
	////	String publicKeyString = byteArrayToHexString(rsaPublicKey.getEncoded());
	//	String base64StringPublicKey = Base64.encodeBase64String(rsaPublicKey.getEncoded());//二进制数据编码为BASE64字符串
	//	System.out.println("公钥字符串： " + base64StringPublicKey);
	//	
	//	String base64StringPrivateKey = Base64.encodeBase64String(rsaPrivateKey.getEncoded());//二进制数据编码为BASE64字符串
	//	System.out.println("私钥字符串： " + base64StringPrivateKey);
	//	
	//	rsaPublicKey = getRSAPublicKeyBybase64StringKey(base64StringPublicKey);//由字符串转化得到的公钥对象
	//	rsaPrivateKey = getRSAPrivateKeyBybase64StringKey(base64StringPrivateKey);
	//	
	//	
	//	//加密
	////	byte[] cipherBytes = encrypt(rsaPublicKey, testString.getBytes("UTF-8"));
	//	byte[] cipherBytes = encryptWithBase64StringPublicKey(base64StringPublicKey, testString.getBytes("UTF-8"));
	//	
	////	String cipherString = new String(cipherBytes);//会乱码
	//	String cipherString = byteArrayToHexString(cipherBytes);//将字节数组转成16进制数字字符串
	//	 System.out.println("加密后的密文: "+cipherString);
	//	
	//	//解密
	//	cipherBytes = hexToByteArray(cipherString);
	////    byte[] plainTextBytes = decrypt(rsaPrivateKey, cipherBytes);
	//    byte[] plainTextBytes = decryptWithBase64StringPrivateKey(base64StringPrivateKey, cipherBytes);
	//    
	//    String plainText = new String(plainTextBytes);
	////    String plainText= byteArrayToHexString(plainTextBytes);
	////    plainText = new String(plainText);
	//    System.out.println("解密结果: "+plainText);  
	//    
	//    System.out.println(DateUtils.parseDateToString(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
	//}

	@Test
	public void rsaUtilTest2() throws Exception {//生成公钥私钥加密解密--连串操作
//		System.out.println(DateUtils.parseDateToString(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
		
		//测试字符串  
	//    String testString = "{\"result\":\"成功\",\"reason\":\"00\",\"sessionInfo\":{\"sessionId\":\"47dcd0dc151d44d1af4316d7acb35b3b-1361066802848\",\"pubKey\":\"WoFk5k7KIE2goNDdDOU9NCUw\",\"priKey\":\"OqdNp3UwMCG7OJCsBysx9OTs\",\"deviceNo\":\"1\",\"userId\":\"3807\",\"userLevel\":\"2\",\"userPhone\":\"13316089112\",\"provCode\":\"440000\",\"cityCode\":\"440100\"}}";  
		String testString = "tttest123";
		KeyPair keyPair = RSAUtils.genKeyPair();
		
		String base64StringPublicKey = RSAUtils.getBase64StringPublicKey(keyPair);
		System.out.println("公钥字符串： " + base64StringPublicKey);
		
		String base64StringPrivateKey = RSAUtils.getBase64StringPrivateKey(keyPair);
		System.out.println("私钥字符串： " + base64StringPrivateKey);		
		
		//加密
		byte[] cipherBytes = RSAUtils.encryptWithBase64StringPublicKey(base64StringPublicKey, testString.getBytes("UTF-8"));
		System.out.println("加密后的字节数组的长度: "+cipherBytes.length);
		
	//	String cipherString = new String(cipherBytes);//会乱码
		String cipherString = RSAUtils.byteArrayToHexString(cipherBytes);//将字节数组转成16进制数字字符串
		System.out.println("加密后的字符串的长度: "+cipherString.length());
		
		System.out.println("加密后的密文: "+cipherString);
		
		//解密
		byte[] cipherBytes2 = RSAUtils.hexToByteArray(cipherString);
	    byte[] plainTextBytes = RSAUtils.decryptWithBase64StringPrivateKey(base64StringPrivateKey, cipherBytes2);
	    
	    String plainText = new String(plainTextBytes);
	    System.out.println("解密结果: "+plainText);  
	    
	    System.out.println(DateUtils.parseDateToString(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
	}
	
	@Test
	public void encryptTest() throws Exception {//加密
		String base64StringPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCOLV+fd2cHb8BjDsYwN3+Ig51rVFBr7ioXhriM" + "\r"+
				"U+E/LmR4T7gckuKbOQS1rtY0n+OldPeDCO5roSQ4MAlaCkCJ8Bngee8H5Z704k/uy8ABCE1dujsG" + "\r"+
				"LOt1XwH6PbB5GH1xn7x6+50WDjYJLh5RAa7UrnfZ35XyplG1XqVv69z6GwIDAQAB";
		//测试字符串  
	//    String testString = "{\"result\":\"成功\",\"reason\":\"00\",\"sessionInfo\":{\"sessionId\":\"47dcd0dc151d44d1af4316d7acb35b3b-1361066802848\",\"pubKey\":\"WoFk5k7KIE2goNDdDOU9NCUw\",\"priKey\":\"OqdNp3UwMCG7OJCsBysx9OTs\",\"deviceNo\":\"1\",\"userId\":\"3807\",\"userLevel\":\"2\",\"userPhone\":\"13316089112\",\"provCode\":\"440000\",\"cityCode\":\"440100\"}}";  
		String testString = "tttest123";
	    
		//加密
		byte[] cipherBytes = RSAUtils.encryptWithBase64StringPublicKey(base64StringPublicKey, testString.getBytes("UTF-8"));
		
	//	String cipherString = new String(cipherBytes);//会乱码
		String cipherString = RSAUtils.byteArrayToHexString(cipherBytes);//将字节数组转成16进制数字字符串
		System.out.println("加密后的密文: "+cipherString);
	}
	
	@Test
	public void decryptTest() throws Exception {//解密
		String aak =  "MIICXQIBAAKBgQDDEDWzOuUVKBKWdTmkBG++kuJMs9TTxZEXkMd80DtK1Nm7ifWyz/HyDXrxTXy/M15b56IcJ4XrrmJK5Cs+58GL9dTutbH1wU7QLYsgCRnps/EWe7ScKHkE657KdGOUctoSrpyrAT5Bejm16/G1Dt22EE8bjJt7FhiuKRGIk0W24wIDAQABAoGAGX+p9WbygUR+wYpLYaRu0xOyX2J2pMRDxjfXy/oQdiTC3aAYLSq/ruuSMa72aqwVXqA18cY83GUsVivjd1KmWyGXIAwGsORmiVFOWRXrAD5rs9FXcIONGHzf/tXA0xTKnqtVHzty8kqUo5MgtFjg7yBxxZnlX1Z04v0e2Y7tjVkCQQD6aseubZFBeeQ0M/6EFLezPYuYKEkZFYnfflJqqCbeNbZmDx95H7CnMpWxNpLliVrxWh+RJeVU1JAcDtrJQCEtAkEAx2mBgcMBaFTip81VNSw3bb4kwCFSKfcoJ8Da1fTlcwDzjE1IGcJqxMayB/ua1nzn4lvtQqOn/Pa9QzEJHZGiTwJALn9vTnMn2pmQhyT0aIAUUCLobbtlP1Qhgq9JDGbmuLIOiUrblDkPKyyYYQx6IEbt7QzH+cKon6TMkXkspNvUjQJBALUfSXgZLUAcSbpsMEaZoMbW+/5exYhEa36gFqRbScUJSi1XTHPJOLz+VFoX1+2VifD52ii7CSwk2IcdFNB8pNUCQQDRtH2tBh9vkAUVjF9LS2JV829g/Lmer4ZuhDr4Ddyi+1MBVHiB7w6YFLK+5XY8tV1o5wUb1ayjOSvX5Ku0LTcq";
		String base64StringPrivateKey =  "MIICXQIBAAKBgQDDEDWzOuUVKBKWdTmkBG++kuJMs9TTxZEXkMd80DtK1Nm7ifWy" +
				"z/HyDXrxTXy/M15b56IcJ4XrrmJK5Cs+58GL9dTutbH1wU7QLYsgCRnps/EWe7Sc" +
				"KHkE657KdGOUctoSrpyrAT5Bejm16/G1Dt22EE8bjJt7FhiuKRGIk0W24wIDAQAB" +
				"AoGAGX+p9WbygUR+wYpLYaRu0xOyX2J2pMRDxjfXy/oQdiTC3aAYLSq/ruuSMa72" +
				"aqwVXqA18cY83GUsVivjd1KmWyGXIAwGsORmiVFOWRXrAD5rs9FXcIONGHzf/tXA" +
				"0xTKnqtVHzty8kqUo5MgtFjg7yBxxZnlX1Z04v0e2Y7tjVkCQQD6aseubZFBeeQ0" +
				"M/6EFLezPYuYKEkZFYnfflJqqCbeNbZmDx95H7CnMpWxNpLliVrxWh+RJeVU1JAc" +
				"DtrJQCEtAkEAx2mBgcMBaFTip81VNSw3bb4kwCFSKfcoJ8Da1fTlcwDzjE1IGcJq" +
				"xMayB/ua1nzn4lvtQqOn/Pa9QzEJHZGiTwJALn9vTnMn2pmQhyT0aIAUUCLobbtl" +
				"P1Qhgq9JDGbmuLIOiUrblDkPKyyYYQx6IEbt7QzH+cKon6TMkXkspNvUjQJBALUf" +
				"SXgZLUAcSbpsMEaZoMbW+/5exYhEa36gFqRbScUJSi1XTHPJOLz+VFoX1+2VifD5" +
				"2ii7CSwk2IcdFNB8pNUCQQDRtH2tBh9vkAUVjF9LS2JV829g/Lmer4ZuhDr4Ddyi" +
				"+1MBVHiB7w6YFLK+5XY8tV1o5wUb1ayjOSvX5Ku0LTcq";
		String base64StringPrivateKeyPKCS8 =  "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMMQNbM65RUoEpZ1" +
				"OaQEb76S4kyz1NPFkReQx3zQO0rU2buJ9bLP8fINevFNfL8zXlvnohwnheuuYkrk" +
				"Kz7nwYv11O61sfXBTtAtiyAJGemz8RZ7tJwoeQTrnsp0Y5Ry2hKunKsBPkF6ObXr" +
				"8bUO3bYQTxuMm3sWGK4pEYiTRbbjAgMBAAECgYAZf6n1ZvKBRH7BikthpG7TE7Jf" +
				"YnakxEPGN9fL+hB2JMLdoBgtKr+u65IxrvZqrBVeoDXxxjzcZSxWK+N3UqZbIZcg" +
				"DAaw5GaJUU5ZFesAPmuz0Vdwg40YfN/+1cDTFMqeq1UfO3LySpSjkyC0WODvIHHF" +
				"meVfVnTi/R7Zju2NWQJBAPpqx65tkUF55DQz/oQUt7M9i5goSRkVid9+UmqoJt41" +
				"tmYPH3kfsKcylbE2kuWJWvFaH5El5VTUkBwO2slAIS0CQQDHaYGBwwFoVOKnzVU1" +
				"LDdtviTAIVIp9ygnwNrV9OVzAPOMTUgZwmrExrIH+5rWfOfiW+1Co6f89r1DMQkd" +
				"kaJPAkAuf29OcyfamZCHJPRogBRQIuhtu2U/VCGCr0kMZua4sg6JStuUOQ8rLJhh" +
				"DHogRu3tDMf5wqifpMyReSyk29SNAkEAtR9JeBktQBxJumwwRpmgxtb7/l7FiERr" +
				"fqAWpFtJxQlKLVdMc8k4vP5UWhfX7ZWJ8PnaKLsJLCTYhx0U0Hyk1QJBANG0fa0G" +
				"H2+QBRWMX0tLYlXzb2D8uZ6vhm6EOvgN3KL7UwFUeIHvDpgUsr7ldjy1XWjnBRvV" +
				"rKM5K9fkq7QtNyo=";
		
		base64StringPrivateKeyPKCS8= RSAUtilsTestConstants.API_RESP_PRIVATE_KEY;
//		String cipherString = "50b9c12cea1514af8c86856378c1c57b8026c3bd3c659184701022dba3876bc52f222c1dd2c3b6a0e2f87a8edd707cf4e47733693c86ace13aeaa64c3e7ef55fc13131b88d90c014d20558ca8c27b1b6cfdfadc9e055b742ad1d1c9adf4572168bd4213692bcab1400599670bed8732fa4949ecae0eeff64c207d26d2d88c08f27c37e76f0bfe5e08052fb218ec64be24032b710e0c0bf482597152a8e8b0a405d9b883d3a4b9f99c80b303dcbf35c96db83bb9b393c3d4472b7121d2918a757c906e38426d88da321baf6a8a2b7fbdd7c9a9089468537d154632101d4a737dd2e3a228a7d147501dbc497094ff4ea95a4dd62bbf2c7be656e88dc8fd3b43622829f078c8c733aa6c1b70342006736f10461bb5b9979ae3b2387368631fdfd1e9255fd52f4c57d6fe256998f7951cba7b84017426bfd443dc419f14324caece23423a43ba758adb6e5fa5833a74007444d7b23baced9ed4400c7cb2de7c03bbea9e5d8dcf3e7c4c2996137d907c74a6ec3efdcd3d8a544eb0ba3b4d13522fbf1".toUpperCase();
		String cipherString = "B42BAE4A34C976FD9EC58B93E891D1D724CBA5FC08B077B4C32BE1F0A4DDB8DC869C4D28C42D9BFB57F093DF3AB2B2819A7EC79B7A64B369F36D1DDB2BD09E2B9BBD00DF605C1B9721FEDDAA48B337B9BFDBDDA60E7FB83A8ECD5F701AFD6EFF2F495D3ACBC613CBE19D1D7F0C18B859A79BFFD9C8D41270A71521B3F5CC1471";
		
		
		byte[] cipherBytes = RSAUtils.hexToByteArray(cipherString);
		byte[] plainTextBytes = RSAUtils.decryptWithBase64StringPrivateKey(base64StringPrivateKeyPKCS8, cipherBytes);
	
		String plainText = new String(plainTextBytes);
		System.out.println("解密结果: " + plainText);
	}
	
//	public void genDatTest() throws Exception {// 生成文件
//		KeyPair keyPair = RSAUtils.genKeyPair();
//		String fileName = "d:\\mykey.dat";
//		ObjectOutputStream out;
//		try {
//			out = new ObjectOutputStream(new FileOutputStream(fileName));
//		} catch (IOException e) {
//			e.printStackTrace();
//			return;
//		}
//		try {
//			out.writeObject(keyPair);
//		} catch (IOException e) {
//			e.printStackTrace();
//			return;
//		} finally {
//			try {
//				out.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//				return;
//			}
//		}
//	}
}
