package kr.hhplus.be.server.controller;

import kr.hhplus.be.server.controller.spec.OrderApiSpec;
import kr.hhplus.be.server.domain.dto.OrderRequest;
import kr.hhplus.be.server.domain.dto.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class OrderController implements OrderApiSpec {
    @PostMapping("/orders")
    public ResponseEntity<OrderResponse.ProcessOrderDto> createOrder(
            @RequestBody OrderRequest.OrderDto orderDto) {
        return ResponseEntity.ok(new OrderResponse.ProcessOrderDto("주문 완료"));
    }
    @PostMapping("/orders/payments")
    public ResponseEntity<OrderResponse.ProcessPaymentDto> processPayment(
            @RequestBody OrderRequest.ProcessProductsDto processInfo) {
        return ResponseEntity.ok(new OrderResponse.ProcessPaymentDto("결제 완료"));
    }
}
