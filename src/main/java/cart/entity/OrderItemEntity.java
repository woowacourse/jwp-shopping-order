package cart.entity;

import cart.domain.OrderItem;
import cart.domain.Product;

public class OrderItemEntity {

    private final Long id;
    private final Long orderId;
    private final Long productId;
    private final String productName;
    private final int productPrice;
    private final String productImageUrl;
    private final int quantity;

    public OrderItemEntity(
            Long id,
            Long orderId,
            Long productId,
            String productName,
            int productPrice,
            String productImageUrl,
            int quantity
    ) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImageUrl = productImageUrl;
        this.quantity = quantity;
    }

    public OrderItemEntity(
            Long orderId,
            Long productId,
            String productName,
            int productPrice,
            String productImageUrl,
            int quantity
    ) {
        this(null, orderId, productId, productName, productPrice, productImageUrl, quantity);
    }

    public static OrderItemEntity of(Long orderId, OrderItem orderItem) {
        Product product = orderItem.getProduct();

        return new OrderItemEntity(
                orderId,
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                orderItem.getQuantity()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public int getQuantity() {
        return quantity;
    }
}
