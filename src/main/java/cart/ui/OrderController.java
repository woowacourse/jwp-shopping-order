package cart.ui;

import cart.domain.Member;
import cart.dto.DiscountResponse;
import cart.dto.OrderDetailResponse;
import cart.dto.OrderItemResponse;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.dto.PaymentResponse;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @PostMapping
    public ResponseEntity<Void> postOrder(Member member, @RequestBody OrderRequest orderRequest) {
        return ResponseEntity.created(URI.create("/orders/1")).build();
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders(Member member) {
        return ResponseEntity.ok(List.of(new OrderResponse(1L, null,
                        List.of(new OrderItemResponse("치킨", 1, "http://example.com/chicken.jpg", 10_000),
                                new OrderItemResponse("피자", 2, "http://example.com/pizza.jpg", 30_000),
                                new OrderItemResponse("보쌈", 1, "http://example.com/pizza.jpg", 15_000)
                        )
                )
        ));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponse> getOrderDetail(Member member, @PathVariable Long orderId) {
        return ResponseEntity.ok(new OrderDetailResponse(List.of(
                        new OrderItemResponse("치킨", 1, "http://example.com/chicken.jpg", 10_000),
                        new OrderItemResponse("피자", 2, "http://example.com/pizza.jpg", 30_000),
                        new OrderItemResponse("보쌈", 1, "http://example.com/pizza.jpg", 15_000)
                ), orderId, null, new PaymentResponse(55_000,
                        List.of(new DiscountResponse("5만원 이상 구매 시 10% 할인", 5_500)), 49_500, 3_500, 53_000))
        );
    }
}
