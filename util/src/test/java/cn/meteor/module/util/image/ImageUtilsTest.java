package cn.meteor.module.util.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.junit.Test;

import cn.meteor.module.util.file.FileUtils;

public class ImageUtilsTest {

	@Test
	public void testJpgToPng() {
		try {
			File jpgFile = new File("D:/00_test_data/image/gd_fpzhang.jpg");
			BufferedImage bufferedImage = ImageUtils.jpgToPng(jpgFile);
			byte[] pngBytes = ImageUtils.bufferedImageToBytes(bufferedImage, "png");
			String targetFilePath = "D:/00_test_data/image/gd_fpzhang.png";
//			File targetFile = new File(targetFilePath);
//			FileUtils.writeByteArrayToFile(targetFile, pngBytes);
			FileUtils.writeFile(targetFilePath, pngBytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
