package cn.meteor.module.util.lang;

import java.text.DecimalFormat;

public class StringExtUtils {
	
	/**
	 * 数字之前补零
	 * @param num 数字
	 * @param finalLength 最终字符串长度
	 * @return 补零后的字符串
	 */
	public static String numberFillZeroFront(int num, int finalLength) {
		// String result = String.format("%04d", num); // 0 代表前面补充0 // 4 代表长度为4 // d 代表参数为正数型
		String result = String.format("%" + 0 + finalLength + "d", num);
		return result;
	}

	/**
	 * 流水号按格式增长
	 * @param currentSerialNumber 当前流水号
	 * @param stringFormat 格式，如00000
	 * @param increase 增长长度，如1
	 * @return 增长后的流水号
	 */
	public static String serialNumberIncrease(String currentSerialNumber, String stringFormat, int increase) {
		Integer currentSerialNumberInteger = Integer.parseInt(currentSerialNumber);
		currentSerialNumberInteger = currentSerialNumberInteger + increase;
		DecimalFormat df = new DecimalFormat(stringFormat);
		return df.format(currentSerialNumberInteger);
	}

	public static String translateForSnakeCase(String input) {
        if (input == null) return input; // garbage in, garbage out
        int length = input.length();
        StringBuilder result = new StringBuilder(length * 2);
        int resultLength = 0;
        boolean wasPrevTranslated = false;
        for (int i = 0; i < length; i++)
        {
            char c = input.charAt(i);
            if(c!='_') {
            	if (!wasPrevTranslated && i!=0) {//如果前一个是下划线，当前则转换为大写
            		c = Character.toUpperCase(c);
                    wasPrevTranslated = true;
            	} else {
                	if (Character.isUpperCase(c)) {            		
                		c = Character.toLowerCase(c);
                        wasPrevTranslated = true;
                	}
            	}
            	result.append(c);
                resultLength++;
            } else {
            	wasPrevTranslated = false;
            }
        }
        return resultLength > 0 ? result.toString() : input;
    }
	
	public static String translateForSnakeCase1(String input) {
        if (input == null) return input; // garbage in, garbage out
        int length = input.length();
        StringBuilder result = new StringBuilder(length * 2);
        int resultLength = 0;
        boolean wasPrevTranslated = false;
        for (int i = 0; i < length; i++)
        {
            char c = input.charAt(i);
            if (i > 0 || c != '_') // skip first starting underscore
            {
                if (Character.isUpperCase(c))
                {
                    if (!wasPrevTranslated && resultLength > 0 && result.charAt(resultLength - 1) != '_')
                    {
                        result.append('_');
                        resultLength++;
                    }
                    c = Character.toLowerCase(c);
                    wasPrevTranslated = true;
                }
                else
                {
                    wasPrevTranslated = false;
                }
                result.append(c);
                resultLength++;
            }
        }
        return resultLength > 0 ? result.toString() : input;
    }
}
