package cart.ui.order;

import cart.application.order.OrderService;
import cart.domain.member.Member;
import cart.dto.order.OrderRequest;
import cart.dto.order.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequestMapping("/orders")
@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping()
    public ResponseEntity<Void> createOrder(final Member member, @RequestBody final OrderRequest orderRequest) {
        Long orderId = orderService.save(member, orderRequest);
        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }

    @GetMapping()
    public ResponseEntity<List<OrderResponse>> findOrdersByMember(final Member member) {
        List<OrderResponse> orderResponses = orderService.findByMember(member);
        return ResponseEntity.ok(orderResponses);
    }

    @GetMapping("{id}")
    public ResponseEntity<OrderResponse> findOrderById(final Member member, @PathVariable Long id) {
        OrderResponse orderResponse = orderService.findById(member, id);
        return ResponseEntity.ok(orderResponse);
    }
}
