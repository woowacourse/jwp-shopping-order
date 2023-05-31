package cart.domain;

public class OrderInfo {

    private Long id;
    private final Long orderId;
    private final Product product;
    private final Long quantity;

    public OrderInfo(Long orderId, Product product, Long quantity) {
        this.orderId = orderId;
        this.product = product;
        this.quantity = quantity;
    }

    public OrderInfo(Long id, Long orderId, Product product, Long quantity) {
        this.id = id;
        this.orderId = orderId;
        this.product = product;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Product getProduct() {
        return product;
    }

    public Long getQuantity() {
        return quantity;
    }
}
