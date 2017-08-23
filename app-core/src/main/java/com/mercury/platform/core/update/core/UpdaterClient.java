package com.mercury.platform.core.update.core;

import com.mercury.platform.core.update.bus.UpdaterClientEventBus;
import com.mercury.platform.core.update.bus.handlers.UpdateEventHandler;
import com.mercury.platform.core.update.core.holder.ApplicationHolder;
import com.mercury.platform.shared.store.MercuryStoreCore;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.util.Random;
import java.util.concurrent.TimeUnit;

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

        MercuryStoreCore.requestPatchSubject.subscribe(state -> {
            if (!connectionEstablished) {
                ApplicationHolder.getInstance().setManualRequest(false);
                MercuryStoreCore.stringAlertSubject.onNext("Server is currently down, please try again later.");
            } else {
                MercuryStoreCore.checkOutPatchSubject.onNext(true);
            }
        });
    }

    public void start() {
        ClientChannelInitializer clientChannelInitializer = new ClientChannelInitializer();
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(host, port))
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(clientChannelInitializer)
        ;
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        doConnect();
    }

    private void doConnect() {
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
                        }, new Random().nextInt(5), TimeUnit.MINUTES);
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
        } catch (Exception e) {
            doConnect();
        }
    }

    public void shutdown() {
        try {
            if (group != null)
                group.shutdownGracefully().sync();
        } catch (InterruptedException e) {
        }
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
