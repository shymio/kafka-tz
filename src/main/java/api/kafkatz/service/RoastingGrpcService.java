package api.kafkatz.service;

import api.kafkatz.entities.RoastingRecord;
import api.kafkatz.grpc.RoastingProto;
import api.kafkatz.grpc.RoastingServiceGrpc;
import api.kafkatz.repository.RoastingRecordRepository;
import lombok.RequiredArgsConstructor;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class RoastingGrpcService extends RoastingServiceGrpc.RoastingServiceImplBase {

    private final RoastingRecordRepository roastingRecordRepository;

    @Override
    public void sendRoastingInfo(RoastingProto.RoastingInfo request, StreamObserver<RoastingProto.Empty> responseObserver) {

        RoastingRecord record = RoastingRecord.builder()
                .brigadeId(UUID.fromString(request.getBrigadeId()))
                .country(request.getCountry())
                .type(request.getType())
                .inputWeight(request.getInputWeight())
                .outputWeight(request.getOutputWeight())
                .build();

        double lossPercent = ((double) (request.getInputWeight() - request.getOutputWeight()) / request.getInputWeight()) * 100;
        record.setLossPercent(lossPercent);

        roastingRecordRepository.save(record);

        responseObserver.onNext(RoastingProto.Empty.newBuilder().build());
        responseObserver.onCompleted();
    }
}
