package com.mercury.platform.server.handlers;

import com.mercury.platform.holder.UpdateHolder;
import com.mercury.platform.server.bus.UpdaterServerAsyncEventBus;
import com.mercury.platform.server.bus.event.ClientActiveEvent;
import com.mercury.platform.server.bus.event.ClientUnregisteredEvent;
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

    private static final Logger LOGGER = LogManager.getLogger(ServerHandler.class);
    private UpdaterServerAsyncEventBus eventBus = UpdaterServerAsyncEventBus.getInstance();
    private UpdateHolder updateHolder = UpdateHolder.getInstance();

    @Override
    public void channelActive(ChannelHandlerContext context) throws Exception {
        LOGGER.info("{} channel is active" , this);
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

                while (chunkEnd < update.length) {
                    System.out.println(update.length);
                    if (chunkStart + chunkSize > update.length) {
                        chunkSize = update.length - chunkStart;
                    }
                    chunkStart += chunkSize;

                    chunkEnd = chunkStart + chunkSize;
                    System.out.println(chunkStart + " " + chunkSize + " " + chunkEnd);

                    context.channel().writeAndFlush(Arrays.copyOfRange(update, chunkStart, chunkEnd));
                }
            }
        }
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("Channel {} read complete" , ctx.channel().id());
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
