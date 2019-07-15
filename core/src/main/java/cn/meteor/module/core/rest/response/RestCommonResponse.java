package cn.meteor.module.core.rest.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import cn.meteor.module.core.rest.exception.ErrorResponse;

public class RestCommonResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 代码
	 */
	private String code = ErrorResponse.CODE_DEFAULT;
	
	/**
	 * 描述
	 */
	private String msg = ErrorResponse.MSG_DEFAULT;
	
	private String detailCode;
	
	private String detailMsg;
	
	/**
	 * 响应body参数格式，默认base64，开发环境可在客户端传递使用json进行调试
	 */
	@JsonProperty("_respBodyFormat")
	private String responseBodyFormat = "base64";	

	
	/**
	 * 时间戳
	 */
	private long timestamp;
	
	/**
	 * 业务对象
	 */
	private Object body;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getDetailCode() {
		return detailCode;
	}

	public void setDetailCode(String detailCode) {
		this.detailCode = detailCode;
	}

	public String getDetailMsg() {
		return detailMsg;
	}

	public void setDetailMsg(String detailMsg) {
		this.detailMsg = detailMsg;
	}

	public String getResponseBodyFormat() {
		return responseBodyFormat;
	}

	public void setResponseBodyFormat(String responseBodyFormat) {
		this.responseBodyFormat = responseBodyFormat;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}
	

	
	
	
}
