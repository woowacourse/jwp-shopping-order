package cart.cart.application;

import cart.cart.Cart;
import cart.cart.domain.deliveryprice.DeliveryPrice;
import cart.cart.presentation.DeliveryResponse;
import cart.cart.presentation.dto.CartItemResponse;
import cart.cart.presentation.dto.DiscountResponse;
import cart.coupon.application.CouponService;
import cart.member.Member;
import cart.sale.SaleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final SaleService saleService;
    private final CouponService couponService;

    public CartService(CartRepository cartRepository, SaleService saleService, CouponService couponService) {
        this.cartRepository = cartRepository;
        this.saleService = saleService;
        this.couponService = couponService;
    }

    public List<CartItemResponse> findCartItemsByMember(Member member) {
        final Cart cart = cartRepository.findCart(member);
        saleService.applySale(cart);

        return cart.getCartItems()
                .stream().map(CartItemResponse::from)
                .collect(Collectors.toList());
    }

    public DiscountResponse discountWithCoupons(Member member, List<Long> couponIds) {
        final Cart cart = cartRepository.findCart(member);
        saleService.applySale(cart);
        for (Long couponId : couponIds) {
            couponService.applyCoupon(couponId, cart);
        }
        final var discountPriceFromTotalPrice = couponService.findAllDiscountPriceFromTotalPrice(couponIds, cart);
        return DiscountResponse.from(cart, discountPriceFromTotalPrice);
    }

    public DeliveryResponse findDeliveryPrice(Member member) {
        final Cart cart = cartRepository.findCart(member);
        saleService.applySale(cart);
        final var deliveryPrice = cart.getDeliveryPrice();
        return new DeliveryResponse(deliveryPrice.getPrice(), DeliveryPrice.FREE_LIMIT);
    }
}
