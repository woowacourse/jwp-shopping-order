package com.woowahan.techcourse.order.domain;

public class OrderItem {

    private final Long cartItemId;
    private final int quantity;
    private final Long productId;
    private final int price;
    private final String name;
    private final String imageUrl;

    public OrderItem(Long cartItemId, int quantity, Long productId, int price, String name, String imageUrl) {
        this.cartItemId = cartItemId;
        this.quantity = quantity;
        this.productId = productId;
        this.price = price;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public long calculateOriginalPrice() {
        return (long) price * quantity;
    }
}
