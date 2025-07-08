package com.zenith.batchlistener.config;

import com.zenith.batchlistener.auditor.BatchListenerAuditor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sanjay Kumar Senthilvel on 06-07-2025.
 */
@Configuration
public class KafkaConfiguration {
    private ApplicationProperties configs;

    private BatchListenerAuditor auditor;

    @Autowired
    public KafkaConfiguration(@Qualifier("listenerConfigs") ApplicationProperties configs,
                              BatchListenerAuditor auditor) {
        this.configs = configs;
        this.auditor = auditor;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, configs.getBootstrapServers());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, configs.getConsumerGroup());
        properties.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, configs.getMaxPollInterval());
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, configs.getMaxPollRecords());
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        return new DefaultKafkaConsumerFactory<>(properties);
    }

    @Bean
    public CommonErrorHandler commonErrorHandler() {
        FixedBackOff backOff = new FixedBackOff(500, 2);
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(auditor, backOff);
        errorHandler.setSeekAfterError(true);
        errorHandler.setCommitRecovered(true);
        errorHandler.setLogLevel(KafkaException.Level.valueOf(configs.getErrorLogLevel()));
        errorHandler.setRetryListeners(auditor);
        return errorHandler;
    }

    @Bean
    @DependsOn({"consumerFactory", "commonErrorHandler"})
    public ConcurrentKafkaListenerContainerFactory<String, String> batchListenerContainerFactory(ConsumerFactory<String, String> consumerFactory,
                                                                                                 CommonErrorHandler commonErrorHandler) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setBatchListener(true);
        factory.setConcurrency(1);
        factory.setBatchInterceptor(auditor);
        factory.setCommonErrorHandler(commonErrorHandler);
        factory.setConsumerFactory(consumerFactory);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }
}
