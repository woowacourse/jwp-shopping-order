package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.request.OrderRequest;
import cart.dto.response.OrderAdditionResponse;
import cart.dto.response.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<OrderAdditionResponse> saveOrder(Member member, @RequestBody OrderRequest orderRequest) {
        OrderAdditionResponse response = orderService.saveOrder(member, orderRequest);
        return ResponseEntity.created(URI.create("/orders/" + response.getOrderId())).body(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderByOrderId(Member member, @PathVariable long orderId) {
        OrderResponse response = orderService.getOrderByOrderId(member, orderId);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrdersByMember(Member member) {
        List<OrderResponse> response = orderService.getOrdersByMember(member);
        return ResponseEntity.ok().body(response);
    }

}
