package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.EachOrderResponse;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.net.URI;
import java.util.List;

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
    public ResponseEntity<List<OrderResponse>> showOrders(Member member){
        List<OrderResponse> orderResponses = orderService.showOrders(member);
        return ResponseEntity.ok(orderResponses);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<EachOrderResponse> showOrder(Member member, @PathVariable Long orderId){
        return ResponseEntity.ok(orderService.showOrder(member, orderId));
    }
}
