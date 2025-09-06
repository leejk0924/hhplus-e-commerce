package kr.hhplus.be.server.order.application.event;

import kr.hhplus.be.server.order.application.dto.OrderItemEventDto;
import kr.hhplus.be.server.order.application.repository.SalesProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PaymentEventListenerTest {
    @Mock
    private SalesProductRepository salesProductRepository;
    private PaymentEventListener sut;

    @BeforeEach
    void setUp() {
        sut = new PaymentEventListener(salesProductRepository);
    }

    @Test
    void 이벤트_발행되면_제품수량_집계() throws Exception {
        // Given
        var items = List.of(
                new OrderItemEventDto(1L, 2),
                new OrderItemEventDto(2L, 3),
                new OrderItemEventDto(3L, 4)
        );
        Map<String, Long> expect = Map.of(
                "1", 2L,
                "2", 3L,
                "3", 4L
        );
        var event = new PaymentCompletedEvent(1L, items, false);

        // When
        sut.handlePaymentCompleted(event);

        // Then
        verify(salesProductRepository).addSalesQuantity(expect);
    }
}