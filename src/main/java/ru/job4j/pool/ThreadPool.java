package ru.job4j.pool;

import java.util.LinkedList;
import java.util.List;

import ru.job4j.buffer.SimpleBlockingQueue;

public class ThreadPool {
    private final List<Thread> threads = new LinkedList<>();
    private final SimpleBlockingQueue<Runnable> tasks;

    int size = Runtime.getRuntime().availableProcessors();

    public ThreadPool(final int total) {
        tasks = new SimpleBlockingQueue<>(total);
        for (int i = 0; i < size; i++) {
            threads.add(
                    new Thread(
                            () -> {
                                while (!Thread.currentThread().isInterrupted()) {
                                    try {
                                        Runnable task = tasks.poll();
                                        task.run();
                                    } catch (InterruptedException ex) {
                                        Thread.currentThread().interrupt();
                                    }
                                }
                            }
                    )
            );
        }
        for (Thread thread : threads) {
            thread.start();
        }
    }

    public void work(Runnable task) {
        try {
            tasks.offer(task);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public void shutdown() {
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }
}
