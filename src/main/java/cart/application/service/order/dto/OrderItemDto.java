package cart.application.service.order.dto;

import cart.domain.order.OrderItems;

import java.util.List;
import java.util.stream.Collectors;

public class OrderItemDto {
    private final Long id;
    private final String productName;
    private final int productPrice;
    private final int paymentPrice;
    private final int productQuantity;
    private final String imageUrl;

    public OrderItemDto(final Long id, final String productName, final int productPrice, final int paymentPrice, final int productQuantity, final String imageUrl) {
        this.id = id;
        this.productName = productName;
        this.productPrice = productPrice;
        this.paymentPrice = paymentPrice;
        this.productQuantity = productQuantity;
        this.imageUrl = imageUrl;
    }

    public static List<OrderItemDto> from(final OrderItems orderItems, final int paymentPrice) {
        return orderItems.getOrderItems().stream()
                .map(orderItem -> new OrderItemDto(
                        orderItem.getId(),
                        orderItem.getProductName(),
                        orderItems.calculateOrderItemsPrice(),
                        paymentPrice,
                        orderItem.getProductQuantity(),
                        orderItem.getProductImage())
                )
                .collect(Collectors.toUnmodifiableList());
    }

    public Long getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public int getPaymentPrice() {
        return paymentPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
