package cn.meteor.module.core.openApi.exception;

public class IspException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8591338430432884385L;

	private String code;

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
	
	public IspException() {
		
	}

	public IspException(String detailMsg) {
        super(detailMsg);
        this.detailMsg = detailMsg;
    }
	
	public IspException(String code, String msg, String detailCode, String detailMsg) {
		super(detailMsg);
		this.code = code;
		this.msg = msg;
		this.detailCode=detailCode;
		this.detailMsg = detailMsg;   
    }
	
	public IspException(String code, String msg, String detailCode, String detailMsg, Throwable cause) {
		super(detailMsg, cause);
		this.code = code;
		this.msg = msg;
		this.detailCode=detailCode;
		this.detailMsg = detailMsg;   
    }
	
}
