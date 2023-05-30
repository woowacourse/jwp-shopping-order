package cart.domain.couponissuer;

import cart.domain.Coupon;
import cart.domain.Orders;

// TODO : 쿠폰 지급 방법 고민해보기
public interface CouponIssuer {
    Coupon issueCoupon(Orders orders);
}
