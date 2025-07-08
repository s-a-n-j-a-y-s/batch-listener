package com.zenith.batchlistener.exception;

import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * Created by Sanjay Kumar Senthilvel on 07-07-2025.
 */
public class BatchProcessingException extends RuntimeException {

    private final ConsumerRecord<String, String> failedRecord;

    public BatchProcessingException(String message, Throwable cause, ConsumerRecord<String, String> failedRecord) {
        super(message, cause);
        this.failedRecord = failedRecord;
    }

    public ConsumerRecord<String, String> getFailedRecord() {
        return failedRecord;
    }
}
