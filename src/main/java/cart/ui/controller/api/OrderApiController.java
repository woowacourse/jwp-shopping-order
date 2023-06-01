package cart.ui.controller.api;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.request.OrderCreateRequest;
import cart.dto.response.CartPointsResponse;
import cart.dto.response.OrderResponse;
import cart.ui.auth.Auth;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "주문 생성")
    @PostMapping("/orders")
    public ResponseEntity<Void> createOrder(@Auth Member member, @RequestBody OrderCreateRequest orderCreateRequest) {
        final Long id = orderService.createOrder(orderCreateRequest, member);

        return ResponseEntity.created(URI.create("/orders/" + id)).build();
    }

    @Operation(summary = "단건 주문 조회")
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderResponse> findSingleOrder(@PathVariable Long orderId, @Auth Member member) {
        final OrderResponse response = orderService.findById(orderId, member);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "전체 주문 조회")
    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> findAllOrders(@Auth Member member) {
        final List<OrderResponse> orderResponses = orderService.findAll(member);
        return ResponseEntity.ok(orderResponses);
    }

    @Operation(summary = "현재 주문 적립 포인트 조회")
    @GetMapping("/cart-points")
    public ResponseEntity<CartPointsResponse> calculatePoints(@Auth Member member) {
        final CartPointsResponse cartPointsResponse = orderService.calculatePoints(member);
        return ResponseEntity.ok(cartPointsResponse);
    }
}
