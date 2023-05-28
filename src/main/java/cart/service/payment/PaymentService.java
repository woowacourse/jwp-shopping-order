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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private final CartRepository cartRepository;
    private final CouponRepository couponRepository;

    public PaymentService(final CartRepository cartRepository, final CouponRepository couponRepository) {
        this.cartRepository = cartRepository;
        this.couponRepository = couponRepository;
    }


    @Transactional(readOnly = true)
    public PaymentResponse findPaymentPage(final Member member) {
        Cart cart = cartRepository.findCartByMemberId(member.getId());
        Coupons coupons = couponRepository.findAllByMemberId(member.getId());
        cart.getMember().initCoupons(coupons);

        return PaymentResponse.from(cart);
    }

    @Transactional
    public PaymentUsingCouponsResponse applyCoupons(final Member member, final CouponsApplyRequest request) {
        Cart cart = cartRepository.findCartByMemberId(member.getId());

        Coupons memberCoupons = couponRepository.findAllByMemberId(member.getId());
        Coupons requestCoupons = couponRepository.findAllByCouponIds(parseCouponRequestIds(request));

        // TODO: validate member and request coupons equals or diff

        cart.getMember().initCoupons(requestCoupons);
        return PaymentUsingCouponsResponse.from(cart);
    }

    private List<Long> parseCouponRequestIds(final CouponsApplyRequest request) {
        return request.getCoupons().stream()
                .map(CouponIdRequest::getId)
                .collect(Collectors.toList());
    }

    @Transactional
    public long pay(final Member member, final PaymentRequest request) {
        return 0;
    }
}
