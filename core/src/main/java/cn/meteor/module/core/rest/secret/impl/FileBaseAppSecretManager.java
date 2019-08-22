package cn.meteor.module.core.rest.secret.impl;

import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.Assert;

import cn.meteor.module.core.openApi.secret.AppSecretManager;

public class FileBaseAppSecretManager implements AppSecretManager,InitializingBean {
	
	private final Logger logger = LogManager.getLogger(getClass());
	
	private String propertiesFilePath;

	public String getPropertiesFilePath() {
		return propertiesFilePath;
	}

	public void setPropertiesFilePath(String propertiesFilePath) {
		this.propertiesFilePath = propertiesFilePath;
	}

	private Properties properties;
	
	public FileBaseAppSecretManager() {
		
	}
	
	public boolean isContainAppKey(String appKey) {
		boolean isContainsKey = false;
		if (properties != null && appKey != null) {
			isContainsKey = properties.containsKey(appKey);
		}
		return isContainsKey;
	}
	
	@Override
	public String getSecret(String appKey) {
		String secret=null;
		if(properties!=null){
			secret = properties.getProperty(appKey);
	        Assert.notNull(secret,"appKey["+appKey+"]的密钥为空");
        }
		return secret;        
	}
	
	public void afterPropertiesSet() throws Exception {
		if(properties == null){
            try {
//            	if(propertiesFilePath==null) {
//            		propertiesFilePath = "openApi/appSecret.properties";
//            	}
                properties = PropertiesLoaderUtils.loadAllProperties(propertiesFilePath);
            } catch (IOException e) {
            	logger.error(e.toString());
            }
        }
	}

}
