package com.mercury.platform.server.handlers;

import com.mercury.platform.holder.UpdateHolder;
import com.mercury.platform.server.bus.UpdaterServerAsyncEventBus;
import com.mercury.platform.server.bus.event.ClientActiveEvent;
import com.mercury.platform.server.bus.event.ClientUnregisteredEvent;
import com.mercury.platform.server.bus.event.ClientUpdatedEvent;
import com.mercury.platform.update.AlreadyLatestUpdateMessage;
import com.mercury.platform.update.PatchNotesDescriptor;
import com.mercury.platform.update.UpdateDescriptor;
import com.mercury.platform.update.UpdateType;
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

    private String test = "{\n" +
            "  \"version\":\"1.0.0.2\",\n" +
            "  \"notes\":[\n" +
            "  {\n" +
            "    \"title\" : \"Update 1.0.0.1\",\n" +
            "    \"text\" : \"- Removed the \\\"Donate\\\" button, as it proved to be too immersion breaking. \\nWe would appretiate more feedback from our first users!\\n\\n (Yes, we are aware the reddit topic has been hidden from the frontpage. No two-way communication yet so far.)\",\n" +
            "    \"image\" : \"\",\n" +
            "    \"layout\" : \"VERTICAL\"\n" +
            "  }\n" +
            "]}";
    private static final Logger LOGGER = LogManager.getLogger(ServerHandler.class.getSimpleName());
    private UpdaterServerAsyncEventBus eventBus = UpdaterServerAsyncEventBus.getInstance();
    private UpdateHolder updateHolder = UpdateHolder.getInstance();

    @Override
    public void channelActive(ChannelHandlerContext context) throws Exception {
        InetSocketAddress address = (InetSocketAddress) context.channel().remoteAddress();
        eventBus.post(new ClientActiveEvent(address));
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) throws Exception {

        if (msg instanceof UpdateDescriptor) {
            UpdateDescriptor descriptor = (UpdateDescriptor) msg;
            if (descriptor.getVersion() < updateHolder.getVersion()) {
                switch (descriptor.getType()) {
                    case REQUEST_PATCH_NOTES:{
                        PatchNotesDescriptor patchDescriptor = new PatchNotesDescriptor(test);
                        context.channel().writeAndFlush(patchDescriptor);
                        break;
                    }
                    case REQUEST_INFO: {
                        UpdateDescriptor updateDescriptor = new UpdateDescriptor(UpdateType.REQUEST_INFO,updateHolder.getVersion());
                        context.channel().writeAndFlush(updateDescriptor);
                        break;
                    }
                    case REQUEST_UPDATE: {
                        byte[] update = UpdateHolder.getInstance().getUpdate();
                        context.channel().writeAndFlush(update.length);
                        int chunkSize = 800 * 1024;
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
                        break;
                    }
                }
            }else {
                context.channel().writeAndFlush(new AlreadyLatestUpdateMessage());
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
