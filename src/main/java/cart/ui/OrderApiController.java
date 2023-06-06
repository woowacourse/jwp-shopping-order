package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.OrderDetailResponse;
import cart.dto.OrderRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "order", description = "주문 API")
@SecurityRequirement(name = "basicAuth")
@RestController
@RequestMapping("/orders")
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(
            summary = "주문",
            description = "주문한다.",
            responses = {
                    @ApiResponse(description = "주문 성공", responseCode = "201", headers = {@Header(name = "Location")}),
                    @ApiResponse(description = "인증에러", responseCode = "401"),
                    @ApiResponse(description = "도중에 발생한 에러", responseCode = "409"),
                    @ApiResponse(description = "알 수 없는 에러", responseCode = "500")
            }
    )
    @PostMapping
    public ResponseEntity<Void> createOrder(@Parameter(name = "member", description = "인증된 멤버", required = true, hidden = true) Member member, @RequestBody OrderRequest orderRequest) {
        Long orderId = orderService.createOrder(member, orderRequest);

        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }

    @Operation(
            summary = "주문 전체 조회",
            description = "주문을 모두 조회한다.",
            responses = {
                    @ApiResponse(description = "조회 성공", responseCode = "200"),
                    @ApiResponse(description = "인증에러", responseCode = "401")
            }
    )
    @GetMapping
    public ResponseEntity<List<OrderDetailResponse>> showOrders(@Parameter(name = "member", description = "인증된 멤버", required = true, hidden = true) Member member) {
        return ResponseEntity.ok(orderService.findOrderByMember(member));
    }

    @Operation(
            summary = "주문 단일 조회",
            description = "주문을 하나 조회한다.",
            responses = {
                    @ApiResponse(description = "조회 성공", responseCode = "200"),
                    @ApiResponse(description = "인증에러", responseCode = "401")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailResponse> showOrderDetail(@Parameter(name = "member", description = "인증된 멤버", required = true, hidden = true) Member member,
                                                               @Parameter(description = "주문 id", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(orderService.findOrderDetailByOrderId(id));
    }
}
