package kr.hhplus.be.server.order.application.event;

import kr.hhplus.be.server.order.application.dto.OrderItemEventDto;
import kr.hhplus.be.server.order.application.repository.SalesProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PaymentEventListener {
    private final SalesProductRepository salesProductRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        Map<String, Long> salesData = event.getOrderItems().stream().collect(Collectors.groupingBy(
                item -> String.valueOf(item.productId()),
                Collectors.summingLong(OrderItemEventDto::quantity)
        ));
        salesProductRepository.addSalesQuantity(salesData);
    }
}

