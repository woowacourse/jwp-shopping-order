package cart.ui;

import cart.application.OrderService;
import cart.application.PaymentService;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.PaymentRecord;
import cart.dto.OrderDetailResponse;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final PaymentService paymentService;

    public OrderController(OrderService orderService, PaymentService paymentService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<Void> postOrder(Member member, @RequestBody OrderRequest orderRequest) {
        Long orderId = orderService.createOrderAndSave(member, orderRequest.getCartItemIds());
        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders(Member member) {
        List<Order> orders = orderService.retrieveMemberOrders(member);
        List<OrderResponse> orderResponses = orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderResponses);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponse> getOrderDetail(Member member, @PathVariable Long orderId) {
        Order order = orderService.retrieveOrderById(orderId);
        PaymentRecord paymentRecord = paymentService.createPaymentRecordAndSave(order);
        return ResponseEntity.ok(OrderDetailResponse.from(paymentRecord));
    }
}
