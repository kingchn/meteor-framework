package cn.meteor.module.core.rest.exception;

public class ErrorResponse {
	
	public static final String CODE_DEFAULT = "0";// 初始状态
	public static final String MSG_DEFAULT = "";

	public static final String CODE_SUCCESS = "1";
	public static final String MSG_SUCCESS = "Success";

	public static final String CODE_FAILURE = "2";
	public static final String MSG_FAILURE = "Failure";

	public static final String DETAIL_CODE_SUCCESS = "isv.success";
	public static final String DETAIL_CODE_FAILURE = "isv.failure";

	/**
	 * 代码
	 */
	private String code = CODE_DEFAULT;
	
	/**
	 * 描述
	 */
	private String msg = MSG_DEFAULT;
	
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
