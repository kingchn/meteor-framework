package cn.meteor.module.util.lang;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * bytes与inputStream互转工具类
 * @author shenjc
 *
 */
public class BytesAndInputStreamConvertUtils {

	public static final InputStream bytesToInputStream(byte[] bytes) {
		return new ByteArrayInputStream(bytes);
	}

	public static final byte[] inputStreamToBytes(InputStream inputStream) throws IOException {
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		byte[] buffer = new byte[1024];
//		int len = 0;
//		while ((len = inputStream.read(buffer, 0, 1024)) > 0) {
//			baos.write(buffer, 0, len);
//		}
		ByteArrayOutputStream baos = inputStreamToByteArrayOutputStream(inputStream);
		return baos.toByteArray();
	}
	
	public static final ByteArrayOutputStream inputStreamToByteArrayOutputStream(InputStream inputStream) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inputStream.read(buffer, 0, 1024)) > 0) {
			baos.write(buffer, 0, len);
		}
		baos.flush();//是否需要??
		return baos;
	}
}
