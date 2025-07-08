package com.zenith.batchlistener.runner;

import org.slf4j.MDC;

import java.util.Map;

/**
 * Created by Sanjay Kumar Senthilvel on 07-07-2025.
 */
public class MDCAwareRunnable implements Runnable {
    private Runnable delegate;
    private Map<String, String> contextMap;

    public MDCAwareRunnable(Runnable delegate) {
        this.delegate = delegate;
        this.contextMap = MDC.getCopyOfContextMap();
    }

    public static Runnable wrap(Runnable runnable) {
        return new MDCAwareRunnable(runnable);
    }

    @Override
    public void run() {
        try {
            MDC.setContextMap(this.contextMap);
            delegate.run();
        } finally {
            MDC.clear();
        }
    }
}
