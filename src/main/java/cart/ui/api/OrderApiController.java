package cart.ui.api;

import cart.application.OrderService;
import cart.domain.member.Member;
import cart.dto.order.OrderRequest;
import cart.dto.order.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@Validated
@RestController
@RequestMapping("/orders")
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(Member member, @RequestBody @Valid OrderRequest orderRequest) {
        Long orderId = orderService.orderCartItems(member, orderRequest);
        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> showAllOrders(Member member) {
        List<OrderResponse> orderResponses = orderService.getOrdersByMember(member);
        return ResponseEntity.ok().body(orderResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> showOrderDetail(Member member, @PathVariable @Positive Long id) {
        OrderResponse orderResponse = orderService.getOrderById(member, id);
        return ResponseEntity.ok().body(orderResponse);
    }
}
