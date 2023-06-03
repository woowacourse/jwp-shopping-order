package shop.application.order.dto;

import shop.application.product.dto.ProductDto;
import shop.domain.order.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderItemDto {
    private final Long id;
    private final ProductDto product;
    private final int quantity;

    private OrderItemDto(Long id, ProductDto product, int quantity) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
    }

    public static OrderItemDto of(OrderItem orderItem) {
        return new OrderItemDto(
                orderItem.getId(),
                ProductDto.of(orderItem.getProduct()),
                orderItem.getQuantity()
        );
    }

    public static List<OrderItemDto> of(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(OrderItemDto::of)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public ProductDto getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}
