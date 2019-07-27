package cn.meteor.module.core.rest.request;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CommonTRequest<T> {

	@NotNull
	@JsonProperty("data")
	private T data;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
