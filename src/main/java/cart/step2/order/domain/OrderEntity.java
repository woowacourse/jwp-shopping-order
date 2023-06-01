package cart.step2.order.domain;

public class OrderEntity {

    private final Long id;
    private final int price;
    private final Long couponId;
    private final Long memberId;

    private OrderEntity(final Long id, final int price, final Long couponId, final Long memberId) {
        this.id = id;
        this.price = price;
        this.couponId = couponId;
        this.memberId = memberId;
    }

    public static OrderEntity createNonePkOrder(final int price, final Long couponId, final Long memberId) {
        return new OrderEntity(null, price, couponId, memberId);
    }

    public static OrderEntity of(final Long id, final int price, final Long couponId, final Long memberId) {
        return new OrderEntity(id, price, couponId, memberId);
    }

    public Long getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public Long getCouponId() {
        return couponId;
    }

    public Long getMemberId() {
        return memberId;
    }
}
