package cart.application;

import cart.domain.*;
import cart.dto.OrderCouponResponse;
import cart.repository.CartItemRepository;
import cart.repository.CouponRepository;
import org.springframework.stereotype.Service;

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

    public List<OrderCouponResponse> calculateCouponForCarts(final Member member, final List<Long> selectedCartItemIds) {
        List<CartItem> cartItems = cartItemRepository.findByMember(member);
        // TODO: 5/30/23 이거 도메인 로직으로 빼기
        List<CartItem> selectedCartItems = cartItems.stream()
                .filter(cartItem -> selectedCartItemIds.contains(cartItem.getId()))
                .collect(Collectors.toList());
        // TODO: 5/30/23 이거 도메인 로직으로 뺴기
        int totalPrice = selectedCartItems.stream()
                .mapToInt(cartItem -> cartItem.getProduct().getPrice() * cartItem.getQuantity())
                .sum();
        List<Coupon> coupons = couponRepository.findCouponByMember(member);
        return coupons.stream()
                .map(coupon -> convertToOrderResponse(coupon, totalPrice))
                .collect(Collectors.toList());
    }

    private OrderCouponResponse convertToOrderResponse(final Coupon coupon, final int totalPrice) {
        CouponInfo couponInfo = coupon.getCouponInfo();
        if (coupon.isAvailable(totalPrice)) {
            return new OrderCouponResponse(
                    couponInfo.getId(),
                    couponInfo.getName(),
                    couponInfo.getMinPrice(),
                    true,
                    coupon.calculateDiscount(totalPrice));
        }
        return new OrderCouponResponse(
                couponInfo.getId(),
                couponInfo.getName(),
                couponInfo.getMinPrice(),
                false,
                null
        );
    }
}
