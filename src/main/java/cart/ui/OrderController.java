package cart.ui;

import cart.application.OrderService;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.dto.order.OrderItemsRequests;
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
        List<Order> orders = orderService.findAllByMemberId(member.getId());
        List<OrderResponse> responses = orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findById(@PathVariable final Long id, Member member) {
        Order order = orderService.findById(id, member);

        return ResponseEntity.ok(OrderResponse.from(order));
    }

    @PostMapping
    public ResponseEntity<Void> addOrder(@RequestBody @Valid OrderItemsRequests request, Member member) {
        orderService.createOrder(request, member);
        return ResponseEntity.noContent().build();
    }
}
