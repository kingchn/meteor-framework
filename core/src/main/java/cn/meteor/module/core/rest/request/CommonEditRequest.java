package cn.meteor.module.core.rest.request;

public class CommonEditRequest<T> {

	private T data;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
