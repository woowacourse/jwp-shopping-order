package cart.controller;

import cart.config.auth.Auth;
import cart.domain.Member;
import cart.dto.CartItemResponse;
import cart.dto.CouponResponse;
import cart.dto.PaymentsResponse;
import cart.service.CartItemService;
import cart.service.CouponService;
import cart.service.PaymentsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentsController {
    private final PaymentsService paymentsService;

    public PaymentsController(PaymentsService paymentsService) {
        this.paymentsService = paymentsService;
    }

    @GetMapping
    public ResponseEntity<PaymentsResponse> getPayments(@Auth Member member) {
        PaymentsResponse paymentsResponse = paymentsService.getPayments(member);
        return ResponseEntity.ok().body(paymentsResponse);
    }
}
