package com.Blogs.Backend.BlogsBackend.Security.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync // Enable asynchronous processing
public class ThreadPoolConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4); // Minimum number of threads
        executor.setMaxPoolSize(8); // Maximum number of threads
        executor.setQueueCapacity(25); // Queue size for holding tasks
        executor.setThreadNamePrefix("JwtExecutor-");
        executor.initialize();
        return executor;
    }
}
