package kr.hhplus.be.server.common.redis.key;

import kr.hhplus.be.server.common.redis.LockKeyGenerator;
import kr.hhplus.be.server.order.application.dto.PayCommand;
import kr.hhplus.be.server.order.application.service.OrderItemService;
import kr.hhplus.be.server.order.application.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OrderItemLockKeyGenerator implements LockKeyGenerator {
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    @Override
    @Transactional
    public String[] generateKey(MethodSignature signature, Object[] args) {
        var payCommand = (PayCommand) args[0];
        var orderItemIds = orderService.searchOrder(payCommand.orderId())
                .toOrderItemIds();
        var productIds = orderItemService.searchProductIdsByOrderItemIds(orderItemIds);

        return productIds.stream()
                .map(id -> "LOCK:PRODUCT:" + id)
                .sorted()
                .toArray(String[]::new);
    }
}
