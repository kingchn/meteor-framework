package cn.meteor.module.util.lang;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class StringExtUtilsTest {
	
	private  static final Logger logger = LogManager.getLogger(StringExtUtilsTest.class);

	@Test
	public void testNumberFillZeroFront() {
		int num = 345;
		int finalLength = 6;
		String result = StringExtUtils.numberFillZeroFront(num, finalLength);
		logger.info(result);
	}
	
	@Test
	public void testSerialNumberIncrease() {
		String currentSerialNumber = "00123";
		String stringFormat = "00000";
		int increase = 7;
		String result = StringExtUtils.serialNumberIncrease(currentSerialNumber, stringFormat, increase);
		logger.info(result);
	}
	
}
