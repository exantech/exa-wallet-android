package com.exawallet.engine;

import com.exawallet.engine.operations.EngineOperation;
import com.exawallet.utils.HLog;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.max;

public enum Engine {
    ENGINE;

    private static final String TAG = Engine.class.getSimpleName();

    private ScheduledExecutorService mEngine;

    public final void submit(EngineOperation runnable) {
        if (!mEngine.isShutdown() && !mEngine.isTerminated()) {
            mEngine.submit(runnable);
        }
    }

    public final void schedule(EngineOperation runnable, int timeout) {
        if (!mEngine.isShutdown() && !mEngine.isTerminated()) {
            mEngine.schedule(runnable, timeout, TimeUnit.SECONDS);
        }
    }

    public final void restart() {
        if (null != mEngine && !mEngine.isShutdown() && !mEngine.isTerminated()) {
            shutdown();
        }
        mEngine = Executors.newScheduledThreadPool(getThreads());
    }

    private int getThreads() {
        // While ServiceInstrumentManager and others use that crazy loops (3 loops in the SIM),
        // threads count should be at least 3 + 2 = 5; should be:
        //  return max(5, Runtime.getRuntime().availableProcessors() / 2 + 2);
        return max(3, Runtime.getRuntime().availableProcessors() / 2 + 2);
    }

    public void shutdown() {
        mEngine.shutdownNow();
        try {
            mEngine.awaitTermination(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            HLog.error(TAG, "shutdown", e);
        }
    }
}
