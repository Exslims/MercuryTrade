package com.mercury.platform.server.core;

import com.mercury.platform.config.MercuryServerConfig;
import com.mercury.platform.server.init.ServerChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Created by Frost on 05.01.2017.
 */
public class UpdaterServer {

    private static final Logger LOGGER = LogManager.getLogger(UpdaterServer.class);
    private static final int DEFAULT_THREADS_COUNT = MercuryServerConfig.getInstance().getThreadsCount();


    private int port;
    private int nThreads;

    public UpdaterServer(int port) {
        this(port, DEFAULT_THREADS_COUNT);
    }

    public UpdaterServer(int port, int nThreads) {
        this.port = port;
        this.nThreads = nThreads;
    }

    public void run() throws InterruptedException {
        LOGGER.info("Starting server on {} port", port);
        LOGGER.info("Event loop group threads count = {}" , nThreads);
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup(nThreads);
        try {
            LOGGER.info("Initializing server bootstrap");
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ServerChannelInitializer())
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            LOGGER.info("Server started");
            ChannelFuture sync = serverBootstrap.bind(port).sync();
            sync.channel().closeFuture().sync();

        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

}
