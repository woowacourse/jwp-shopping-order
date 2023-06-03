package com.woowahan.techcourse.order.domain;

public class OrderItem {

    private final Long id;
    private final int quantity;
    private final long productId;
    private final int price;
    private final String name;
    private final String imageUrl;

    public OrderItem(Long id, int quantity, long productId, int price, String name, String imageUrl) {
        this.id = id;
        this.quantity = quantity;
        this.productId = productId;
        this.price = price;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public OrderItem(int quantity, long productId, int price, String name, String imageUrl) {
        this(null, quantity, productId, price, name, imageUrl);
    }

    public int getQuantity() {
        return quantity;
    }

    public long getProductId() {
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

    public long getId() {
        return id;
    }
}
