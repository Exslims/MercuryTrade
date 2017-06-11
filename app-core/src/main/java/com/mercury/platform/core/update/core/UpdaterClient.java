package com.mercury.platform.core.update.core;

import com.mercury.platform.core.update.bus.UpdaterClientEventBus;
import com.mercury.platform.core.update.bus.handlers.UpdateEventHandler;
import com.mercury.platform.core.update.core.holder.ApplicationHolder;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.RequestPatchNotesEvent;
import com.mercury.platform.shared.store.MercuryStore;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * Created by Frost on 14.01.2017.
 */
public class UpdaterClient {

    private static final Logger LOGGER = LogManager.getLogger(UpdaterClient.class.getSimpleName());

    private final String host;
    private final int port;
    private Bootstrap bootstrap;
    private EventLoopGroup group;
    private Channel channel;
    private boolean connectionEstablished;
    public UpdaterClient(String host, String mercuryVersion, int port) {
        this.host = host;
        this.port = port;
        String version = mercuryVersion.replace(".", "0");
        ApplicationHolder.getInstance().setVersion(Integer.valueOf(version));
        connectionEstablished = false;

        EventRouter.CORE.registerHandler(RequestPatchNotesEvent.class, event -> {
            if(!connectionEstablished){
                ApplicationHolder.getInstance().setManualRequest(false);
                MercuryStore.INSTANCE.stringAlertSubject.onNext("Server is currently down, please try again later.");
            }else {
                MercuryStore.INSTANCE.checkOutPatchSubject.onNext(true);
            }
        });
    }

    public void start(){
        ClientChannelInitializer clientChannelInitializer = new ClientChannelInitializer();
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(host, port))
                .option(ChannelOption.SO_KEEPALIVE,true)
                .handler(clientChannelInitializer)
        ;
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        doConnect();
    }
    private void doConnect(){
        try {
            ChannelFuture channelFuture = bootstrap.connect();
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (!future.isSuccess()) {
                        connectionEstablished = false;
                        future.channel().close();
                        future.channel().eventLoop().schedule(() -> {
                            bootstrap.connect().addListener(this);
                        },3,TimeUnit.MINUTES);
                    } else {
                        channel = future.channel();
                        connectionEstablished = true;
                        addCLoseDetectListener(channel);
                        LOGGER.debug("Connection established");
                    }
                }

                private void addCLoseDetectListener(Channel _channel) {
                    _channel.closeFuture().addListener((ChannelFutureListener) future -> {
                        LOGGER.debug("Connection lost");
                        connectionEstablished = false;
                        doConnect();
                    });
                }
            });
        }catch (Exception e){
            doConnect();
        }
    }
    public void shutdown() {
        try {
            if (group != null)
                group.shutdownGracefully().sync();
        }catch (InterruptedException e){}
    }
    public int getMercuryVersion() {
        return ApplicationHolder.getInstance().getVersion();
    }
    public void registerListener(UpdateEventHandler handler) {
        UpdaterClientEventBus.getInstance().register(handler);
    }
    public void removeListener(UpdateEventHandler handler) {
        UpdaterClientEventBus.getInstance().unregister(handler);
    }
}
