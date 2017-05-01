package cn.meteor.module.core.openApi.response;

import javax.xml.bind.annotation.XmlRootElement;

import cn.meteor.module.core.openApi.annotation.RestJsonRootName;


//@XmlRootElement(name="response")
@RestJsonRootName
public class ErrorResponse extends BaseResponse {

	/**
	 * 代码
	 */
	private String code;
	
	/**
	 * 描述
	 */
	private String msg;
	
	private String detailCode;
	
	private String detailMsg;

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
}
