package cart.application;

import cart.dao.CouponDao;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.vo.Amount;
import cart.exception.BusinessException;
import cart.ui.dto.response.CouponDiscountResponse;
import cart.ui.dto.response.CouponResponse;
import cart.ui.dto.response.PossibleCouponResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
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

    @Transactional
    public void registerCouponToMember(final Long couponId, final Member member) {
        final Coupon coupon = findCoupon(couponId);
        couponDao.save(coupon, member.getId());
    }

    private Coupon findCoupon(final Long couponId) {
        return couponDao.findById(couponId)
            .orElseThrow(() -> new BusinessException("존재하지 않는 쿠폰입니다."));
    }

    public List<PossibleCouponResponse> findPossibleCouponByMember(final Member member, final int totalProductAmount) {
        final List<Coupon> coupons = couponDao.findAllByMemberWhereIsNotUsed(member)
            .stream()
            .filter(it -> Amount.of(totalProductAmount).isMoreOrEqualsThan(it.getMinAmount()))
            .collect(Collectors.toList());
        return coupons.stream()
            .map(this::makePossibleCouponResponse)
            .collect(Collectors.toUnmodifiableList());
    }

    private PossibleCouponResponse makePossibleCouponResponse(final Coupon coupon) {
        return new PossibleCouponResponse(coupon.getId(), coupon.getName(), coupon.getMinAmount().getValue());
    }

    public CouponDiscountResponse calculateCouponDiscount(final Long couponId, final int totalProductAmount) {
        final Coupon coupon = findCoupon(couponId);
        final Amount discountedAmount = coupon.calculateProduct(Amount.of(totalProductAmount));
        return new CouponDiscountResponse(discountedAmount.getValue(),
            totalProductAmount - discountedAmount.getValue());
    }
}
