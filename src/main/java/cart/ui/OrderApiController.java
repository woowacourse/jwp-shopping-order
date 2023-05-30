package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.request.CartItemRequest;
import cart.dto.request.OrderRequest;
import cart.dto.response.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/orders")
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> saveOrder(Member member, @RequestBody OrderRequest orderRequest) {
        long orderId = orderService.saveOrder(member, orderRequest);
        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderByOrderId(Member member, @PathVariable long orderId) {
        OrderResponse response = orderService.getOrderByOrderId(orderId);
        return ResponseEntity.ok().body(response);
    }

}
