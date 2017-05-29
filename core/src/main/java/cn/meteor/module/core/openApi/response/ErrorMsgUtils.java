package cn.meteor.module.core.openApi.response;

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;

public class ErrorMsgUtils implements MessageSourceAware {

	
	// 错误信息的国际化信息
	protected static MessageSourceAccessor messageSourceAccessor;

//	public static void setMessageSourceAccessor(MessageSourceAccessor messageSourceAccessor) {
//		ErrorMsgUtils.messageSourceAccessor = messageSourceAccessor;
//	}
	
	@Override
	public void setMessageSource(MessageSource messageSource) {
		messageSourceAccessor = new MessageSourceAccessor(messageSource);		
	}
	
	
	
	protected static final EnumMap<ErrorType, String> errorCodeMap = new EnumMap<ErrorType, String>(ErrorType.class);
	
	
//	public static EnumMap<ErrorType, String> getErrorCodeMap() {
////		return (EnumMap<ErrorType, String>) Collections.unmodifiableMap(errorCodeMap);
//		return errorCodeMap;
//	}

	static {
		
//		/********************************错误信息****************************************/				
//		errorCodeMap.put(ErrorType.UPLOAD_FAIL, "3");
//		errorCodeMap.put(ErrorType.APP_CALL_LIMITED, "7");
//		errorCodeMap.put(ErrorType.HTTP_ACTION_NOT_ALLOWED, "9");
//		errorCodeMap.put(ErrorType.SERVICE_CURRENTLY_UNAVAILABLE, "10");
//		errorCodeMap.put(ErrorType.INSUFFICIENT_ISV_PERMISSIONS, "11");
//		errorCodeMap.put(ErrorType.INSUFFICIENT_USER_PERMISSIONS, "12");
//		errorCodeMap.put(ErrorType.INSUFFICIENT_PARTNER_PERMISSIONS, "13");
//		errorCodeMap.put(ErrorType.MISSING_METHOD, "21");
//		errorCodeMap.put(ErrorType.INVALID_METHOD, "22");
//		errorCodeMap.put(ErrorType.INVALID_FORMAT, "23");
//		errorCodeMap.put(ErrorType.MISSING_SIGNATURE, "24");
//		errorCodeMap.put(ErrorType.INVALID_SIGNATURE, "25");
//		errorCodeMap.put(ErrorType.MISSING_SESSION, "26");
//		errorCodeMap.put(ErrorType.INVALID_SESSION, "27");
//		errorCodeMap.put(ErrorType.MISSING_APP_KEY, "28");
//		errorCodeMap.put(ErrorType.INVALID_APP_KEY, "29");
//		errorCodeMap.put(ErrorType.MISSING_TIMESTAMP, "30");
//		errorCodeMap.put(ErrorType.INVALID_TIMESTAMP, "31");
//		errorCodeMap.put(ErrorType.MISSING_VERSION, "32");
//		errorCodeMap.put(ErrorType.INVALID_VERSION, "33");
//		errorCodeMap.put(ErrorType.UNSUPPORTED_VERSION, "34");
//		errorCodeMap.put(ErrorType.INSUFFICIENT_SESSION_PERMISSIONS, "42");
//		errorCodeMap.put(ErrorType.PARAMETER_ERROR, "43");
//		errorCodeMap.put(ErrorType.INVALID_ENCODING, "47");
//		
//		errorCodeMap.put(ErrorType.MISSING_SIGN_METHOD, "51");
//		errorCodeMap.put(ErrorType.INVALID_SIGN_METHOD, "52");
//		
//		
//		errorCodeMap.put(ErrorType.INVALID_BUSINESS_PARAMETER, "101");
//		
//		errorCodeMap.put(ErrorType.BUSSINESS_ERROR, "1000");//业务级错误(父级)
        
        
        
//        /********************************详细错误信息*************************************/
//		errorCodeMap.put(ErrorType.ISP_SERVICE_UNAVAILABLE, "isp.***-service-unavailable");
//		errorCodeMap.put(ErrorType.ISP_REMOTE_SERVICE_ERROR, "isp.remote-service-error");
//		errorCodeMap.put(ErrorType.ISP_REMOTE_SERVICE_TIMEOUT, "isp.remote-service-timeout");
//		errorCodeMap.put(ErrorType.ISP_REMOTE_CONNECTION_ERROR, "isp.remote-connection-error");
//		errorCodeMap.put(ErrorType.ISP_NULL_POINTER_EXCEPTION, "isp.null-pointer-exception");
//		errorCodeMap.put(ErrorType.ISP_MOP_PARSE_ERROR, "isp.mop-parse-error");
//		errorCodeMap.put(ErrorType.ISP_MOP_REMOTE_CONNECTION_TIMEOUT, "isp.mop-remote-connection-timeout");
//		errorCodeMap.put(ErrorType.ISP_MOP_REMOTE_CONNECTION_ERROR, "isp.mop-remote-connection-error");
//		errorCodeMap.put(ErrorType.ISP_MOP_MAPPING_PARSE_ERROR, "isp.mop-mapping-parse-error");
//		errorCodeMap.put(ErrorType.ISP_UNKNOWN_ERROR, "isp.unknown-error");
//		
//		errorCodeMap.put(ErrorType.ISP_HIBERNATE_ERROR, "isp.hibernate-error");
//		errorCodeMap.put(ErrorType.ISP_SEARCH_ENGINE, "isp.search-engine-error");
//		
//		
//		
//		errorCodeMap.put(ErrorType.ISV_NOT_EXIST, "isv.{1}-not-exist:{0}");
//		errorCodeMap.put(ErrorType.ISV_MISSING_PARAMETER, "isv.missing-parameter:{0}");
//		errorCodeMap.put(ErrorType.ISV_INVALID_PARAMETER, "isv.invalid-parameter:{0}");
//		errorCodeMap.put(ErrorType.ISV_INVALID_PERMISSION, "isv.invalid-permission");
//		errorCodeMap.put(ErrorType.ISV_PARAMETERS_MISMATCH, "isv.parameters-mismatch:***-and-###");
//		errorCodeMap.put(ErrorType.ISV_SERVICE_ERROR, "isv.***-service-error:###");
		
	}
	
//	private static String getErrorMsg(String code, Locale locale) {
//		return messageSourceAccessor.getMessage(code, locale);
//	}
	
