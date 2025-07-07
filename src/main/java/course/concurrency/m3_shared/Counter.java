package course.concurrency.m3_shared;

public class Counter {

    private static int count = 1;
    private static final Object lock = new Object();

    private static void printCount(int c) {
        synchronized (lock) {
            while (true) {
                while (count != c) {
                    try {
                        lock.wait();
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(count);
                count = count % 3 + 1;
                lock.notifyAll();
            }
        }
    }

    public static void first() {
        printCount(1);
    }

    public static void second() {
        printCount(2);
    }

    public static void third() {
        printCount(3);
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(Counter::first);
        Thread t2 = new Thread(Counter::second);
        Thread t3 = new Thread(Counter::third);
        t1.start();
        t2.start();
        t3.start();
    }
}
