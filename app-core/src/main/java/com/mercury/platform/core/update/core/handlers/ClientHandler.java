package com.mercury.platform.core.update.core.handlers;

import com.google.common.primitives.Bytes;
import com.mercury.platform.core.update.bus.UpdaterClientEventBus;
import com.mercury.platform.core.update.bus.event.UpdateReceivedEvent;
import com.mercury.platform.core.update.core.holder.VersionHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Frost on 14.01.2017.
 */
public class ClientHandler extends SimpleChannelInboundHandler<Object> {


    private static final Logger LOGGER = LogManager.getLogger(ClientHandler.class.getSimpleName());

    private volatile byte[] chunks;
    private volatile int length;

    public ClientHandler() {
        chunks = new byte[0];
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object object) throws Exception {
        if (object instanceof Integer) {
            this.length = (int) object;
        }

        if (object instanceof byte[]) {
            byte[] bytes = (byte[]) object;
            chunks = Bytes.concat(chunks,bytes);
            if (chunks.length == length) {
                UpdateReceivedEvent event = new UpdateReceivedEvent(chunks);
                UpdaterClientEventBus.getInstance().post(event);
            }
        }
    }


    @Override
    public void channelActive(ChannelHandlerContext context) throws Exception {
        LOGGER.debug("Sending version message to server");
        Integer version = VersionHolder.getInstance().getVersion();
        context.channel().writeAndFlush(version);

    }


    @Override
    public void channelInactive(ChannelHandlerContext context) throws Exception {
        LOGGER.info("Channel is inactive");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
        LOGGER.error(Arrays.toString(cause.getStackTrace()));
    }

}
