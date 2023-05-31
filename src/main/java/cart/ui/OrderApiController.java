package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.OrderDetailResponse;
import cart.dto.OrderRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(Member member, @RequestBody OrderRequest orderRequest) {
        Long orderId = orderService.createOrder(member, orderRequest);

        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }

    @GetMapping
    public ResponseEntity<List<OrderDetailResponse>> showOrders(Member member) {
        return ResponseEntity.ok(orderService.findOrderByMember(member));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailResponse> showOrderDetail(Member member, @PathVariable Long id) {
        return ResponseEntity.ok(orderService.findOrderDetailByOrderId(id));
    }
}
