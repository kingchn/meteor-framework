package cn.meteor.module.core.rest.request;

import java.io.Serializable;

public class RestBodyRequest<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private T data;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
