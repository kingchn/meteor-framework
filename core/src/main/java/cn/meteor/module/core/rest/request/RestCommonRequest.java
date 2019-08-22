package cn.meteor.module.core.rest.request;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RestCommonRequest implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotBlank(message = "serviceId不能为空" )
	private String serviceId;
	
	/**
	 * 交易流水号
	 */
	private String tranSeq;
	
	/**
	 * 请求时的时间戳
	 */
	private String timestamp;
	
	/**
	 * 请求body参数格式，默认base64，开发环境可在客户端传递使用json进行调试
	 */
	@JsonProperty("_reqBodyFormat")
	private String requestBodyFormat = "base64";
	

//	@NotNull(message = "body不能为空" )
	private Object body;
	
	private String sign;
	
	private String appKey;

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getTranSeq() {
		return tranSeq;
	}

	public void setTranSeq(String tranSeq) {
		this.tranSeq = tranSeq;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getRequestBodyFormat() {
		return requestBodyFormat;
	}

	public void setRequestBodyFormat(String requestBodyFormat) {
		this.requestBodyFormat = requestBodyFormat;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	
}
