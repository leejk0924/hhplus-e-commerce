package kr.hhplus.be.server.order.presentation.controller;

import kr.hhplus.be.server.order.application.dto.OrderItemCommand;
import kr.hhplus.be.server.order.application.dto.PayCommand;
import kr.hhplus.be.server.order.application.facade.OrderFacade;
import kr.hhplus.be.server.order.application.facade.PaymentFacade;
import kr.hhplus.be.server.order.presentation.controller.spec.OrderApiSpec;
import kr.hhplus.be.server.order.presentation.dto.OrderRequest;
import kr.hhplus.be.server.order.presentation.dto.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderController implements OrderApiSpec {
    private final OrderFacade orderFacade;
    private final PaymentFacade paymentFacade;
    @PostMapping("/orders")
    public ResponseEntity<OrderResponse.OrderDto> createOrder(@RequestBody OrderRequest.OrderDto requestOrderDto) {
        OrderItemCommand command = OrderItemCommand.fromPresentation(requestOrderDto.orderItems());
        orderFacade.createOrder(command, requestOrderDto.userId());
        return ResponseEntity.ok(OrderResponse.OrderDto.of("주문이 완료되었습니다."));
    }

    @PostMapping("/orders/payments")
    public ResponseEntity<OrderResponse.PaymentDto> processPayment(@RequestBody OrderRequest.ProcessPaymentDto processInfo) {
        PayCommand payCommand = PayCommand.toPayCommand(processInfo);
        paymentFacade.payProcess(payCommand);
        return ResponseEntity.ok(OrderResponse.PaymentDto.of("결제가 완료되었습니다."));
    }
}
