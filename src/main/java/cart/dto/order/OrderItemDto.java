package cart.dto.order;

import cart.domain.order.OrderItem;

public class OrderItemDto {

    private Long id;
    private String name;
    private String imageUrl;
    private int quantity;
    private int discountRate;
    private int discountedPrice;

    public OrderItemDto(
            final Long id,
            final String name,
            final String imageUrl,
            final int quantity,
            final int discountRate,
            final int discountedPrice
    ) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.discountRate = discountRate;
        this.discountedPrice = discountedPrice;
    }

    public OrderItemDto(final OrderItem orderItem) {
        this(
                orderItem.getId(),
                orderItem.getName(),
                orderItem.getImageUrl(),
                orderItem.getQuantity(),
                orderItem.getDiscountRate(),
                orderItem.getItemDiscountedPrice()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
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
