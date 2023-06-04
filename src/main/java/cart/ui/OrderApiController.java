package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.order.OrderCreateResponse;
import cart.dto.order.OrderRequest;
import cart.dto.order.OrderResponse;
import cart.dto.order.OrdersResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderCreateResponse> createOrder(Member member, @RequestBody OrderRequest orderRequest) {
        OrderCreateResponse response = orderService.createOrder(member, orderRequest);
        return ResponseEntity.created(URI.create("/orders/" + response.getOrderId())).body(response);
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
