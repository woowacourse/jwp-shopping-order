package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.domain.Order;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/orders")
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> addOrder(Member member, @Valid @RequestBody OrderRequest orderRequest) {
        final Long orderId = orderService.add(member, orderRequest);

        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }

    @GetMapping("/{orderId}")
    public OrderResponse getOrders(Member member, @PathVariable Long orderId) {
        final Order order = orderService.findById(member, orderId);

        return OrderResponse.from(order);
    }
}
