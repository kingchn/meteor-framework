package cn.meteor.module.core.openApi.exception;

public class BusinessException extends Exception {

	private static final long serialVersionUID = -8699725529015368087L;
	
	private String code;
	private String msg;

	public BusinessException(String msg) {  
        super(msg);  
    }
	
	public BusinessException(String code, String msg) {
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
