package com.mercury.platform.server.handlers;

import com.mercury.platform.server.bus.UpdaterServerAsyncEventBus;
import com.mercury.platform.server.bus.event.ClientActiveEvent;
import com.mercury.platform.server.bus.event.ClientUnregisteredEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;

/**
 * Created by Frost on 14.01.2017.
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LogManager.getLogger(ServerHandler.class);
    private UpdaterServerAsyncEventBus eventBus = UpdaterServerAsyncEventBus.getInstance();

    @Override
    public void channelActive(ChannelHandlerContext context) throws Exception {
        LOGGER.info("{} channel is active" , this);
        InetSocketAddress address = (InetSocketAddress) context.channel().remoteAddress();
        eventBus.post(new ClientActiveEvent(address));
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) throws Exception {
        LOGGER.info("Message = {}" , msg);
        context.channel().writeAndFlush(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
        LOGGER.error(cause);
    }


    @Override
    public void channelInactive(ChannelHandlerContext context) throws Exception {

    }


    @Override
    public void channelUnregistered(ChannelHandlerContext context) throws Exception {
        LOGGER.info("{} channel is unregistered" , this);
        InetSocketAddress address = (InetSocketAddress) context.channel().remoteAddress();
        eventBus.post(new ClientUnregisteredEvent(address));
    }
}
