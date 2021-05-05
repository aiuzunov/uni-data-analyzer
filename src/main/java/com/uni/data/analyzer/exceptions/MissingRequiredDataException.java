package com.uni.data.analyzer.exceptions;

public class MissingRequiredDataException extends RuntimeException {

    public MissingRequiredDataException(String message) {
        super(message);
    }
}