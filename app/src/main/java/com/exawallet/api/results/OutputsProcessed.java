package com.exawallet.api.results;

public class OutputsProcessed {
    int processed;
    int total;

    public boolean isDone() {
        return processed == total;
    }
}