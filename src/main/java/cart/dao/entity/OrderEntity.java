package cart.dao.entity;

public class OrderEntity {

    private final Long id;
    private final int price;
    private final Long couponId;
    private final Long memberId;

    public OrderEntity(int price, Long couponId, Long memberId) {
        this(null, price, couponId, memberId);
    }

    public OrderEntity(Long id, int price, Long couponId, Long memberId) {
        this.id = id;
        this.price = price;
        this.couponId = couponId;
        this.memberId = memberId;
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
