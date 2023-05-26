package cart.order.infrastructure.persistence.entity;

public class OrderItemEntity {

    private Long id;
    private int quantity;
    private Long productId;
    private String name;
    private int productPrice;
    private String imageUrl;
    private Long orderId;

    OrderItemEntity() {
    }

    public OrderItemEntity(
            Long id,
            int quantity,
            Long productId,
            String name,
            int productPrice,
            String imageUrl,
            Long orderId
    ) {
        this.id = id;
        this.quantity = quantity;
        this.productId = productId;
        this.name = name;
        this.productPrice = productPrice;
        this.imageUrl = imageUrl;
        this.orderId = orderId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getName() {
        return name;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
