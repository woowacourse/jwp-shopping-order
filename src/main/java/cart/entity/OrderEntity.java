package cart.entity;

public class OrderEntity {

    private final Long id;
    private final long memberId;
    private final int totalPrice;
    private final int discountPrice;

    public OrderEntity(final Long id, final long memberId, final int totalPrice, final int discountPrice) {
        this.id = id;
        this.memberId = memberId;
        this.totalPrice = totalPrice;
        this.discountPrice = discountPrice;
    }

    public OrderEntity(final long memberId, final int totalPrice, final int discountPrice) {
        this(null, memberId, totalPrice, discountPrice);
    }

    public static OrderEntity of(final long id, final OrderEntity orderEntity) {
        return new OrderEntity(id, orderEntity.memberId, orderEntity.totalPrice, orderEntity.discountPrice);
    }

    public Long getId() {
        return id;
    }

    public long getMemberId() {
        return memberId;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }
}
