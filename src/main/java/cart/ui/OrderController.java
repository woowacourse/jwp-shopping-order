package cart.ui;

import cart.application.OrderService;
import cart.auth.Auth;
import cart.domain.Member;
import cart.ui.dto.OrderCreateRequest;
import java.net.URI;
import org.springframework.http.ResponseEntity;
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
}
