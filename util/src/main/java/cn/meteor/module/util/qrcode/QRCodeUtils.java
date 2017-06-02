package cn.meteor.module.util.qrcode;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QRCodeUtils {
	
	
	public static void encode(String content, String filePath) {//TODO: 优化及测试
		Charset charset = Charset.forName("UTF-8");
		CharsetEncoder encoder = charset.newEncoder();
		byte[] b = null;
		try { // Convert a string to ISO-8859-1 bytes in a ByteBuffer
			System.out.println("-------->" + content.length());
			ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(content));
			b = bbuf.array();
		} catch (CharacterCodingException e) {
			System.out.println(e.getMessage());
		}
		String data = "";
		try {
			data = new String(b, "iso8859-1");
		} catch (UnsupportedEncodingException e) {
			System.out.println(e.getMessage());
		} // get a byte matrix for the data
		BitMatrix matrix = null;
		int h = 900;
		int w = 800;
		com.google.zxing.Writer writer = new QRCodeWriter();
		try {
			matrix = writer.encode(data, com.google.zxing.BarcodeFormat.QR_CODE, w, h);
		} catch (com.google.zxing.WriterException e) {
			System.out.println(e.getMessage());
		}
		File file = new File(filePath);
		try {
			MatrixToImageWriter.writeToFile(matrix, "PNG", file);
			System.out.println("printing to " + file.getAbsolutePath());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * 生成二维码图片字节数组
	 * @param text 内容文本
	 * @param width 宽度
	 * @param height 高度
	 * @return 二维码图片字节数组
	 * @throws IOException
	 * @throws WriterException
	 */
	public static byte[] encodeQRCodeToImageBytes(String text, int width, int height) throws IOException, WriterException {
//		Charset charset = Charset.forName("UTF-8");
//		CharsetEncoder encoder = charset.newEncoder();
//		byte[] bytes = null;
//		// Convert a string to ISO-8859-1 bytes in a ByteBuffer
//		ByteBuffer byteBuffer = encoder.encode(CharBuffer.wrap(text));
//		bytes = byteBuffer.array();
//		
//		String data = new String(bytes, "iso8859-1");
		com.google.zxing.Writer writer = new QRCodeWriter();
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
//        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
//        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.MARGIN, 1);
		BitMatrix matrix = writer.encode(text, com.google.zxing.BarcodeFormat.QR_CODE, width, height, hints); // get a byte matrix for the data
		
//		matrix = deleteWhite(matrix);//删除白边
//		
//		width = matrix.getWidth();
//        height = matrix.getHeight();
//        int[] pixels = new int[width * height];
//        for (int y = 0; y < height; y++) {
//            for (int x = 0; x < width; x++) {
//                if (matrix.get(x, y)) {
//                    pixels[y * width + x] = Color.BLACK.getRGB();
//                } else {
//                    pixels[y * width + x] = Color.WHITE.getRGB();
//                }
//            }
//        }
//        BinaryBitmap bitmap = BinaryBitmap.createBitmap(width, height, BinaryBitmap.Config.ARGB_8888);
//        bitmap..setPixels(pixels, 0, width, 0, 0, width, height);
		
		BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(matrix);
		ByteArrayOutputStream out = new ByteArrayOutputStream();  
		boolean flag = ImageIO.write(bufferedImage, "jpg", out);
        byte[] qrCodeBytes = out.toByteArray();
		return qrCodeBytes;
	}
	
//	private static BitMatrix deleteWhite(BitMatrix matrix) {
//        int[] rec = matrix.getEnclosingRectangle();
//        int resWidth = rec[2] + 1;
//        int resHeight = rec[3] + 1;
//
//        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
//        resMatrix.clear();
//        for (int i = 0; i < resWidth; i++) {
//            for (int j = 0; j < resHeight; j++) {
//                if (matrix.get(i + rec[0], j + rec[1]))
//                    resMatrix.set(i, j);
//            }
//        }
//        return resMatrix;
//    }
	
	/**
	 * 生成二维码图片字节数组后做base64的字符串
	 * @param text 内容文本
	 * @param width 宽度
	 * @param height 高度
	 * @return 二维码图片字节数组base64的字符串
	 * @throws IOException
	 * @throws WriterException
	 */
	public static String encodeQRCodeToBase64String(String text, int width, int height) throws IOException, WriterException {
		byte[] qrCodeBytes = encodeQRCodeToImageBytes(text, width, height);
		return Base64.encodeBase64String(qrCodeBytes);
	}
	
	public static String decodeQRCodeBase64StringToString(String qrCodeBase64String) throws IOException, NotFoundException {
		Result result = decodeQRCodeBase64String(qrCodeBase64String);
		return result.getText();
	}

	public static Result decodeQRCodeBase64String(String qrCodeBase64String) throws IOException, NotFoundException {
		byte[] bytes = Base64.decodeBase64(qrCodeBase64String);
		InputStream inputStream = new ByteArrayInputStream(bytes);
		BufferedImage bufferedImage = ImageIO.read(inputStream);
		
		return decodeQRCode(bufferedImage);
	}
	
	public static String decodeQRCodeToString(File qrCodeFile) throws IOException, NotFoundException {
		Result result = decodeQRCode(qrCodeFile);
		return result.getText();
	}
	
	public static Result decodeQRCode(File qrCodeFile) throws IOException, NotFoundException {
		BufferedImage bufferedImage = ImageIO.read(qrCodeFile);
		
		return decodeQRCode(bufferedImage);
	}
	
	public static Result decodeQRCode(BufferedImage bufferedImage) throws IOException, NotFoundException {
		LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
		hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
		Result result = new MultiFormatReader().decode(bitmap, hints);
//		String rtn = result.getText();
//		System.out.println(rtn);
//		System.out.println(rtn.length());
		return result;
	}
}
