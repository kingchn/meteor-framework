package cn.meteor.module.core.rest.exception;

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;

public class ErrorMsgUtils implements MessageSourceAware {
	
	private static final Logger logger = LogManager.getLogger(ErrorMsgUtils.class);
	
	// 错误信息的国际化信息
	protected static MessageSourceAccessor messageSourceAccessor;
	
	@Override
	public void setMessageSource(MessageSource messageSource) {
		messageSourceAccessor = new MessageSourceAccessor(messageSource);
	}	
	
	/**
	 * 枚举及code映射关系
	 * key 枚举类型；value 消息code
	 */
	protected static final EnumMap<ErrorType, String> errorCodeMap = new EnumMap<ErrorType, String>(ErrorType.class);
	
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
	
	protected void putErrorCodeMap(ErrorType errorType, Locale locale) {
		String code = messageSourceAccessor.getMessage(errorType.toString(), locale);
		errorCodeMap.put(errorType, code);
	}
	
	public void init() {		
		try {
			//初始化errorCodeMap  map数据结构： key 枚举类型；value 消息code  ；配置文件code设定不要有中文，一般是数字
			//遍历所有错误类型，将枚举及code映射关系存入errorCodeMap，这个映射跟地域无关，仅需要一个一般版本的配置文件，这里使用中文版本，即msg_zh_CN.properties
			for (ErrorType errorType : ErrorType.values()) {
				putErrorCodeMap(errorType, Locale.SIMPLIFIED_CHINESE);
			}
		} catch (Exception e) {
			logger.error("调用ErrorMsgUtils的init出错:", e);
		}
	}
	
	
	
	
	/**
	 * 校验注解(messageTemplate)与枚举映射关系
	 */
	public static final Map<String, ErrorType> JSR_MSG_CONSTRAINT_ERROR_TYPE_MAPPINGS = new LinkedHashMap<String, ErrorType>();
	
	 static {
		 
		JSR_MSG_CONSTRAINT_ERROR_TYPE_MAPPINGS.put("NotNull", ErrorType.ISV_MISSING_PARAMETER);
		JSR_MSG_CONSTRAINT_ERROR_TYPE_MAPPINGS.put("NotEmpty", ErrorType.ISV_PARAMETER_BLANK);
		JSR_MSG_CONSTRAINT_ERROR_TYPE_MAPPINGS.put("NotBlank", ErrorType.ISV_PARAMETER_BLANK);
		 
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
		 
//		JSR_MSG_CONSTRAINT_ERROR_TYPE_MAPPINGS.put("typeMismatch", ErrorType.ISV_PARAMETERS_MISMATCH);
		
		
//		JSR_MSG_CONSTRAINT_ERROR_TYPE_MAPPINGS.put("ApiPermission.invalid.format", ErrorType.INVALID_FORMAT);
//		
//		JSR_MSG_CONSTRAINT_ERROR_TYPE_MAPPINGS.put("ApiPermission.missing.timestamp", ErrorType.MISSING_TIMESTAMP);
//		JSR_MSG_CONSTRAINT_ERROR_TYPE_MAPPINGS.put("ApiPermission.invalid.timestamp", ErrorType.INVALID_TIMESTAMP);		
//		
//		JSR_MSG_CONSTRAINT_ERROR_TYPE_MAPPINGS.put("ApiPermission.missing.appKey", ErrorType.MISSING_APP_KEY);
//		JSR_MSG_CONSTRAINT_ERROR_TYPE_MAPPINGS.put("ApiPermission.invalid.appKey", ErrorType.INVALID_APP_KEY);
//		
//		JSR_MSG_CONSTRAINT_ERROR_TYPE_MAPPINGS.put("ApiPermission.missing.signMethod", ErrorType.MISSING_SIGN_METHOD);
//		JSR_MSG_CONSTRAINT_ERROR_TYPE_MAPPINGS.put("ApiPermission.invalid.signMethod", ErrorType.INVALID_SIGN_METHOD);
//		
//		JSR_MSG_CONSTRAINT_ERROR_TYPE_MAPPINGS.put("ApiPermission.missing.sign", ErrorType.MISSING_SIGNATURE);
//		JSR_MSG_CONSTRAINT_ERROR_TYPE_MAPPINGS.put("ApiPermission.invalid.sign", ErrorType.INVALID_SIGNATURE);
        
	    }


	 /**
		 * 将errorType转化为抛出BusinessException
		 */
		public static void throwIsvException(ErrorType errorType) throws IsvException {
			throwIsvException(errorType, Locale.SIMPLIFIED_CHINESE);//TODO: 从request中取
		}
		
		/**
		 * 将errorType转化为抛出BusinessException（带args）
		 */
		public static void throwIsvException(ErrorType errorType, Object[] args) throws IsvException {
			throwIsvException(errorType, args, Locale.SIMPLIFIED_CHINESE);//TODO: 从request中取
		}
		
		/**
		 * 将errorType转化为抛出BusinessException（带Locale）
		 */
		public static void throwIsvException(ErrorType errorType, Locale locale) throws IsvException {
			throw new IsvException(ErrorMsgUtils.getErrorCode(errorType), ErrorMsgUtils.getErrorMsg(errorType, locale));
		}
		
		/**
		 * 将errorType转化为抛出BusinessException（带Locale，带args）
		 */
		public static void throwIsvException(ErrorType errorType, Object[] args, Locale locale) throws IsvException {
			throw new IsvException(ErrorMsgUtils.getErrorCode(errorType), ErrorMsgUtils.getErrorMsg(errorType, args, locale));
		}
	
}
