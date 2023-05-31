package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.OrderDetailResponse;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    public ResponseEntity<Void> addOrder(final Member member, @RequestBody final OrderRequest orderRequest) {
        final Long createdId = orderService.add(member, orderRequest);
        return ResponseEntity.created(URI.create("/orders/" + createdId)).build();
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> showOrders(final Member member) {
        final List<OrderResponse> orders = orderService.findOrdersByMember(member);
        return ResponseEntity.ok().body(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponse> showOrderDetail(final Member member, @PathVariable final Long orderId) {
        final OrderDetailResponse order = orderService.findOrderDetailById(member, orderId);
        return ResponseEntity.ok().body(order);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(final Member member, @PathVariable final Long orderId) {
        orderService.remove(member, orderId);
        return ResponseEntity.noContent().build();
    }
}
