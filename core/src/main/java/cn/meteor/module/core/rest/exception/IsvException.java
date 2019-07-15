package cn.meteor.module.core.rest.exception;

public class IsvException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8831971489544705469L;

	private String code;
	
	private String msg;

	public IsvException(String msg) {  
        super(msg);  
    }
	
	public IsvException(String code, String msg) {
		super(msg);
		this.code=code;
		this.msg=msg;        
    }

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
