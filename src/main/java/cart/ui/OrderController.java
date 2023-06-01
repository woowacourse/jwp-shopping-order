package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.OrderCreateRequest;
import cart.dto.OrderDetailResponse;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping
    public ResponseEntity<List<OrderDetailResponse>> findOrders(Member member) {
        final List<OrderDetailResponse> response = orderService.findOrders(member);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailResponse> findOrderDetailById(Member member, @PathVariable Long id) {
        final OrderDetailResponse response = orderService.findOrderDetailById(member, id);
        return ResponseEntity.ok(response);
    }
}
