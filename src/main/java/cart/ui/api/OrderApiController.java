package cart.ui.api;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.order.OrderRequest;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(Member member, @RequestBody OrderRequest request) {
        Long orderId = orderService.createOrder(member, request);
        return ResponseEntity.created(URI.create("/order/" + orderId)).build();
    }
}
