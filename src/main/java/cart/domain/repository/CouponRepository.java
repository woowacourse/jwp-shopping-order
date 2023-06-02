package cart.domain.repository;

import cart.domain.Member;
import cart.domain.coupon.Coupon;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
public interface CouponRepository {

    Long publishUserCoupon(Member member, Long couponId);

    List<Coupon> getUserCoupon(Member member);

    List<Coupon> findAllCoupons();
}
