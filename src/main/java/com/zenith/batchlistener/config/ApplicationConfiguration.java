package com.zenith.batchlistener.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Created by Sanjay Kumar Senthilvel on 07-07-2025.
 */
@Configuration
public class ApplicationConfiguration {

    private ApplicationProperties configs;

    @Autowired
    public ApplicationConfiguration(@Qualifier("listenerConfigs") ApplicationProperties configs) {
        this.configs = configs;
    }

    @Bean
    public AsyncTaskExecutor asyncTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(configs.getCorePoolSize());
        threadPoolTaskExecutor.setMaxPoolSize(configs.getMaxPoolSize());
        threadPoolTaskExecutor.setQueueCapacity(configs.getQueueCapacity());
        threadPoolTaskExecutor.setThreadNamePrefix("BATCH-EXEC-");
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        return threadPoolTaskExecutor;
    }
}
