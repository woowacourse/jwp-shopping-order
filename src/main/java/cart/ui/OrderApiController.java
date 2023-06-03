package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.OrderDetailResponse;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
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

    @PostMapping
    public ResponseEntity<Void> order(Member member, @RequestBody @Valid OrderRequest orderRequest) {
        orderService.addOrder(member, orderRequest);
        return ResponseEntity.created(URI.create("/orders")).build();
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
