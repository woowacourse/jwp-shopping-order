package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.OrderDetailResponse;
import cart.dto.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return ResponseEntity.ok(orderService.findByMember(member));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailResponse> showOrderDetail(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.findById(id));
    }
}
