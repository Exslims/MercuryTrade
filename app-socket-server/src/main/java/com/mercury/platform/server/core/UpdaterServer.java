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
    private ChannelFuture sync;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    public UpdaterServer(int port) {
        this(port, DEFAULT_THREADS_COUNT);
    }

    public UpdaterServer(int port, int nThreads) {
        this.port = port;
        this.nThreads = nThreads;
    }

    public void run() {
        LOGGER.info("Starting server on {} port", port);
        LOGGER.info("Event loop group threads count = {}", nThreads);
        this.bossGroup = new NioEventLoopGroup();
        this.workerGroup = new NioEventLoopGroup(nThreads);

        try {
            LOGGER.info("Initializing server bootstrap");
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ServerChannelInitializer())
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            LOGGER.info("Server started");
            this.sync = serverBootstrap.bind(port).sync();
            sync.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            LOGGER.error(e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }


    public void shutdown() {
        LOGGER.info("Shutting down updater server...");

        LOGGER.info("Shutting down worker group = {}", workerGroup.toString());
        workerGroup.shutdownGracefully();

        LOGGER.info("Shutting down boss group = {}", bossGroup);
        bossGroup.shutdownGracefully();
        try {
            LOGGER.info("Shutting down channel");
            this.sync.channel().close();
        } catch (Exception e) {
            LOGGER.info(e);
        }
        LOGGER.info("Server is stopped");
    }

}
