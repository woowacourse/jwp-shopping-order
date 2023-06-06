package cart.domain.repository;

import cart.domain.coupon.Coupon;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CouponRepository {

    Coupon save(Long memberId, Long couponId);

    List<Coupon> findByMemberId(Long memberId);

    List<Coupon> findAll();

    Coupon findAvailableCouponByIdAndMemberId(Long couponId, Long memberId);

    boolean existsById(Long couponId);

    boolean existsByCouponIdAndMemberId(Long couponId, Long memberId);
}
