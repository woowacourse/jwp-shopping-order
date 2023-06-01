package cart.dto;

import cart.domain.CartItem;

public class OrderItemResponse {

    private final Long productId;
    private final String productName;
    private final int quantity;
    private final int price;
    private final String imageUrl;

    public OrderItemResponse(final Long productId, final String productName, final int quantity, final int price, final String imageUrl) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static OrderItemResponse of(final CartItem cartItem) {
        return new OrderItemResponse(
                cartItem.getProduct().getId(),
                cartItem.getProduct().getName(),
                cartItem.getQuantity(),
                cartItem.getProduct().getPrice(),
                cartItem.getProduct().getImageUrl()
        );
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
