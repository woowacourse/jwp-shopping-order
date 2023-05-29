package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.CartPointsResponse;
import cart.dto.OrderCreateRequest;
import cart.dto.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    public ResponseEntity<Void> createOrder(@Auth Member member, @RequestBody OrderCreateRequest orderCreateRequest) {
        final Long id = orderService.createOrder(orderCreateRequest, member);

        return ResponseEntity.created(URI.create("/orders/" + id)).build();
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderResponse> findSingleOrder(@PathVariable Long orderId, @Auth Member member) {
        final OrderResponse response = orderService.findById(orderId, member);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> findAllOrders(@Auth Member member) {
        final List<OrderResponse> orderResponses = orderService.findAll(member);
        return ResponseEntity.ok(orderResponses);
    }

    @GetMapping("/cart-points")
    public ResponseEntity<CartPointsResponse> calculatePoints(@Auth Member member) {
        final CartPointsResponse cartPointsResponse = orderService.calculatePoints(member);
        return ResponseEntity.ok(cartPointsResponse);
    }
}
