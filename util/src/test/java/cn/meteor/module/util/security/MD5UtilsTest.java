package cn.meteor.module.util.security;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class MD5UtilsTest {

	private  static final Logger logger = LogManager.getLogger(MD5UtilsTest.class);

	@Test
	public void testMd5Digest() {
		try {
//			String src = " MERCHANTID=123456789&ORDERSEQ=20060314000001&ORDERDATE=20060314&ORDERAMOUNT=10000";
			String src = "111111";
			String result = MD5Utils.md5Digest(src);			
			logger.info(result);
//			String result2=DigestUtils.md5Hex(src);
//			logger.info(result2);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}		
	}
	
	@Test
	public void testSign() throws Exception {
		// 使用key值生成 SIGN
		String keyStr = "123456";// 使用固定key
		// 获得的明文数据
		String desStr = "UPTRANSEQ=20080101000001&MERCHANTID=0250000001&ORDERID=2006050112564931556&PAYMENT=10000&RETNCODE=00&RETNINFO=00&PAYDATE =20060101";
		// 将key值和明文数据组织成一个待签名的串
		desStr = desStr + "&KEY=" + keyStr;
		logger.info("原文字符串 desStr ＝＝ " + desStr);
		// 生成 SIGN
		String SIGN = MD5Utils.md5Digest(desStr);
		logger.info("SIGN == " + SIGN);
	}
	
	@Test
	public void testMd5FileDigest() {
		try {
//			String pathname = "E:/z-push/a.txt";
			String pathname = "D:/77/adApi.log20130115/adApi.log20130115";
			File file = new File(pathname);
			String result = MD5Utils.md5Digest(file);			
			logger.info(result);
//			String result2=DigestUtils.md5Hex(src);
//			logger.info(result2);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}		
	}
}
