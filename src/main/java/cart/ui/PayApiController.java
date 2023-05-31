package cart.ui;

import cart.application.PayService;
import cart.application.dto.request.PaymentRequest;
import cart.domain.member.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.net.URI;

@Controller
@RequestMapping("/pay")
public class PayApiController {

    private final PayService payService;

    public PayApiController(final PayService payService) {
        this.payService = payService;
    }

    @PostMapping()
    public ResponseEntity<String> paymentCartItems(final Member member, @Valid @RequestBody final PaymentRequest request) {
        final Long historyId = payService.payment(member, request);

        return ResponseEntity.created(URI.create("redirect:/orders/histories/" + historyId)).build();
    }
}
