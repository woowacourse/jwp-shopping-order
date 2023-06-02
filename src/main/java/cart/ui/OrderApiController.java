package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.dto.OrdersResponse;
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

    @GetMapping
    public ResponseEntity<OrdersResponse> showAllOrders(Member member) {
        return ResponseEntity.ok(orderService.findAll(member));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> removeOrder(Member member, @PathVariable Long id) {
        orderService.remove(member, id);

        return ResponseEntity.noContent().build();
    }
}
