package cart.application;

import cart.dao.CouponDao;
import cart.domain.*;
import cart.dto.CouponResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CouponService {

    public static final int COUPON_ADD_MONEY = 100000;
    private final CouponDao couponDao;

    public CouponService(CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    @Transactional
    public Money apply(Order order, Long couponId) {
        Coupon coupon = couponDao.findById(couponId);
        Money discounting = order.apply(coupon);
        couponDao.delete(couponId);
        return discounting;
    }

    public List<CouponResponse> findByMember(Member member) {
        List<Coupon> coupons = couponDao.findByMemberId(member.getId());
        return coupons.stream().map(CouponResponse::of).collect(Collectors.toList());
    }

    public void addCouponDependsOnPay(Member member, Order order) {
        if (!order.isBiggerPrice(Money.from(COUPON_ADD_MONEY))) {
            return;
        }

        Coupon coupon = new FixedDiscountCoupon(member.getId(), "10만원 주문 - 1000원 할인쿠폰", "image.com", 1000);
        couponDao.save(coupon);
    }
}
