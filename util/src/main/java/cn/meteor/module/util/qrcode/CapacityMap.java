package cn.meteor.module.util.qrcode;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class CapacityMap {

	public final static Map<Integer, Integer> binaryMMap = new HashMap<Integer, Integer>();
	
	static {
		binaryMMap.put(1, 14);
		binaryMMap.put(2, 26);
		binaryMMap.put(3, 42);
		binaryMMap.put(4, 62);
		binaryMMap.put(5, 84);
		binaryMMap.put(6, 106);
		binaryMMap.put(7, 122);
		binaryMMap.put(8, 152);
		binaryMMap.put(9, 180);
		binaryMMap.put(10, 213);
		binaryMMap.put(11, 251);
		binaryMMap.put(12, 287);
		binaryMMap.put(13, 331);
		binaryMMap.put(14, 362);
		binaryMMap.put(15, 412);
		binaryMMap.put(16, 450);
		binaryMMap.put(17, 504);
		binaryMMap.put(18, 560);
		binaryMMap.put(19, 624);
		binaryMMap.put(20, 666);
		binaryMMap.put(21, 711);
		binaryMMap.put(22, 779);
		binaryMMap.put(23, 857);
		binaryMMap.put(24, 911);
		binaryMMap.put(25, 997);
		binaryMMap.put(26, 1059);
		binaryMMap.put(27, 1125);
		binaryMMap.put(28, 1190);
		binaryMMap.put(29, 1264);
		binaryMMap.put(30, 1370);
		binaryMMap.put(31, 1452);
		binaryMMap.put(32, 1538);
		binaryMMap.put(33, 1628);
		binaryMMap.put(34, 1722);
		binaryMMap.put(35, 1809);
		binaryMMap.put(36, 1911);
		binaryMMap.put(37, 1989);
		binaryMMap.put(38, 2099);
		binaryMMap.put(39, 2213);
		binaryMMap.put(40, 2331);

	}
	
	public static Integer getVersionByLength(Map<Integer, Integer> map , int length) {
		int version =1;
//		for (Entry<Integer, Integer> entry : CapacityMap.binaryMMap.entrySet()) {
		for (Entry<Integer, Integer> entry : map.entrySet()) {
		    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
		    if(length>entry.getValue()) {//如果内容长度大于当前版本的长度，则下一个
		    	continue;
		    } else {
		    	version=entry.getKey();
		    	break;
		    }		  
		}
		return version;
	}
}
