package cart.application;

import cart.domain.*;
import cart.dto.*;
import cart.repository.CartItemRepository;
import cart.repository.CouponRepository;
import cart.repository.MemberCouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
        Integer totalPrice = selectedCartItems.calculateTotalPrice();

        return convertToAllOrderCouponResponse(memberCoupons, totalPrice);
    }

    @Transactional(readOnly = true)
    public AllCouponResponse findAllCoupon() {
        List<Coupon> allCoupons = couponRepository.findAllCoupons();

        List<CouponResponse> couponResponses = allCoupons.stream()
                .map(coupon -> new CouponResponse(
                        coupon.getCouponInfo().getId(),
                        coupon.getCouponInfo().getName(),
                        coupon.getCouponInfo().getMinOrderPrice(),
                        coupon.getCouponInfo().getMaxDiscountPrice(),
                        CouponType.from(coupon).name(),
                        coupon.getDiscountAmount(),
                        coupon.getDiscountPercentage()
                ))
                .collect(Collectors.toList());

        return new AllCouponResponse(couponResponses);
    }

    private AllOrderCouponResponse convertToAllOrderCouponResponse(final List<MemberCoupon> memberCoupons, final int totalPrice) {
        List<OrderCouponResponse> orderCouponResponses = memberCoupons.stream()
                .map(memberCoupon -> convertToOrderResponse(memberCoupon, totalPrice))
                .collect(Collectors.toList());
        return new AllOrderCouponResponse(orderCouponResponses);
    }

    private OrderCouponResponse convertToOrderResponse(final MemberCoupon memberCoupon, final int totalPrice) {
        Coupon coupon = memberCoupon.getCoupon();
        CouponInfo couponInfo = coupon.getCouponInfo();
        if (coupon.isAvailable(totalPrice)) {
            return new OrderCouponResponse(
                    couponInfo.getId(),
                    couponInfo.getName(),
                    couponInfo.getMinOrderPrice(),
                    couponInfo.getMaxDiscountPrice(),
                    true,
                    memberCoupon.calculateDiscount(totalPrice),
                    memberCoupon.getExpiredAt()
            );
        }
        return new OrderCouponResponse(
                couponInfo.getId(),
                couponInfo.getName(),
                couponInfo.getMinOrderPrice(),
                couponInfo.getMaxDiscountPrice(),
                false,
                null,
                memberCoupon.getExpiredAt()
        );
    }
}
