package cn.meteor.module.util.security;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class DESUtilsTest {
	
	private static final Logger logger = LogManager.getLogger(DESUtilsTest.class);

	@Test
	public void testEncrypt() {
		String data = "abcdefghijklmnopqrst";
		String desKey = "12345678";
		byte[] encryptBytes = DESUtils.encrypt(data.getBytes(), desKey.getBytes());
		String encryptBase64String = Base64.encodeBase64String(encryptBytes);
		logger.info(encryptBase64String);
	}
	
	@Test
	public void testEncryptAndDecrypt() throws UnsupportedEncodingException {
		String data = "abcdefghijklmnopqrst";
		String desKey = "12345678";
		byte[] encryptBytes = DESUtils.encrypt(data.getBytes("utf-8"), desKey.getBytes("utf-8"));
		String encryptBase64String = Base64.encodeBase64String(encryptBytes);
		byte[] encrypt2Bytes = Base64.decodeBase64(encryptBase64String);
		byte[] decryptBytes = DESUtils.decrypt(encrypt2Bytes, desKey.getBytes());
		logger.info(new String(decryptBytes, "utf-8").trim());
	}
	
	@Test
	public void testEncryptAndDecryptWithBase64() throws UnsupportedEncodingException {
		String data = "abcdefghijklmnopqrst";
		String desKey = "12345678";
		String encryptBase64String = DESUtils.encryptWithBase64(data, desKey);
		String plainString = DESUtils.decryptWithBase64(encryptBase64String, desKey);
		logger.info(plainString);
	}
}
