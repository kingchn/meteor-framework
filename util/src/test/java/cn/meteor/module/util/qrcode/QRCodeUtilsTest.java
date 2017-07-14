package cn.meteor.module.util.qrcode;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.Encoder;

public class QRCodeUtilsTest {

	private  static final Logger logger = LogManager.getLogger(QRCodeUtilsTest.class);
	
	@Test
	public void encodeQRCodeToBase64String() {
		String text = "01,51,044001600211,98580990,149.60,20170515,80692991863912413132,4BE5";
//		int width=75;
//		int height=75;
//		int width=100;
//		int height=100;
		int width = 430; // 二维码图片宽度 300
        int height = 430; // 二维码图片高度300
		try {
			File logoFile = new File("E:\\defa01.png");
//			String qrCodeBase64String = QRCodeUtils2.encodeQRCodeToBase64String(text, width, height, "jpg");
			String qrCodeBase64String = QRCodeUtils.encodeQRCodeToBase64String(text, width, height, "jpg", logoFile);
//			byte[] imageBytes = QRCodeUtils.encodeQRCodeToImageBytes(text, width, height, "jpg");
			System.out.println(qrCodeBase64String);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testDecodeQRCodeBase64StringToString() {
//		String qrCodeBase64String ="Qk3CAwAAAAAAAD4AAAAoAAAASwAAAEsAAAABAAEAAAAAAIQDAAAAAAAAAAAAAAAAAAACAAAAAAAA///////////////////gAAAAAwM/A8/z/8AgAAAAAwM/A8/z/8AgAAA/8/DAAA/z/8DgAAA/8/DAAA/z/8DgAAAwM8A8zMA8M/PgAAAwM8A8zMA8M/PgAAAwM/D/8PA88/MgAAAwM/D/8PA88/MgAAAwMwAPPDzzADMgAAAwMwAPPDzzADMgAAA/8/AD/wzwPwPgAAA/8/AD/wzwPwPgAAAAAwPA88D/MzDgAAAAAwPA88D/MzDgAAD//wPAzzAMPzzgAAD//wPAzzAMPzzgAADw88zAAM/zAAAgAADw88zAAM/zAAAgAAAAz8Mzz8P/D8PgAAAAz8Mzz8P/D8PgAAADAMPDDwzwzwzgAAADAMPDDwzwzwzgAADwPPz/PAw8zAzgAADwPPz/PAw8zAzgAADP8MwD8/MzM8AgAADP8MwD8/MzM8AgAAAzPzMwA/Azz8PgAAAzPzMwA/Azz8PgAAA/MDPADzwMA/DgAAA/MDPADzwMA/DgAAAAzwww/wwAzA/gAAAAzwww/wwAzA/gAADwA///AwzwM8MgAADwA///AwzwM8MgAAAwzDzMDAAwDwDgAAAwzDzMDAAwDwDgAADw88P8/MD8zMDgAADw88P8/MD8zMDgAAA8z//D8PwMzDzgAAA8z//D8PwMzDzgAAAz8ADPPzzzM8DgAAAz8ADPPzzzM8DgAAAMz8/8/AzwPMDgAAAMz8/8/AzwPMDgAADDMDAA88z8D8DgAADDMDAA88z8D8DgAAAwPPwwzzAMzADgAAAwPPwwzzAMzADgAADMAw8wAM/zM8MgAADMAw8wAM/zM8MgAAAwPPwPz8wwDwPgAAAwPPwPz8wwDwPgAADMwAz/AwD8/MDgAADMwAz/AwD8/MDgAADzDAA/DAPMzDPgAADzDAA/DAPMzDPgAAA/MAAz8M8zAA8gAAA/MAAz8M8zAA8gAAD//zMwwAz8P//gAAD//zMwwAz8P//gAAAAAzMzMzMzMAAgAAAAAzMzMzMzMAAgAAA/8zwP8DMP8/8gAAA/8zwP8DMP8/8gAAAwMw888zAM8wMgAAAwMw888zAM8wMgAAAwMwz8/DAM8wMgAAAwMwz8/DAM8wMgAAAwM8P8zA//MwMgAAAwM8P8zA//MwMgAAA/88MAMz8z8/8gAAA/88MAMz8z8/8gAAAAAzzA8M/M8AAgAAAAAzzA8M/M8AAgAAA=";
		String qrCodeBase64String ="Qk3CAwAAAAAAAD4AAAAoAAAASwAAAEsAAAABAAEAAAAAAIQDAAAAAAAAAAAAAAAAAAACAAAAAAAA///////////////////gAAAAAzPMw8PDPMwgAAAAAzPMw8PDPMwgAAA/8/PDPAAPDA/gAAA/8/PDPAAPDA/gAAAwM8D88P/zzDAgAAAwM8D88P/zzDAgAAAwMz88AM/DzwzgAAAwMz88AM/DzwzgAAAwM8DzAzzPAD8gAAAwM8DzAzzPAD8gAAA/8zMAAzP8PwMgAAA/8zMAAzP8PwMgAAAAA/PP888AMzMgAAAAA/PP888AMzMgAAD//zP/8zDPPwwgAAD//zP/8zDPPwwgAADzAzA/A8/zAAAgAADzAzA/A8/zAAAgAAAzDPPwzwzPz8wgAAAzDPPwzwzPz8wgAADAMPMD8PwzwPAgAADAMPMD8PwzwPAgAAAADDAwDzPDM/MgAAAADDAwDzPDM/MgAAA/M/AD/zPD8M8gAAA/M/AD/zPD8M8gAAD/PzPDDw/DDA/gAAD/PzPDDw/DDA/gAAAzwwA/DP8DPA8gAAAzwwA/DP8DPA8gAAAP/ADP8///8M8gAAAP/ADP8///8M8gAAAAMwAM8wDPwAwgAAAAMwAM8wDPwAwgAAAwD/APz8wzDMDgAAAwD/APz8wzDMDgAAAAAw/AP/Mz8zMgAAAAAw/AP/Mz8zMgAAADz/AMAADPwDwgAAADz/AMAADPwDwgAADA8P8z8M8zM8AgAADA8P8z8M8zM8AgAADzDD/AwAzA/8zgAADzDD/AwAzA/8zgAADAMzwzAwDPD/AgAADAMzwzAwDPD/AgAADwPP/zDM/zM/wgAADwPP/zDM/zM/wgAAAPAP//wADD8M8gAAAPAP//wADD8M8gAADADDzA/8MMPwzgAADADDzA/8MMPwzgAADAAwADMw8DwD8gAADAAwADMw8DwD8gAAA//wP8zAwD8PAgAAA//wP8zAwD8PAgAAA8ww/zzPwPzP/gAAA8ww/zzPwPzP/gAAD///AAPAD8P//gAAD///AAPAD8P//gAAAAAzMzMzMzMAAgAAAAAzMzMzMzMAAgAAA/8wzPwAMw8/8gAAA/8wzPwAMw8/8gAAAwM/M/DzAM8wMgAAAwM/M/DzAM8wMgAAAwM/Dz/Dw8MwMgAAAwM/Dz/Dw8MwMgAAAwM/8wz/AwMwMgAAAwM/8wz/AwMwMgAAA/8/w/zzw88/8gAAA/8/w/zzw88/8gAAAAA/APzzP8MAAgAAAAA/APzzP8MAAgAAA=";
		
		try {
			String content = QRCodeUtils.decodeQRCodeBase64StringToString(qrCodeBase64String);
			System.out.println(content);
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void encodeQRCodeToImageBytes() {
////		a123456789012345aaaaaaa
//		String text1 = "$01YTwvPjEyMzQ1Njc4OTAxMjM0NTwvPmFhYWFhYWE8Lz48Lz5DN0FE$";
//
////		a123456789012345aaaaaaaa
//		String text2 = "$01YTwvPjEyMzQ1Njc4OTAxMjM0NTwvPmFhYWFhYWFhPC8+PC8+Mzc1Mw==$";
//		System.out.println("text 1 length:" + text1.length());
//		System.out.println("text 2 length:" + text2.length());
//		System.out.println("text 1 length:" + text1.getBytes().length);
//		System.out.println("text 2 length:" + text2.getBytes().length);
		
		String text ="$015pa55qyj56eR5oqA5pyJ6ZmQ5YWs5Y+4PC8+OTE0NDAxMDE3MDgzNTg4MzNNPC8+5bm/5bee6auY5paw5oqA5pyv5Lqn5Lia5byA5Y+R5Yy656eR5a2m5Z+O56eR5a2m5aSn6YGTMTgy5Y+35Yib5paw5aSn5Y6mQzHljLrnrKzkupTlsYI0MDEtNTAy5Y2V5L2NMDIwLTYyOTgzMzMzPC8+5Lit5Zu96ZO26KGM5bm/5bee6LaK56eA5pSv6KGMNzM5MzU3NzU1NTQ2PC8+QUE3MQ==$";
//		String text ="$01QzjlpKfmjpLmoaM8Lz44ODg4MjgyODI4Mzk0ODU8Lz7lpKnmsrPlspHmnZE4MjI2NzM8Lz7lub/lj5Hpk7booYw4ODk5MjIzPC8+OUJCMA==$";
//		String text ="$01YTwvPjEyMzQ1Njc4OTAxMjM0NTwvPjwvPjwvPjM4N0Q=$";
//		String text ="$01YTwvPjEyMzQ1Njc4OTAxMjM0NTwvPjwvPjwvPjM$";
		int length = text.length();
		System.out.println("length:" + text.length());	
		
//		编码模式:
//			Numeric             数字
//			Alphanumeric        英文字母
//			Binary              二进制
//			Kanji             汉字
		
//		本规范中QR码符号规格采用版本12（小于等于419字符）、18（大于419字符，小于等于816字符）和25（大于816字符，小于等于1451字符）规格，并根据内容长度自动匹配。
//		 规范的判断是按编码模式Alphanumeric
		
		//我们按Binary模式判断 版本12 287个字符； 版本18 560个字符； 版本25 997个字符
//		int ver=1;
//		for (Entry<Integer, Integer> entry : CapacityMap.binaryMMap.entrySet()) {			
//		    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
//		    if(length>entry.getValue()) {//如果内容长度大于当前版本的长度，则下一个
//		    	continue;
//		    } else {
//		    	ver=entry.getKey();
//		    	break;
//		    }		  
//		}
		int ver = CapacityMap.getVersionByLength(CapacityMap.binaryMMap, length);
//		ver =18;
//		ver =12;
		
//		=ROUNDUP(((SQRT(L5*8+8*9*3+8+1)-21)/4+1),0)		
//		int length = text.length();
//		int unitSizeSquare = length*8+8*9*3+8+1;
//		double unitSizeDouble = Math.sqrt(unitSizeSquare);
//		double verDouble = (unitSizeDouble-21)/4+1;
//		ver = (int)Math.ceil(verDouble);
//		Encoder.chooseMode(content)

	
		
		int unitSize=QRCodeUtils.getVersionUnitSize(ver);
		int pxPerUnit = 3;
		int width = unitSize * pxPerUnit;//每个码元两个像素
		int height = unitSize * pxPerUnit;
		try {
			byte[] imageBytes = QRCodeUtils.encodeQRCodeToImageBytes(text, width, height, "png", null , ErrorCorrectionLevel.M, ver);
//			byte[] imageBytes = QRCodeUtils.encodeQRCodeToImageBytes(text, "png", null , ErrorCorrectionLevel.M, null);
			String filePath = "E:\\";
			String fileName = "ewm_" + pxPerUnit +"_"+ ver + ".png";
			BufferedOutputStream bos = null;  
	        FileOutputStream fos = null;  
	        File file = null;  
	        try {  
	            File dir = new File(filePath);  
	            if(!dir.exists()&&dir.isDirectory()){//判断文件目录是否存在  
	                dir.mkdirs();  
	            }  
	            file = new File(filePath+"\\"+fileName);
	            fos = new FileOutputStream(file);
	            bos = new BufferedOutputStream(fos);
	            bos.write(imageBytes);
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        } finally {  
	            if (bos != null) {  
	                try {  
	                    bos.close();  
	                } catch (IOException e1) {  
	                    e1.printStackTrace();  
	                }  
	            }  
	            if (fos != null) {  
	                try {  
	                    fos.close();  
	                } catch (IOException e1) {  
	                    e1.printStackTrace();  
	                }  
	            }  
	        }			
			
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (WriterException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		
	}
}
