package cart.application;

import cart.domain.*;
import cart.dto.AllCouponResponse;
import cart.dto.AllOrderCouponResponse;
import cart.dto.MemberCouponRequest;
import cart.repository.CartItemRepository;
import cart.repository.CouponRepository;
import cart.repository.MemberCouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final MemberCouponRepository memberCouponRepository;
    private final CartItemRepository cartItemRepository;

    public CouponService(final CouponRepository couponRepository, final MemberCouponRepository memberCouponRepository, final CartItemRepository cartItemRepository) {
        this.couponRepository = couponRepository;
        this.memberCouponRepository = memberCouponRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional
    public Long issueCouponToMemberByCouponId(final Long couponId, final MemberCouponRequest request, final Member member) {
        LocalDateTime expiredAt = request.getExpiredAt();
        Coupon coupon = couponRepository.findById(couponId);
        return memberCouponRepository.save(coupon, expiredAt, member);
    }

    @Transactional(readOnly = true)
    public AllOrderCouponResponse calculateCouponForCarts(final Member member, final List<Long> selectedCartItemIds) {
        List<CartItem> cartItemsByMember = cartItemRepository.findByMember(member);
        List<MemberCoupon> memberCoupons = memberCouponRepository.findUsableByMember(member);

        CartItems cartItems = new CartItems(cartItemsByMember);
        CartItems selectedCartItems = cartItems.filterByIds(selectedCartItemIds);
        selectedCartItems.checkNotEmpty();
        Integer totalPrice = selectedCartItems.calculateTotalPrice();

        return AllOrderCouponResponse.of(memberCoupons, totalPrice);
    }

    @Transactional(readOnly = true)
    public AllCouponResponse findAllCoupon() {
        List<Coupon> allCoupons = couponRepository.findAllCoupons();

        return AllCouponResponse.from(allCoupons);
    }
}
