package cn.meteor.module.core.spring;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.FactoryBean;

public class NullFactoryBean<T> implements FactoryBean<T> {
	
	private  static final Logger logger = LogManager.getLogger(NullFactoryBean.class);

//	private T bean;

	@Override
	public T getObject() throws Exception {
		return null;
	}

	@Override
	public Class<T> getObjectType() {
		return null;
	}

	@Override
	public boolean isSingleton() {
		// return false;
		return true;
	}

}
