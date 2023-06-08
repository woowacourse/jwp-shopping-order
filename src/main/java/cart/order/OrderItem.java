package cart.order;

import cart.cartitem.CartItem;

public class OrderItem {
    private final Long productId;
    private final String productName;
    private final Integer originalPrice;
    private final Integer discountPrice;
    private final Integer quantity;
    private final String imgUri;

    private OrderItem(Long productId, String productName, Integer originalPrice, Integer discountPrice, Integer quantity, String imgUri) {
        this.productId = productId;
        this.productName = productName;
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
        this.quantity = quantity;
        this.imgUri = imgUri;
    }

    public static OrderItem from(CartItem cartItem) {
        return new OrderItem(cartItem.getProductId(), cartItem.getName(), cartItem.getOriginalPrice(),
                cartItem.getDiscountPrice(), cartItem.getQuantity(), cartItem.getImageUrl());
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getOriginalPrice() {
        return originalPrice;
    }

    public Integer getDiscountPrice() {
        return discountPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getImgUri() {
        return imgUri;
    }
}
