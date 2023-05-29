package cart.controller.order;

import cart.config.auth.guard.basic.Auth;
import cart.domain.member.Member;
import cart.dto.order.OrderResponse;
import cart.dto.order.OrdersResponse;
import cart.service.order.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/orders")
@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<OrdersResponse> findOrders(@Auth final Member member) {
        return ResponseEntity.ok(orderService.findOrders(member));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findOrder(@Auth final Member member,
                                                   @PathVariable("id") final Long orderId) {
        return ResponseEntity.ok(orderService.findOrder(member, orderId));
    }
}
