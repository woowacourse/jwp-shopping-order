package cart.controller;

import cart.config.auth.Auth;
import cart.domain.Member;
import cart.service.CouponService;
import cart.service.PaymentsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/payments")
public class PaymentsController {
    private final PaymentsService paymentsService;

    public PaymentsController(PaymentsService paymentsService) {
        this.paymentsService = paymentsService;
    }

//    @GetMapping("/coupons/{couponIds}")
//    public ResponseEntity applyCoupons(@Auth Member member, @PathVariable List<Long> couponIds) {
//        List<Long> notNullCouponIds = couponIds.stream().
//                filter(Objects::nonNull)
//                .collect(Collectors.toList());
//        return null;
//    }
}
