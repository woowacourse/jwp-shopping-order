package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/cart/order")
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> addOrder(Member member, @RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.add(member, orderRequest);

        return ResponseEntity.created(URI.create("/order/" + orderResponse.getId())).body(orderResponse);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> showOrder(Member member, @PathVariable Long orderId) {

        return ResponseEntity.ok(orderService.findById(member, orderId));
    }
}
