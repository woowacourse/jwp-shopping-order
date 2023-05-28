package cart.controller.payment;

import cart.config.auth.Auth;
import cart.domain.member.Member;
import cart.dto.coupon.CouponsApplyRequest;
import cart.dto.payment.PaymentRequest;
import cart.dto.payment.PaymentResponse;
import cart.dto.payment.PaymentUsingCouponResponse;
import cart.service.payment.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RequestMapping("/payments")
@RestController
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(final PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public ResponseEntity<PaymentResponse> findPaymentPage(@Auth final Member member) {
        return ResponseEntity.ok(paymentService.findPaymentPage(member));
    }

    @PostMapping("/coupons")
    public ResponseEntity<PaymentUsingCouponResponse> applyCoupon(@Auth final Member member,
                                                                  @RequestBody @Valid final CouponsApplyRequest request) {
        return ResponseEntity.ok(paymentService.applyCoupons(member, request));
    }

    @PostMapping
    public ResponseEntity<Void> pay(@Auth final Member member,
                                    @RequestBody @Valid final PaymentRequest request) {
        long id = paymentService.pay(member, request);
        return ResponseEntity.created(URI.create("/payments/" + id)).build();
    }
}
