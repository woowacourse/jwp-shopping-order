package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrdersApiController {

    private final OrderService orderService;

    public OrdersApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders(Member member) {
        return ResponseEntity.ok(orderService.findOrdersByMember(member));
    }

    @PostMapping
    public ResponseEntity<Void> order(Member member, @RequestBody OrderRequest orderRequest) {
        Long orderId = orderService.orderItems(member.getId(), orderRequest);
        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderDetail(@PathVariable Long orderId, Member member) {
        return ResponseEntity.ok(orderService.findOrderDetail(orderId));
    }
}
