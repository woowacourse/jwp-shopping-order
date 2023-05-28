package cart.coupon.application;

import cart.coupon.Coupon;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CouponRepositoryCollectionImpl implements CouponRepository {
    private final List<Coupon> coupons;

    public CouponRepositoryCollectionImpl(List<Coupon> coupons) {
        this.coupons = List.of(
                new Coupon(1L, 1L, "10% 전체 할인 쿠폰"),
                new Coupon(2L, 1L, "20% 전체 할인 쿠폰"),
                new Coupon(3L, 1L, "배송비 무료 쿠폰")
        );
    }

    @Override
    public List<Coupon> findAllByMemberId(Long memberId) {
        return coupons.stream()
                .filter(coupon -> coupon.getMemberId() == memberId)
                .collect(Collectors.toList());
    }
}
