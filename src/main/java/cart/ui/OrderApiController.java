package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.OrderCreateResponse;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.dto.OrderResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order", description = "주문 API")
@RestController
@RequestMapping("/orders")
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "주문 등록")
    @PostMapping
    public ResponseEntity<OrderCreateResponse> createOrderOfCart(
            Member member,
            @RequestBody OrderRequest orderRequest
    ) {
        OrderCreateResponse orderCreateResponse = orderService.add(member, orderRequest);
        return ResponseEntity
                .created(URI.create("/orders/" + orderCreateResponse.getOrderId()))
                .body(orderCreateResponse);
    }

    @Operation(summary = "사용자의 모든 주문 조회")
    @GetMapping
    public ResponseEntity<OrderResponses> findAllOrders(Member member) {
        return ResponseEntity.ok(orderService.findAll(member));
    }

    @Operation(summary = "특정 주문 조회")
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> findAllOrders(Member member, @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.findById(member, orderId));
    }
}
