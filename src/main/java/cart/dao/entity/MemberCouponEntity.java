package cart.dao.entity;

public class MemberCouponEntity {
    private final Long id;
    private final Long memberId;
    private final Long couponId;
    private final int quantity;

    public MemberCouponEntity(Long id, Long memberId, Long couponId, int quantity) {
        this.id = id;
        this.memberId = memberId;
        this.couponId = couponId;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getCouponId() {
        return couponId;
    }

    public int getQuantity() {
        return quantity;
    }
}
