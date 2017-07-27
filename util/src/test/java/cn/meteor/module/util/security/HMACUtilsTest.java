package cn.meteor.module.util.security;

import java.io.UnsupportedEncodingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class HMACUtilsTest {
	
//	private final static Logger logger = LoggerFactory.getLogger(HMACUtilsTest.class);
	private static final Logger logger = LogManager.getLogger(HMACUtilsTest.class);

	@Test
	public void testHMACDigest() {
		String data = "appKeyTEST_MOBILE_CLIENT_ANDROIDcurrentPage1methodcompany.logs.getpageSize2signMethodhmactimestamp2012-04-04 15:31:49";
		String key = "rUMfztTMjgyg9548uoA4zI2bjg3nKcaU";
		try {
			String result = HMACUtils.hmacDigest(data, key);
			logger.info(result);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
}
