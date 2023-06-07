package cart.presentation;

import cart.domain.Member;
import cart.dto.request.OrderRequest;
import cart.dto.response.OrderResponse;
import cart.service.OrderService;
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
    public ResponseEntity<Void> order(final Member member, final @RequestBody OrderRequest orderRequest) {
        long orderId = orderService.order(member, orderRequest);

        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders(final Member member) {
        List<OrderResponse> orderResponses = orderService.getAllOrderBy(member.getId());

        return ResponseEntity.ok(orderResponses);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(final @PathVariable Long orderId) {
        OrderResponse orderResponse = orderService.findOrderBy(orderId);

        return ResponseEntity.ok(orderResponse);
    }
}
