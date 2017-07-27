package cn.meteor.module.util.security;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class SHA1UtilsTest {

//	private final static Logger logger = LoggerFactory.getLogger(SHA1UtilsTest.class);
	private static final Logger logger = LogManager.getLogger(SHA1UtilsTest.class);
	
	@Test
	public void testSha1Digest() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		
		String data="admin";
		logger.info(SHA1Utils.sha1Digest(data));
		
//		org.apache.commons.codec.digest.DigestUtils
//		logger.info(DigestUtils.shaHex(data));
	}
	
	
}
