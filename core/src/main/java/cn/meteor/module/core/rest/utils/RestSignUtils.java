package cn.meteor.module.core.rest.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.meteor.module.core.rest.request.RestCommonRequest;
import cn.meteor.module.util.security.MD5Utils;

public class RestSignUtils {

	private static final Logger logger = LogManager.getLogger(RestSignUtils.class);

	/**
	 * 根据请求对象及密钥得到签名值
	 * @param request 请求对象 
	 * @param requestBodyString 请求报文中body的原始字符串。base64则为base64字符串；json则为json字符串
	 * @param appSecret 密钥
	 * @return 签名值
	 */
	public static String getSign(RestCommonRequest request, String requestBodyString, String appSecret) {
		
		String joinString = "";
		joinString = request.getServiceId() + request.getTranSeq() + request.getTimestamp();
		if (StringUtils.isNotBlank(requestBodyString)) {
			joinString += requestBodyString;
		}
		joinString = appSecret + joinString + appSecret;

		String sign = null;
		try {
			sign = MD5Utils.md5Digest(joinString).toLowerCase();//转小写
		} catch (Exception e) {
			logger.error("请求报文getSign中md5Digest出现异常：", e);
		}
		return sign;
	}
}
