package cart.application;

import cart.domain.*;
import cart.dto.AllOrderCouponResponse;
import cart.dto.MemberCouponRequest;
import cart.dto.OrderCouponResponse;
import cart.repository.CartItemRepository;
import cart.repository.CouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final CartItemRepository cartItemRepository;

    public CouponService(final CouponRepository couponRepository, final CartItemRepository cartItemRepository) {
        this.couponRepository = couponRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional(readOnly = true)
    public AllOrderCouponResponse calculateCouponForCarts(final Member member, final List<Long> selectedCartItemIds) {
        List<CartItem> cartItemsByMember = cartItemRepository.findByMember(member);
        List<Coupon> coupons = couponRepository.findUsableByMember(member);

        CartItems cartItems = new CartItems(cartItemsByMember);
        CartItems selectedCartItems = cartItems.filterByIds(selectedCartItemIds);
        Integer totalPrice = selectedCartItems.calculateTotalPrice();

        return convertToAllOrderCouponResponse(coupons, totalPrice);
    }

    private AllOrderCouponResponse convertToAllOrderCouponResponse(final List<Coupon> coupons, final int totalPrice) {
        List<OrderCouponResponse> orderCouponResponses = coupons.stream()
                .map(coupon -> convertToOrderResponse(coupon, totalPrice))
                .collect(Collectors.toList());
        return new AllOrderCouponResponse(orderCouponResponses);
    }

    private OrderCouponResponse convertToOrderResponse(final Coupon coupon, final int totalPrice) {
        CouponInfo couponInfo = coupon.getCouponInfo();
        if (coupon.isAvailable(totalPrice)) {
            return new OrderCouponResponse(
                    couponInfo.getId(),
                    couponInfo.getName(),
                    couponInfo.getMinPrice(),
                    couponInfo.getMaxPrice(),
                    true,
                    coupon.calculateDiscount(totalPrice),
                    couponInfo.getExpiredAt()
            );
        }
        return new OrderCouponResponse(
                couponInfo.getId(),
                couponInfo.getName(),
                couponInfo.getMinPrice(),
                couponInfo.getMaxPrice(),
                false,
                null,
                couponInfo.getExpiredAt()
        );
    }

    public Long issueCouponToMemberByCouponId(final Long couponId, final MemberCouponRequest request, final Member member) {
        LocalDateTime expiredAt = request.getExpiredAt();
        return couponRepository.save(couponId, expiredAt, member);
    }
}
