package course.concurrency.m4_tests.bq;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyBlockingQueue<T> {

    private final ArrayList<T> list;
    private final int capacity;

    private static final Lock lock = new ReentrantLock();
    private static final Condition cond = lock.newCondition();

    public MyBlockingQueue(int capacity) {
        this.list = new ArrayList<>(capacity);
        this.capacity = capacity;
    }

    public void enqueue(T value) {
        try {
            lock.lock();
            while (list.size() >= capacity) {
                cond.await();
            }
            list.add(value);
            cond.signalAll();
        } catch (InterruptedException ignored) {
        } finally {
            lock.unlock();
        }
    }

    public T dequeue() {
        try {
            lock.lock();
            while (list.size() == 0) {
                cond.await();
            }
            cond.signalAll();
            return list.remove(0);
        } catch (InterruptedException e) {
            throw new RuntimeException();
        } finally {
            lock.unlock();
        }
    }

    public int getSize() {
        try {
            lock.lock();
            return list.size();
        } finally {
            lock.unlock();
        }
    }

    public int getCapacity() {
        return capacity;
    }
}
