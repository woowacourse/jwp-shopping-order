package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.OrderCreateRequest;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(
            Member member,
            @RequestBody OrderCreateRequest orderCreateRequest
    ) {
        Long orderId = orderService.createOrder(member, orderCreateRequest);
        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }
}
