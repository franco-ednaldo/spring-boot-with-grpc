package com.example.grpc.service.exception;

import io.grpc.Status;

public abstract class BaseBusinessException extends RuntimeException {
    public BaseBusinessException(String message) {
        super(message);
    }

    public abstract String getErrorMessage();

    public abstract Status getStatusCode();
}
