package cart.ui;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import cart.application.OrderService;
import cart.domain.Member;
import cart.domain.Order;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    public ResponseEntity<Void> order(Member member, @RequestBody OrderRequest request) {
        orderService.order(member, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> showOrdersOf(Member member) {
        List<Order> orders = orderService.getBy(member);
        return ResponseEntity.ok(OrderResponse.of(orders));
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderResponse> showOrderBy(Member member, @PathVariable Long id) {
        Order order = orderService.getBy(member, id);
        return ResponseEntity.ok(OrderResponse.of(order));
    }
}
