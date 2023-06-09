package cart.web.order;

import cart.application.OrderService;
import cart.application.PaymentService;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.order.payment.Payment;
import cart.dto.order.PaymentResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentApiController {

    private final OrderService orderService;

    private final PaymentService paymentService;


    public PaymentApiController(OrderService orderService, PaymentService paymentService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @GetMapping("/total-cart-price")
    public ResponseEntity<PaymentResponse> getCartPrice(Member member, @RequestParam List<Long> cartItemIds) {
        Order draftOrder = orderService.createDraftOrder(member, cartItemIds);
        Payment draftPayment = paymentService.createDraftPaymentRecord(draftOrder);
        PaymentResponse paymentResponse = PaymentResponse.of(draftPayment);
        return ResponseEntity.ok(paymentResponse);
    }

}
