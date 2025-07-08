package com.zenith.batchlistener.constants;

/**
 * Created by Sanjay Kumar Senthilvel on 07-07-2025.
 */
public interface MessageConstants {
    String BATCH_EXP_MSG = "Record processing failed. Message: {0}, Cause: {1}";
    String BATCH_PROCESS_FAILURE = "Batch processing failed :: {} :: Failed record: {} :: Cause: {}";
    String BATCH_PROCESS_SUCCESS = "Batch processed successfully :: {}";
    String RETRYING_RECORD_WITH_ATTEMPT = "{} :: Record processing failed: {} :: Retrying attempt: {}";
    String RETRY_EXHAUSTED = "{} :: Record retries exhausted. Committing offset.";


}
