package course.concurrency.m2_async.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
@EnableAsync
public class AsyncClassTest {

    @Autowired
    public ApplicationContext context;

    @Autowired
    @Lazy
    private AsyncClassTest test;

    @Autowired
    @Qualifier("applicationTaskExecutor")
    private ThreadPoolTaskExecutor executor;

    @Async("applicationTaskExecutor")
    public void runAsyncTask() {
        System.out.println("runAsyncTask: " + Thread.currentThread().getName());
        test.internalTask();
    }

    @Async("applicationTaskExecutor")
    public void internalTask() {
        System.out.println("internalTask: " + Thread.currentThread().getName());
    }
}
