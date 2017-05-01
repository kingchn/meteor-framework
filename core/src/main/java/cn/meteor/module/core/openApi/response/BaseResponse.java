package cn.meteor.module.core.openApi.response;

public class BaseResponse {
	/**
	 * 代码
	 */
	private String code = RestMsgUtils.CODE_DEFAULT;
	
	/**
	 * 描述
	 */
	private String msg = RestMsgUtils.MSG_DEFAULT;
	
	
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
}
