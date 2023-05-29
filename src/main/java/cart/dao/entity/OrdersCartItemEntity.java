package cart.dao.entity;

public class OrdersCartItemEntity {
    private final long id;
    private final long ordersId;
    private final long productId;
    private final int quantity;

    public OrdersCartItemEntity(long id, long ordersId, long productId, int quantity) {
        this.id = id;
        this.ordersId = ordersId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public long getOrdersId() {
        return ordersId;
    }

    public long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
