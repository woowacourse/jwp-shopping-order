package cart.entity;

public class OrderEntity {
    
    private final long Id;
    private final long memberId;
    private final int price;

    public OrderEntity(final long id, final long memberId, final int price) {
        Id = id;
        this.memberId = memberId;
        this.price = price;
    }

    public long getId() {
        return Id;
    }

    public long getMemberId() {
        return memberId;
    }

    public int getPrice() {
        return price;
    }
}
