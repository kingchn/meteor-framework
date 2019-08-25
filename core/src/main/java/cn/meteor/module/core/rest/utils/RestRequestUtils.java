package cn.meteor.module.core.rest.utils;

import java.util.Date;

import javax.validation.Validation;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.meteor.module.core.openApi.secret.AppSecretManager;
import cn.meteor.module.core.rest.exception.ErrorMsgUtils;
import cn.meteor.module.core.rest.exception.ErrorType;
import cn.meteor.module.core.rest.request.RestCommonRequest;
import cn.meteor.module.util.security.MD5Utils;
import cn.meteor.module.util.time.DateUtils;

public class RestRequestUtils {
	
	private static final Logger logger = LogManager.getLogger(RestRequestUtils.class);

	
	/**
	 * 验证请求时间戳
	 * @param restCommonRequest
	 */
	public static void validRequestTimestamp(RestCommonRequest restCommonRequest) {
		boolean isTimeStampValid = false;
		Date formTimeStampDate = null;
		try {
			Long timestampLong = Long.valueOf(restCommonRequest.getTimestamp());
			formTimeStampDate = new Date(timestampLong);
		} catch (Exception e) {
			isTimeStampValid = false;
		}
		if (formTimeStampDate == null) {
			isTimeStampValid = false;
		} else {
			long s = DateUtils.getTimeMinus(new Date(), formTimeStampDate);
			if (s > 600000 || s < -600000) {// 前后10分钟以外的为无效
				isTimeStampValid = false;
			} else {
				isTimeStampValid = true;
			}
		}
		if(isTimeStampValid == false) {//非法的时间戳参数
			ErrorMsgUtils.throwIsvException(ErrorType.INVALID_TIMESTAMP);
		}
	}
	
	/**
	 * 获取请求报文中body的原始字符串。base64则为base64字符串；json则为json字符串
	 * @throws JsonProcessingException 
	 */
	public static String getRequestJsonBodyString(RestCommonRequest restCommonRequest, ObjectMapper objectMapper) throws JsonProcessingException {
		String requestBodyString = null;
		String requestBodyFormat = restCommonRequest.getRequestBodyFormat();
		if(requestBodyFormat!=null) {
			if(!"base64".equals(requestBodyFormat) && !"json".equals(requestBodyFormat)) {//requestBodyFormat格式不在支持的格式范围内
				ErrorMsgUtils.throwIsvException(ErrorType.INVALID_PARAM_REQUEST_BODY_FORMAT);
			}
		}
		if(restCommonRequest.getBody()!=null) {
			if("base64".equals(restCommonRequest.getRequestBodyFormat())) {
				requestBodyString = "" + restCommonRequest.getBody();
			} else {//默认json对象（明文）
				requestBodyString = objectMapper.writeValueAsString(restCommonRequest.getBody());
			}
		}
		return requestBodyString;
	}
	
	/**
	 * 验证请求签名
	 * @param restCommonRequest
	 */
	public static void validRequestSign(RestCommonRequest restCommonRequest, String requestBodyString, AppSecretManager appSecretManager) {
		String appKey =  restCommonRequest.getAppKey();
		String appSecret = null;
		if(appSecretManager.isContainAppKey(appKey)) {
			appSecret = appSecretManager.getSecret(appKey);
		} else {//appKey 无效
			ErrorMsgUtils.throwIsvException(ErrorType.INVALID_APP_KEY);
		}
		String requestSignString = restCommonRequest.getSign();
		String serverSignString = RestRequestUtils.getSign(restCommonRequest, requestBodyString, appSecret);
		logger.debug("requestSign:" + requestSignString);
		logger.debug("serverSign:" + serverSignString);
		if(!requestSignString.equals(serverSignString)) {//无效签名
			ErrorMsgUtils.throwIsvException(ErrorType.INVALID_SIGNATURE);
		}
	}
	
//	/**
//	 * 验证请求对象
//	 * @param object
//	 * @throws BindException
//	 */
//	protected <T> void validCommonRequest(T object) throws BindException {
//		Validator validator = new SpringValidatorAdapter(Validation.buildDefaultValidatorFactory().getValidator());
//		BindException bindException = new BindException(object, object.getClass().getSimpleName());
//		validator.validate(object,bindException);
//		if (bindException.hasErrors()) {
//			throw bindException;
//		}		
//	}
	
	/**
	 * 校验业务请求实体
	 * @param object
	 * @throws BindException
	 */
	public static <T> void validBusinessRequest(T object) throws BindException {
		Validator validator = new SpringValidatorAdapter(Validation.buildDefaultValidatorFactory().getValidator());
		BindException bindException = new BindException(object, object.getClass().getSimpleName());
		validator.validate(object,bindException);
		if (bindException.hasErrors()) {
			throw bindException;
		}		
	}
	
	
	/**
	 * 根据请求对象及密钥得到签名值
	 * @param request 请求对象 
	 * @param requestBodyString 请求报文中body的原始字符串。base64则为base64字符串；json则为json字符串
	 * @param appSecret 密钥
	 * @return 签名值
	 */
	public static String getSign(RestCommonRequest request, String requestBodyString, String appSecret) {
		
		String joinString = "";
		joinString = request.getAppKey() + request.getServiceId() + request.getTranSeq() + request.getTimestamp();
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
