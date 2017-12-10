package cn.meteor.module.util.security;

public class ZigZagUtils {
	
	public static String convert(String data, int nRows) {
		if (data == null || data.isEmpty()) {
			return data;
		}

		int length = data.length();
		if (length <= nRows || nRows == 1) {
			return data;
		}

		StringBuilder sb = new StringBuilder();

		int step = 2 * (nRows - 1);
		int count = 0;

		for (int i = 0; i < nRows; i++) {
			int interval = step - 2 * i;

			for (int j = i; j < length; j += step) {
				sb.append(data.charAt(j));
//				int y = sb.length()-1;
				count++;
				if (interval > 0 && interval < step && j + interval < length && count < length) {
					sb.append(data.charAt(j + interval));
					count++;
				}
			}
		}
		return sb.toString();
	}
	
	
	public static String unConvert(String zigString, int nRows) {
		char[] oriChars = new char[zigString.length()];		
		int[] idxArray = ZigZagUtils.getIndexArray(zigString, nRows);
		for (int i = 0; i < idxArray.length; i++) {
			int j = idxArray[i];
//			System.out.print(j + " ");
			oriChars[j] = zigString.charAt(i);
		}
		String oriString = new String(oriChars);
		return oriString;
	}
	
	private static int[] getIndexArray(String data, int nRows) {
		if (data == null || data.isEmpty()) {
			return null;
		}

		int length = data.length();
		if (length <= nRows || nRows == 1) {
//			return s.toCharArray();
			return null;
		}
		

		int[] indexArr = new int[data.length()];
		int idx = 0;

		int step = 2 * (nRows - 1);
		int count = 0;

		for (int i = 0; i < nRows; i++) {
			int interval = step - 2 * i;

			for (int j = i; j < length; j += step) {
				indexArr[idx] = j;
				idx ++;
				count++;
				if (interval > 0 && interval < step && j + interval < length && count < length) {
					indexArr[idx] = (j+interval);
					idx ++;
					count++;
				}
			}
		}
		return indexArr;
	}

}
