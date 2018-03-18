package cn.meteor.module.core.openApi.validation.impl;

import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cn.meteor.module.core.openApi.request.BaseApiRequest;
import cn.meteor.module.core.openApi.secret.AppSecretManager;
import cn.meteor.module.core.openApi.utils.StringBuilderUtils;
import cn.meteor.module.core.openApi.validation.ApiPermission;
import cn.meteor.module.util.time.DateUtils;

public class ApiPermissionValidator_bak implements ConstraintValidator<ApiPermission, BaseApiRequest> {	
	
	private final Logger logger = LogManager.getLogger(getClass());

	private Class entityClass;
	
	private String charset;
	
	/**
	 * 是否验证权限
	 */
	private boolean isValidatePermission;
	
	/**
	 * 是否启用日志
	 */
	private boolean isEnableLogger;
	
	@Autowired
	private AppSecretManager appSecretManager;
//	
//	@Autowired
//	private cn.meteor.spring.openApi.logs.Logger openApiLogger;

	@Override
	public void initialize(ApiPermission constraintAnnotation) {
		
		entityClass = constraintAnnotation.value();
		this.charset = constraintAnnotation.charset();
		this.isValidatePermission = constraintAnnotation.isValidatePermission();
		this.isEnableLogger = constraintAnnotation.isEnableLogger();

	}

