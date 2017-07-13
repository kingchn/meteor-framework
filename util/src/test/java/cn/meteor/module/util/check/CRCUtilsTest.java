package cn.meteor.module.util.check;

import java.io.UnsupportedEncodingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import cn.meteor.module.util.check.CRCUtils.Parameters;
import cn.meteor.module.util.lang.HexUtils;

public class CRCUtilsTest {

	private  static final Logger logger = LogManager.getLogger(CRCUtilsTest.class);

	@Test
	public void testCRC() {
		String data = "123456789";
        long ccittCrc = CRCUtils.calculateCRC(CRCUtils.Parameters.CCITT, data.getBytes());
        System.out.printf("CRC is 0x%04X\n", ccittCrc); // prints "CRC is 0x29B1"
	}
	
	/**
	 * For larger data, table driven implementation is faster. Here is how to use it.
	 */
	@Test
	public void testLargeDataCRC() {
		String data = "123456789";
		CRCUtils tableDriven = new CRCUtils(CRCUtils.Parameters.XMODEM);
       	long xmodemCrc = tableDriven.calculateCRC(data.getBytes());
        System.out.printf("CRC is 0x%04X\n", xmodemCrc); // prints "CRC is 0x31C3"

       	// You can also reuse CRC object instance for another crc calculation.
        // Given that the only state for a CRC calculation is the "intermediate value"
        // and it is stored in your code, you can even use same CRC instance to calculate CRC
        // of multiple data sets in parallel.
       	// And if data is too big, you may feed it in chunks
       	long curValue = tableDriven.init(); // initialize intermediate value
       	curValue = tableDriven.update(curValue, "123456789".getBytes()); // feed first chunk
        curValue = tableDriven.update(curValue, "01234567890".getBytes()); // feed next chunk
       	long xmodemCrc2 = tableDriven.finalCRC(curValue); // gets CRC of whole data ("12345678901234567890")
        System.out.printf("CRC is 0x%04X\n", xmodemCrc2); // prints "CRC is 0x2C89"
	}
	
	@Test
	public void testCRC16() throws UnsupportedEncodingException {
		String data = "C8大排档</>888828282839485</>天河岑村822673</>广发银行8899223</>";
		long crcLong = CRCUtils.calculateCRC(new Parameters(16, 0x8005, 0x0000, false, false, 0x0), data.getBytes());
        System.out.printf("%04X\n", crcLong); // prints "CRC is 9BB0"
	}
	

}
