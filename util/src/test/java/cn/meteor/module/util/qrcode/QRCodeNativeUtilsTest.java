package cn.meteor.module.util.qrcode;

import org.junit.Test;

public class QRCodeNativeUtilsTest {

	@Test
	public void testTextToQRCodeJBIG2() {
		String jbig2Base64String = QRCodeNativeUtils.textToQRCodeJBIG2("abc123", 148, 148);
		System.out.println(jbig2Base64String);
	}
}
