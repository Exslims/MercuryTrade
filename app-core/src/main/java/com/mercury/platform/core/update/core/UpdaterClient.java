package com.mercury.platform.core.update.core;

import com.mercury.platform.core.update.bus.UpdaterClientEventBus;
import com.mercury.platform.core.update.bus.handlers.UpdateEventHandler;
import com.mercury.platform.core.update.core.handlers.ClientHandler;
import com.mercury.platform.core.update.core.holder.ApplicationHolder;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.AlertEvent;
import com.mercury.platform.shared.events.custom.CheckOutPatchNotes;
import com.mercury.platform.shared.events.custom.RequestPatchNotesEvent;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class UpdaterClient {

    private static final Logger LOGGER = LogManager.getLogger(UpdaterClient.class.getSimpleName());

    private final String host;
    private final int port;
    private Bootstrap bootstrap;
    private EventLoopGroup group;
    private Channel channel;
    private InetSocketAddress serverAddress;
    private  ChannelPoolMap<InetSocketAddress,FixedChannelPool> poolMap;
    private boolean connectionEstablished;
    public UpdaterClient(String host, String mercuryVersion, int port) {
        this.host = host;
        this.port = port;
        String version = mercuryVersion.replace(".", "0");
        ApplicationHolder.getInstance().setVersion(Integer.valueOf(version));
        connectionEstablished = false;

        EventRouter.INSTANCE.registerHandler(RequestPatchNotesEvent.class, event -> {
            if(!connectionEstablished){
                EventRouter.INSTANCE.fireEvent(new AlertEvent("Server is currently down, please try again later."));
            }else {
                EventRouter.INSTANCE.fireEvent(new CheckOutPatchNotes());
            }
        });
    }

    public void start(){
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        serverAddress = new InetSocketAddress(host, port);
        bootstrap.group(group).channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE,true);

        poolMap = new AbstractChannelPoolMap<InetSocketAddress, FixedChannelPool>() {
                    @Override
                    protected FixedChannelPool newPool(InetSocketAddress key) {
                        return new FixedChannelPool(bootstrap.remoteAddress(key), new AbstractChannelPoolHandler() {
                            @Override
                            public void channelCreated(Channel ch) throws Exception {
                                ChannelPipeline pipeline = ch.pipeline();
                                pipeline.addLast(new ObjectEncoder());
                                ClassLoader classLoader = this.getClass().getClassLoader();
                                ClassResolver classResolver = ClassResolvers.weakCachingResolver(classLoader);
                                pipeline.addLast(new ObjectDecoder(classResolver));
                                pipeline.addLast(new ClientHandler());
                            }
                        },1,1);
                    }
                };
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        doConnect();
    }
    private void doConnect(){
        try {
            SimpleChannelPool pool = poolMap.get(serverAddress);
            Future<Channel> future = pool.acquire();
            future.addListener(new GenericFutureListener<Future<? super Channel>>() {
                @Override
                public void operationComplete(Future<? super Channel> future) throws Exception {
                    if (!future.isSuccess()) {
                        connectionEstablished = false;
                        group.schedule(() -> {
                            doConnect();
                        },5,TimeUnit.MINUTES);
                    } else {
                        channel = (Channel)future.getNow();
                        connectionEstablished = true;
                        addCLoseDetectListener(channel);
                        LOGGER.debug("Connection established");
                    }
                }
                private void addCLoseDetectListener(Channel _channel) {
                    _channel.closeFuture().addListener((ChannelFutureListener) future -> {
                        LOGGER.debug("Connection lost");
                        pool.release(channel);
                        connectionEstablished = false;
                        doConnect();
                    });
                }
            });
//            future.addListener(new ChannelFutureListener() {
//                @Override
//                public void operationComplete(ChannelFuture future) throws Exception {
//                    if (!future.isSuccess()) {
//                        connectionEstablished = false;
//                        future.channel().close();
//                        future.channel().eventLoop().schedule(() -> {
//                            bootstrap.connect().addListener(this);
//                        },10,TimeUnit.MINUTES);
//                    } else {
//                        channel = future.channel();
//                        connectionEstablished = true;
//                        addCLoseDetectListener(channel);
//                        LOGGER.debug("Connection established");
//                    }
//                }
//
//                private void addCLoseDetectListener(Channel _channel) {
//                    _channel.closeFuture().addListener((ChannelFutureListener) future -> {
//                        LOGGER.debug("Connection lost");
//                        connectionEstablished = false;
//                        doConnect();
//                    });
//                }
//            });
        }catch (Exception e){
            LOGGER.error("Error while connection to the server: ",e);
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
