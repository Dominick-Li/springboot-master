package com.ljm.boot.nettywebsocket.netty;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ljm.boot.nettywebsocket.model.SocketMessage;
import com.ljm.boot.nettywebsocket.repository.UserGroupRepository;
import com.ljm.boot.nettywebsocket.util.SpringUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Dominick Li
 * @createTime 2020/2/28  13:07
 * @description
 **/
public class TestWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private final Logger logger = LoggerFactory.getLogger(TestWebSocketHandler.class);
    /**
     * 存储已经登录用户的channel对象
     */
    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 存储用户id和用户的channelId绑定
     */
    public static ConcurrentHashMap<Integer, ChannelId> userMap = new ConcurrentHashMap<>();
    /**
     * 用于存储群聊房间号和群聊成员的channel信息
     */
    public static ConcurrentHashMap<Integer, ChannelGroup> groupMap = new ConcurrentHashMap<>();

    /**
     * 获取用户拥有的群聊id号
     */
    UserGroupRepository userGroupRepositor = SpringUtil.getBean(UserGroupRepository.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("与客户端建立连接，通道开启！");
        //添加到channelGroup通道组
        channelGroup.add(ctx.channel());
        ctx.channel().id();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("与客户端断开连接，通道关闭！");
        //添加到channelGroup 通道组
        channelGroup.remove(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //首次连接是FullHttpRequest，把用户id和对应的channel对象存储起来
        if (null != msg && msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            String uri = request.uri();
            Integer userId = getUrlParams(uri);
            userMap.put(getUrlParams(uri), ctx.channel().id());
            logger.info("登录的用户id是：{}", userId);
            //第1次登录,需要查询下当前用户是否加入过群,加入过群,则放入对应的群聊里
            List<Integer> groupIds = userGroupRepositor.findGroupIdByUserId(userId);
            ChannelGroup cGroup = null;
            //查询用户拥有的组是否已经创建了
            for (Integer groupId : groupIds) {
                cGroup = groupMap.get(groupId);
                //如果群聊管理对象没有创建
                if (cGroup == null) {
                    //构建一个channelGroup群聊管理对象然后放入groupMap中
                    cGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
                    groupMap.put(groupId, cGroup);
                }
                //把用户放到群聊管理对象里去
                cGroup.add(ctx.channel());
            }
            //如果url包含参数，需要处理
            if (uri.contains("?")) {
                String newUri = uri.substring(0, uri.indexOf("?"));
                request.setUri(newUri);
            }

        } else if (msg instanceof TextWebSocketFrame) {
            //正常的TEXT消息类型
            TextWebSocketFrame frame = (TextWebSocketFrame) msg;
            logger.info("客户端收到服务器数据：{}", frame.text());
            SocketMessage socketMessage = JSON.parseObject(frame.text(), SocketMessage.class);
            //处理群聊任务
            if ("group".equals(socketMessage.getMessageType())) {
                //推送群聊信息
                groupMap.get(socketMessage.getChatId()).writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(socketMessage)));
            } else {
                //处理私聊的任务，如果对方也在线,则推送消息
                ChannelId channelId = userMap.get(socketMessage.getChatId());
                if (channelId != null) {
                    Channel ct = channelGroup.find(channelId);
                    if (ct != null) {
                        ct.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(socketMessage)));
                    }
                }
            }
        }
        super.channelRead(ctx, msg);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {

    }

    private static Integer getUrlParams(String url) {
        if (!url.contains("=")) {
            return null;
        }
        String userId = url.substring(url.indexOf("=") + 1);
        return Integer.parseInt(userId);
    }

}
