package com.zenith.batchlistener.model;

import com.zenith.batchlistener.exception.BatchProcessingException;

/**
 * Created by Sanjay Kumar Senthilvel on 07-07-2025.
 */
public class ProcessResult {
    private ProcessStatus status;
    private BatchProcessingException exp;

    public ProcessResult(ProcessStatus status, BatchProcessingException exp) {
        this.status = status;
        this.exp = exp;
    }

    public ProcessStatus getStatus() {
        return status;
    }

    public BatchProcessingException getExp() {
        return exp;
    }
}
