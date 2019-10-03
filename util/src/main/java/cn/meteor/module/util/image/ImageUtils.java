package cn.meteor.module.util.image;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * 图片工具类
 *
 */
public class ImageUtils {

	/**
	 * jpg图片转换成png并透明化
	 * 
	 * @param jpgFile jpg图片文件
	 * @return 透明化的png BufferedImage
	 * @throws IOException
	 */
	public static BufferedImage jpgToPng(File jpgFile) throws IOException {
		return jpgToPng(ImageIO.read(jpgFile));
	}

	/**
	 * jpg图片转换成png并透明化
	 * 
	 * @param jpgBufferedImage jpg图片BufferedImage
	 * @return 透明化的png BufferedImage
	 */
	public static BufferedImage jpgToPng(BufferedImage jpgBufferedImage) {
		if (jpgBufferedImage.getType() == BufferedImage.TYPE_4BYTE_ABGR) {
			// create a blank, RGB, same width and height, and a white background
			BufferedImage newBufferedImage = new BufferedImage(jpgBufferedImage.getWidth(),
					jpgBufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
			// TYPE_INT_RGB:创建一个RBG图像，24位深度，成功将32位图转化成24位
			newBufferedImage.createGraphics().drawImage(jpgBufferedImage, 0, 0, Color.WHITE, null);
			jpgBufferedImage = newBufferedImage;
		}
		ImageIcon imageIcon = new ImageIcon(jpgBufferedImage);
		BufferedImage bufferedImage = new BufferedImage(imageIcon.getIconWidth(), imageIcon.getIconHeight(),
				BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics();
		g2D.drawImage(imageIcon.getImage(), 0, 0, imageIcon.getImageObserver());
		int alpha = 0;
		for (int j1 = bufferedImage.getMinY(); j1 < bufferedImage.getHeight(); j1++) {
			for (int j2 = bufferedImage.getMinX(); j2 < bufferedImage.getWidth(); j2++) {
				int rgb = bufferedImage.getRGB(j2, j1);
				int r = (rgb & 0xff0000) >> 16;
				int g = (rgb & 0xff00) >> 8;
				int b = (rgb & 0xff);
				if (((255 - r) < 30) && ((255 - g) < 30) && ((255 - b) < 30)) {
					rgb = ((alpha + 1) << 24) | (rgb & 0x00ffffff);
				}
				bufferedImage.setRGB(j2, j1, rgb);
			}
		}
		g2D.drawImage(bufferedImage, 0, 0, imageIcon.getImageObserver());
		return bufferedImage;
	}

	/**
	 * 将bufferedImage转字节数组
	 * @param bufferedImage BufferedImage对象
	 * @param formatName 文件格式，如jpg、png
	 * @return 图片字节数组
	 * @throws IOException
	 */
	public static byte[] bufferedImageToBytes(BufferedImage bufferedImage, String formatName) throws IOException {
		// 创建输出流
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		// 写入流
		ImageIO.write(bufferedImage, formatName, byteArrayOutputStream);
		// 清流
		byteArrayOutputStream.flush();
		// 转为字节数组
		byte[] byteArray = byteArrayOutputStream.toByteArray();
		// 关闭流
		byteArrayOutputStream.close();
		return byteArray;
	}

}
