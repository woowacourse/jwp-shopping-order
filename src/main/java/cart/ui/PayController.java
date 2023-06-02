package cart.ui;

import cart.application.PayService;
import cart.domain.Member;
import cart.dto.request.PaymentRequest;
import cart.dto.response.OrderIdResponse;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/pay")
@RestController
public class PayController {

    private final PayService payService;

    public PayController(final PayService payService) {
        this.payService = payService;
    }

    @PostMapping
    public ResponseEntity<OrderIdResponse> pay(Member member, @Valid @RequestBody PaymentRequest paymentRequest) {
        OrderIdResponse response = payService.pay(member, paymentRequest);
        return ResponseEntity.ok(response);
    }
}
