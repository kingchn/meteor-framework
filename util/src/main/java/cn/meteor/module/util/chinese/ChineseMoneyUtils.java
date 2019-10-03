package cn.meteor.module.util.chinese;

import java.text.DecimalFormat;

/**
 * 中文金额工具类
 *
 */
public class ChineseMoneyUtils {

	/**
	 * 货币大写数组
	 */
	private static String[] CN_NUMBERS = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
	
	/**
	 * 货币单位数组
	 */
	private static String[] CN_UNITS = { "分", "角", "元", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "万" };

	/**
	 * 将数字转成中文大写金额
	 * @param numString 数字
	 * @return 中文大写金额
	 */
	public static String numToChineseMoney(String numString) {
		StringBuilder sb = new StringBuilder();

		DecimalFormat df = new DecimalFormat("#0");

		// 转成分
		double d = Double.valueOf(numString) * 100;
		// 处理负数
		if (d < 0) {
			sb.append("负");
			d = -d;
		}
		String numFormatedString = df.format(d);

		for (int i = 0; i < numFormatedString.length(); i++) {
			int numIndex = numFormatedString.charAt(i) - 48;
			sb.append(CN_NUMBERS[numIndex]);
			sb.append(CN_UNITS[numFormatedString.length() - 1 - i]);
		}

		String moneyString = sb.toString();
		moneyString = moneyString.replaceAll("(零[拾|佰|仟|角|分])+", "零").replaceAll("零+元", "元").replaceAll("零+万", "万")
				.replaceAll("零+亿", "亿").replaceAll("亿万元", "亿元").replaceAll("亿万亿", "亿亿").replaceAll("亿万", "亿零")
				.replaceAll("零$", "");

		if (moneyString.endsWith("元")) {
			moneyString += "整";
		}

		return moneyString;
	}
	
	
}
