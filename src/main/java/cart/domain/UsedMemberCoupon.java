package cart.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class UsedMemberCoupon implements MemberCoupon {
    private final Long id;
    private final Coupon coupon;
    private final Member member;
    private final LocalDateTime expiredAt;
    private final LocalDateTime createdAt;

    public UsedMemberCoupon(final Long id, final Coupon coupon, final Member member, final LocalDateTime expiredAt, final LocalDateTime createdAt) {
        this.id = id;
        this.coupon = coupon;
        this.member = member;
        this.expiredAt = expiredAt;
        this.createdAt = createdAt;
    }

    @Override
    public Integer calculateDiscount(final Integer totalPrice) {
        // TODO: 6/4/23 이게 내가 원한 상황이 맞나?
        return coupon.calculateDiscount(totalPrice);
    }

    @Override
    public MemberCoupon use() {
        throw new IllegalArgumentException("이미 사용된 쿠폰입니다.");
    }

    @Override
    public void checkOwner(final Member member) {
        if (!Objects.equals(this.member, member)) {
            throw new IllegalArgumentException("다른 사용자의 쿠폰입니다.");
        }
    }

    @Override
    public void checkExpired() {
        if (LocalDateTime.now().isAfter(expiredAt)) {
            throw new IllegalArgumentException("유효기간이 지난 쿠폰입니다다");
        }
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Coupon getCoupon() {
        return coupon;
    }

    @Override
    public Member getMember() {
        return member;
    }

    @Override
    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final UsedMemberCoupon that = (UsedMemberCoupon) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UsedMemberCoupon{" +
                "id=" + id +
                ", coupon=" + coupon +
                ", member=" + member +
                ", expiredAt=" + expiredAt +
                ", createdAt=" + createdAt +
                '}';
    }
}
