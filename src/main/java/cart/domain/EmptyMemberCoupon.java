package cart.domain;

import java.time.LocalDateTime;

public class EmptyMemberCoupon implements MemberCoupon {
    @Override
    public Integer calculateDiscount(final Integer totalPrice) {
        return 0;
    }

    @Override
    public MemberCoupon use() {
        return this;
    }

    @Override
    public MemberCoupon cancelUsed() {
        return this;
    }

    @Override
    public void checkOwner(final Member member) {
    }

    @Override
    public void checkExpired() {
    }

    @Override
    public Long getId() {
        throw new UnsupportedOperationException("지원하지 않는 기능입니다.");
    }

    @Override
    public Member getMember() {
        throw new UnsupportedOperationException("지원하지 않는 기능입니다.");
    }

    @Override
    public Coupon getCoupon() {
        throw new UnsupportedOperationException("지원하지 않는 기능입니다.");
    }

    @Override
    public LocalDateTime getExpiredAt() {
        throw new UnsupportedOperationException("지원하지 않는 기능입니다.");
    }

    @Override
    public LocalDateTime getCreatedAt() {
        throw new UnsupportedOperationException("지원하지 않는 기능입니다.");
    }
}
