package cart.ui.order;

import cart.application.service.order.OrderReadService;
import cart.ui.order.dto.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderReadController {

    private final OrderReadService orderReadService;

    public OrderReadController(final OrderReadService orderReadService) {
        this.orderReadService = orderReadService;
    }

    @GetMapping
    public ResponseEntity<OrdersResponse> findOrders() {
        return ResponseEntity.ok(new OrdersResponse());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> findProductsByOrder(@PathVariable("orderId") Long orderId) {
        return ResponseEntity.ok(new OrderResponse());
    }

}
