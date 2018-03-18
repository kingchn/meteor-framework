package cn.meteor.module.core.openApi.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.meteor.module.util.mapper.BeanMapper;
import cn.meteor.module.util.security.MD5Utils;

public class ApiSignUtils {

	private static final Logger logger = LogManager.getLogger(ApiSignUtils.class);

	/**
	 * 根据请求对象及密钥得到包含签名的map
	 * @param requestObject 请求对象
	 * @param appSecret 密钥
	 * @return 包含签名的map
	 */
	public static Map<String, Object> requestObjectToMapWithSign(Object requestObject, String appSecret) {
		Map<String, Object> map = new HashMap<String, Object>();
		BeanMapper.mapWithDozer(requestObject, map);// 1.将请求对象数据转换到map中
		map.remove("sign");// 2.移除sign
		Object[] keyArr = map.keySet().toArray(); // 3.把所有key 转成数组
		Arrays.sort(keyArr);// 4.排序数据
		String joinString = "";// 5循排序过的数据
		for (Object key : keyArr) {
			Object value = map.get(key);
			if (value != null) {
				joinString += key.toString().trim() + value.toString().trim();
			}
		}
		joinString = appSecret + joinString + appSecret;
		try {
			String sign = MD5Utils.md5Digest(joinString).toUpperCase();
			map.put("sign", sign);
		} catch (Exception e) {
			logger.error("requestObjectToMap中md5Digest出现异常：", e);
		}
		return map;
	}

	/**
	 * 根据请求对象及密钥得到签名值
	 * @param requestObject 请求对象
	 * @param appSecret 密钥
	 * @return 签名值
	 */
	public static String getSign(Object requestObject, String appSecret) {
		String sign = null;
		Map<String, Object> map = requestObjectToMapWithSign(requestObject, appSecret);
		Object signObject = map.get("sign");
		if (signObject != null) {
			sign = "" + signObject;
		}
		return sign;
	}
}
