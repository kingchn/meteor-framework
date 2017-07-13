package cn.meteor.module.util.qrcode;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QRCodeUtils {
	
	
//	public static void encode111(String content, String filePath) {//TODO: 优化及测试
//		Charset charset = Charset.forName("UTF-8");
//		CharsetEncoder encoder = charset.newEncoder();
//		byte[] b = null;
//		try { // Convert a string to ISO-8859-1 bytes in a ByteBuffer
//			System.out.println("-------->" + content.length());
//			ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(content));
//			b = bbuf.array();
//		} catch (CharacterCodingException e) {
//			System.out.println(e.getMessage());
//		}
//		String data = "";
//		try {
//			data = new String(b, "iso8859-1");
//		} catch (UnsupportedEncodingException e) {
//			System.out.println(e.getMessage());
//		} // get a byte matrix for the data
//		BitMatrix matrix = null;
//		int h = 900;
//		int w = 800;
//		com.google.zxing.Writer writer = new QRCodeWriter();
//		try {
//			matrix = writer.encode(data, com.google.zxing.BarcodeFormat.QR_CODE, w, h);
//		} catch (com.google.zxing.WriterException e) {
//			System.out.println(e.getMessage());
//		}
//		File file = new File(filePath);
//		try {
//			MatrixToImageWriter.writeToFile(matrix, "PNG", file);
//			System.out.println("printing to " + file.getAbsolutePath());
//		} catch (IOException e) {
//			System.out.println(e.getMessage());
//		}
//	}
	
	/**
	 * 生成二维码图片字节数组
	 * @param text 内容文本
	 * @param width 宽度
	 * @param height 高度
	 * @param formatName 图片文件格式
	 * @param logoFile logo文件
	 * @return 二维码图片字节数组
	 * @throws IOException
	 * @throws WriterException
	 */
	public static byte[] encodeQRCodeToImageBytes(String text, int width, int height, String formatName, File logoFile) throws IOException, WriterException {
//		int left = 10;
//		int top = 10;
//		width = width + 2 * left + 2;
//		height = height + 2 * top + 2;
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();  
        // 指定纠错等级,纠错级别（L 7%、M 15%、Q 25%、H 30%）  
       hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);//
       // 内容所使用字符集编码  
       hints.put(EncodeHintType.CHARACTER_SET, "utf-8");//
//     hints.put(EncodeHintType.MAX_SIZE, 350);//设置图片的最大值  
//     hints.put(EncodeHintType.MIN_SIZE, 100);//设置图片的最小值  
       hints.put(EncodeHintType.MARGIN, 1);//设置二维码边的空度，非负数
       
//       com.google.zxing.Writer writer = new QRCodeWriter();
       com.google.zxing.Writer writer = new MultiFormatWriter();
         
       // get a byte matrix for the data
       BitMatrix bitMatrix = writer.encode(text,//要编码的内容  
               //编码类型，目前zxing支持：Aztec 2D,CODABAR 1D format,Code 39 1D,Code 93 1D ,Code 128 1D,  
               //Data Matrix 2D , EAN-8 1D,EAN-13 1D,ITF (Interleaved Two of Five) 1D,  
               //MaxiCode 2D barcode,PDF417,QR Code 2D,RSS 14,RSS EXPANDED,UPC-A 1D,UPC-E 1D,UPC/EAN extension,UPC_EAN_EXTENSION  
               BarcodeFormat.QR_CODE,  
               width, //条形码的宽度  
               height, //条形码的高度  
               hints);//生成条形码时的一些配置,此项可选
       
       bitMatrix = deleteWhite(bitMatrix);//删除白边
		
//		BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
		 BufferedImage bufferedImage = toBufferedImage(bitMatrix);
		 
		 bufferedImage = zoomInImage(bufferedImage, width, height);
		
		if(logoFile!=null) {
			//设置logo图标 
			bufferedImage = mergeBufferedImage(bufferedImage, logoFile);
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();		
		boolean flag = ImageIO.write(bufferedImage, formatName, out);
        byte[] qrCodeBytes = out.toByteArray();
		return qrCodeBytes;
	}
	
	/**
	 * 删除白边
	 * @param matrix
	 * @return
	 */
	private static BitMatrix deleteWhite(BitMatrix matrix) {
		int[] rec = matrix.getEnclosingRectangle();
		int resWidth = rec[2] + 1;
		int resHeight = rec[3] + 1;

		BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
		resMatrix.clear();
		for (int i = 0; i < resWidth; i++) {
			for (int j = 0; j < resHeight; j++) {
				if (matrix.get(i + rec[0], j + rec[1]))
					resMatrix.set(i, j);
			}
		}
		return resMatrix;
	}
	
	/**
     * 对图片进行放大
     * @param originalImage 原始图片
     * @param width
     * @param height
     * @return
     */
	public static BufferedImage zoomInImage(BufferedImage originalImage, int width, int height) {
		BufferedImage newImage = new BufferedImage(width, height, originalImage.getType());
		Graphics g = newImage.getGraphics();
		g.drawImage(originalImage, 0, 0, width, height, null);
		g.dispose();
		return newImage;
	}
	

     /**
      * 将logo合并到二维码中
     * @param bufferedImage 源二维码图片 
     * @param logoFile logo文件
     * @return 带有logo的二维码图片 
     * @throws IOException
     */
    private static BufferedImage mergeBufferedImage(BufferedImage bufferedImage, File logoFile) throws IOException{  
         /** 
          * 读取二维码图片，并构建绘图对象 
          */  
         Graphics2D g2 = bufferedImage.createGraphics();  
           
         int matrixWidth = bufferedImage.getWidth();  
         int matrixHeigh = bufferedImage.getHeight();  
           
         /** 
          * 读取Logo图片 
          */  
         BufferedImage logoBufferedImage = ImageIO.read(logoFile);  
  
         //开始绘制图片  
         g2.drawImage(logoBufferedImage,matrixWidth/5*2,matrixHeigh/5*2, matrixWidth/5, matrixHeigh/5, null);//绘制       
         BasicStroke stroke = new BasicStroke(5,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);   
         g2.setStroke(stroke);// 设置笔画对象  
         //指定弧度的圆角矩形  
         RoundRectangle2D.Float round = new RoundRectangle2D.Float(matrixWidth/5*2, matrixHeigh/5*2, matrixWidth/5, matrixHeigh/5,20,20);  
         g2.setColor(Color.white);  
         g2.draw(round);// 绘制圆弧矩形  
           
         //设置logo 有一道灰色边框  
         BasicStroke stroke2 = new BasicStroke(1,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);   
         g2.setStroke(stroke2);// 设置笔画对象  
         RoundRectangle2D.Float round2 = new RoundRectangle2D.Float(matrixWidth/5*2+2, matrixHeigh/5*2+2, matrixWidth/5-4, matrixHeigh/5-4,20,20);  
         g2.setColor(new Color(128,128,128));  
         g2.draw(round2);// 绘制圆弧矩形  
           
         g2.dispose();  
         bufferedImage.flush() ;  
         return bufferedImage ;  
     }
	
	private static final int BLACK = 0xFF000000;// 用于设置图案的颜色
	private static final int WHITE = 0xFFFFFFFF; // 用于背景色

	public static BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, (matrix.get(x, y) ? BLACK : WHITE));
				// image.setRGB(x, y, (matrix.get(x, y) ? Color.YELLOW.getRGB() : Color.CYAN.getRGB()));
			}
		}
		return image;
	}
    
    /**
	 * 生成二维码图片字节数组
	 * @param text 内容文本
	 * @param width 宽度
	 * @param height 高度
	 * @param formatName 图片文件格式
	 * @return 二维码图片字节数组
	 * @throws IOException
	 * @throws WriterException
	 */
	public static byte[] encodeQRCodeToImageBytes(String text, int width, int height, String formatName) throws IOException, WriterException {
		return encodeQRCodeToImageBytes(text, width, height, formatName, null); 
	}
	
	/**
	 * 生成二维码图片字节数组后做base64的字符串
	 * @param text 内容文本
	 * @param width 宽度
	 * @param height 高度
	 * @param formatName 图片文件格式
	 * @param logoFile logo文件
	 * @return 二维码图片字节数组base64的字符串
	 * @throws IOException
	 * @throws WriterException
	 */
	public static String encodeQRCodeToBase64String(String text, int width, int height, String formatName, File logoFile) throws IOException, WriterException {
		byte[] qrCodeBytes = encodeQRCodeToImageBytes(text, width, height, formatName, logoFile);
		return Base64.encodeBase64String(qrCodeBytes);
	}
	
	/**
	 * 生成二维码图片字节数组后做base64的字符串
	 * @param text 内容文本
	 * @param width 宽度
	 * @param height 高度
	 * @param formatName 图片文件格式
	 * @return 二维码图片字节数组base64的字符串
	 * @throws IOException
	 * @throws WriterException
	 */
	public static String encodeQRCodeToBase64String(String text, int width, int height, String formatName) throws IOException, WriterException {
		return encodeQRCodeToBase64String(text, width, height, formatName, null);
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
		//优化精度
//		hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
		//复杂模式，开启PURE_BARCODE模式
		hints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
		Result result = new MultiFormatReader().decode(bitmap, hints);
//		String rtn = result.getText();
//		System.out.println(rtn);
//		System.out.println(rtn.length());
		return result;
	}
}
