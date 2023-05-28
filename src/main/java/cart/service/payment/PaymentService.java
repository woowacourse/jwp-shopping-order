package cart.service.payment;

import cart.domain.cart.Cart;
import cart.domain.coupon.Coupons;
import cart.domain.member.Member;
import cart.dto.coupon.CouponIdRequest;
import cart.dto.coupon.CouponsApplyRequest;
import cart.dto.payment.PaymentRequest;
import cart.dto.payment.PaymentResponse;
import cart.dto.payment.PaymentUsingCouponsResponse;
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

    @Transactional
    public long pay(final Member member, final PaymentRequest request) {
        Cart cart = cartRepository.findCartByMemberId(member.getId());
        Coupons memberCoupons = couponRepository.findAllByMemberId(member.getId());
        Coupons requestCoupons = couponRepository.findAllByCouponIds(parseCouponRequestIds(request.getCoupons()));

        // TODO: validate member and request coupons equals or diff
        member.initCoupons(requestCoupons);

        //
        long orderId = orderRepository.save(member, cart, requestCoupons.getCoupons());
        cartRepository.deleteAllByMemberId(cart.getId());

        // TODO: 쿠폰 삭제

        return orderId;
    }
}
