package cart.domain.order.presentation;

import java.net.URI;

import cart.domain.order.application.OrderService;
import cart.domain.order.application.dto.OrderRequest;
import cart.domain.order.application.dto.OrderResponse;
import cart.domain.order.application.dto.OrdersResponse;
import cart.global.config.AuthMember;
import cart.global.config.AuthPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/orders")
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> order(@AuthPrincipal AuthMember authMember, @RequestBody OrderRequest orderRequest) {
        Long orderedId = orderService.order(authMember, orderRequest);
        return ResponseEntity.created(URI.create("/orders/" + orderedId)).build();
    }

    @GetMapping
    public ResponseEntity<OrdersResponse> showOrders(@AuthPrincipal AuthMember authMember) {
        OrdersResponse ordersResponse = orderService.findOrders(authMember);
        return ResponseEntity.ok(ordersResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> showOrder(@AuthPrincipal AuthMember authMember, @PathVariable Long id) {
        OrderResponse orderResponse = orderService.findOrder(authMember, id);
        return ResponseEntity.ok(orderResponse);
    }
}
