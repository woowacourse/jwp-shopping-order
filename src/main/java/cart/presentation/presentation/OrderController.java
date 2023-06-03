package cart.presentation.presentation;

import cart.cart.application.CartService;
import cart.common.auth.Auth;
import cart.member.Member;
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
    public ResponseEntity<Void> order(@Auth Member member, @RequestBody OrderRequest orderRequest) {
        cartService.order(member, orderRequest);
        final var orderId = cartService.order(member, orderRequest);
        return ResponseEntity.created(URI.create(orderId.toString())).build();
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> findOrders(@Auth Member member) {
        final var orderHistories = orderService.findOrderHistories(member.getId());
        return ResponseEntity.ok(orderHistories);
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderDetailResponse> findOrder(@Auth Member member, @PathVariable long orderId) {
        final var orderHistory = orderService.findOrderHistory(orderId);
        return ResponseEntity.ok(orderHistory);
    }
}
