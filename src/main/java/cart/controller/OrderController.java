package cart.controller;

import cart.auth.Auth;
import cart.auth.Credential;
import cart.dto.OrderSaveRequest;
import cart.dto.OrdersDto;
import cart.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "basicAuth")
@Tag(name = "주문", description = "주문을 관리한다.")
@RequestMapping("/orders")
@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "주문 추가", description = "장바구니 상품을 주문한다.")
    @ApiResponse(
            responseCode = "201",
            description = "주문 추가 성공."
    )
    @PostMapping
    public ResponseEntity<Void> order(@Auth final Credential credential, @RequestBody final OrderSaveRequest request) {
        final Long orderId = orderService.order(credential.getMemberId(), request);
        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }

    @Operation(summary = "주문 전체 조회", description = "해당 사용자의 주문내역을 전체 조회한다.")
    @ApiResponse(
            responseCode = "200",
            description = "주문 전체 조회 성공."
    )
    @GetMapping
    public ResponseEntity<List<OrdersDto>> findAllOrderByMemberId(@Auth final Credential credential) {
        final List<OrdersDto> findAllOrders = orderService.findAllByMemberId(credential.getMemberId());
        return ResponseEntity.ok(findAllOrders);
    }

    @Operation(summary = "주문 상세 조회", description = "해당 사용자의 주문내역을 상세 조회한다.")
    @ApiResponse(
            responseCode = "200",
            description = "주문 상세 조회 성공."
    )
    @GetMapping("/{orderId}")
    public ResponseEntity<OrdersDto> findOrderByMemberId(@Auth final Credential credential,
                                                         @PathVariable final Long orderId) {
        final OrdersDto orderDto = orderService.findByOrderId(credential.getMemberId(), orderId);
        return ResponseEntity.ok(orderDto);
    }
}
