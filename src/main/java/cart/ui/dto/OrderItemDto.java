package cart.ui.dto;

import cart.domain.OrderItem;

public class OrderItemDto {

    private Long id;
    private String name;
    private int price;
    private String imageUrl;
    private int quantity;
    private int discountRate;
    private int discountedPrice;

    private OrderItemDto(
            final Long id,
            final String name,
            final int price,
            final String imageUrl,
            final int quantity,
            final int discountRate,
            final int discountedPrice
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.discountRate = discountRate;
        this.discountedPrice = discountedPrice;
    }

    public static OrderItemDto from(final OrderItem orderItem) {
        return new OrderItemDto(
                orderItem.getId(),
                orderItem.getName(),
                orderItem.getPrice(),
                orderItem.getImageUrl(),
                orderItem.getQuantity(),
                orderItem.getDiscountRate(),
                orderItem.calculateDiscountedPrice()
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

    public String getImageUrl() {
        return imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getDiscountRate() {
        return discountRate;
    }

    public int getDiscountedPrice() {
        return discountedPrice;
    }
}
