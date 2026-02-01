package ru.job4j.buffer;

import java.util.Queue;
import java.util.LinkedList;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

@ThreadSafe
public class SimpleBlockingQueue<T> {

    @GuardedBy("this")
    private final Queue<T> queue = new LinkedList<>();

    private final int total;

    public SimpleBlockingQueue(final int total) {
        this.total = total;
    }

    public void offer(T value) throws InterruptedException {
        synchronized (this) {
            while (queue.size() == total) {
                this.wait();
            }
            queue.offer(value);
            this.notifyAll();
        }
    }

    public T poll() throws InterruptedException {
        synchronized (this) {
            while (queue.isEmpty()) {
                this.wait();
            }
            T result = queue.poll();
            this.notifyAll();
            return result;
        }
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}
