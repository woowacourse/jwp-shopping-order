package cart.ui;

import java.net.URI;
import java.util.List;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.OrderItemResponse;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
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
    public ResponseEntity<OrderResponse> createOrder(final Member member, @RequestBody final OrderRequest orderRequest) {
        final OrderResponse orderResponse = orderService.createOrder(member, orderRequest);
        return ResponseEntity.created(URI.create("/orders/" + orderResponse.getOrderId())).body(orderResponse);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders(final Member member) {
        final List<OrderResponse> orderResponses = orderService.getOrders(member);
        return ResponseEntity.ok().body(orderResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable final Long id) {
        return ResponseEntity.ok().body(new OrderResponse(
                1L,
                "2023-05-26",
                List.of(
                        new OrderItemResponse(10L, "새우깡", 3, 1500, "http://example.com/dfdf"),
                        new OrderItemResponse(22L, "감자깡", 1, 1200, "http://example.com/abcd")
                ),
                15000,
                1700,
                300
        ));
    }
}
