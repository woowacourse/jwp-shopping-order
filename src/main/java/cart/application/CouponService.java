package cart.application;

import cart.dao.CouponDao;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.dto.CouponResponse;
import cart.exception.BusinessException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CouponService {

    private final CouponDao couponDao;

    public CouponService(final CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    public List<CouponResponse> findAllCouponByMember(final Member member) {
        final List<Coupon> coupons = couponDao.findAllByMember(member);
        return coupons.stream()
            .map(this::makeCouponResponse)
            .collect(Collectors.toUnmodifiableList());
    }

    private CouponResponse makeCouponResponse(final Coupon coupon) {
        return new CouponResponse(coupon.getId(), coupon.getName(), coupon.getMinAmount().getValue(),
            coupon.getDiscountAmount().getValue(), coupon.isUsed());
    }

    public void registerCouponToMember(final Long couponId, final Member member) {
        final Coupon coupon = couponDao.findById(couponId)
            .orElseThrow(() -> new BusinessException("존재하지 않는 쿠폰입니다."));
        couponDao.save(coupon, member.getId());
    }
}
