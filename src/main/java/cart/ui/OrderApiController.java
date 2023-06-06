package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.response.OrderDetailResponse;
import cart.dto.request.OrderCreateRequest;
import cart.dto.response.OrderResponse;
import cart.dto.response.ShoppingOrderResponse;
import cart.dto.response.ShoppingOrderResultResponse;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
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
    private final OrderService orderService;

    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<ShoppingOrderResponse> createOrder(
            @RequestBody @Valid OrderCreateRequest orderCreateRequest,
            Member member
    ) {
        Long orderId = orderService.createOrder(orderCreateRequest, member);
        return ResponseEntity.created(URI.create("/orders/" + orderId))
                .body(new ShoppingOrderResponse("주문이 정상적으로 처리되었습니다."));
    }

    @GetMapping
    public ResponseEntity<ShoppingOrderResponse> findAllOrders(Member member) {
        List<OrderResponse> orderResponses = orderService.findAllOrders(member);
        return ResponseEntity.ok()
                .body(new ShoppingOrderResultResponse<>("주문이 조회되었습니다.", orderResponses));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ShoppingOrderResponse> findOrderDetail(@PathVariable Long orderId, Member member) {
        OrderDetailResponse orderDetailResponse = orderService.findOrderById(orderId, member);
        return ResponseEntity.ok()
                .body(new ShoppingOrderResultResponse<>("상세 주문이 조회되었습니다.", orderDetailResponse));
    }
}
