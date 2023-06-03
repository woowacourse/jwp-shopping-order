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
import cart.order.application.OrderService;
import cart.presentation.presentation.OrderRequest;
import cart.sale.SaleService;
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

    public List<CartItemResponse> findCartItemsByMember(Member member) {
        final var cartItems = cartItemRepository.findAllByMemberId(member.getId());
        final var cart = new Cart(cartItems);

        saleService.applySales(cart, DiscountTarget.TOTAL);

        return cartItems.stream()
                .map(CartItemResponse::from)
                .collect(Collectors.toList());
    }

    public DeliveryResponse findDeliveryPrice(Member member) {
        final var cartItems = cartItemRepository.findAllByMemberId(member.getId());
        final var cart = new Cart(cartItems);

        applyDiscountPolicy(cart);

        return new DeliveryResponse(cart.calculateFinalDeliveryPrice(), DeliveryPrice.FREE_LIMIT);
    }

    public DiscountResponse discountWithCoupons(Member member, List<Long> couponIds) {
        final var cartItems = cartItemRepository.findAllByMemberId(member.getId());
        final var cart = new Cart(cartItems);

        applyDiscountPolicyExceptTotalPrice(cart);
        couponService.applyCoupons(cart, couponIds);

        return DiscountResponse.from(cart);
    }

    public Long order(Member member, OrderRequest orderRequest) {
        final var cartItems = cartItemRepository.findAllByMemberId(member.getId());
        final var cart = new Cart(cartItems);

        applyDiscountPolicyExceptTotalPrice(cart);
        couponService.applyCoupons(cart, orderRequest.getCouponIds());

        return orderService.order(member.getId(), cart, orderRequest);
    }

    private void applyDiscountPolicy(Cart cart) {
        applyDiscountPolicyExceptTotalPrice(cart);
        applyDiscountPolicyToTotalPrice(cart);
    }

    private void applyDiscountPolicyToTotalPrice(Cart cart) {
        saleService.applySalesApplyingToTotalPrice(cart);
    }

    private void applyDiscountPolicyExceptTotalPrice(Cart cart) {
        saleService.applySales(cart, DiscountTarget.TOTAL);

    }
}
