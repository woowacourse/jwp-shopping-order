package cart.cart.application;

import cart.cart.Cart;
import cart.cartitem.application.CartItemRepository;
import cart.controller.cart.dto.CartItemResponse;
import cart.controller.cart.dto.DeliveryResponse;
import cart.controller.cart.dto.DiscountResponse;
import cart.controller.order.dto.OrderRequest;
import cart.coupon.application.CouponService;
import cart.discountpolicy.DiscountPolicy;
import cart.order.application.OrderService;
import cart.sale.application.SaleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final CouponService couponService;
    private final SaleService saleService;
    private final OrderService orderService;

    public CartService(CartItemRepository cartItemRepository, CouponService couponService, SaleService saleService, OrderService orderService) {
        this.cartItemRepository = cartItemRepository;
        this.couponService = couponService;
        this.saleService = saleService;
        this.orderService = orderService;
    }

    public List<CartItemResponse> findCartItemsByMember(Long memberId) {
        final var cartItems = cartItemRepository.findAllByMemberId(memberId);
        final var cart = new Cart(cartItems);

        for (DiscountPolicy discountPolicy : saleService.findDiscountPoliciesFromSales()) {
            cart.discount(discountPolicy);
        }

        return cartItems.stream()
                .map(CartItemResponse::from)
                .collect(Collectors.toList());
    }

    public DeliveryResponse findDeliveryPrice(Long memberId) {
        final var cartItems = cartItemRepository.findAllByMemberId(memberId);
        final var cart = new Cart(cartItems);

        final var discountPolicies = saleService.findDiscountPoliciesFromSales();
        for (DiscountPolicy discountPolicy : discountPolicies) {
            cart.discount(discountPolicy);
        }

        return new DeliveryResponse(cart.calculateFinalDeliveryPrice(), Cart.MAN_FREE_DELIVERY_PRICE);
    }

    public DiscountResponse discountWithCoupons(Long memberId, List<Long> couponIds) {
        final var cartItems = cartItemRepository.findAllByMemberId(memberId);
        final var cart = new Cart(cartItems);

        final var discountPoliciesFromSales = saleService.findDiscountPoliciesFromSales();
        for (DiscountPolicy discountPolicy : discountPoliciesFromSales) {
            cart.discount(discountPolicy);
        }

        final var discountPoliciesFromCoupons = couponService.findDiscountPoliciesFromCouponIds(couponIds);
        for (DiscountPolicy discountPolicy : discountPoliciesFromCoupons) {
            cart.discount(discountPolicy);
        }

        return DiscountResponse.from(cart);
    }

    public Long order(Long memberId, OrderRequest orderRequest) {
        final var cartItems = cartItemRepository.findAllByMemberId(memberId);
        final var cart = new Cart(cartItems);

        for (DiscountPolicy discountPolicy : saleService.findDiscountPoliciesFromSales()) {
            cart.discount(discountPolicy);
        }

        for (DiscountPolicy discountPolicy : couponService.findDiscountPoliciesFromCouponIds(orderRequest.getCouponIds())) {
            cart.discount(discountPolicy);
        }

        return orderService.order(memberId, cart, orderRequest);
    }
}
