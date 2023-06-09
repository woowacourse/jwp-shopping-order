package cart.order.application.dto;

import cart.order.ui.request.OrderCartItemRequest;

public class OrderCartItemDto {

    private Long cartItemId;
    private String orderCartItemName;
    private int orderCartItemPrice;
    private String orderCartItemImageUrl;

    private OrderCartItemDto(final Long cartItemId, final String orderCartItemName, final int orderCartItemPrice, final String orderCartItemImageUrl) {
        this.cartItemId = cartItemId;
        this.orderCartItemName = orderCartItemName;
        this.orderCartItemPrice = orderCartItemPrice;
        this.orderCartItemImageUrl = orderCartItemImageUrl;
    }

    public static OrderCartItemDto from(final OrderCartItemRequest request) {
        return new OrderCartItemDto(request.getCartItemId(),
                request.getOrderCartItemName(),
                request.getOrderCartItemPrice(),
                request.getOrderCartItemImageUrl()
        );
    }

    public static OrderCartItemDto of(final Long cartItemId, final String orderCartItemName, final int orderCartItemPrice, final String orderCartItemImageUrl) {
        return new OrderCartItemDto(cartItemId, orderCartItemName, orderCartItemPrice, orderCartItemImageUrl);
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public String getOrderCartItemName() {
        return orderCartItemName;
    }

    public int getOrderCartItemPrice() {
        return orderCartItemPrice;
    }

    public String getOrderCartItemImageUrl() {
        return orderCartItemImageUrl;
    }
}
