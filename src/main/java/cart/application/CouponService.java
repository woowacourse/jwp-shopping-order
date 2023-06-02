package cart.application;

import cart.dao.CouponDao;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.dto.response.CouponResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponService {

    private final CouponDao couponDao;

    public CouponService(final CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    public List<CouponResponse> findMemberCoupon(final Member member) {
        final List<Coupon> couponById = couponDao.findCouponById(member.getId());
        return CouponResponse.of(couponById);
    }
}
