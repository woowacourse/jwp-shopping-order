package cart.controller.order;

import cart.cart.application.CartService;
import cart.common.auth.Auth;
import cart.controller.order.dto.OrderDetailResponse;
import cart.controller.order.dto.OrderRequest;
import cart.controller.order.dto.OrderResponse;
import cart.order.application.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class OrderController {
    private final OrderService orderService;
    private final CartService cartService;

    public OrderController(OrderService orderService, CartService cartService) {
        this.orderService = orderService;
        this.cartService = cartService;
    }

    @PostMapping("/payments")
    public ResponseEntity<Void> order(@Auth Long memberId, @RequestBody OrderRequest orderRequest) {
        final var orderId = cartService.order(memberId, orderRequest);
        return ResponseEntity.created(URI.create(orderId.toString())).build();
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> findOrders(@Auth Long memberId) {
        final var orderHistories = orderService.findOrderHistories(memberId);
        return ResponseEntity.ok(orderHistories);
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderDetailResponse> findOrder(@Auth Long memberId, @PathVariable long orderId) {
        final var orderHistory = orderService.findOrderHistory(orderId);
        return ResponseEntity.ok(orderHistory);
    }
}