	public static String getErrorCode(ErrorType errorType) {
		return errorCodeMap.get(errorType);
	}
	
	public static String getErrorMsg(ErrorType errorType, Locale locale) {
		return messageSourceAccessor.getMessage(ErrorMsgUtils.getErrorCode(errorType), locale);
	}
	
	public static String getErrorMsg(ErrorType errorType, Object[] args, Locale locale) {
		return messageSourceAccessor.getMessage(ErrorMsgUtils.getErrorCode(errorType), args, locale);
	}
	
	protected void putErrorCodeMap(ErrorType errorType) {
		String code = messageSourceAccessor.getMessage(errorType.toString());
		errorCodeMap.put(errorType, code);
	}
	
	public void init() {
//		String isvMissingParameterCode = messageSourceAccessor.getMessage(ErrorType.ISV_MISSING_PARAMETER.toString());
//		errorCodeMap.put(ErrorType.ISV_MISSING_PARAMETER, isvMissingParameterCode);
		
		//遍历所有错误类型，存到errorCodeMap中
		for (ErrorType errorType : ErrorType.values()) {
			putErrorCodeMap(errorType);
		}
		
//		/********************************错误信息****************************************/
//		putErrorCodeMap(ErrorType.UPLOAD_FAIL);
//		putErrorCodeMap(ErrorType.APP_CALL_LIMITED);
//		putErrorCodeMap(ErrorType.HTTP_ACTION_NOT_ALLOWED);
//		putErrorCodeMap(ErrorType.SERVICE_CURRENTLY_UNAVAILABLE);
//		putErrorCodeMap(ErrorType.INSUFFICIENT_ISV_PERMISSIONS);
//		putErrorCodeMap(ErrorType.INSUFFICIENT_USER_PERMISSIONS);
//		putErrorCodeMap(ErrorType.INSUFFICIENT_PARTNER_PERMISSIONS);
//		putErrorCodeMap(ErrorType.MISSING_METHOD);
//		putErrorCodeMap(ErrorType.INVALID_METHOD);
//		putErrorCodeMap(ErrorType.INVALID_FORMAT);
//		putErrorCodeMap(ErrorType.MISSING_SIGNATURE);
//		putErrorCodeMap(ErrorType.INVALID_SIGNATURE);
//		putErrorCodeMap(ErrorType.MISSING_SESSION);
//		putErrorCodeMap(ErrorType.INVALID_SESSION);
//		putErrorCodeMap(ErrorType.MISSING_APP_KEY);
//		putErrorCodeMap(ErrorType.INVALID_APP_KEY);
//		putErrorCodeMap(ErrorType.MISSING_TIMESTAMP);
//		putErrorCodeMap(ErrorType.INVALID_TIMESTAMP);
//		putErrorCodeMap(ErrorType.MISSING_VERSION);
//		putErrorCodeMap(ErrorType.INVALID_VERSION);
//		putErrorCodeMap(ErrorType.UNSUPPORTED_VERSION);
//		putErrorCodeMap(ErrorType.INSUFFICIENT_SESSION_PERMISSIONS);
//		putErrorCodeMap(ErrorType.PARAMETER_ERROR);
//		putErrorCodeMap(ErrorType.INVALID_ENCODING);
//
//		putErrorCodeMap(ErrorType.MISSING_SIGN_METHOD);
//		putErrorCodeMap(ErrorType.INVALID_SIGN_METHOD);
//
//		putErrorCodeMap(ErrorType.INVALID_BUSINESS_PARAMETER);
//
//		putErrorCodeMap(ErrorType.BUSSINESS_ERROR);//业务级错误(父级)
//		
//        /********************************详细错误信息*************************************/
//		putErrorCodeMap(ErrorType.ISP_SERVICE_UNAVAILABLE);
//		putErrorCodeMap(ErrorType.ISP_REMOTE_SERVICE_ERROR);
//		putErrorCodeMap(ErrorType.ISP_REMOTE_SERVICE_TIMEOUT);
//		putErrorCodeMap(ErrorType.ISP_REMOTE_CONNECTION_ERROR);
//		putErrorCodeMap(ErrorType.ISP_NULL_POINTER_EXCEPTION);
//		putErrorCodeMap(ErrorType.ISP_MOP_PARSE_ERROR);
//		putErrorCodeMap(ErrorType.ISP_MOP_REMOTE_CONNECTION_TIMEOUT);
//		putErrorCodeMap(ErrorType.ISP_MOP_REMOTE_CONNECTION_ERROR);
//		putErrorCodeMap(ErrorType.ISP_MOP_MAPPING_PARSE_ERROR);
//		putErrorCodeMap(ErrorType.ISP_UNKNOWN_ERROR);
//
//		putErrorCodeMap(ErrorType.ISP_HIBERNATE_ERROR);
//		putErrorCodeMap(ErrorType.ISP_SEARCH_ENGINE);
//
//		putErrorCodeMap(ErrorType.ISV_NOT_EXIST);
//		putErrorCodeMap(ErrorType.ISV_MISSING_PARAMETER);
//		putErrorCodeMap(ErrorType.ISV_INVALID_PARAMETER);
//		putErrorCodeMap(ErrorType.ISV_INVALID_PERMISSION);
//		putErrorCodeMap(ErrorType.ISV_PARAMETERS_MISMATCH);
//		putErrorCodeMap(ErrorType.ISV_SERVICE_ERROR);
		
	}
	
	
	
	
	public static final Map<String, ErrorType> JSR_MSG_CONSTRAINT_ERROR_TYPE_MAPPINGS = new LinkedHashMap<String, ErrorType>();
	
