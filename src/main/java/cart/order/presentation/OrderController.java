package cart.order.presentation;

import cart.common.auth.Auth;
import cart.member.Member;
import cart.order.application.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/payments")
    public ResponseEntity<Void> order(@Auth Member member, @RequestBody OrderRequest orderRequest) {
        final var orderId = orderService.order(member, orderRequest);
        return ResponseEntity.created(URI.create(orderId.toString())).build();
    }
}
