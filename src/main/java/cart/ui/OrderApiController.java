package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(Member member, @RequestBody final OrderRequest orderRequest) {
        orderService.add(member.getId(), orderRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> findOrdersByMember(Member member) {
        List<OrderResponse> orderResponses = orderService.findOrdersByMemberId(member.getId());
        return ResponseEntity.ok().body(orderResponses);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> findOrdersByMember(Member member, @PathVariable final Long orderId) {
        OrderResponse orderResponse = orderService.findOrdersByIdAndMemberId(member.getId(), orderId);
        return ResponseEntity.ok().body(orderResponse);
    }
}
