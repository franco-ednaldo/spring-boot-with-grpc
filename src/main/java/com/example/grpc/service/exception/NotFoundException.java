package com.example.grpc.service.exception;

import io.grpc.Status;

public class NotFoundException extends BaseBusinessException {
    private static final String ERROR_MESSAGE = "Produto com ID %s não encontrado.";
    private final Long id;

    public NotFoundException(Long id) {
        super(String.format(ERROR_MESSAGE, id));
        this.id = id;
    }

    @Override
    public String getErrorMessage() {
        return String.format(ERROR_MESSAGE, id);
    }

    @Override
    public Status getStatusCode() {
        return Status.ALREADY_EXISTS;
    }
}
