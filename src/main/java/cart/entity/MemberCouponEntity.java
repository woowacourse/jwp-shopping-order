package cart.entity;

import java.time.LocalDate;

public class MemberCouponEntity {

    private final Long id;
    private final Long memberId;
    private final Long couponId;
    private final LocalDate expiredDate;

    public MemberCouponEntity(Long memberId, Long couponId, LocalDate expiredDate) {
        this(null, memberId, couponId, expiredDate);
    }

    public MemberCouponEntity(Long id, Long memberId, Long couponId, LocalDate expiredDate) {
        this.id = id;
        this.memberId = memberId;
        this.couponId = couponId;
        this.expiredDate = expiredDate;
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

    public LocalDate getExpiredDate() {
        return expiredDate;
    }
}
