package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.dto.OrderResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/orders")
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> addOrder(Member member, @RequestBody OrderRequest orderRequest) {
        Long orderId = orderService.addOrder(member, orderRequest);
        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }

    @GetMapping
    public ResponseEntity<OrderResponses> showOrders(@RequestParam Integer page, @RequestParam Integer size, Member member) {
        OrderResponses orderResponses = orderService.showOrders(member,page,size);
        return ResponseEntity.ok(orderResponses);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> showOrder(Member member, @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.showOrder(member, orderId));
    }
}
