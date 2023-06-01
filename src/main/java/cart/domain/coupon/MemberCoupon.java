package cart.domain.coupon;

import cart.domain.Member;
import cart.domain.cart.CartItems;
import cart.exception.forbidden.ForbiddenException;
import java.time.LocalDateTime;
import java.util.Objects;

public class MemberCoupon {

    private final Long id;
    private final Coupon coupon;
    private final Member member;
    private final boolean isUsed;
    private final LocalDateTime expiredAt;
    private final LocalDateTime createdAt;

    public MemberCoupon(final Coupon coupon, final LocalDateTime expiredAt, final Member member) {
        this(null, coupon, member, false, expiredAt, null);
    }

    public MemberCoupon(final Long id, final Coupon coupon, final Member member, final boolean isUsed,
            final LocalDateTime expiredAt,
            final LocalDateTime createdAt) {
        this.id = id;
        this.coupon = coupon;
        this.member = member;
        this.isUsed = isUsed;
        this.expiredAt = expiredAt;
        this.createdAt = createdAt;
    }

    public boolean isApplicable(final CartItems cartItems) {
        if (isUsed || !checkValidPeriod()) {
            return false;
        }
        return coupon.isApplicable(cartItems);
    }

    private boolean checkValidPeriod() {
        return expiredAt.isAfter(LocalDateTime.now());
    }

    public void checkOwner(final Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new ForbiddenException();
        }
    }

    public int getDiscountPrice(final CartItems cartItems) {
        return coupon.getDiscountPrice(cartItems);
    }

    public Long getId() {
        return id;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
