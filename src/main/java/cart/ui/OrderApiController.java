package cart.ui;

import java.net.URI;
import java.util.List;

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

@RequestMapping("/orders")
@RestController
public class OrderApiController {

    @PostMapping
    public ResponseEntity<Void> createOrder(final Member member, @RequestBody final OrderRequest orderRequest) {
        return ResponseEntity.created(URI.create("/orders/1")).build();
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders() {
        return ResponseEntity.ok().body(
                List.of(
                        new OrderResponse(
                                1L,
                                "2023-05-26",
                                List.of(
                                        new OrderItemResponse(10L, "새우깡", 3L, 1500L, "http://example.com/dfdf"),
                                        new OrderItemResponse(22L, "감자깡", 1L, 1200L, "http://example.com/abcd")
                                ),
                                15000L,
                                1700L,
                                300L
                        ),
                        new OrderResponse(
                                3L,
                                "2023-05-25",
                                List.of(
                                        new OrderItemResponse(10L, "새우깡", 3L, 1500L, "http://example.com/dfdf"),
                                        new OrderItemResponse(22L, "감자깡", 1L, 1200L, "http://example.com/abcd")
                                ),
                                15000L,
                                1700L,
                                200L
                        )
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable final Long id) {
        return ResponseEntity.ok().body(new OrderResponse(
                1L,
                "2023-05-26",
                List.of(
                        new OrderItemResponse(10L, "새우깡", 3L, 1500L, "http://example.com/dfdf"),
                        new OrderItemResponse(22L, "감자깡", 1L, 1200L, "http://example.com/abcd")
                ),
                15000L,
                1700L,
                300L
        ));
    }
}
