package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.OrderListResponse;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
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
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> order(@RequestBody final OrderRequest request, final Member member) {
        final OrderResponse response = orderService.order(request, member);
        return ResponseEntity.created(URI.create("/order/" + response.getId()))
            .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findOrderById(@PathVariable final Long id) {
        final OrderResponse response = orderService.findOrder(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderListResponse>> findOrderByMember(final Member member) {
        final List<OrderListResponse> responses = orderService.findOrder(member);
        return ResponseEntity.ok(responses);
    }
}
