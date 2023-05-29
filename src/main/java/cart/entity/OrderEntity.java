package cart.entity;

public class OrderEntity {

    private final Long id;
    private final Long memberId;
    private final int price;

    public OrderEntity(final Long memberId, final int price) {
        this(null, memberId, price);
    }

    public OrderEntity(final Long id, final Long memberId, final int price) {
        this.id = id;
        this.memberId = memberId;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public int getPrice() {
        return price;
    }
}
