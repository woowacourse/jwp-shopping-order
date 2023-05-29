package cart.service.payment;

import cart.domain.cart.Cart;
import cart.domain.cart.CartItem;
import cart.domain.cart.Order;
import cart.domain.coupon.Coupons;
import cart.domain.member.Member;
import cart.dto.coupon.CouponIdRequest;
import cart.dto.coupon.CouponResponse;
import cart.dto.coupon.CouponsApplyRequest;
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
    public PaymentResponse findPaymentPage(final Member member) {
        Cart cart = cartRepository.findCartByMemberId(member.getId());
        Coupons coupons = couponRepository.findAllByMemberId(member.getId());
        member.initCoupons(coupons);

        return PaymentResponse.from(member, cart);
    }

    @Transactional
    public PaymentUsingCouponsResponse applyCoupons(final Member member, final CouponsApplyRequest request) {
        Cart cart = cartRepository.findCartByMemberId(member.getId());
        Coupons memberCoupons = couponRepository.findAllByMemberId(member.getId());
        Coupons requestCoupons = couponRepository.findAllByCouponIds(parseCouponRequestIds(request.getCoupons()));

        // TODO: validate member and request coupons equals or diff
        member.initCoupons(requestCoupons);

        return PaymentUsingCouponsResponse.from(cart, requestCoupons.getCoupons());
    }

    private List<Long> parseCouponRequestIds(final List<CouponIdRequest> request) {
        return request.stream()
                .map(CouponIdRequest::getId)
                .collect(Collectors.toList());
    }

    public List<Long> parseProductIds(final List<ProductIdRequest> request) {
        return request.stream()
                .map(ProductIdRequest::getId)
                .collect(Collectors.toList());
    }

    public List<Integer> parseProductQuantities(final List<ProductIdRequest> request) {
        return request.stream()
                .map(ProductIdRequest::getQuantity)
                .collect(Collectors.toList());
    }

    @Transactional
    public long pay(final Member member, final PaymentRequest request) {
        Cart cart = cartRepository.findCartByMemberId(member.getId());
        Coupons memberCoupons = couponRepository.findAllByMemberId(member.getId());
        Coupons requestCoupons = couponRepository.findAllByCouponIds(parseCouponRequestIds(request.getCoupons()));
        member.initCoupons(requestCoupons);

        Order order = new Order(member, cart);
        OrderResponse orderHistory = order.pay(parseProductIds(request.getProducts()), parseProductQuantities(request.getProducts()), parseCouponRequestIds(request.getCoupons()));


        // 2. 이제 DB 영억
        long orderId = orderRepository.save(member, orderHistory);

        // 2-1 기존 품목 업데이트
        for (CartItem cartItem : order.getCart().getCartItems()) {
            if (cartItem.isEmptyQuantity()) {
                cartRepository.deleteCartItemById(cartItem.getId());
                continue;
            }
            cartRepository.updateCartItemQuantity(cartItem);
        }

        // 3. 사용한 유저의 쿠폰 삭제
        for (CouponResponse coupon : orderHistory.getCoupons()) {
            couponRepository.deleteById(coupon.getCouponId());
        }

        return orderId;
    }
}
