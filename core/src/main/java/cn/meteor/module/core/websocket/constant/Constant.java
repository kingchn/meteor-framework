package cn.meteor.module.core.websocket.constant;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
/**
 * 常量池
 * */
public class Constant {
	
	//存放所有的ChannelHandlerContext
	public static Map<String, ChannelHandlerContext> channelHandlerContextMap = new ConcurrentHashMap<String, ChannelHandlerContext>() ;
	

    // 一个 ChannelGroup 代表一个直播频道
	public static Map<String, ChannelGroup> channelGroupMap = new ConcurrentHashMap<String, ChannelGroup>();
    

}
