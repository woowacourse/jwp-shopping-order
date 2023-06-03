package cart.ui.api;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.order.OrderRequest;
import cart.dto.order.OrderResponse;
import cart.dto.order.OrderSimpleResponse;
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
    public ResponseEntity<Void> createOrder(Member member, @RequestBody OrderRequest request) {
        Long orderId = orderService.createOrder(member, request);
        return ResponseEntity.created(URI.create("/order/" + orderId)).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> showOrder(Member member, @PathVariable Long id) {
        OrderResponse orderResponse = orderService.findOrder(id, member);
        return ResponseEntity.ok().body(orderResponse);
    }

    @GetMapping
    public ResponseEntity<List<OrderSimpleResponse>> showOrders(Member member) {
        List<OrderSimpleResponse> orderSimpleResponses = orderService.findAllByMember(member);
        return ResponseEntity.ok().body(orderSimpleResponses);
    }
}
