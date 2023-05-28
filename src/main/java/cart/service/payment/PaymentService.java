package cart.service.payment;

import cart.domain.cart.Cart;
import cart.domain.member.Member;
import cart.dto.coupon.CouponsApplyRequest;
import cart.dto.payment.PaymentRequest;
import cart.dto.payment.PaymentResponse;
import cart.dto.payment.PaymentUsingCouponResponse;
import cart.repository.cart.CartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    private final CartRepository cartRepository;

    public PaymentService(final CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }


    @Transactional(readOnly = true)
    public PaymentResponse findPaymentPage(final Member member) {
        Cart cart = cartRepository.findCartByMemberId(member.getId());
        return null;
    }

    @Transactional
    public PaymentUsingCouponResponse applyCoupons(final Member member, final CouponsApplyRequest request) {
        Cart cart = cartRepository.findCartByMemberId(member.getId());
        return null;
    }

    @Transactional
    public long pay(final Member member, final PaymentRequest request) {
        return 0;
    }
}
