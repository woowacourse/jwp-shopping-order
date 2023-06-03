package cart.application.service.order.dto;

import cart.domain.order.OrderItem;

public class OrderItemDto {
    private final Long id;
    private final String productName;
    private final int productPrice;
    private final int productQuantity;
    private final String imageUrl;

    public OrderItemDto(final Long id, final String productName, final int productPrice,
                        final int productQuantity, final String imageUrl) {
        this.id = id;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.imageUrl = imageUrl;
    }

    public static OrderItemDto of(final OrderItem orderItem) {
        return new OrderItemDto(orderItem.getId(),
                orderItem.getProductName(),
                orderItem.getProductPrice(),
                orderItem.getProductQuantity(),
                orderItem.getProductImage()
        );
    }

    public Long getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}
