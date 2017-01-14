package com.mercury.platform.server.init;

import com.mercury.platform.server.handlers.ClientHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Frost on 14.01.2017.
 */
public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    private static final Logger LOGGER = LogManager.getLogger(ClientChannelInitializer.class);

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        LOGGER.info("Initializing channel, server address = {}" , socketChannel.remoteAddress().getHostName());
        socketChannel.pipeline().addLast(new ObjectEncoder());
        ClassLoader classLoader = this.getClass().getClassLoader();
        ClassResolver classResolver = ClassResolvers.weakCachingResolver(classLoader);
        socketChannel.pipeline().addLast(new ObjectDecoder(classResolver));
        socketChannel.pipeline().addLast(new ClientHandler());
        LOGGER.info("Client channel was initialized");
    }
}
