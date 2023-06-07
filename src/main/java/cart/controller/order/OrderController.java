package cart.controller.order;

import cart.config.auth.Auth;
import cart.domain.Member;
import cart.dto.order.OrderRequest;
import cart.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/payments")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> payment(@Auth Member member, @RequestBody OrderRequest orderRequest) {
        Long id = orderService.order(member, orderRequest);
        return ResponseEntity.created(URI.create("/orders/" + id)).build();
    }
}
