package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.response.OrderDetailResponse;
import cart.dto.request.OrderRequest;
import cart.dto.response.OrderResponse;
import cart.dto.response.Response;
import cart.dto.response.ResultResponse;
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
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Response> createOrder(
            @RequestBody @Valid OrderRequest orderRequest,
            Member member
    ) {
        Long orderId = orderService.createOrder(orderRequest, member);
        return ResponseEntity.created(URI.create("/orders/" + orderId))
                .body(new Response("주문이 정상적으로 처리되었습니다."));
    }

    @GetMapping
    public ResponseEntity<Response> findAllOrders(Member member) {
        List<OrderResponse> orderResponses = orderService.findAllOrders(member);
        return ResponseEntity.ok()
                .body(new ResultResponse<>("주문이 조회되었습니다.", orderResponses));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Response> findOrderDetail(@PathVariable Long orderId, Member member) {
        OrderDetailResponse orderDetailResponse = orderService.findOrderById(orderId, member);
        return ResponseEntity.ok()
                .body(new ResultResponse<>("상세 주문이 조회되었습니다.", orderDetailResponse));
    }
}
