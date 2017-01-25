package com.mercury.platform.bus.disruptor.events;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

public class UserConnectedEventProducer
{
    private final RingBuffer<UserConnectedEvent> ringBuffer;

    public UserConnectedEventProducer(RingBuffer<UserConnectedEvent> ringBuffer)
    {
        this.ringBuffer = ringBuffer;
    }

    private static final EventTranslatorOneArg<UserConnectedEvent, ByteBuffer> TRANSLATOR =
            (event, sequence, buffer) -> event.setIpAddress(String.valueOf(new String(buffer.array())));

    public void onData(ByteBuffer buffer)
    {
        ringBuffer.publishEvent(TRANSLATOR, buffer);
    }
}
