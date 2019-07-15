package cn.meteor.module.core.rest.controller;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.Validation;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.AnnotatedTypeScanner;
import org.springframework.validation.BindException;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.meteor.module.core.rest.annotation.RestClass;
import cn.meteor.module.core.rest.annotation.RestMethod;
import cn.meteor.module.core.rest.exception.ErrorMsgUtils;
import cn.meteor.module.core.rest.exception.ErrorType;
import cn.meteor.module.core.rest.request.RestCommonRequest;
import cn.meteor.module.core.rest.response.RestCommonResponse;
import cn.meteor.module.util.mapper.BeanMapper;

@RequestMapping("${core.rest.rootPath}")
@RestController
public class RestApiController implements BeanFactoryAware, InitializingBean {
	
	private static final Logger logger = LogManager.getLogger(RestApiController.class);

	private BeanFactory beanFactory;
	
	
	@Value("${core.rest.basePackages}")
	private String basePackages;

	/**
	 * 类map，key RestClass注解的值；value 扫描得到的Class
	 */
	private Map<String, Class<?>> clazzMap = new HashMap<>();
	
	/**
	 * 方法map，key RestClass注解的值 + "." + RestMethod注解的值；value 扫描得到的Method
	 */
	private Map<String, Method> methodMap = new HashMap<>();
	
	/**
	 * 方法map，key RestClass注解的值 + "." + RestMethod注解的值；value RestMethod注解
	 */
	private Map<String, RestMethod> restMethodAnnotationMap = new HashMap<>();
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
		if(StringUtils.isEmpty(basePackages)){
			logger.error("core.rest.basePackages不能为空，服务终止！");
			System.exit(-1);
		}
		
