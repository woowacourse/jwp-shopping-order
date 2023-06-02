package cart.application;

import cart.dao.CouponDao;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.order.Order;
import cart.dto.response.CouponResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponService {

    private static final int COUPON_ISSUE_PRICE = 100_000;
    private final CouponDao couponDao;
    private final int couponId = 1;

    public CouponService(final CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    public List<CouponResponse> findMemberCoupon(final Member member) {
        final List<Coupon> couponById = couponDao.findCouponById(member.getId());
        return CouponResponse.of(couponById);
    }

    public void issueCoupon(final Member member, final Order order) {
        if (order.price() >= COUPON_ISSUE_PRICE) {
            couponDao.issue(member.getId(), couponId);
        }
    }
}
