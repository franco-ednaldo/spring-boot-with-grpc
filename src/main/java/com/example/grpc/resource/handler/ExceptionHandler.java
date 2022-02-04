package com.example.grpc.resource.handler;

import com.example.grpc.service.exception.BaseBusinessException;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@GrpcAdvice
public class ExceptionHandler {
    @GrpcExceptionHandler(BaseBusinessException.class)
    public StatusRuntimeException handleBusinessException(BaseBusinessException ex) {
        return ex.getStatusCode()
                .withCause(ex.getCause())
                .withDescription(ex.getErrorMessage())
                .asRuntimeException();
    }
}
