package cart.entity.order;

public class OrderItemHistoryEntity {

    private final Long id;
    private final Long productId;
    private final String productName;
    private final int price;
    private final Long orderTableId;

    public OrderItemHistoryEntity(final Long id, final Long productId, final String productName, final int price, final Long orderTableId) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.orderTableId = orderTableId;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getPrice() {
        return price;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
