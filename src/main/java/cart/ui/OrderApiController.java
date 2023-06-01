package cart.ui;

import cart.domain.Member;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> showOrders(Member member) {
        return ResponseEntity.ok().body(orderService.findAll(member));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> showOrder(Member member, @PathVariable Long id) {
        OrderResponse response = orderService.findById(id);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(Member member, @RequestBody @Valid OrderRequest request) {
        Long orderId = orderService.order(member, request);
        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }
}
