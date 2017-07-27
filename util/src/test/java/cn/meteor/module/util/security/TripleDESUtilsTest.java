package cn.meteor.module.util.security;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import cn.meteor.module.util.lang.HexUtils;

public class TripleDESUtilsTest {

//	private final static Logger logger = LoggerFactory.getLogger(TripleDESUtilsTest.class);
	private static final Logger logger = LogManager.getLogger(TripleDESUtilsTest.class);

	@Test
	public void testEncrypt() {
		String key = "!@#$%besttone118114^&*()";
		String data = "210300037$$20090819154307";
//		String data = "210300037$$2009081915430";
		String encryptString = TripleDESUtils.encrypt(key.getBytes(), data);
		logger.info(encryptString);
	}
	
	@Test
	public void testDecrypt() {
		String key = "!@#$%besttone118114^&*()";
		String data = "ECBB21D9741C892E9B5C20462DB0FA77E3388322BB57288E70A455B01D0C03DE";
		String decryptString = TripleDESUtils.decrypt(key.getBytes(),data);
		logger.info(decryptString);
	}
	
	@Test
	public void testEncryptWithIV() {
		String key = "225AC1A9A923D3830230AD1F1030334A832D68CF4D94FDCF";
		byte[] keyBytes = HexUtils.hexToByteArray(key);
		
		String data = "2010-03-24 09:36:15$571300225$0$$WPk/mZGNOqoiUGEGtgtqxbB7l8KhskGc";
		
		byte[] iv =  { 1, 2, 3, 4, 5, 6, 7, 8 };
		
		String decryptString =TripleDESUtils.encryptWithIV(keyBytes, data, iv);
		logger.info(decryptString);
		
		logger.info("----------------------------------------------------------------------");
		String encodeBase64Data=Base64.encodeBase64String(HexUtils.hexToByteArray(decryptString));
		logger.info(encodeBase64Data);
	}
	
	@Test
	public void testDecryptWithIV() {
		String key = "225AC1A9A923D3830230AD1F1030334A832D68CF4D94FDCF";
		byte[] keyBytes = HexUtils.hexToByteArray(key);
		
		String data = "0sdqwifG3R4Y+aClzZWb0qrPXDrw0JnhBIo5lmKQHyS8WMehgez1DzZH3nYneEq7HN6VDSlBu+nNY9i8h22CFaPCwQGd+Iwb";
		byte[] decodeBase64Data=Base64.decodeBase64(data);
		String hexString=HexUtils.byteArrayToHex(decodeBase64Data);
		logger.info(hexString);
		
		byte[] iv =  { 1, 2, 3, 4, 5, 6, 7, 8 };
		
		String decryptString =TripleDESUtils.decryptWithIV(keyBytes, hexString , iv);
		logger.info(decryptString);		
	}
	
	@Test
	public void testDecryptWithIV_CIP_Test() {
		String key = "3A753D9FD1B2B2ADCCF4E27E372DD854BB260FB0BF2037CF";
		byte[] keyBytes = HexUtils.hexToByteArray(key);
		
		String data = "eT+WknWz4yOYTULRc6X04GiUEnrCcH7g1eDX5WGWaGBvqOc0MtYOdfFOgptcqva3q/yN2XPg5QKLmVteBSz2M01bO0c/DSZP";
		byte[] decodeBase64Data=Base64.decodeBase64(data);
		String hexString=HexUtils.byteArrayToHex(decodeBase64Data);
		logger.info(hexString);
		
		byte[] iv =  { 1, 2, 3, 4, 5, 6, 7, 8 };
		
		String decryptString =TripleDESUtils.decryptWithIV(keyBytes, hexString , iv);
		logger.info(decryptString);
	}
	
	@Test
	public void testDecryptWithIV_CIP() {
		String key = "FEA53F0FECBF8F2FFA4825DBD0816FAB416A1F8B8FB7C534";
		byte[] keyBytes = HexUtils.hexToByteArray(key);
		
		String data = "TrfSfSQtxOVUMjt31hdhYT9uoDoCq2gfZAjn8yynyLUzhTlzpT5W2qPwVMf+SFvYU5QJMZceTeVK7/IdJLuoaQ==";
		byte[] decodeBase64Data=Base64.decodeBase64(data);
		String hexString=HexUtils.byteArrayToHex(decodeBase64Data);
		logger.info(hexString);
		
		byte[] iv =  { 1, 2, 3, 4, 5, 6, 7, 8 };
		
		String decryptString =TripleDESUtils.decryptWithIV(keyBytes, hexString , iv);
		logger.info(decryptString);
	}

}
