package cart.ui;

import java.net.URI;
import java.util.List;

import cart.dto.OrderItemResponse;
import cart.dto.OrderResponse;
import cart.dto.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MockController {

    @GetMapping("/users")
    public ResponseEntity<UserResponse> getUser() {
        return ResponseEntity.ok().body(new UserResponse("odo27@naver.com", 1000L, 5));
    }

    @PostMapping("/orders")
    public ResponseEntity<Void> createOrder() {
        return ResponseEntity.created(URI.create("/orders/1")).build();
    }

    @GetMapping("/orders")
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

    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderResponse> getOrders(@PathVariable final Long id) {
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
