package com.zenith.batchlistener.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Sanjay Kumar Senthilvel on 06-07-2025.
 */
@Configuration("listenerConfigs")
public class ApplicationProperties {

    @Value("${batch.listener.start}")
    private boolean startListener;

    @Value("${batch.listener.bootstrap.servers}")
    private String bootstrapServers;

    @Value("${batch.listener.topic.name}")
    private String topicName;

    @Value("${batch.listener.consumer.group.name}")
    private String consumerGroup;

    @Value("${batch.listener.max.poll.interval}")
    private int maxPollInterval;

    @Value("${batch.listener.max.poll.records}")
    private int maxPollRecords;

    @Value("${batch.listener.error.log.level}")
    private String errorLogLevel;

    @Value("${batch.listener.error.retry.backoff}")
    private long errorBackoff;

    @Value("${batch.listener.error.retry.attempts}")
    private long errorRetryAttempts;

    @Value("${batch.listener.executor.core.size}")
    private int corePoolSize;

    @Value("${batch.listener.executor.max.size}")
    private int maxPoolSize;

    @Value("${batch.listener.executor.queue.size}")
    private int queueCapacity;

    public boolean isStartListener() {
        return startListener;
    }

    public void setStartListener(boolean startListener) {
        this.startListener = startListener;
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getConsumerGroup() {
        return consumerGroup;
    }

    public void setConsumerGroup(String consumerGroup) {
        this.consumerGroup = consumerGroup;
    }

    public int getMaxPollInterval() {
        return maxPollInterval;
    }

    public void setMaxPollInterval(int maxPollInterval) {
        this.maxPollInterval = maxPollInterval;
    }

    public int getMaxPollRecords() {
        return maxPollRecords;
    }

    public void setMaxPollRecords(int maxPollRecords) {
        this.maxPollRecords = maxPollRecords;
    }

    public String getErrorLogLevel() {
        return errorLogLevel;
    }

    public void setErrorLogLevel(String errorLogLevel) {
        this.errorLogLevel = errorLogLevel;
    }

    public long getErrorBackoff() {
        return errorBackoff;
    }

    public void setErrorBackoff(long errorBackoff) {
        this.errorBackoff = errorBackoff;
    }

    public long getErrorRetryAttempts() {
        return errorRetryAttempts;
    }

    public void setErrorRetryAttempts(long errorRetryAttempts) {
        this.errorRetryAttempts = errorRetryAttempts;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }
}
