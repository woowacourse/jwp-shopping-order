package cart.ui;

import cart.application.OrderService;
import cart.application.PaymentService;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.PaymentRecord;
import cart.dto.PaymentResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    private final OrderService orderService;

    private final PaymentService paymentService;


    public PaymentController(OrderService orderService, PaymentService paymentService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @GetMapping("/total-cart-price")
    public ResponseEntity<PaymentResponse> getCartPrice(Member member, @RequestParam List<Long> cartItemIds) {
        Order draftOrder = orderService.createDraftOrder(member, cartItemIds);
        PaymentRecord draftPaymentRecord = paymentService.createDraftPaymentRecord(draftOrder);
        PaymentResponse paymentResponse = PaymentResponse.of(draftPaymentRecord);
        return ResponseEntity.ok(paymentResponse);
    }

}
