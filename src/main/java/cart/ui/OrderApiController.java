package cart.ui;

import cart.application.OrderService;
import cart.domain.member.Member;
import cart.dto.order.OrderRequest;
import cart.dto.order.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(final Member member, @RequestBody final OrderRequest orderRequest) {
        Long id = orderService.add(member, orderRequest);
        return ResponseEntity.created(URI.create("/orders/" + id)).build();
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> findOrdersByMember(Member member) {
        List<OrderResponse> orderResponses = orderService.findOrdersByMemberId(member.getId());
        return ResponseEntity.ok().body(orderResponses);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> findOrderById(Member member, @PathVariable final Long orderId) {
        OrderResponse orderResponse = orderService.findOrderById(member, orderId);
        return ResponseEntity.ok().body(orderResponse);
    }
}
