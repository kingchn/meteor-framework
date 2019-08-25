package cn.meteor.module.core.rest.vo;

import java.lang.reflect.Method;

public class RestApiVo {

	private String classKey;
	
	private String methodKey;
	
	private Object service;
	
	private Method method;

	public String getClassKey() {
		return classKey;
	}

	public void setClassKey(String classKey) {
		this.classKey = classKey;
	}

	public String getMethodKey() {
		return methodKey;
	}

	public void setMethodKey(String methodKey) {
		this.methodKey = methodKey;
	}

	public Object getService() {
		return service;
	}

	public void setService(Object service) {
		this.service = service;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}
	
	
}
