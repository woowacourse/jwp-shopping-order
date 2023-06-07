package cart.service.payment;

import cart.domain.cart.Cart;
import cart.domain.cart.CartItem;
import cart.domain.cart.Order;
import cart.domain.coupon.Coupons;
import cart.domain.coupon.MemberCoupons;
import cart.dto.coupon.CouponIdRequest;
import cart.dto.coupon.CouponResponse;
import cart.dto.order.OrderResponse;
import cart.dto.payment.PaymentRequest;
import cart.dto.payment.PaymentResponse;
import cart.dto.payment.PaymentUsingCouponsResponse;
import cart.dto.product.ProductIdRequest;
import cart.repository.cart.CartRepository;
import cart.repository.coupon.CouponRepository;
import cart.repository.order.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private final CartRepository cartRepository;
    private final CouponRepository couponRepository;
    private final OrderRepository orderRepository;

    public PaymentService(final CartRepository cartRepository, final CouponRepository couponRepository, final OrderRepository orderRepository) {
        this.cartRepository = cartRepository;
        this.couponRepository = couponRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional(readOnly = true)
    public PaymentResponse findPaymentPage(final MemberCoupons memberCoupons) {
        Cart cart = cartRepository.findCartByMemberId(memberCoupons.getMember().getId());
        return PaymentResponse.from(memberCoupons, cart);
    }

    @Transactional(readOnly = true)
    public PaymentUsingCouponsResponse applyCoupons(final MemberCoupons memberCoupons, final List<Long> request) {
        Cart cart = cartRepository.findCartByMemberId(memberCoupons.getMember().getId());
        Coupons requestCoupons = couponRepository.findAllByCouponIds(request);
        memberCoupons.validateHasCoupons(request);

        return PaymentUsingCouponsResponse.from(cart, requestCoupons.getCoupons());
    }

    private List<Long> parseCouponRequestIds(final List<CouponIdRequest> couponIds) {
        return couponIds.stream()
                .map(CouponIdRequest::getId)
                .collect(Collectors.toList());
    }

    @Transactional
    public long pay(final MemberCoupons memberCoupons, final PaymentRequest paymentRequest) {
        Cart cart = cartRepository.findCartByMemberId(memberCoupons.getMember().getId());
        memberCoupons.validateHasCoupons(parseCouponRequestIds(paymentRequest.getCoupons()));

        Order order = new Order(memberCoupons, cart);
        OrderResponse orderHistory = order.pay(parseProductIds(paymentRequest.getProducts()), parseProductQuantities(paymentRequest.getProducts()), parseCouponRequestIds(paymentRequest.getCoupons()));

        updateCartItems(order);
        deleteUsedCoupons(orderHistory);

        return orderRepository.save(memberCoupons.getMember(), orderHistory);
    }

    private List<Long> parseProductIds(final List<ProductIdRequest> productIds) {
        return productIds.stream()
                .map(ProductIdRequest::getId)
                .collect(Collectors.toList());
    }

    private List<Integer> parseProductQuantities(final List<ProductIdRequest> quantities) {
        return quantities.stream()
                .map(ProductIdRequest::getQuantity)
                .collect(Collectors.toList());
    }

    private void updateCartItems(final Order order) {
        order.getCart().getCartItems()
                .forEach(this::updateCartItem);
    }

    private void updateCartItem(final CartItem cartItem) {
        if (cartItem.isEmptyQuantity()) {
            cartRepository.deleteCartItemById(cartItem.getId());
            return;
        }

        cartRepository.updateCartItemQuantity(cartItem);
    }


    private void deleteUsedCoupons(final OrderResponse orderHistory) {
        List<Long> couponIds = orderHistory.getCoupons().stream()
                .map(CouponResponse::getCouponId)
                .collect(Collectors.toList());

        couponRepository.deleteAllByIds(couponIds);
    }
}
