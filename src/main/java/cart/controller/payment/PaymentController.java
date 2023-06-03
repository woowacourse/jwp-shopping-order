package cart.controller.payment;

import cart.config.auth.guard.order.OrderAuth;
import cart.domain.coupon.MemberCoupons;
import cart.dto.payment.PaymentRequest;
import cart.dto.payment.PaymentResponse;
import cart.dto.payment.PaymentUsingCouponsResponse;
import cart.service.payment.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RequestMapping("/payments")
@RestController
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(final PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public ResponseEntity<PaymentResponse> findPaymentPage(@OrderAuth final MemberCoupons memberCoupons) {
        return ResponseEntity.ok(paymentService.findPaymentPage(memberCoupons));
    }

    @GetMapping("/coupons")
    public ResponseEntity<PaymentUsingCouponsResponse> applyCoupon(@OrderAuth final MemberCoupons memberCoupons,
                                                                   @RequestParam("couponsId") @Valid final List<Long> ids) {
        return ResponseEntity.ok(paymentService.applyCoupons(memberCoupons, ids));
    }


    @PostMapping
    public ResponseEntity<Void> pay(@OrderAuth final MemberCoupons memberCoupons,
                                    @RequestBody @Valid final PaymentRequest request) {
        long id = paymentService.pay(memberCoupons, request);
        return ResponseEntity.created(URI.create("/payments/" + id)).build();
    }
}
