package com.mercury.platform.core.update.core;

import com.mercury.platform.core.update.core.handlers.ClientHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    private static final Logger LOGGER = LogManager.getLogger(ClientChannelInitializer.class.getSimpleName());

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(new ObjectEncoder());
        ClassLoader classLoader = this.getClass().getClassLoader();
        ClassResolver classResolver = ClassResolvers.weakCachingResolver(classLoader);
        socketChannel.pipeline().addLast(new ObjectDecoder(classResolver));
        socketChannel.pipeline().addLast(new ClientHandler());
    }

}
