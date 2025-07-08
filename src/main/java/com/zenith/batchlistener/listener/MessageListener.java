package com.zenith.batchlistener.listener;

import com.zenith.batchlistener.exception.BatchProcessingException;
import com.zenith.batchlistener.model.ProcessResult;
import com.zenith.batchlistener.model.ProcessStatus;
import com.zenith.batchlistener.runner.MDCAwareRunnable;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.BatchListenerFailedException;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

/**
 * Created by Sanjay Kumar Senthilvel on 06-07-2025.
 */
@Component
public class MessageListener {

    private AsyncTaskExecutor executor;

    @Autowired
    public MessageListener(AsyncTaskExecutor executor) {
        this.executor = executor;
    }

    @KafkaListener(topics = "#{@listenerConfigs.topicName}",
            groupId = "#{@listenerConfigs.consumerGroup}",
            containerFactory = "#{@batchListenerContainerFactory}",
            autoStartup = "#{@listenerConfigs.startListener}"
    )
    public void listen(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {
        List<CompletableFuture<ProcessResult>> recordProcessResultsFuture = submitRecordsForProcessing(records);
        validateAndAcknowledgeRecords(recordProcessResultsFuture, ack);
    }

    private void validateAndAcknowledgeRecords(List<CompletableFuture<ProcessResult>> recordProcessResultsFuture, Acknowledgment ack) {
        List<ProcessResult> recordProcessResults = recordProcessResultsFuture.stream().map(CompletableFuture::join).toList();
        for (ProcessResult recordResult : recordProcessResults) {
            if (ProcessStatus.FAILURE == recordResult.getStatus()) {
                BatchProcessingException exp = recordResult.getExp();
                throw new BatchListenerFailedException(exp.getMessage(), exp, exp.getFailedRecord());
            }
        }
        ack.acknowledge();
    }

    private void processRecord(ConsumerRecord<String, String> record) throws Exception {
        // TODO : Processing logic goes here
    }

    private List<CompletableFuture<ProcessResult>> submitRecordsForProcessing(List<ConsumerRecord<String, String>> records) {
        return records
                .stream()
                .map(record -> CompletableFuture.runAsync(MDCAwareRunnable.wrap(buildRecordRunnable(record)), executor).handle(buildRecordResultHandler()))
                .toList();
    }

    private BiFunction<? super Void, Throwable, ProcessResult> buildRecordResultHandler() {
        return (value, exp) -> {
            if (Objects.isNull(exp)) {
                return new ProcessResult(ProcessStatus.SUCCESS, null);
            } else {
                BatchProcessingException batchProcessExp = (BatchProcessingException) exp.getCause();
                return new ProcessResult(ProcessStatus.FAILURE, batchProcessExp);
            }
        };
    }

    private Runnable buildRecordRunnable(ConsumerRecord<String, String> record) {
        return () -> {
            try {
                this.processRecord(record);
            } catch (Exception e) {
                throw new BatchProcessingException(e.getMessage(), e, record);
            }
        };
    }
}
