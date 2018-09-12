package cn.meteor.module.core.websocket.domain;

import java.io.Serializable;
import java.net.SocketAddress;

import io.netty.channel.ChannelId;

public class WebSocketVo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2483041642405477080L;
	
	private String clientId;

	private ChannelId channelId;
	
	private String localHttpAddress;
	
	private SocketAddress localAddress;

	private SocketAddress remoteAddress;

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public ChannelId getChannelId() {
		return channelId;
	}

	public void setChannelId(ChannelId channelId) {
		this.channelId = channelId;
	}

	public String getLocalHttpAddress() {
		return localHttpAddress;
	}

	public void setLocalHttpAddress(String localHttpAddress) {
		this.localHttpAddress = localHttpAddress;
	}

	public SocketAddress getLocalAddress() {
		return localAddress;
	}

	public void setLocalAddress(SocketAddress localAddress) {
		this.localAddress = localAddress;
	}

	public SocketAddress getRemoteAddress() {
		return remoteAddress;
	}

	public void setRemoteAddress(SocketAddress remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	
}
