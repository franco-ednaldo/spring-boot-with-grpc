package com.example.grpc.resource;

import com.example.grpc.HelloReq;
import com.example.grpc.HelloRes;
import com.example.grpc.HelloServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class HelloResource extends HelloServiceGrpc.HelloServiceImplBase {
    @Override
    public void hello(HelloReq request, StreamObserver<HelloRes> responseObserver) {
        var response = HelloRes.newBuilder()
                .setMessage(request.getMessage() + " - passou no servidor")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
