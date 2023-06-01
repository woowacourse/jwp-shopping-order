package cart.application;

import cart.dao.CouponDao;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.Money;
import cart.domain.Order;
import cart.dto.CouponResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CouponService {

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
}
