package cn.meteor.module.core.rest.request;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

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
	 * 请求body参数格式，默认base64，开发环境可在客户端传递使用json进行调试
	 */
	@JsonProperty("_reqBodyFormat")
	private String requestBodyFormat = "base64";
	
	/**
	 * 时间格式
	 * datetime format
	 */
	private String df;

//	@NotNull(message = "body不能为空" )
	private Object body;

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
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

	public String getDf() {
		return df;
	}

	public void setDf(String df) {
		this.df = df;
	}

	
}