	@Override
	public boolean isValid(BaseApiRequest value, ConstraintValidatorContext context) {
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		
		//将所有参数解析以map格式放到请求实体的allRequestParams中
		Map<String, Object> allRequestParams = new HashMap<>();
		Map<String, String[]> paramMap = request.getParameterMap();
		for (String key : paramMap.keySet()) {
			allRequestParams.put(key, paramMap.get(key)[0]);
		} 
		value.setAllRequestParams(allRequestParams);
		
		//以下开始校验
		Date nowDate = new Date();
		String dateTime = DateUtils.parseDateToString(nowDate, "yyyyMMdd HH:mm:ss,SSS");
		String ip=request.getRemoteHost();
		String httpMethod=request.getMethod();
		String apiMethod=request.getParameter("method");
//		Map<String,String> paramMap=getParameterMap(request);
		String params="";
		Set<String> keySet=allRequestParams.keySet();
		for (String key : keySet) {
			params+=  key+"="+allRequestParams.get(key) + ", ";
		}
		if(this.isEnableLogger==true) {//TODO: 记录日志
//			if(!"meteor.openApi.logs.get".equals(apiMethod)) {//如果不是调用查看日志接口，则写入日志
//				openApiLogger.apiInfo(dateTime, ip, httpMethod, apiMethod, params);
//			}
		}		
		
		
		MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
		PropertyDescriptor[] propDescs = PropertyUtils.getPropertyDescriptors(entityClass);
		for (PropertyDescriptor propDesc : propDescs) {
			
			String propertyName = propDesc.getName();
			if("class".equals(propertyName)) {
				continue;
			}
			
			String propertyValue = "";
			try {
				Object propertyValueObject = PropertyUtils.getProperty(value, propertyName);
				if(propertyValueObject!=null) {	
					propertyValue = "" + propertyValueObject;
					if(propertyValueObject.getClass()==Double.class) {//针对Double类型为整数时会保留一位小数的情况的处理(去除.0)
						if(propertyValue.endsWith(".0")) {
							propertyValue = propertyValue.substring(0, propertyValue.length()-2);
						}
					}
				} else {
					propertyValue = null;
				}			
				
				if(propertyValue!=null&&!"allRequestParams".equals(propertyName)) {
					form.add(propertyName, propertyValue);
				}
				
				//解码
				String decodedString=null;
				if(propertyValueObject!=null&&propertyValueObject.getClass()==String.class) {
					try {
						decodedString = URLDecoder.decode(propertyValue, charset);
						PropertyUtils.setProperty(value, propertyName, decodedString);
					} catch (UnsupportedEncodingException e) {
						logger.error(propertyName+"-->URLDecode出错"+e.getMessage());
					}
				}
				
				logger.debug(propertyName + " -> " + propertyValue + " == " + (decodedString==null?"":decodedString) );
				
				
			} catch (IllegalAccessException e) {
				logger.error(propertyName+"-->PropertyUtils处理出错："+e.getMessage());
			} catch (InvocationTargetException e) {
				logger.error(propertyName+"-->PropertyUtils处理出错："+e.getMessage());
			} catch (NoSuchMethodException e) {
				logger.error(propertyName+"-->PropertyUtils处理出错："+e.getMessage());
			}
			
			
		}
		
		//如果不验证权限，则在此直接返回true
		if(this.isValidatePermission==false) {
			return true;
		}
		
		
		String formFormatString = form.getFirst("format");
		if(formFormatString!=null&&!"".equals(formFormatString)) {
			if(!"json".equals(formFormatString)&&!"xml".equals(formFormatString)) {//无效format
				String messageTemplate = "ApiPermission.invalid.format";
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate(messageTemplate)
				.addPropertyNode("format")
				.addConstraintViolation();
				return false;
			}			
		}
		
		String formTimeStampString = form.getFirst("timestamp");
		if(formTimeStampString==null||"".equals(formTimeStampString)) {//传值timestamp为空
			String messageTemplate = "ApiPermission.missing.timestamp";
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(messageTemplate)
			.addPropertyNode("timestamp")
			.addConstraintViolation();
			return false;
		} else {
			boolean isTimeStampValid=false;
			String format="yyyy-MM-dd HH:mm:ss";
			Date formTimeStampDate=null;			
			try {
				formTimeStampDate = DateUtils.parseStringToDate(formTimeStampString, format);
			} catch (ParseException e) {
				isTimeStampValid=false;
			}
			if(formTimeStampDate==null) {
				isTimeStampValid=false;
			} else {
				long s=DateUtils.getTimeMinus(new Date(), formTimeStampDate);
				if(s>600000||s<-600000) {
					isTimeStampValid=false;
				} else {
					isTimeStampValid=true;
				}
			}
			if(isTimeStampValid==false) {
				String messageTemplate = "ApiPermission.invalid.timestamp";
				context.disableDefaultConstraintViolation();
//				context.buildConstraintViolationWithTemplate(messageTemplate)	.addNode("timestamp").addConstraintViolation();
				context.buildConstraintViolationWithTemplate(messageTemplate).addPropertyNode("timestamp").addConstraintViolation();
				return false;
			}			
		}
		
		
		String formAppKey = form.getFirst("appKey");
		String secret="";
		if(formAppKey==null||"".equals(formAppKey)) {
			String messageTemplate = "ApiPermission.missing.appKey";
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(messageTemplate)
			.addPropertyNode("appKey")
			.addConstraintViolation();
			return false;
		} else {
			if(appSecretManager.isContainAppKey(formAppKey)) {
				secret = appSecretManager.getSecret(formAppKey);
			} else {//appKey 无效
				String messageTemplate = "ApiPermission.invalid.appKey";
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate(messageTemplate)
				.addPropertyNode("appKey")
				.addConstraintViolation();
				return false;
			}
			
		}
		
		
		String formSignMethod = form.getFirst("signMethod");
		if(formSignMethod==null||"".equals(formSignMethod)) {
			String messageTemplate = "ApiPermission.missing.signMethod";
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(messageTemplate)
			.addPropertyNode("signMethod")
			.addConstraintViolation();
			return false;
		} else {
			if(!"md5".equals(formSignMethod)&&!"hmac".equals(formSignMethod)) {
				String messageTemplate = "ApiPermission.invalid.signMethod";
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate(messageTemplate)
				.addPropertyNode("signMethod")
				.addConstraintViolation();
				return false;
			}
		}
		
		
		String formSignString = form.getFirst("sign");
		form.remove("sign");//清除表单中的sign以便后面对比签名
		if(formSignString==null||"".equals(formSignString)) {//传值签名为空
			String messageTemplate = "ApiPermission.missing.sign";
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(messageTemplate)
			.addPropertyNode("sign")
			.addConstraintViolation();
			return false;
		} else {
			String signString = StringBuilderUtils.sign(form, secret, formSignMethod);
			logger.debug("sign:" + formSignString);
			logger.debug("sign:" + signString);
			if(!formSignString.equals(signString)) {
				String messageTemplate = "ApiPermission.invalid.sign";
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate(messageTemplate)
				.addPropertyNode("sign")
				.addConstraintViolation();
				return false;
			}
		}
		
		
		
		
		return true;
	}
	
	   /**
	 * 从request中获得参数Map，并返回可读的Map
	 * 
	 * @param request
	 * @return
	 */
	public TreeMap<String, String> getParameterMap(HttpServletRequest request) {
		// 参数Map
		Map properties = request.getParameterMap();
		// 返回值Map
		TreeMap<String, String> returnMap = new TreeMap<String, String>();
		Iterator entries = properties.entrySet().iterator();
		Map.Entry entry;
		String name = "";
		String value = "";
		while (entries.hasNext()) {
			entry = (Map.Entry) entries.next();
			name = (String) entry.getKey();
			Object valueObj = entry.getValue();
			if(null == valueObj){
				value = "";
			}else if(valueObj instanceof String[]){
				String[] values = (String[])valueObj;
				for(int i=0;i<values.length;i++){
					value = values[i] + ",";
				}
				value = value.substring(0, value.length()-1);
			}else{
				value = valueObj.toString();
			}
			returnMap.put(name, value);
		}
		return returnMap;
	}

}
