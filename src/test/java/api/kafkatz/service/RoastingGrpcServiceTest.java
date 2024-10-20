package api.kafkatz.service;

import api.kafkatz.entities.RoastingRecord;
import api.kafkatz.grpc.RoastingProto;
import api.kafkatz.repository.RoastingRecordRepository;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RoastingGrpcServiceTest {

    @Mock
    private RoastingRecordRepository roastingRecordRepository;

    @Mock
    private StreamObserver<RoastingProto.Empty> responseObserver;

    @InjectMocks
    private RoastingGrpcService roastingGrpcService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendRoastingInfo() {
        // Arrange
        RoastingProto.RoastingInfo request = RoastingProto.RoastingInfo.newBuilder()
                .setBrigadeId("123e4567-e89b-12d3-a456-426614174000")
                .setCountry("Colombia")
                .setType("Arabica")
                .setInputWeight(1000)
                .setOutputWeight(800)
                .build();

        // Act
        roastingGrpcService.sendRoastingInfo(request, responseObserver);

        // Assert
        ArgumentCaptor<RoastingRecord> roastingRecordCaptor = ArgumentCaptor.forClass(RoastingRecord.class);
        verify(roastingRecordRepository).save(roastingRecordCaptor.capture());

        RoastingRecord savedRoastingRecord = roastingRecordCaptor.getValue();
        assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), savedRoastingRecord.getBrigadeId());
        assertEquals("Colombia", savedRoastingRecord.getCountry());
        assertEquals("Arabica", savedRoastingRecord.getType());
        assertEquals(1000, savedRoastingRecord.getInputWeight());
        assertEquals(800, savedRoastingRecord.getOutputWeight());
        assertEquals(20.0, savedRoastingRecord.getLossPercent()); // Проверка процента потерь

        verify(responseObserver).onNext(RoastingProto.Empty.newBuilder().build());
        verify(responseObserver).onCompleted();
    }
}

