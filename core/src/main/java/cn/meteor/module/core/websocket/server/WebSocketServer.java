package cn.meteor.module.core.websocket.server;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PreDestroy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import cn.meteor.module.core.websocket.init.AfterSpringBegin;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.socket.nio.NioServerSocketChannel;


public class WebSocketServer extends AfterSpringBegin {
	
	private static final Logger logger = LogManager.getLogger(WebSocketServer.class);
	
	// 用于客户端连接请求
	@Autowired
	private EventLoopGroup bossGroup;

	// 用于处理客户端I/O操作
	@Autowired
	private EventLoopGroup workerGroup;

	// 服务器的辅助启动类
	@Autowired
	private ServerBootstrap serverBootstrap;	

	//服务端口
	protected int port;
	
	//服务对
	protected String serverParis;
	

	//BS的I/O处理类
	private ChannelHandler childChannelHandler;
	
	

	private ChannelFuture channelFuture;
	
	public WebSocketServer() {
	}

	public EventLoopGroup getBossGroup() {
		return bossGroup;
	}

	public void setBossGroup(EventLoopGroup bossGroup) {
		this.bossGroup = bossGroup;
	}

	public EventLoopGroup getWorkerGroup() {
		return workerGroup;
	}

	public void setWorkerGroup(EventLoopGroup workerGroup) {
		this.workerGroup = workerGroup;
	}

	public ServerBootstrap getServerBootstrap() {
		return serverBootstrap;
	}

	public void setServerBootstrap(ServerBootstrap serverBootstrap) {
		this.serverBootstrap = serverBootstrap;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public ChannelHandler getChildChannelHandler() {
		return childChannelHandler;
	}

	public void setChildChannelHandler(ChannelHandler childChannelHandler) {
		this.childChannelHandler = childChannelHandler;
	}

	public ChannelFuture getChannelFuture() {
		return channelFuture;
	}

	public void setChannelFuture(ChannelFuture channelFuture) {
		this.channelFuture = channelFuture;
	}
	
	@Override
	public void run() {
		try {
			bulid(port);
		} catch (Exception e) {
			logger.error("netty启动出错:", e);
		}
	}
	
	public void bulid(int port) throws Exception{		
		try {
			
			//（1）boss辅助客户端的tcp连接请求  worker负责与客户端之前的读写操作
			//（2）配置客户端的channel类型
			//(3)配置TCP参数，握手字符串长度设置
			//(4)TCP_NODELAY是一种算法，为了充分利用带宽，尽可能发送大块数据，减少充斥的小块数据，true是关闭，可以保持高实时性,若开启，减少交互次数，但是时效性相对无法保证
			//(5)开启心跳包活机制，就是客户端、服务端建立连接处于ESTABLISHED状态，超过2小时没有交流，机制会被启动
			//(6)netty提供了2种接受缓存区分配器，FixedRecvByteBufAllocator是固定长度，但是拓展，AdaptiveRecvByteBufAllocator动态长度
			//(7)绑定I/O事件的处理类,WebSocketChildChannelHandler中定义
//			AttributeKey<String> keyForLocalHttpAddress = AttributeKey.valueOf("localHttpAddress");
//			String localServerIpAndPort = getLocalHttpAddress();
			serverBootstrap.group(bossGroup,workerGroup)
						   .channel(NioServerSocketChannel.class)
//						   .handler(new LoggingHandler(LogLevel.INFO))
						   .option(ChannelOption.SO_BACKLOG, 1024)
//						   .option(ChannelOption.TCP_NODELAY, true)
						   .childOption(ChannelOption.SO_KEEPALIVE, true)
						   .childOption(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(592048))
//						   .childAttr(keyForLocalHttpAddress, localServerIpAndPort)
						   .childHandler(childChannelHandler);
			

			logger.info("netty启动，端口号:"+ port);
			channelFuture = serverBootstrap.bind(port).sync();
			logger.info("netty启动成功，端口号:"+ port);
			channelFuture.channel().closeFuture().sync();
			
		} catch (Exception e) {
			logger.error("netty启动出错:", e);
			bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
			
		}

	}
	
//	public abstract String getLocalHttpAddress();
	
	Map<String, String> serverMap = new HashMap<>();
	
	private String getLocalHttpAddress(String key) {
		String[] servers = serverParis.split(";");
		for (String serv : servers) {
			String[] oneServer = serv.split(",");
			serverMap.put(oneServer[0], oneServer[0]);
		}
		String result = serverMap.get(key);
		return result;
	}
	
	//执行之后关闭
	@PreDestroy
	public void close(){
		bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();		
	}

	public String getServerParis() {
		return serverParis;
	}

	public void setServerParis(String serverParis) {
		this.serverParis = serverParis;
	}
	
	

//    private static final int PORT = 8083;
//
//    public static void main(String[] args) throws Exception {
//
//        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
//        EventLoopGroup workerGroup = new NioEventLoopGroup();
//        try {
//            ServerBootstrap b = new ServerBootstrap();
//            b.group(bossGroup, workerGroup)
//                    .channel(NioServerSocketChannel.class)
//                    .handler(new LoggingHandler(LogLevel.INFO))
//					.childHandler(new WebSocketServerInitializer());
//
//            Channel ch = b.bind(PORT).sync().channel();
//            ch.closeFuture().sync();
//        } finally {
//            bossGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
//        }
//    }
}
