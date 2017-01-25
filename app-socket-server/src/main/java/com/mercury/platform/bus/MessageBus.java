package com.mercury.platform.bus;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.mercury.platform.bus.disruptor.events.UserConnectedEvent;
import com.mercury.platform.bus.disruptor.events.UserConnectedEventProducer;
import com.mercury.platform.bus.disruptor.handlers.UserConnectedEventHandler;
import com.mercury.platform.config.MercuryServerConfig;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Frost on 25.01.2017.
 */
public class MessageBus {

    private RingBuffer<UserConnectedEvent> ringBuffer;
    private Disruptor<UserConnectedEvent> disruptor;
    private ExecutorService executor;
    private static final MessageBus INSTANCE = new MessageBus();


    public static MessageBus getInstance() {
        return INSTANCE;
    }

    private MessageBus() {
        this.executor = Executors.newCachedThreadPool();
        int bufferSize = MercuryServerConfig.getInstance().disruptorBufferSize();
        this.disruptor = new Disruptor<>(UserConnectedEvent::new, bufferSize, executor);
        this.ringBuffer = disruptor.getRingBuffer();
        this.disruptor.handleEventsWith(new UserConnectedEventHandler());
        this.disruptor.start();
    }

    public void fireUserConnectedEvent(InetSocketAddress address) {
        UserConnectedEventProducer producer = new UserConnectedEventProducer(ringBuffer);
        String hostAddress = address.getAddress().getHostAddress();
        ByteBuffer buffer = ByteBuffer.allocate(hostAddress.length());
        buffer.put(String.valueOf(hostAddress).getBytes());
        producer.onData(buffer);
    }

    public void shutdown() {
        this.disruptor.shutdown();
        this.executor.shutdown();
    }

}
