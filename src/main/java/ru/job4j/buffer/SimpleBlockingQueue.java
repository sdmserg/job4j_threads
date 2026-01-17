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

    private int count = 0;

    public SimpleBlockingQueue(final int total) {
        this.total = total;
    }

    public void offer(T value) throws InterruptedException {
        synchronized (this) {
            while (count == total) {
                this.wait();
            }
            queue.offer(value);
            count++;
            this.notify();
        }
    }

    public T poll() throws InterruptedException {
        synchronized (this) {
            while (count == 0) {
                this.wait();
            }
            count--;
            T result = queue.poll();
            this.notify();
            return result;
        }
    }
}
