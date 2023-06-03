package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.request.OrderRequest;
import cart.dto.response.OrderResponse;
import cart.dto.response.OrdersResponse;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderResponse> createOrder(Member member, @RequestBody OrderRequest orderRequest) {
        final OrderResponse orderResponse = orderService.createOrder(member.getId(), orderRequest);
        return ResponseEntity.created(URI.create("/orders/" + orderResponse.getOrderId())).body(orderResponse);
    }

    @GetMapping("/orders")
    public ResponseEntity<OrdersResponse> showOrders(Member member) {
        return ResponseEntity.ok(orderService.getOrderByMemberId(member.getId()));
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderResponse> showOrder(Member member, @PathVariable(name = "id") final Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }
}
