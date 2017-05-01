package cn.meteor.module.core.openApi.request;

import java.io.Serializable;

//@JsonSerialize(include=Inclusion.NON_NULL)
public class BaseApiRequest implements Serializable {
	
	private String method;

	private String session;
	
	private String timestamp;
	
	private String format;
	
	private String appKey;
	
	private String v;
	
	private String sign;
	
	private String signMethod;
	
	

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getV() {
		return v;
	}

	public void setV(String v) {
		this.v = v;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSignMethod() {
		return signMethod;
	}

	public void setSignMethod(String signMethod) {
		this.signMethod = signMethod;
	}
}
