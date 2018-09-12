package cn.meteor.module.core.websocket.domain;

public class Client {

	/**
	 * 客户端id，0为游客模式
	 */
	private String id;

	/**
	 * 房间id，all为所有人房间
	 */
	private String roomId;

	public Client() {
//		id = "0";
//		roomId = "0";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
}
