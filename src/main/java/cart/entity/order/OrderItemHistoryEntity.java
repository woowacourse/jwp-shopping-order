package cart.entity.order;

public class OrderItemHistoryEntity {

    private final Long id;
    private final Long productId;
    private final String productName;
    private final String imgUrl;
    private final int price;
    private final int quantity;
    private final Long orderTableId;

    public OrderItemHistoryEntity(final Long id, final Long productId, final String productName, final String imgUrl, final int price, final int quantity, final Long orderTableId) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.imgUrl = imgUrl;
        this.price = price;
        this.quantity = quantity;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
