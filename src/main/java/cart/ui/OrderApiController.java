package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> addOrder(final Member member, @RequestBody final OrderRequest orderRequest) {
        final Long createdId = orderService.add(member, orderRequest);
        return ResponseEntity.created(URI.create("/orders/" + createdId)).build();
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> showOrders(final Member member) {
        final List<OrderResponse> orders = orderService.findOrdersByMember(member);
        return ResponseEntity.ok().body(orders);
    }
}