	 static {
		 		 
		JSR_MSG_CONSTRAINT_ERROR_TYPE_MAPPINGS.put("NotNull", ErrorType.ISV_MISSING_PARAMETER);
		JSR_MSG_CONSTRAINT_ERROR_TYPE_MAPPINGS.put("NotEmpty", ErrorType.ISV_MISSING_PARAMETER);
		JSR_MSG_CONSTRAINT_ERROR_TYPE_MAPPINGS.put("NotBlank", ErrorType.ISV_MISSING_PARAMETER);
		 
		JSR_MSG_CONSTRAINT_ERROR_TYPE_MAPPINGS.put("Size", ErrorType.ISV_INVALID_PARAMETER);
		JSR_MSG_CONSTRAINT_ERROR_TYPE_MAPPINGS.put("Range", ErrorType.ISV_INVALID_PARAMETER);
		JSR_MSG_CONSTRAINT_ERROR_TYPE_MAPPINGS.put("Pattern", ErrorType.ISV_INVALID_PARAMETER);
		JSR_MSG_CONSTRAINT_ERROR_TYPE_MAPPINGS.put("Min", ErrorType.ISV_INVALID_PARAMETER);
		JSR_MSG_CONSTRAINT_ERROR_TYPE_MAPPINGS.put("Max", ErrorType.ISV_INVALID_PARAMETER);
		JSR_MSG_CONSTRAINT_ERROR_TYPE_MAPPINGS.put("DecimalMin", ErrorType.ISV_INVALID_PARAMETER);
		JSR_MSG_CONSTRAINT_ERROR_TYPE_MAPPINGS.put("DecimalMax", ErrorType.ISV_INVALID_PARAMETER);
		JSR_MSG_CONSTRAINT_ERROR_TYPE_MAPPINGS.put("Digits", ErrorType.ISV_INVALID_PARAMETER);
		JSR_MSG_CONSTRAINT_ERROR_TYPE_MAPPINGS.put("Past", ErrorType.ISV_INVALID_PARAMETER);
		JSR_MSG_CONSTRAINT_ERROR_TYPE_MAPPINGS.put("Future", ErrorType.ISV_INVALID_PARAMETER);
		JSR_MSG_CONSTRAINT_ERROR_TYPE_MAPPINGS.put("AssertFalse", ErrorType.ISV_INVALID_PARAMETER);
		
		JSR_MSG_CONSTRAINT_ERROR_TYPE_MAPPINGS.put("Email", ErrorType.ISV_INVALID_PARAMETER);
		 
		JSR_MSG_CONSTRAINT_ERROR_TYPE_MAPPINGS.put("typeMismatch", ErrorType.ISV_PARAMETERS_MISMATCH);
		
		
		
		JSR_MSG_CONSTRAINT_ERROR_TYPE_MAPPINGS.put("ApiPermission.invalid.format", ErrorType.INVALID_FORMAT);
		
		JSR_MSG_CONSTRAINT_ERROR_TYPE_MAPPINGS.put("ApiPermission.missing.timestamp", ErrorType.MISSING_TIMESTAMP);
		JSR_MSG_CONSTRAINT_ERROR_TYPE_MAPPINGS.put("ApiPermission.invalid.timestamp", ErrorType.INVALID_TIMESTAMP);
		
		
		JSR_MSG_CONSTRAINT_ERROR_TYPE_MAPPINGS.put("ApiPermission.missing.appKey", ErrorType.MISSING_APP_KEY);
		JSR_MSG_CONSTRAINT_ERROR_TYPE_MAPPINGS.put("ApiPermission.invalid.appKey", ErrorType.INVALID_APP_KEY);
		
		JSR_MSG_CONSTRAINT_ERROR_TYPE_MAPPINGS.put("ApiPermission.missing.signMethod", ErrorType.MISSING_SIGN_METHOD);
		JSR_MSG_CONSTRAINT_ERROR_TYPE_MAPPINGS.put("ApiPermission.invalid.signMethod", ErrorType.INVALID_SIGN_METHOD);
		
		JSR_MSG_CONSTRAINT_ERROR_TYPE_MAPPINGS.put("ApiPermission.missing.sign", ErrorType.MISSING_SIGNATURE);
		JSR_MSG_CONSTRAINT_ERROR_TYPE_MAPPINGS.put("ApiPermission.invalid.sign", ErrorType.INVALID_SIGNATURE);
		
		
        
        
	    }


	
}
