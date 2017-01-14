package com.mercury.platform.queue;

import com.mercury.platform.queue.message.Message;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by Frost on 14.01.2017.
 */
public class MessageQueue {

    private BlockingQueue<Message> queue;

    public MessageQueue() {
        this.queue = new PriorityBlockingQueue<>();
    }

    public boolean add(Message message) {
        return queue.add(message);
    }

    public boolean offer(Message message) {
        return queue.offer(message);
    }

    public Message remove() {
        return queue.remove();
    }

    public Message poll() {
        return queue.poll();
    }

    public Message peek() {
        return queue.peek();
    }
}
