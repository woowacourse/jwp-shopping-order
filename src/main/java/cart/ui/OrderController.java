package cart.ui;

import cart.application.OrderService;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.dto.order.OrderRequests;
import cart.dto.order.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> findById(@PathVariable final String orderId, Member member) {
        Order orders = orderService.findById(member.getId());

        return ResponseEntity.ok(OrderResponse.from(orders));
    }

    @PostMapping
    public ResponseEntity<Void> addOrder(@RequestBody @Valid OrderRequests request, Member member) {
        orderService.createOrder(request.getOrderRequests(), member);
        return ResponseEntity.noContent().build();
    }
}
