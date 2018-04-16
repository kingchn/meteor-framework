package cn.meteor.module.core.openApi.response;

import java.io.UnsupportedEncodingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;

import cn.meteor.module.util.mapper.BeanMapper;
import cn.meteor.module.util.reflect.ObjectUtils;
import cn.meteor.module.util.security.DESUtils;

public class BaseResponse {
	
	private static final Logger logger = LogManager.getLogger(BaseResponse.class);
	
	/**
	 * 代码
	 */
	private String code = RestMsgUtils.CODE_DEFAULT;
	
	/**
	 * 描述
	 */
	private String msg = RestMsgUtils.MSG_DEFAULT;
	
	/**
	 * 密文
	 */
	private String cipherText;
	
	@JsonIgnore
	private String cipherKey;
	
	
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

	public String getCipherText() {
		return cipherText;
	}

	public void setCipherText(String cipherText) {
		this.cipherText = cipherText;
	}
	
	@JsonIgnore
	public BaseResponse getCipherObject() {
		BaseResponse object = null;
		try {
			object = this.getClass().newInstance();
			BeanMapper.mapWithDozer(this, object);			
			ObjectUtils.setObjectFieldsEmpty(this);//清空对象所有字段值，设置为null
			this.setCode(object.getCode());//保留code、msg
			this.setMsg(object.getMsg());
			object.setCode(null);
			object.setMsg(null);
			return object;
		} catch (InstantiationException e) {
			logger.error("BaseResponse getCipherObject出错：", e);
		} catch (IllegalAccessException e) {
			logger.error("BaseResponse getCipherObject出错：", e);
		}		
		return null;		
	}
	
	public String encrypt(String data) {
		String encryptString = null;
		try {
			encryptString = DESUtils.encryptWithBase64(data, cipherKey);
		} catch (UnsupportedEncodingException e) {
			logger.error("BaseResponse encrypt出错：", e);
		}
		return encryptString;
	}
	
	/**
	 * 清空对象
	 */
	

	public String getCipherKey() {
		return cipherKey;
	}

	public void setCipherKey(String cipherKey) {
		this.cipherKey = cipherKey;
	}  
}
