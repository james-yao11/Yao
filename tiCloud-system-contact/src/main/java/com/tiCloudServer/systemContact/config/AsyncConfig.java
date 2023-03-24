package com.tiCloudServer.systemContact.config;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Setter
@Configuration
@ConfigurationProperties("async-thread-pool")
public class AsyncConfig {
    /**核心线程数*/
    private int corePoolSize=3;
    /**最大线程数*/
    private int maxPoolSize=5;
    /**线程空闲时间*/
    private int keepAliveTime=30;
    /**队列容量*/
    private int queueCapacity=100;
    /**构建线程工厂*/
    private ThreadFactory threadFactory=new ThreadFactory() {
        private final AtomicInteger at=new AtomicInteger(1000);
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "db-async-thread-"+at.getAndIncrement());
        }
    };
    /**
     */
    @Bean("asyncPoolExecutor")
    public ThreadPoolExecutor newPoolExecutor() {
        //创建阻塞式对象
        BlockingQueue<Runnable> workQueue=
                new ArrayBlockingQueue<>(queueCapacity);
        //创建池对象
        return new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                workQueue,
                threadFactory);
    }
}
