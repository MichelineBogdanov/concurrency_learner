package course.concurrency.m4_tests.bq;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.MINUTES;
import static org.junit.jupiter.api.Assertions.*;

class MyBlockingQueueTest {

    @Test
    public void elementsShouldBeOrderedOneThread() {
        MyBlockingQueue<Integer> queue = new MyBlockingQueue<>(5);
        for (int i = 0; i < 5; i++) {
            queue.enqueue(i);
        }
        assertEquals(5, queue.getSize());
        for (int i = 0; i < 5; i++) {
            assertEquals(i, queue.dequeue());
        }
    }

    @Test
    public void elementsShouldBeRetrieved() throws InterruptedException {
        int count = 500;
        MyBlockingQueue<Integer> queue = new MyBlockingQueue<>(count);

        ExecutorService service = Executors.newFixedThreadPool(count * 2);
        CountDownLatch latch = new CountDownLatch(1);

        for (int i = 0; i < count; i++) {
            final Integer el = i;
            service.submit(() -> {
                try {
                    latch.await();
                } catch (InterruptedException ignored) {
                }
                queue.enqueue(el);
            });
        }

        ConcurrentLinkedQueue<Integer> resultQueue = new ConcurrentLinkedQueue<>();
        for (int i = 0; i < count; i++) {
            service.submit(() -> {
                try {
                    latch.await();
                } catch (InterruptedException ignored) {
                }
                Integer dequeue = queue.dequeue();
                resultQueue.add(dequeue);
            });
        }

        latch.countDown();
        service.shutdown();
        service.awaitTermination(1, MINUTES);

        for (int i = 0; i < count; i++) {
            assertTrue(resultQueue.contains(i));
        }
    }

}