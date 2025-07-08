package com.zenith.batchlistener.auditor;

import com.zenith.batchlistener.constants.MessageConstants;
import com.zenith.batchlistener.exception.BatchProcessingException;
import com.zenith.batchlistener.util.Pair;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.BatchInterceptor;
import org.springframework.kafka.listener.BatchListenerFailedException;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.kafka.listener.RetryListener;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Sanjay Kumar Senthilvel on 06-07-2025.
 */
@Component
public class BatchListenerAuditor implements BatchInterceptor<String, String>, RetryListener, ConsumerRecordRecoverer {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchListenerAuditor.class);

    @Override
    public void accept(ConsumerRecord<?, ?> consumerRecord, Exception e) {
        LOGGER.error(MessageConstants.RETRY_EXHAUSTED, consumerRecord.key());
    }

    @Override
    public ConsumerRecords<String, String> intercept(ConsumerRecords<String, String> records, Consumer<String, String> consumer) {
        return records;
    }

    @Override
    public void success(ConsumerRecords<String, String> records, Consumer<String, String> consumer) {
        auditRecords(records, null);
    }

    @Override
    public void failure(ConsumerRecords<String, String> records, Exception exception, Consumer<String, String> consumer) {
        auditRecords(records, exception);
    }

    private void auditRecords(ConsumerRecords<String, String> records, Exception exception) {
        Map<String, Pair<Long, Long>> offsetInfo = buildOffsetInfo(records);
        if (Objects.isNull(exception)) {
            LOGGER.info(MessageConstants.BATCH_PROCESS_SUCCESS, offsetInfo);
        } else {
            BatchProcessingException batchProcessingException = getBatchProcessingException(exception);
            LOGGER.error(MessageConstants.BATCH_PROCESS_FAILURE, offsetInfo, batchProcessingException.getFailedRecord().key(), getExceptionMessage(exception));
        }
    }

    @Override
    public void failedDelivery(ConsumerRecord<?, ?> record, Exception ex, int deliveryAttempt) {
        LOGGER.error(MessageConstants.RETRYING_RECORD_WITH_ATTEMPT, record.key(), getExceptionMessage(ex), deliveryAttempt);
    }

    private String getExceptionMessage(Exception ex) {
        BatchProcessingException exception = getBatchProcessingException(ex);
        return MessageFormat.format(MessageConstants.BATCH_EXP_MSG, exception.getMessage(), exception.getCause());
    }

    private Map<String, Pair<Long, Long>> buildOffsetInfo(ConsumerRecords<String, String> records) {
        Map<String, List<ConsumerRecord<String, String>>> recordsByPartition = new HashMap<>();
        Map<String, Pair<Long, Long>> offsetInfoByPartition = new HashMap<>();
        records.forEach(record -> recordsByPartition.computeIfAbsent(record.key(), recs -> new ArrayList<>()).add(record));
        for (Map.Entry<String, List<ConsumerRecord<String, String>>> recordsByPartitionEntry : recordsByPartition.entrySet()) {
            LongSummaryStatistics offsetStats = recordsByPartitionEntry.getValue().stream().mapToLong(ConsumerRecord::offset).summaryStatistics();
            offsetInfoByPartition.put(recordsByPartitionEntry.getKey(), new Pair<>(offsetStats.getMin(), offsetStats.getMax()));
        }
        return offsetInfoByPartition;
    }

    private BatchProcessingException getBatchProcessingException(Exception ex) {
        BatchListenerFailedException batchListenerFailedException = (BatchListenerFailedException) ex.getCause();
        return (BatchProcessingException) batchListenerFailedException.getCause();
    }
}
