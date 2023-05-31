package cart.dao.entity;

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
}
