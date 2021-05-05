package com.uni.data.analyzer.exception.handlers;

import org.springframework.http.HttpStatus;

public class ApiError {

    private final HttpStatus status;
    private final String error;
    private final long timestamp;

    ApiError(HttpStatus status, String error) {
        this.status = status;
        this.error = error;
        timestamp = System.currentTimeMillis();
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public long getTimestamp() {
        return timestamp;
    }
}