		AnnotatedTypeScanner s = new AnnotatedTypeScanner(RestClass.class);
		Set<Class<?>> clzSet = s.findTypes(basePackages);
		for(Class<?> clz : clzSet) {			
			RestClass restClass = clz.getAnnotation(RestClass.class);
			if(restClass != null){
				String restClassValue = restClass.value();
				clazzMap.put(restClassValue, clz);

				Method[] methods = clz.getMethods();
				for (Method m : methods) {
					RestMethod restMethod = m.getAnnotation(RestMethod.class);
					if(restMethod != null) {
						String restMethodValue = null;
						if(StringUtils.isBlank(restMethod.value())) {//如果RestMethod注解值为空，则使用方法名
							restMethodValue = restClassValue + "." + m.getName();
						} else {//否则使用RestMethod注解值
							restMethodValue = restClassValue + "." + restMethod.value();
						}
						methodMap.put(restMethodValue, m);
						restMethodAnnotationMap.put(restMethodValue, restMethod);
					}
				}
			}			
			
		}
		
	}
	
	/**
	 * 验证请求对象
	 * @param object
	 * @throws BindException
	 */
	protected <T> void validCommonRequest(T object) throws BindException {
		Validator validator = new SpringValidatorAdapter(Validation.buildDefaultValidatorFactory().getValidator());
		BindException bindException = new BindException(object, object.getClass().getSimpleName());
		validator.validate(object,bindException);
		if (bindException.hasErrors()) {
			throw bindException;
		}		
	}

	@RequestMapping(value="")
	@ResponseBody
	public RestCommonResponse doService(@Valid @RequestBody RestCommonRequest restCommonRequest) throws Exception {
		RestCommonResponse restCommonResponse = new RestCommonResponse();		
		
		String methodString=restCommonRequest.getServiceId();
		String[] strs = methodString.split("[.]");//第一点之前是类 ；后面的都是方法名
		String className = strs[0];
		String methodName = methodString.substring(methodString.indexOf(".")+1);
		
		String classKey = className;
		String methodKey= className + "." + methodName;
		
		Class<?> clazz = clazzMap.get(classKey);
		
		if(clazz==null) {//找不到这个类，说明serviceId不正确
			ErrorMsgUtils.throwIsvException(ErrorType.INVALID_PARAM_SERVICE_ID);
		}
		
		Object service = beanFactory.getBean(clazz);
		Method m = methodMap.get(methodKey);
		
		if(m==null) {//找不到这个方法，说明serviceId不正确
			ErrorMsgUtils.throwIsvException(ErrorType.INVALID_PARAM_SERVICE_ID);
		}
		RestMethod restMethod = restMethodAnnotationMap.get(methodKey);
		
		Class<?>[] cls = m.getParameterTypes();
		Object result = null;
	
		if (cls == null || cls.length == 0) {
			result = m.invoke(service);
		} else {			
			Class<?> paramClazz = m.getParameterTypes()[0];
			Object param = null;
			
			String requestBodyFormat = restCommonRequest.getRequestBodyFormat();
			if(requestBodyFormat!=null) {
				if(!"base64".equals(requestBodyFormat) && !"json".equals(requestBodyFormat)) {//requestBodyFormat格式不在支持的格式范围内
					ErrorMsgUtils.throwIsvException(ErrorType.INVALID_PARAM_REQUEST_BODY_FORMAT);
				}
			}
			
			//根据请求参数body的格式类型，通过请求body的数据转化来设置param的值
			if(restCommonRequest.getBody()!=null) {//_reqBodyFormat 为base64
				if("base64".equals(restCommonRequest.getRequestBodyFormat())) {
					String bodyJsonBase64 = "" + restCommonRequest.getBody();
					if(StringUtils.isNotBlank(bodyJsonBase64)) {
						String bodyJson = new String(Base64.decodeBase64(bodyJsonBase64));
						if (paramClazz.isAssignableFrom(List.class)) {
//							Class<? extends Type> clz = m.getGenericParameterTypes()[0].getClass();
//							param = JSON.parseArray(restCommonRequestBodyContent, clz);
							//TODO:
						} else {
							ObjectMapper objectMapper = new ObjectMapper();
							objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);//忽略未知字段
							objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);//只显示非空字段
							
							Class<?>[] parameterTypes =m.getParameterTypes();
							param = objectMapper.readValue(bodyJson, parameterTypes[0]);
						}
					}
				} else {//默认json对象（明文）
					if( !(restCommonRequest.getBody() instanceof LinkedHashMap) ) {//如果不是LinkedHashMap，即body节点的报文不是json对象结构数据的情况
						ErrorMsgUtils.throwIsvException(ErrorType.INVALID_REQUEST_BODY_DATA_FORMAT, new Object[] { "body不是json对象结构数据" });
					}
					Class<?>[] parameterTypes =m.getParameterTypes();
					
					String dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS";
					if(StringUtils.isNotBlank(restCommonRequest.getDf())) {
						dateFormat = restCommonRequest.getDf();
						String configurationXmlString = BeanMapper.getConfirurationXmlString(dateFormat);
						
						DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
						dozerBeanMapper.addMapping(new ByteArrayInputStream(configurationXmlString.getBytes()));
						param = dozerBeanMapper.map(restCommonRequest.getBody(), parameterTypes[0]);
					} else {
						param = BeanMapper.mapWithDozer(restCommonRequest.getBody(), parameterTypes[0]);
					}
				}
			}
			
			//校验业务请求实体
			if( param !=null)
				validBusinessRequest(param);
			
			//执行业务接口
			result = m.invoke(service, param);
			
			String responseBodyFormat = restMethod.responseBodyFormat();
			if("".contentEquals(restMethod.responseBodyFormat())) {//如果responseBodyFormat没设置值，则使用requestBodyFormat的值
				responseBodyFormat = restCommonRequest.getRequestBodyFormat();
			}
			
			if("json".equals(responseBodyFormat)) {
				restCommonResponse.setBody(result);
			} else {
				if(result instanceof byte[]) {
					byte[] bytes = (byte[]) result;
					String contentBase64 =Base64.encodeBase64String(bytes);
					restCommonResponse.setBody(contentBase64);
				} else {
					ObjectMapper objectMapper = new ObjectMapper();
					objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);//只显示非空字段
					String contentJson = objectMapper.writeValueAsString(result);
					String contentBase64 =Base64.encodeBase64String(contentJson.getBytes());
					restCommonResponse.setBody(contentBase64);
				}
			}
			
			restCommonResponse.setCode("1");
			restCommonResponse.setMsg("success");
			restCommonResponse.setResponseBodyFormat(responseBodyFormat);
			if("base64".equals(restCommonResponse.getResponseBodyFormat())) {//如果是默认的base64，则返回null，表示默认值，即base64
				restCommonResponse.setResponseBodyFormat(null);
			}
			restCommonResponse.setTimestamp((new Date()).getTime());
			
		}
		
		
		return restCommonResponse;
	}
	
	/**
	 * 验证请求对象
	 * @param object
	 * @throws BindException
	 */
	protected <T> void validBusinessRequest(T object) throws BindException {
		Validator validator = new SpringValidatorAdapter(Validation.buildDefaultValidatorFactory().getValidator());
		BindException bindException = new BindException(object, object.getClass().getSimpleName());
		validator.validate(object,bindException);
		if (bindException.hasErrors()) {
			throw bindException;
		}		
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
		
	}
}
