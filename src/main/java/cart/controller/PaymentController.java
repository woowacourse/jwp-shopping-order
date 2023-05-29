package cart.controller;

import cart.config.auth.Auth;
import cart.domain.Member;
import cart.dto.PaymentsResponse;
import cart.dto.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @GetMapping
    public ResponseEntity<PaymentsResponse> getPayments(@Auth Member member) {



        return null;
    }

}
