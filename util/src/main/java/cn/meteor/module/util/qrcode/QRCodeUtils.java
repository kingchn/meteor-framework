package cn.meteor.module.util.qrcode;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
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
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;

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
