package kr.hhplus.be.server.controller.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.domain.dto.OrderRequest;
import kr.hhplus.be.server.domain.dto.OrderResponse;
import org.springframework.http.ResponseEntity;

@Tag(name = "주문", description = "주문 관련 API")
public interface OrderApiSpec {
    @Operation(summary = "상품 주문")
    ResponseEntity<OrderResponse.ProcessOrderDto> createOrder(OrderRequest.OrderDto orderDto);
    @Operation(summary = "상품 결제")
    ResponseEntity<OrderResponse.ProcessPaymentDto> processPayment(OrderRequest.ProcessProductsDto processInfo);
}
