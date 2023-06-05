package cart.dao.entity;

import java.util.Objects;

public class OrdersCartItemEntity {
    private final long id;
    private final long ordersId;
    private final long productId;
    private final int price;
    private final int quantity;

    public OrdersCartItemEntity(long id, long ordersId, long productId, int price, int quantity) {
        this.id = id;
        this.ordersId = ordersId;
        this.productId = productId;
        this.price = price;
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

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrdersCartItemEntity that = (OrdersCartItemEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
