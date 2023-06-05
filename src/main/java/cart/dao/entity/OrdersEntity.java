package cart.dao.entity;

import java.util.Objects;

public class OrdersEntity {
    private final long id;
    private final long memberId;
    private final int price;
    private final boolean confirmState;

    public OrdersEntity(long id, long memberId, int price, boolean confirmState) {
        this.id = id;
        this.memberId = memberId;
        this.price = price;
        this.confirmState = confirmState;
    }

    public long getId() {
        return id;
    }

    public long getMemberId() {
        return memberId;
    }

    public int getPrice() {
        return price;
    }

    public boolean getConfirmState() {
        return confirmState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrdersEntity that = (OrdersEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
