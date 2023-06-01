package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/orders")
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> orderCartItems(Member member, @RequestBody OrderRequest orderRequest) {
        long saved = orderService.order(member, orderRequest);
        return ResponseEntity.created(URI.create("/orders/" + saved)).build();
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> findOrder(Member member, @PathVariable Long orderId) {
        OrderResponse orderResponse = orderService.findOrder(member, orderId);
        return ResponseEntity.ok().body(orderResponse);
    }
}
