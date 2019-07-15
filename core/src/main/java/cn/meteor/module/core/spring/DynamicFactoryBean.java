package cn.meteor.module.core.spring;

import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class DynamicFactoryBean<T> implements FactoryBean<T>, InitializingBean {
	
	private  static final Logger logger = LogManager.getLogger(DynamicFactoryBean.class);

	private Class<T> targetClass;

	public void setTargetClass(Class<T> targetClass) {
		this.targetClass = targetClass;
	}

	private T bean;

	private Map<String, Object> propertyMap;

	public Map<String, Object> getPropertyMap() {
		return propertyMap;
	}

	public void setPropertyMap(Map<String, Object> propertyMap) {
		this.propertyMap = propertyMap;
	}

	@Override
	public T getObject() throws Exception {
		return bean;
	}

	@Override
	public Class<T> getObjectType() {
		return targetClass;
	}

	@Override
	public boolean isSingleton() {
		// return false;
		return true;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		bean = targetClass.newInstance();
		if(propertyMap!=null) {
			for (String key : propertyMap.keySet()) {
				String fieldName = key;
				Object value = propertyMap.get(key);
				BeanUtils.setProperty(bean, fieldName, value);
			}
		}
		try {
			Method afterPropertiesSetMethod = targetClass.getDeclaredMethod("init");//init-method="init"
			if(afterPropertiesSetMethod!=null) {
				afterPropertiesSetMethod.invoke(bean);//调用该类的afterPropertiesSet
			}
		} catch (Exception e) {
//			logger.error("调用init出错",e);
			logger.error("执行" + getClass().getName() + "的afterPropertiesSet()时异常: " + e);
		}
	}
	
//	public void init() {
////		http://stackoverflow.com/questions/8519187/spring-postconstruct-vs-init-method-attribute
////		No practically I don't think there is any difference but there are priorities in the way they work. @PostConstruct, init-method are BeanPostProcessors.
////
////		@PostConstruct is a JSR-250 annotation while init-method is Spring's way of having an initializing method.
////		If you have a @PostConstruct method, this will be called first before the initializing methods are called.
////		If your bean implements InitializingBean and overrides afterPropertiesSet , first @PostConstruct is called, then the afterPropertiesSet and then init-method.		
//	}

}
