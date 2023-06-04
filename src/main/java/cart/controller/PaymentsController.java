package cart.controller;

import cart.config.auth.Auth;
import cart.domain.Member;
import cart.dto.OrderReqeust;
import cart.service.PaymentsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/payments")
public class PaymentsController {
    private final PaymentsService paymentsService;

    public PaymentsController(PaymentsService paymentsService) {
        this.paymentsService = paymentsService;
    }

    @PostMapping
    public ResponseEntity<Void> payment(@Auth Member member, @RequestBody OrderReqeust orderReqeust) {
        Long id = paymentsService.order(member, orderReqeust);
        return ResponseEntity.created(URI.create("/payments/" + id)).build();
    }
}
