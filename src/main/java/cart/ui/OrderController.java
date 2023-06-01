package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.OrderConfirmResponse;
import cart.dto.order.OrderDetailResponse;
import cart.dto.order.OrderRequest;
import cart.dto.order.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Void> addOrder(Member member, @RequestBody OrderRequest orderRequest) {
        Long id = orderService.addOrder(member, orderRequest);
        return ResponseEntity.created(URI.create("/orders/" + id)).build();
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> findOrdersOfMember(Member member) {
        return ResponseEntity.ok(orderService.findOrdersByMember(member));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailResponse> findOrderById(Member member, @PathVariable Long id) {
        return ResponseEntity.ok(orderService.findOrderById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderById(Member member, @PathVariable Long id) {
        orderService.deleteOrderById(member, id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<OrderConfirmResponse> confirmOrder(Member member, @PathVariable Long id) {
        return ResponseEntity.ok(orderService.confirmOrder(id));
    }
}
