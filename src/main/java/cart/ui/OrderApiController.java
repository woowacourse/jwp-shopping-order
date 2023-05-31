package cart.ui;

import cart.application.OrderService;
import cart.dto.OrderItemsRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.net.URI;

@RestController
@RequestMapping("/orders")
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> orderProducts(@RequestBody @NotNull OrderItemsRequest orderItemsRequest) {
        long orderId = orderService.orderProducts(orderItemsRequest);
        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }
}
