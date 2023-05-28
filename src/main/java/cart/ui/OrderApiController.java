package cart.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderApiController {

    @PostMapping
    public ResponseEntity<Void> createOrder() {
        return ResponseEntity.created(URI.create("orders/" + 1 + "/products")).build();
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> findOrders() {
        return ResponseEntity.ok(List.of(new OrderResponse(1L, Collections.emptyList())));
    }

    @GetMapping("{orderId}")
    public ResponseEntity<List<OrderItemResponse>> findProductsByOrder(@PathVariable("orderId") Long orderId) {
        return ResponseEntity.ok(List.of(new OrderItemResponse(1L, "레오배변패드", 10000000, 100, "2023-05-28 20:37:45", 3, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRTv8IeCd6YNPDF4jXyf2AvQo8LcP4KK0qERw&usqp=CAU")));
    }
}
