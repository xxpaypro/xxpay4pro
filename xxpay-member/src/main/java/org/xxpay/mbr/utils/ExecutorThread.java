package org.xxpay.mbr.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
@Component
public class ExecutorThread {

    @Bean("appPushExecutor")
    public Executor appPushExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(30);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("appPushExecutor-");
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy()); //对拒绝task的处理策略
        executor.setKeepAliveSeconds(60);
        executor.initialize();
        return executor;
    }
}
