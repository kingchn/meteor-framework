package cn.meteor.module.core.openApi.request;

import java.io.Serializable;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestParam;

import cn.meteor.module.core.security.xss.filter.XSSUtils;

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
	
	/**
	 * 请求参数map
	 */
	private Map<String, Object> allRequestParams;

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

	public Map<String, Object> getAllRequestParams() {//不建议使用该方法，为了兼容已经调用该方法，以下在调用该方法时处理过滤不安全信息
		if (allRequestParams != null) {
			for (String key : allRequestParams.keySet()) {
				Object value = allRequestParams.get(key);
				if (value != null && value instanceof String) {// 过滤不安全信息
					String valueString = XSSUtils.stripXSS("" + value);
					allRequestParams.put(key, valueString);
				}
			}
		}		
		return allRequestParams;
	}

	public void setAllRequestParams(@RequestParam Map<String, Object> allRequestParams) {
		this.allRequestParams = allRequestParams;
	}
}
