package com.mercury.platform.server.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Frost on 14.01.2017.
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LogManager.getLogger(ServerHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext context) throws Exception {
        LOGGER.info("{} channel is active" , this);
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) throws Exception {
        LOGGER.info("Message = {}" , msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
        LOGGER.error(cause);
    }
}
