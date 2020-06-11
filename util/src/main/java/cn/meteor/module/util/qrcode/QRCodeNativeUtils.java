package cn.meteor.module.util.qrcode;

import com.sun.jna.Library;
import com.sun.jna.Native;

public class QRCodeNativeUtils {
	
	public interface CLibrary extends Library {
//		CLibrary cLibrary = (CLibrary) Native.loadLibrary("gBig2", CLibrary.class);
		CLibrary cLibrary = (CLibrary) Native.load("gBig2", CLibrary.class);

//		int Sum(int a, int b);
//		
//		void Hello1(GoString.ByValue msg);
//		
//		GoString.ByValue Hello2(GoString.ByValue msg);
//		
//		String Hello3(GoString.ByValue msg);
//		
//		int PrintHello2(byte[] input, byte[] output);
//		
//		int PrintHello3(byte[] input, String output);
		
		String TextToQRCodeJBIG2(GoString.ByValue content, int width, int height);		
	}
	
	/**
	 * 生成指定内容的二维码JBIG2图片
	 * @param content 二维码文本内容
	 * @return 二维码JBIG2图片base64字符串
	 */
	public static String textToQRCodeJBIG2(String content,  int width, int height) {
		String jbig2Base64String = QRCodeNativeUtils.CLibrary.cLibrary.TextToQRCodeJBIG2(new GoString.ByValue(content), width, height);
		return jbig2Base64String;
	}

}
