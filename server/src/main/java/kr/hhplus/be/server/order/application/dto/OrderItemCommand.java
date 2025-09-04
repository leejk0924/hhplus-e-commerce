package kr.hhplus.be.server.order.application.dto;

import kr.hhplus.be.server.order.presentation.dto.OrderRequest;

import java.util.List;

public record OrderItemCommand(
        List<ProductCommand> productCommands
) {
    public static OrderItemCommand fromPresentation(List<OrderRequest.OrderItemDto> requestOrderItems) {
        List<ProductCommand> products = requestOrderItems.stream()
                .map(item -> new ProductCommand(item.productId(), item.quantity())).toList();
        return new OrderItemCommand(products);
    }

    public List<ProductCommand> flatProductCommands() {
        return productCommands;
    }

    public List<Long> toProductIds() {
        return productCommands().stream()
                .map(OrderItemCommand.ProductCommand::productId)
                .toList();
    }

    public static OrderItemCommand from(List<ProductCommand> productCommands) {
        return new OrderItemCommand(productCommands);
    }

    public record ProductCommand(
            Long productId,
            Integer quantity
    ){}
}
