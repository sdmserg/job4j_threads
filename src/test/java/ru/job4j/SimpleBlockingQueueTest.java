package ru.job4j;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class SimpleBlockingQueueTest {

    @Test
    public void whenProducerAdd3ElementsAndConsumerReadAll() throws InterruptedException {
        var queue = new SimpleBlockingQueue<Integer>(3);
        var list = new ArrayList<Integer>();
        Thread producer = new Thread(
                () -> {
                    try {
                        queue.offer(1);
                        queue.offer(2);
                        queue.offer(3);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }
        );
        Thread consumer = new Thread(
                () -> {
                    try {
                        list.add(queue.poll());
                        list.add(queue.poll());
                        list.add(queue.poll());
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }
        );
        producer.start();
        producer.join();
        consumer.start();
        consumer.join();
        assertThat(list).containsExactly(1, 2, 3);
    }

    @Test
    public void whenProducerAdd4ElementsQueueIsFullAndConsumerRead3Elements() throws InterruptedException {
        var queue = new SimpleBlockingQueue<Integer>(3);
        var list = new ArrayList<Integer>();
        Thread producer = new Thread(
                () -> {
                    while (!Thread.currentThread().isInterrupted()) {
                        try {
                            queue.offer(1);
                            queue.offer(2);
                            queue.offer(3);
                            queue.offer(4);
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
        );
        Thread consumer = new Thread(
                () -> {
                    while (!Thread.currentThread().isInterrupted()) {
                        try {
                            while (true) {
                                Integer value = queue.poll();
                                if (value == null) {
                                    break;
                                }
                                list.add(value);
                            }
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
        );
        producer.start();
        Thread.sleep(1000);
        producer.interrupt();
        producer.join();
        consumer.start();
        Thread.sleep(1000);
        consumer.interrupt();
        consumer.join();
        assertThat(list).hasSize(3).containsExactly(1, 2, 3);
    }

    @Test
    public void whenConsumerStartBeforeProducerThenConsumerWaitProducer() throws InterruptedException {
        var queue = new SimpleBlockingQueue<Integer>(3);
        var list = new ArrayList<Integer>();
        Thread producer = new Thread(
                () -> {
                    while (!Thread.currentThread().isInterrupted()) {
                        try {
                            queue.offer(1);
                            Thread.currentThread().interrupt();
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
        );
        Thread consumer = new Thread(
                () -> {
                    while (!Thread.currentThread().isInterrupted()) {
                        try {
                            list.add(queue.poll());
                            Thread.currentThread().interrupt();
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
        );
        consumer.start();
        Thread.sleep(2000);
        producer.start();
        producer.join();
        consumer.join();
        assertThat(list).hasSize(1).containsExactly(1);
    }
}