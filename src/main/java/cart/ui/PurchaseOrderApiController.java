package cart.ui;

import cart.application.PaymentService;
import cart.domain.Member;
import cart.dto.PurchaseOrderRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

@Controller
@RequestMapping("/orders")
public class PurchaseOrderApiController {

    private final PaymentService paymentService;

    public PurchaseOrderApiController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<Void> addPurchaseOrder(Member member, @RequestBody PurchaseOrderRequest purchaseOrderRequest) {
        Long purchaseOrderId = paymentService.createPurchaseOrder(member, purchaseOrderRequest);
        return ResponseEntity.created(URI.create("/orders/" + purchaseOrderId))
                             .build();
    }
}
