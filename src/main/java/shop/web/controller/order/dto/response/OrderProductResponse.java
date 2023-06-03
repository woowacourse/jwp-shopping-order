package shop.web.controller.order.dto.response;

import shop.application.order.dto.OrderItemDto;

import java.util.List;
import java.util.stream.Collectors;

public class OrderProductResponse {
    OrderProductDetailResponse product;
    private Integer quantity;

    private OrderProductResponse() {
    }

    private OrderProductResponse(OrderProductDetailResponse product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public static OrderProductResponse of(OrderItemDto orderItemDto) {
        return new OrderProductResponse(
                OrderProductDetailResponse.of(orderItemDto.getProduct()),
                orderItemDto.getQuantity()
        );
    }

    public static List<OrderProductResponse> of(List<OrderItemDto> orderItemDtos) {
        return orderItemDtos.stream()
                .map(OrderProductResponse::of)
                .collect(Collectors.toList());
    }

    public OrderProductDetailResponse getProduct() {
        return product;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
