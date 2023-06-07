package cart.persistance.entity;

import cart.domain.product.OrderItem;
import cart.domain.product.Product;

public class OrderItemEntity {

    private Long id;
    private String productName;
    private Integer price;
    private String imageUrl;
    private Integer quantity;
    private Long productId;
    private Long ordersId;

    public OrderItemEntity(
            Long id,
            String productName,
            Integer price,
            String imageUrl,
            Integer quantity,
            Long productId,
            Long ordersId
    ) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.productId = productId;
        this.ordersId = ordersId;
    }

    public static OrderItemEntity of(OrderItem orderItem, Long ordersId) {
        Product product = orderItem.getProduct();
        return new OrderItemEntity(
                orderItem.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                orderItem.getQuantity(),
                product.getId(),
                ordersId
        );
    }

    public OrderItem toDomain() {
        Product product = new Product(productId, productName, price, imageUrl);
        return new OrderItem(id, quantity, product);
    }

    public Long getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getOrdersId() {
        return ordersId;
    }
}
