package cart.presentation;

import cart.domain.Member;
import cart.dto.request.OrderRequest;
import cart.dto.response.OrderResponse;
import cart.service.OrderService;
import java.net.URI;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderApiController {

    private final OrderService orderService;

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
