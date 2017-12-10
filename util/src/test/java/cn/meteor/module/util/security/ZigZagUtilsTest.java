package cn.meteor.module.util.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class ZigZagUtilsTest {

	private static final Logger logger = LogManager.getLogger(ZigZagUtilsTest.class);

	@Test
	public void testConvert() {
		String data="PAYPALISHIRING";
//		String data="0123456789";
//		String data="abcdefghijklmnopq";
		
		int nRows =4;		
		String ret= ZigZagUtils.convert(data, nRows);
		System.out.println(data + "=>" + ret);
		
		String oriString= ZigZagUtils.unConvert(ret, nRows);
		System.out.println(ret + "=>" + oriString);
	}
}
