package cart.order.application.dto;

import cart.order.domain.OrderHistory;
import cart.order.ui.request.OrderCartItemRequest;

public class OrderItemDto {

    private final Long id;
    private final OrderHistory orderHistory;
    private final Long productId;
    private final String name;
    private final int price;
    private final String imageUrl;
    private final int quantity;

    public OrderItemDto(final Long id, final OrderHistory orderHistory, final Long productId, final String name, final int price, final String imageUrl, final int quantity) {
        this.id = id;
        this.orderHistory = orderHistory;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public static OrderItemDto of(final OrderHistory orderHistory, final Long productId, final OrderCartItemRequest orderCartItemDto, final int quantity) {
        return new OrderItemDto(
                null,
                orderHistory,
                productId,
                orderCartItemDto.getOrderCartItemName(),
                orderCartItemDto.getOrderCartItemPrice(),
                orderCartItemDto.getOrderCartItemImageUrl(),
                quantity
        );
    }

    public Long getId() {
        return id;
    }

    public OrderHistory getOrderHistory() {
        return orderHistory;
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }
}
