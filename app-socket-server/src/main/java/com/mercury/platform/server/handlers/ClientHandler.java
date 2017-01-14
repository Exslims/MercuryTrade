package com.mercury.platform.server.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Frost on 14.01.2017.
 */
public class ClientHandler extends SimpleChannelInboundHandler<Object> {


    private static final Logger LOGGER = LogManager.getLogger(ClientHandler.class);


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

    }


    @Override
    public void channelActive(ChannelHandlerContext context) throws Exception {
        context.channel().writeAndFlush("echo");
        context.disconnect();
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
        LOGGER.info(cause);
    }

}
