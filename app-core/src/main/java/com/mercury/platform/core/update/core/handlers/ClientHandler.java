package com.mercury.platform.core.update.core.handlers;

import com.google.common.primitives.Bytes;
import com.mercury.platform.core.update.bus.UpdaterClientEventBus;
import com.mercury.platform.core.update.bus.event.UpdateReceivedEvent;
import com.mercury.platform.core.update.core.holder.VersionHolder;
import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.CheckUpdatesEvent;
import com.mercury.platform.shared.events.custom.ShowPatchNotesEvent;
import com.mercury.platform.shared.events.custom.StartUpdateEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

/**
 * Need to refactoring.
 */
public class ClientHandler extends SimpleChannelInboundHandler<Object> {
    private static final Logger LOGGER = LogManager.getLogger(ClientHandler.class.getSimpleName());

    private volatile byte[] chunks;
    private volatile int length;

    private ChannelHandlerContext context;
    public ClientHandler() {
        chunks = new byte[0];
        EventRouter.INSTANCE.registerHandler(CheckUpdatesEvent.class, handler -> {
            checkUpdate();
        });
        EventRouter.INSTANCE.registerHandler(StartUpdateEvent.class, handler -> {
            getLatestUpdate();
        });
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object object) throws Exception {
        if (object instanceof String) {
            EventRouter.INSTANCE.fireEvent(new ShowPatchNotesEvent((String)object));
        }
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
        this.context = context;
    }
    private void checkUpdate(){
        LOGGER.debug("Sending version message to server");
        Integer version = VersionHolder.getInstance().getVersion();
        if(context != null) {
            context.channel().writeAndFlush(version);
        }
    }
    private void getLatestUpdate(){
        LOGGER.debug("Sending update message to server");
        context.channel().writeAndFlush("UPDATE ME");
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
