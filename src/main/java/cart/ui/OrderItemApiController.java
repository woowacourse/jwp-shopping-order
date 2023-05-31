package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.order.OrderRequest;
import cart.dto.order.OrderResponse;
import cart.dto.order.OrdersResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderItemApiController {

    private final OrderService orderService;

    public OrderItemApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(Member member, @RequestBody OrderRequest orderRequest) {
        Long id = orderService.createOrder(member, orderRequest);
        return ResponseEntity.created(URI.create("/orders/" + id)).build();
    }

    @GetMapping
    public ResponseEntity<List<OrdersResponse>> showOrders(Member member) {
        return ResponseEntity.ok(orderService.findAllOrdersByMember(member));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> showOrderDetails(Member member, @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.findOrderDetailsByOrderId(member, orderId));
    }
}
