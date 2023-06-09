package cart.dto.order;

import cart.entity.OrderCartItemEntity;

public class OrderItemHistoryResponse {
    private final Long id;
    private final String name;
    private final int quantity;
    private final String imageUrl;
    private final int totalPrice;
    private final int totalDiscountPrice;

    public OrderItemHistoryResponse(Long id, String name, int quantity, String imageUrl,
                                    int totalPrice, int totalDiscountPrice) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.totalPrice = totalPrice;
        this.totalDiscountPrice = totalDiscountPrice;
    }

    public static OrderItemHistoryResponse from(OrderCartItemEntity orderCartItemEntity) {
        return new OrderItemHistoryResponse(
                orderCartItemEntity.getId(),
                orderCartItemEntity.getName(),
                orderCartItemEntity.getQuantity(),
                orderCartItemEntity.getImageUrl(),
                orderCartItemEntity.getTotalPrice(),
                orderCartItemEntity.getTotalDiscountPrice()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
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
