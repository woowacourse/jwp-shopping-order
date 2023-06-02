package cart.cart.application;

import cart.cart.Cart;
import cart.cart.presentation.dto.CartItemResponse;
import cart.cart.presentation.dto.DeliveryResponse;
import cart.cart.presentation.dto.DiscountResponse;
import cart.cartitem.application.CartItemRepository;
import cart.coupon.application.CouponService;
import cart.deliveryprice.DeliveryPrice;
import cart.discountpolicy.discountcondition.DiscountTarget;
import cart.member.Member;
import cart.sale.SaleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final CouponService couponService;
    private final SaleService saleService;

    public CartService(CartItemRepository cartItemRepository, CouponService couponService, SaleService saleService) {
        this.cartItemRepository = cartItemRepository;
        this.couponService = couponService;
        this.saleService = saleService;
    }

    public List<CartItemResponse> findCartItemsByMember(Member member) {
        final var cartItems = cartItemRepository.findAllByMemberId(member.getId());
        final var cart = new Cart(cartItems);

        saleService.applySales(cart, DiscountTarget.TOTAL);

        return cartItems.stream()
                .map(CartItemResponse::from)
                .collect(Collectors.toList());
    }

    public DiscountResponse discountWithCoupons(Member member, List<Long> couponIds) {
        final var cartItems = cartItemRepository.findAllByMemberId(member.getId());
        final var cart = new Cart(cartItems);

        applyDiscountPolicy(member, cart);

        return DiscountResponse.from(cart);
    }

    public DeliveryResponse findDeliveryPrice(Member member) {
        final var cartItems = cartItemRepository.findAllByMemberId(member.getId());
        final var cart = new Cart(cartItems);

        applyDiscountPolicy(member, cart);

        return new DeliveryResponse(cart.calculateFinalDeliveryPrice(), DeliveryPrice.FREE_LIMIT);
    }

    private void applyDiscountPolicy(Member member, Cart cart) {
        applyDiscountPolicyExceptTotalPrice(member, cart);
        applyDiscountPolicyToTotalPrice(member, cart);
    }

    private void applyDiscountPolicyToTotalPrice(Member member, Cart cart) {
        saleService.applySalesApplyingToTotalPrice(cart);
        couponService.applyCouponsApplyingToTotalPrice(member.getId(), cart);
    }

    private void applyDiscountPolicyExceptTotalPrice(Member member, Cart cart) {
        saleService.applySales(cart, DiscountTarget.TOTAL);
        couponService.applyCoupons(member.getId(), cart, DiscountTarget.TOTAL);
    }
}
