package com.mercury.platform.core.update.core.handlers;

import com.google.common.primitives.Bytes;
import com.mercury.platform.core.update.bus.UpdaterClientEventBus;
import com.mercury.platform.core.update.bus.event.UpdateReceivedEvent;
import com.mercury.platform.core.update.core.holder.ApplicationHolder;
import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.update.UpdateDescriptor;
import com.mercury.platform.update.UpdateType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Need to refactoring.
 */
public class ClientHandler extends SimpleChannelInboundHandler<Object> {
    private static final Logger LOGGER = LogManager.getLogger(ClientHandler.class.getSimpleName());

    private volatile byte[] chunks;
    private volatile int length;
    private volatile int percentDelta;
    private ResponseDispatcher responseDispatcher;

    private ChannelHandlerContext context;

    public ClientHandler() {
        chunks = new byte[0];
        responseDispatcher = new ResponseDispatcher();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object object) throws Exception {
        responseDispatcher.process(object);
        if (object instanceof Integer) {
            this.length = (int) object;
            this.percentDelta = getPercentOf(this.length, 800 * 1024);
        }
        if (object instanceof byte[]) {
            byte[] bytes = (byte[]) object;
            chunks = Bytes.concat(chunks, bytes);
            MercuryStoreCore.chunkLoadedSubject.onNext(percentDelta);
            if (chunks.length == length) {
                UpdateReceivedEvent event = new UpdateReceivedEvent(chunks);
                UpdaterClientEventBus.getInstance().post(event);
            }
        }
    }

    private int getPercentOf(int length, int value) {
        return (value * 100) / length;
    }


    @Override
    public void channelActive(ChannelHandlerContext context) throws Exception {
        this.context = context;
        if (context != null) {
            if (Configuration.get().applicationConfiguration().get().isCheckOutUpdate()) {
                LOGGER.info("Requesting update info from server");
                Integer version = ApplicationHolder.getInstance().getVersion();
                context.channel().writeAndFlush(new UpdateDescriptor(UpdateType.REQUEST_INFO, version));
            }
            MercuryStoreCore.checkOutPatchSubject.subscribe(state -> this.checkOutPatchNotes());
            MercuryStoreCore.startUpdateSubject.subscribe(state -> this.getLatestUpdate());
        }
    }

    private void checkOutPatchNotes() {
        Integer version = ApplicationHolder.getInstance().getVersion();
        if (context != null) {
            LOGGER.debug("Requesting patch notes message from server");
            context.channel().writeAndFlush(new UpdateDescriptor(UpdateType.REQUEST_PATCH_NOTES, version));
        }
    }

    private void getLatestUpdate() {
        if (context != null) {
            LOGGER.debug("Requesting update message from server");
            Integer version = ApplicationHolder.getInstance().getVersion();
            context.channel().writeAndFlush(new UpdateDescriptor(UpdateType.REQUEST_UPDATE, version));
        }
    }


    @Override
    public void channelInactive(ChannelHandlerContext context) throws Exception {
        LOGGER.info("Channel is inactive");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
        LOGGER.error(cause);
    }


}
