package cart.ui.controller;

import cart.application.OrderService;
import cart.domain.member.Member;
import cart.ui.controller.dto.request.OrderRequest;
import cart.ui.controller.dto.response.OrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@SecurityRequirement(name = "basic")
@Tag(name = "주문", description = "주문 API")
@RequestMapping("/orders")
@RestController
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "주문 목록 조회", description = "주문 목록을 조회한다.")
    @ApiResponse(responseCode = "200", description = "주문 목록 조회 성공")
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders(Member member) {
        List<OrderResponse> response = orderService.getOrders(member);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "주문 조회", description = "주문을 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주문 조회 성공"),
            @ApiResponse(responseCode = "400", description = "주문 조회 실패"),
            @ApiResponse(responseCode = "404", description = "등록되지 않은 데이터(주문) 요청")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderDetail(Member member, @PathVariable Long id) {
        OrderResponse response = orderService.getOrderDetail(id, member);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "주문 진행", description = "주문을 진행한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주문 진행 성공"),
            @ApiResponse(responseCode = "400", description = "주문 진행 실패"),
            @ApiResponse(responseCode = "404", description = "등록되지 않은 데이터(장바구니 상품) 요청")
    })
    @PostMapping
    public ResponseEntity<Void> processOrder(Member member, @RequestBody @Valid OrderRequest orderRequest) {
        Long orderId = orderService.processOrder(member, orderRequest);
        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }
}
