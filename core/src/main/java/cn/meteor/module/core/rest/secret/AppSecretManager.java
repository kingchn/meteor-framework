package cn.meteor.module.core.rest.secret;

public interface AppSecretManager {
	
	boolean isContainAppKey(String appKey);

	/**
     * 获取应用程序的密钥
     * @param appKey
     * @return
     */
    String getSecret(String appKey);
}
