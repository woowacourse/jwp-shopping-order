package cart.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import cart.entity.OrderItemEntity;

public class OrderItemResponse {
    private final Long productId;
    private final String name;
    private final int price;
    private final int quantity;
    private final String imageUrl;

    public OrderItemResponse(final Long productId, final String name, final int price, final int quantity, final String imageUrl) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    public static List<OrderItemResponse> from(final List<OrderItemEntity> orderItemEntities){
        return orderItemEntities.stream()
            .map(orderItemEntity -> new OrderItemResponse(
                orderItemEntity.getProductId(),
                orderItemEntity.getProductName(),
                orderItemEntity.getProductPrice(),
                orderItemEntity.getProductQuantity(),
                orderItemEntity.getProductImageUrl()
            )).collect(Collectors.toList());
    }

    public Long getProductId() {
        return productId;
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
}
