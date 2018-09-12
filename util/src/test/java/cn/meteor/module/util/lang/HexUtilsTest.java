package cn.meteor.module.util.lang;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class HexUtilsTest {
	private  static final Logger logger = LogManager.getLogger(HexUtilsTest.class);

	@Test
	public void testHexToString() throws IOException {
		
//		String hexString = "\\x23\\x66\\x70\\x68\\x6d\\x6a\\x79";
		String hexString = "\\x64\\x6f\\x63\\x75\\x6d\\x65\\x6e\\x74";
		hexString = hexString.replace("\\x", "").toUpperCase();
		String result = HexUtils.hexToString(hexString);
		System.out.println(result);
	}
	
	@Test
	public void testHexToStringForJs() throws IOException {
		
//		String hexString = "\\x23\\x66\\x70\\x68\\x6d\\x6a\\x79";
		
//		String fileName = "showModalDialog1.js";
		String fileName = "10.js";
		ClassLoader classLoader = getClass().getClassLoader();
		URL url = classLoader.getResource(fileName);
		File file = FileUtils.toFile(url);
		String content = FileUtils.readFileToString(file, "utf-8");
		content = parseUnicodeString(content);
		System.out.println("==>" + content);
		content = parseHexString(content);
		System.out.println("==>" + content);
	}
	
	/**
	 * 解析unicode码
	 * 
	 * @param text 带unicode码的字符串
	 * @return 解析后的字符串
	 */
	public static String parseHex(String hexString) {
		hexString = hexString.replace("\\x", "").toUpperCase();
		String result = HexUtils.hexToString(hexString);
		return result;
	}
	
	/**
	 * 解析包含unicode的字符串
	 * 
	 * @param text
	 * @return
	 */
	public static String parseHexString(String text) {
		String ret = text;
		Pattern p = Pattern.compile("(\\\\x.{2})");
		Matcher m = p.matcher(ret);
		while (m.find()) {
			String xxx = m.group(0);
			String plainText = parseHex(xxx);
			ret = ret.replaceAll("\\" + xxx, plainText);
		}
		return ret;
	}
	
	/**
	 * 解析unicode码
	 * 
	 * @param text 带unicode码的字符串
	 * @return 解析后的字符串
	 */
	public static String parseUnicode(String text) {
		StringBuilder sb = new StringBuilder(text.length());
		char[] chars = text.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (c == '\\' && chars[i + 1] == 'u') {
				char cc = 0;
				for (int j = 0; j < 4; j++) {
					char ch = Character.toLowerCase(chars[i + 2 + j]);
					if ('0' <= ch && ch <= '9' || 'a' <= ch && ch <= 'f') {
						cc |= (Character.digit(ch, 16) << (3 - j) * 4);
					} else {
						cc = 0;
						break;
					}
				}
				if (cc > 0) {
					i += 5;
					sb.append(cc);
					continue;
				}
			}
			sb.append(c);
		}
		return sb.toString();
	}

	/**
	 * 解析包含unicode的字符串
	 * 
	 * @param text
	 * @return
	 */
	public static String parseUnicodeString(String text) {
		String ret = text;
		Pattern p = Pattern.compile("(\\\\u.{4})");
		Matcher m = p.matcher(ret);
		while (m.find()) {
			String xxx = m.group(0);
			ret = ret.replaceAll("\\" + xxx, parseUnicode(xxx));
		}
		return ret;
	}
}
