package cart.cart.application;

import cart.cart.Cart;
import cart.cart.domain.cartitem.CartItem;
import cart.cart.domain.cartitem.application.CartItemRepository;
import cart.cart.domain.cartitem.application.CartItemService;
import cart.cart.presentation.DeliveryResponse;
import cart.cart.presentation.dto.CartItemResponse;
import cart.cart.presentation.dto.CartResponse;
import cart.cart.presentation.dto.DiscountResponse;
import cart.coupon.application.CouponService;
import cart.member.Member;
import cart.sale.SaleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final CartRepository cartItemRepository;
    private final CartItemService cartItemService;
    private final CouponService couponService;
    private final SaleService saleService;

    public CartService(CartRepository cartItemRepository, CartItemService cartItemService, CouponService couponService, SaleService saleService) {
        this.cartItemRepository = cartItemRepository;
        this.cartItemService = cartItemService;
        this.couponService = couponService;
        this.saleService = saleService;
    }

    public List<CartItemResponse> findCartItemsByMember(Member member) {
        final Cart cart = findCart(member);

        return cart.getCartItems()
                .stream().map(CartItemResponse::from)
                .collect(Collectors.toList());
    }

    public DiscountResponse discountWithCoupons(Member member, List<Long> couponIds) {
        final var cart = findCart(member);
        for (Long couponId : couponIds) {
            couponService.applyCoupon(couponId, cart);
        }
        return DiscountResponse.from(cart);
    }

    public DeliveryResponse findDeliveryPrice(Member member) {
        final var cart = findCart(member);
        final var deliveryPrice = cart.getDeliveryPrice();
        return new DeliveryResponse(deliveryPrice, Cart.FREE_DELIVERY_PRICE_LIMIT);
    }

    private Cart findCart(Member member) {
        final Cart cart = cartItemRepository.findCart(member);
        saleService.applySale(cart);
        return cart;
    }
}
