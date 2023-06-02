package cart.ui.order.dto;

import cart.application.service.order.OrderItemDto;

import java.util.List;
import java.util.stream.Collectors;

public class OrderItemResponse {
    private final Long id;
    private final String productName;
    private final Integer productPrice;
    private final Integer paymentPrice;
    private final String createdAt;
    private final Integer productQuantity;
    private final String imageUrl;

    public OrderItemResponse(
            final Long id,
            final String productName,
            final Integer productPrice,
            final Integer paymentPrice,
            final String createdAt,
            final Integer productQuantity,
            final String imageUrl
    ) {
        this.id = id;
        this.productName = productName;
        this.productPrice = productPrice;
        this.paymentPrice = paymentPrice;
        this.createdAt = createdAt;
        this.productQuantity = productQuantity;
        this.imageUrl = imageUrl;
    }

    public static List<OrderItemResponse> from(final List<OrderItemDto> orderItems) {
        return orderItems.stream()
                .map(orderItemDto -> new OrderItemResponse(
                        orderItemDto.getId(),
                        orderItemDto.getProductName(),
                        orderItemDto.getProductPrice(),
                        orderItemDto.getPaymentPrice(),
                        orderItemDto.getCreatedAt(),
                        orderItemDto.getProductQuantity(),
                        orderItemDto.getImageUrl()
                ))
                .collect(Collectors.toUnmodifiableList());
    }

    public Long getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getProductPrice() {
        return productPrice;
    }

    public Integer getPaymentPrice() {
        return paymentPrice;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}


