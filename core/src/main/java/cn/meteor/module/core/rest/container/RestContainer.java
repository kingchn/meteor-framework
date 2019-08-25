package cn.meteor.module.core.rest.container;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.AnnotatedTypeScanner;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.meteor.module.core.openApi.secret.AppSecretManager;
import cn.meteor.module.core.rest.annotation.RestClass;
import cn.meteor.module.core.rest.annotation.RestMethod;

@Component
public class RestContainer implements InitializingBean {
	
	private static final Logger logger = LogManager.getLogger(RestContainer.class);
	
	@Value("${core.rest.basePackages}")
	private String basePackages;	
	@Autowired
	private AppSecretManager appSecretManager;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
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
	
	/**
	 * 方法map，key RestClass注解的值 + "." + RestMethod注解的值；value RequiresPermissions注解
	 */
	private Map<String, RequiresPermissions> requiresPermissionsAnnotationMap = new HashMap<>();

	
	@Override
	public void afterPropertiesSet() throws Exception {
		
		if(StringUtils.isEmpty(basePackages)){
			logger.error("core.rest.basePackages不能为空，服务终止！");
			System.exit(-1);
		}
		
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);	//忽略未知字段   
		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);			//兼容字段名没双引号包围
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);//只显示非空字段
		
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
						
						//初始化requiresPermissionsAnnotationMap数据
						RequiresPermissions requiresPermissions = m.getAnnotation(RequiresPermissions.class);
						if(requiresPermissions != null) {
							requiresPermissionsAnnotationMap.put(restMethodValue, requiresPermissions);
						}
					}
				}
			}			
			
		}
		
	}

	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public Map<String, Class<?>> getClazzMap() {
		return clazzMap;
	}

	public Map<String, Method> getMethodMap() {
		return methodMap;
	}

	public Map<String, RestMethod> getRestMethodAnnotationMap() {
		return restMethodAnnotationMap;
	}

	public Map<String, RequiresPermissions> getRequiresPermissionsAnnotationMap() {
		return requiresPermissionsAnnotationMap;
	}

	public AppSecretManager getAppSecretManager() {
		return appSecretManager;
	}
}
