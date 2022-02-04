package com.example.grpc.service.exception;

import io.grpc.Status;

public class AlreadyExistisException extends BaseBusinessException {
    private static final String ERROR_MESSAGE = "Produto %s já cadastrado no Sistema.";
    private final String name;

    public AlreadyExistisException(String name) {
        super(String.format(ERROR_MESSAGE, name));
        this.name = name;
    }

    @Override
    public String getErrorMessage() {
        return String.format(ERROR_MESSAGE, name);
    }

    @Override
    public Status getStatusCode() {
        return Status.ALREADY_EXISTS;
    }
}
