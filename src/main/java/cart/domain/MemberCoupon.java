package cart.domain;

import cart.domain.vo.Amount;
import cart.exception.BusinessException;

public class MemberCoupon {

    private final Long id;
    private final Long memberId;
    private final Coupon coupon;
    private boolean isUsed;

    public MemberCoupon(final Long memberId, final Coupon coupon) {
        this(null, memberId, coupon, false);
    }

    public MemberCoupon(final Long id, final Long memberId, final Coupon coupon, final boolean isUsed) {
        this.id = id;
        this.memberId = memberId;
        this.coupon = coupon;
        this.isUsed = isUsed;
    }

    public Amount calculateProduct(final Amount productAmount) {
        if (isUsed) {
            throw new BusinessException("이미 사용한 쿠폰입니다.");
        }
        if (productAmount.isLessThan(coupon.getMinAmount())) {
            throw new BusinessException("총 금액은 최소금액보다 커야합니다.");
        }
        return productAmount.minus(coupon.getDiscountAmount());
    }

    public void use() {
        isUsed = true;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public boolean isUsed() {
        return isUsed;
    }
}
