package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.request.OrderRequest;
import cart.dto.response.OrderIdResponse;
import cart.dto.response.OrderResponse;
import cart.dto.response.OrdersResponse;
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
    public ResponseEntity<OrderIdResponse> order(Member member, @RequestBody OrderRequest orderRequest) {
        OrderIdResponse response = orderService.makeOrder(member, orderRequest);

        return ResponseEntity.created(URI.create("/orders/" + response.getOrderId()))
                .body(response);
    }

    @GetMapping
    public ResponseEntity<OrdersResponse> getAllOrderInfo(Member member) {
        OrdersResponse response = orderService.findAllOrderInfo(member);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderInfo(Member member, @PathVariable(value = "orderId") Long orderId) {
        OrderResponse response = orderService.findOrderInfo(member, orderId);

        return ResponseEntity.ok(response);
    }
}
