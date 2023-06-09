package cart.persistence.entity;

import java.util.Objects;

public class MemberCouponEntity {
    private final Long id;
    private final Long memberId;
    private final Long couponId;

    public MemberCouponEntity(Long id, Long memberId, Long couponId) {
        this.id = id;
        this.memberId = memberId;
        this.couponId = couponId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberCouponEntity that = (MemberCouponEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
