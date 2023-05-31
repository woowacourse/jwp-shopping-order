package cart.entity;

public class OrderEntity {

    private final Long id;
    private final long memberId;
    private final int originalPrice;
    private final int discountPrice;

    public OrderEntity(final Long id, final long memberId, final int originalPrice, final int discountPrice) {
        this.id = id;
        this.memberId = memberId;
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
    }

    public OrderEntity(final long memberId, final int originalPrice, final int discountPrice) {
        this(null, memberId, originalPrice, discountPrice);
    }

    public static OrderEntity of(final long id, final OrderEntity orderEntity) {
        return new OrderEntity(id, orderEntity.memberId, orderEntity.originalPrice, orderEntity.discountPrice);
    }

    public Long getId() {
        return id;
    }

    public long getMemberId() {
        return memberId;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }
}
