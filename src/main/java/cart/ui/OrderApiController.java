package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.OrderCreateRequest;
import cart.dto.OrderSelectResponse;
import cart.dto.OrdersSelectResponse;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(Member member, @RequestBody OrderCreateRequest orderCreateRequest) {
        Long orderId = orderService.order(member, orderCreateRequest);

        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderSelectResponse> showOrderById(Member member, @PathVariable Long orderId) {
        OrderSelectResponse orderSelectResponse = orderService.getOrder(member, orderId);

        return ResponseEntity.ok(orderSelectResponse);
    }

    @GetMapping
    public ResponseEntity<OrdersSelectResponse> showOrders(Member member) {
        OrdersSelectResponse ordersSelectResponse = orderService.getAllOrders(member);

        return ResponseEntity.ok(ordersSelectResponse);
    }
}
