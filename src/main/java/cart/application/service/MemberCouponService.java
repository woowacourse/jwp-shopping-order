package cart.application.service;

import cart.application.dto.coupon.IssueCouponRequest;
import cart.application.dto.order.FindOrderCouponsResponse;
import cart.application.repository.CartItemRepository;
import cart.application.repository.CouponRepository;
import cart.application.repository.MemberCouponRepository;
import cart.domain.Member;
import cart.domain.cart.CartItems;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.MemberCoupon;
import cart.exception.noexist.NoExistErrorType;
import cart.exception.noexist.NoExistException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberCouponService {

    private final MemberCouponRepository memberCouponRepository;
    private final CouponRepository couponRepository;
    private final CartItemRepository cartItemRepository;

    public MemberCouponService(final MemberCouponRepository memberCouponRepository,
            final CouponRepository couponRepository,
            final CartItemRepository cartItemRepository) {
        this.memberCouponRepository = memberCouponRepository;
        this.couponRepository = couponRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public long createMemberCoupon(final Member member, final long couponId, final IssueCouponRequest request) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new NoExistException(NoExistErrorType.COUPON_NO_EXIST));
        MemberCoupon memberCoupon = new MemberCoupon(coupon, member, request.getExpiredAt());
        return memberCouponRepository.create(memberCoupon);
    }

    @Transactional(readOnly = true)
    public FindOrderCouponsResponse findOrderCoupons(final Member member, final List<Long> cartItemIds) {
        CartItems cartItems = cartItemRepository.findByIds(cartItemIds);
        cartItems.checkOwner(member);

        List<MemberCoupon> memberCoupons = memberCouponRepository.findValidCouponsByMember(member);
        return FindOrderCouponsResponse.from(memberCoupons, cartItems);
    }
}
