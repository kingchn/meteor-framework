package cn.meteor.module.core.websocket.server;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebSocketChildChannelHandler extends ChannelInitializer<SocketChannel>{

	private ChannelHandler webSocketServerHandler;
	
	private Boolean isSsl = false;
	
	private String keyStoreType;//JKS
	
	private String keyStorePath;
	
	private String keyStorePassword;
	
	public ChannelHandler getWebSocketServerHandler() {
		return webSocketServerHandler;
	}

	public void setWebSocketServerHandler(ChannelHandler webSocketServerHandler) {
		this.webSocketServerHandler = webSocketServerHandler;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		
		if(this.isSsl==true) {//启用ssl
//			SSLContext sslContext = createSSLContext("JKS","D://wss.jks","netty123");
//			SSLContext sslContext = createSSLContext("JKS","F:/websocket_keystore/b.keystore","12345678");
			SSLContext sslContext = createSSLContext(keyStoreType, keyStorePath, keyStorePassword);        
			//SSLEngine 此类允许使用ssl安全套接层协议进行安全通信
	        SSLEngine sslEngine = sslContext.createSSLEngine();
	        sslEngine.setUseClientMode(false); //服务器端模式
	        sslEngine.setNeedClientAuth(false); //不需要验证客户端	        

			pipeline.addLast("ssl", new SslHandler(sslEngine));
		}
        
        
//		pipeline.addLast(new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS));//https
		pipeline.addLast("http-codec", new HttpServerCodec());
		pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
		pipeline.addLast("http-chunked", new ChunkedWriteHandler());//
//        pipeline.addLast(new WebSocketServerCompressionHandler());//
//		pipeline.addLast(new AcceptorIdleStateTrigger());//https
		pipeline.addLast("handler",webSocketServerHandler);
	}
	
	private SSLContext createSSLContext(String type, String path, String password) throws Exception {
	    KeyStore ks = KeyStore.getInstance(type); /// "JKS"
	    InputStream ksInputStream = new FileInputStream(path); /// 证书存放地址
	    ks.load(ksInputStream, password.toCharArray());
	 	//KeyManagerFactory充当基于密钥内容源的密钥管理器的工厂。
	    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());//getDefaultAlgorithm:获取默认的 KeyManagerFactory 算法名称。
	    kmf.init(ks, password.toCharArray());
	    //SSLContext的实例表示安全套接字协议的实现，它充当用于安全套接字工厂或 SSLEngine 的工厂。
	    SSLContext sslContext = SSLContext.getInstance("TLS");
	    sslContext.init(kmf.getKeyManagers(), null, null);
	    return sslContext;
	}

	public String getKeyStoreType() {
		return keyStoreType;
	}

	public void setKeyStoreType(String keyStoreType) {
		this.keyStoreType = keyStoreType;
	}

	public String getKeyStorePath() {
		return keyStorePath;
	}

	public void setKeyStorePath(String keyStorePath) {
		this.keyStorePath = keyStorePath;
	}

	public String getKeyStorePassword() {
		return keyStorePassword;
	}

	public void setKeyStorePassword(String keyStorePassword) {
		this.keyStorePassword = keyStorePassword;
	}

	public Boolean getIsSsl() {
		return isSsl;
	}

	public void setIsSsl(Boolean isSsl) {
		this.isSsl = isSsl;
	}
}
