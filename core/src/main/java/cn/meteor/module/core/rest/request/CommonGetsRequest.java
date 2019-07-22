package cn.meteor.module.core.rest.request;

import java.util.Map;

public class CommonGetsRequest {
	
	private Map<String, String> paramMap;

	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Map<String, String> getParamMap() {
		return paramMap;
	}

	public void setParamMap(Map<String, String> paramMap) {
		this.paramMap = paramMap;
	}
}
