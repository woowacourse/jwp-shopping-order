package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.domain.Order;
import cart.dto.order.OrderRequest;
import cart.dto.order.OrderResponse;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
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

    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(@RequestBody @Valid OrderRequest request, Member member) {
        long orderId = orderService.createOrder(request, member);
        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findById(@PathVariable Long id, Member member) {
        Order order = orderService.findById(id, member);
        return ResponseEntity.ok(OrderResponse.fromOrder(order));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> findAllByMember(Member member) {
        List<Order> orders = orderService.findAllByMember(member);
        List<OrderResponse> orderResponses = mapToOrderResponses(orders);
        return ResponseEntity.ok(orderResponses);
    }

    private List<OrderResponse> mapToOrderResponses(List<Order> orders) {
        return orders.stream()
            .map(OrderResponse::fromOrder)
            .collect(Collectors.toList());
    }

}
