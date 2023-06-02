package cart.application;

import cart.dao.CouponDao;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.dto.CouponResponse;
import cart.dto.PossibleCouponResponse;
import cart.exception.BusinessException;
import java.util.ArrayList;
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
        final List<Coupon> coupons = couponDao.findAll();
        final List<CouponResponse> couponResponses = new ArrayList<>();
        for (final Coupon coupon : coupons) {
            checkIsPublished(member, couponResponses, coupon);
        }
        return couponResponses;
    }

    private void checkIsPublished(final Member member, final List<CouponResponse> couponResponses,
        final Coupon coupon) {
        if (couponDao.exists(coupon, member.getId())) {
            couponResponses.add(makeCouponResponse(coupon, true));
            return;
        }
        couponResponses.add(makeCouponResponse(coupon, false));
    }

    private CouponResponse makeCouponResponse(final Coupon coupon, final boolean isPublished) {
        return new CouponResponse(coupon.getId(), coupon.getName(), coupon.getMinAmount().getValue(),
            coupon.getDiscountAmount().getValue(), isPublished);
    }

    public void registerCouponToMember(final Long couponId, final Member member) {
        final Coupon coupon = couponDao.findById(couponId)
            .orElseThrow(() -> new BusinessException("존재하지 않는 쿠폰입니다."));
        couponDao.save(coupon, member.getId());
    }

    public List<PossibleCouponResponse> findPossibleCouponByMember(final Member member) {
        final List<Coupon> coupons = couponDao.findAllByMemberWhereIsNotUsed(member);
        return coupons.stream()
            .map(this::makePossibleCouponResponse)
            .collect(Collectors.toUnmodifiableList());
    }

    private PossibleCouponResponse makePossibleCouponResponse(final Coupon coupon) {
        return new PossibleCouponResponse(coupon.getId(), coupon.getName(), coupon.getMinAmount().getValue());
    }
}
