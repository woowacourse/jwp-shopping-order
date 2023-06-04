package cart.ui;

import cart.application.OrderService;
import cart.config.auth.Principal;
import cart.dto.User;
import cart.dto.request.OrderRequest;
import cart.dto.response.OrderDetailResponse;
import cart.dto.response.OrderResponse;
import cart.dto.response.Response;
import cart.dto.response.ResultResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
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

@Api(tags = "Order Controller")
@RequestMapping("/orders")
@RestController
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @ApiOperation(value = "주문 요청", authorizations = {@Authorization("Basic")})
    @PostMapping
    public ResponseEntity<Response> createOrder(@RequestBody @Valid OrderRequest orderRequest,
                                                @Principal User user) {
        Long orderId = orderService.createOrder(orderRequest, user.getMemberId());
        return ResponseEntity.created(URI.create("/orders/" + orderId))
                .body(new Response("주문이 정상적으로 처리되었습니다."));
    }

    @ApiOperation(value = "주문 목록 조회", authorizations = {@Authorization("Basic")})
    @GetMapping
    public ResponseEntity<Response> findAllOrders(@Principal User user) {
        List<OrderResponse> orderResponses = orderService.findAllOrders(user.getMemberId());
        return ResponseEntity.ok()
                .body(new ResultResponse<>("주문이 조회되었습니다.", orderResponses));
    }

    @ApiOperation(value = "상세 주문 조회", authorizations = {@Authorization("Basic")})
    @GetMapping("/{orderId}")
    public ResponseEntity<Response> findOrderDetail(@PathVariable Long orderId,
                                                    @Principal User user) {
        OrderDetailResponse orderDetailResponse = orderService.findOrderById(orderId, user.getMemberId());
        return ResponseEntity.ok()
                .body(new ResultResponse<>("상세 주문이 조회되었습니다.", orderDetailResponse));
    }
}
