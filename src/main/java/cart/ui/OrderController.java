package cart.ui;

import cart.application.OrderService;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.dto.order.OrderRequest;
import cart.dto.order.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> findAll(Member member) {
        List<Order> orders = orderService.findAll(member.getId());
        List<OrderResponse> responses = orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<Void> addOrder(@RequestBody List<OrderRequest> request, Member member) {
        orderService.createOrder(request, member.getId());
        return ResponseEntity.noContent().build();
    }
}
