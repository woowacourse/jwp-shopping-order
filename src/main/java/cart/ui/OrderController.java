package cart.ui;

import cart.service.OrderService;
import cart.domain.Member;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(Member member, @RequestBody @Valid OrderRequest orderRequest) {
        Long id = orderService.createOrder(member, orderRequest);

        return ResponseEntity.created(URI.create("/orders/" + id)).build();
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders(Member member) {
        List<OrderResponse> orders = orderService.findAll(member.getId());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderDetail(Member member, @PathVariable Long id) {
        OrderResponse orderResponse = orderService.findById(member, id);
        return ResponseEntity.ok().body(orderResponse);
    }
}
