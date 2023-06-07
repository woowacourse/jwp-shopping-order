package cart.application;

import cart.dao.CouponDao;
import cart.domain.Member;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.CouponIssuePolicy;
import cart.domain.order.Order;
import cart.dto.response.CouponResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CouponService {
    private final CouponDao couponDao;

    public CouponService(final CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    @Transactional(readOnly = true)
    public List<CouponResponse> findMemberCoupon(final Member member) {
        final List<Coupon> couponById = couponDao.findCouponById(member.getId());
        return CouponResponse.of(couponById);
    }

    public void issueCoupon(final Member member, final Order order) {
        final Optional<Coupon> coupon = CouponIssuePolicy.of(member)
                .issueCoupon(member, order);
        if (coupon.isPresent()) {
            couponDao.issue(member, coupon.get());
        }
    }
}
