package cart.controller.order.dto;

import cart.order.OrderItem;

public class OrderItemResponse {
    private Long id;
    private String name;
    private int price;
    private int quantity;
    private String imageUrl;
    private int totalPrice;
    private int totalDiscountPrice;

    public OrderItemResponse(Long id, String name, int price, int quantity, String imageUrl, int totalPrice, int totalDiscountPrice) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.totalPrice = totalPrice;
        this.totalDiscountPrice = totalDiscountPrice;
    }

    public static OrderItemResponse from(OrderItem orderItem) {
        return new OrderItemResponse(
                orderItem.getProductId(),
                orderItem.getProductName(),
                orderItem.getOriginalPrice(),
                orderItem.getQuantity(),
                orderItem.getImgUri(),
                (orderItem.getOriginalPrice() - orderItem.getDiscountPrice()) * orderItem.getQuantity(),
                orderItem.getDiscountPrice() * orderItem.getQuantity()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getTotalDiscountPrice() {
        return totalDiscountPrice;
    }
}
