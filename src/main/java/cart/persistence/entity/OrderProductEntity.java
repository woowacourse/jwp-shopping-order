package cart.persistence.entity;

public class OrderProductEntity {
    private final Long id;
    private final String name;
    private final Integer price;
    private final String imageUrl;
    private final Integer quantity;
    private final Long orderId;
    private final Long productId;

    public OrderProductEntity(Long id, String name, Integer price, String imageUrl, Integer quantity, Long orderId, Long productId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.orderId = orderId;
        this.productId = productId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
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

    public Long getOrderId() {
        return orderId;
    }

    public Long getProductId() {
        return productId;
    }
}
