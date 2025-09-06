package kr.hhplus.be.server.order.presentation.controller.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.order.presentation.dto.OrderRequest;
import kr.hhplus.be.server.order.presentation.dto.OrderResponse;
import org.springframework.http.ResponseEntity;

@Tag(name = "주문", description = "주문 관련 API")
public interface OrderApiSpec {
    @Operation(summary = "상품 주문")
    ResponseEntity<OrderResponse.OrderDto> createOrder(OrderRequest.OrderDto orderDto);
    @Operation(summary = "상품 결제")
    ResponseEntity<OrderResponse.PaymentDto> processPayment(OrderRequest.ProcessPaymentDto processInfo);
}
