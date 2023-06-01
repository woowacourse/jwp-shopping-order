package cart.dto;

public class OrderCartItemDto {

    private final Long cartItemId;
    private final String orderCartItemName;
    private final int orderCartItemPrice;
    private final String orderCartItemImageUrl;

    public OrderCartItemDto(final Long cartItemId, final String orderCartItemName,
                            final int orderCartItemPrice, final String orderCartItemImageUrl) {
        this.cartItemId = cartItemId;
        this.orderCartItemName = orderCartItemName;
        this.orderCartItemPrice = orderCartItemPrice;
        this.orderCartItemImageUrl = orderCartItemImageUrl;
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
