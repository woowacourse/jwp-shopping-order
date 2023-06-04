package cart.domain.repository;

import cart.domain.Member;
import cart.domain.coupon.Coupon;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CouponRepository {

    Long save(Member member, Long couponId);

    List<Coupon> findByMemberId(Member member);

    List<Coupon> findAll();
}
