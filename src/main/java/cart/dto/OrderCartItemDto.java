package cart.dto;

public class OrderCartItemDto {

    private Long cartItemId;
    private String orderCartItemName;
    private int orderCartItemPrice;
    private String orderCartItemImageUrl;

    private OrderCartItemDto() {
    }

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
