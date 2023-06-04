package cart.ui;

import cart.application.PaymentService;
import cart.application.PurchaseOrderService;
import cart.domain.Member;
import cart.dto.purchaseorder.request.PurchaseOrderRequest;
import cart.dto.purchaseorder.response.PurchaseOrderPageResponse;
import cart.dto.purchaseorder.response.PurchaseOrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Controller
@RequestMapping("/orders")
public class PurchaseOrderApiController {

    private final PaymentService paymentService;
    private final PurchaseOrderService purchaseOrderService;

    public PurchaseOrderApiController(PaymentService paymentService,
                                      PurchaseOrderService purchaseOrderService) {
        this.paymentService = paymentService;
        this.purchaseOrderService = purchaseOrderService;
    }

    @PostMapping
    public ResponseEntity<Void> addPurchaseOrder(Member member, @RequestBody PurchaseOrderRequest purchaseOrderRequest) {
        System.out.println(purchaseOrderRequest);
        Long purchaseOrderId = paymentService.createPurchaseOrder(member, purchaseOrderRequest);
        return ResponseEntity.created(URI.create("/orders/" + purchaseOrderId))
                             .build();
    }

    @GetMapping
    public ResponseEntity<PurchaseOrderPageResponse> showPurchaseOrders(Member member, @RequestParam int page) {
        PurchaseOrderPageResponse purchaseOrderPageResponse = purchaseOrderService.getAllByMemberId(member, page);
        return ResponseEntity.ok()
                             .body(purchaseOrderPageResponse);
    }

    @GetMapping("{orderId}")
    public ResponseEntity<PurchaseOrderResponse> showPurchaseOrder(@PathVariable Long orderId) {
        PurchaseOrderResponse purchaseOrderResponse = purchaseOrderService.getPurchaseOrderByOrderId(orderId);
        return ResponseEntity.ok()
                             .body(purchaseOrderResponse);
    }

    @DeleteMapping("{orderId}")
    public ResponseEntity<Void> cancelledPurchaseOrder(Member member, @PathVariable Long orderId) {
        paymentService.deleteOrder(member, orderId);
        return ResponseEntity.ok()
                             .build();
    }
}
