package cart.dao.entity;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberCouponEntity that = (MemberCouponEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
