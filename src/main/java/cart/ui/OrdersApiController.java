package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrdersApiController {

    private final OrderService orderService;

    public OrdersApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getPastOrders(Member member) {
        return ResponseEntity.ok(orderService.findOrdersByMember(member));
    }

    @PostMapping
    public ResponseEntity<Void> order(Member member, OrderRequest orderRequest) {
        Long orderId = orderService.orderItems(member.getId(), orderRequest);
        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }
}
