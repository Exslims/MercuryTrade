package com.mercury.platform.server.handlers;

import com.mercury.platform.holder.UpdateHolder;
import com.mercury.platform.server.bus.UpdaterServerAsyncEventBus;
import com.mercury.platform.server.bus.event.ClientActiveEvent;
import com.mercury.platform.server.bus.event.ClientUnregisteredEvent;
import com.mercury.platform.server.bus.event.ClientUpdatedEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.util.Arrays;

/**
 * Created by Frost on 14.01.2017.
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LogManager.getLogger(ServerHandler.class.getSimpleName());
    private UpdaterServerAsyncEventBus eventBus = UpdaterServerAsyncEventBus.getInstance();
    private UpdateHolder updateHolder = UpdateHolder.getInstance();

    @Override
    public void channelActive(ChannelHandlerContext context) throws Exception {
//        LOGGER.info("{} channel is active" , this.getClass().getSimpleName());
        InetSocketAddress address = (InetSocketAddress) context.channel().remoteAddress();
        eventBus.post(new ClientActiveEvent(address));
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) throws Exception {

        if (msg instanceof Integer) {
            Integer version = (Integer) msg;
            LOGGER.info("client {} version = {}", context.channel().remoteAddress(), version);

            if (version < updateHolder.getVersion()) {
                byte[] update = UpdateHolder.getInstance().getUpdate();
                context.channel().writeAndFlush(update.length);
                int chunkSize = 800*1024;
                int chunkStart = 0;
                int chunkEnd = 0;

                while (chunkStart < update.length) {
                    if (chunkStart + chunkSize > update.length) {
                        chunkSize = update.length - chunkStart;
                    }

                    chunkEnd = chunkStart + chunkSize;

                    context.channel().writeAndFlush(Arrays.copyOfRange(update, chunkStart, chunkEnd));

                    chunkStart += chunkSize;
                }
                InetSocketAddress address = (InetSocketAddress) context.channel().remoteAddress();
                eventBus.post(new ClientUpdatedEvent(address));
            }
        }
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("Channel {} read complete" , ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
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
