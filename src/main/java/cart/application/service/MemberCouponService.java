package cart.application.service;

import cart.application.dto.order.FindOrderCouponsResponse;
import cart.application.repository.CartItemRepository;
import cart.application.repository.MemberCouponRepository;
import cart.domain.Member;
import cart.domain.cart.CartItems;
import cart.domain.coupon.MemberCoupon;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MemberCouponService {

    private final MemberCouponRepository memberCouponRepository;
    private final CartItemRepository cartItemRepository;

    public MemberCouponService(final MemberCouponRepository memberCouponRepository,
            final CartItemRepository cartItemRepository) {
        this.memberCouponRepository = memberCouponRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public FindOrderCouponsResponse findOrderCoupons(final Member member, final List<Long> cartItemIds) {
        CartItems cartItems = cartItemRepository.findByIds(cartItemIds);
        cartItems.checkOwner(member);

        List<MemberCoupon> memberCoupons = memberCouponRepository.findValidCouponsByMember(member);
        return FindOrderCouponsResponse.from(memberCoupons, cartItems);
    }
}
