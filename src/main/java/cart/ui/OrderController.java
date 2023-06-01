package cart.ui;

import cart.application.OrderService;
import cart.auth.Auth;
import cart.domain.Member;
import cart.ui.dto.OrderCreateRequest;
import cart.ui.dto.OrderShowResponse;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    public ResponseEntity<Void> createOrder(@Auth final Member member, @RequestBody final OrderCreateRequest request) {
        final Long orderId = orderService.createOrder(member, request);
        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderShowResponse> showOrder(@Auth final Member member, @PathVariable final Long orderId) {
        return ResponseEntity.ok(orderService.showOrder(member, orderId));
    }
}
