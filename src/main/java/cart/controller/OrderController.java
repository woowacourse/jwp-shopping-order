package cart.controller;

import cart.auth.Auth;
import cart.auth.Credential;
import cart.domain.order.Order;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.exception.ExceptionResponse;
import cart.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@SecurityRequirement(name = "basicAuth")
@Tag(name = "주문", description = "장바구니의 상품을 주문한다")
@RequestMapping("/orders")
@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "장바구니 상품 주문", description = "장바구니에 담긴 상품을 주문한다.")
    @PostMapping
    public ResponseEntity<Void> save(@Auth final Credential credential, @RequestBody final OrderRequest request) {
        final Order order = orderService.save(credential.getMemberId(), request);
        return ResponseEntity.created(URI.create("/orders/" + order.getId())).build();
    }

    @Operation(summary = "사용자 주문 조회", description = "사용자의 모든 주문 내역을 조회한다")
    @GetMapping
    public ResponseEntity<List<OrderResponse>> findAll(@Auth final Credential credential) {
        final List<Order> orders = orderService.findAll(credential.getMemberId());
        final List<OrderResponse> orderResponses = orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderResponses);
    }

    @Operation(summary = "단일 주문 조회", description = "단일 주문을 조회한다")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "단일 주문 조회 성공."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "단일 주문 조회 실패",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            )
    })
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> findById(@Auth final Credential credential, @PathVariable final Long orderId) {
        final Order order = orderService.findById(credential.getMemberId(), orderId);
        return ResponseEntity.ok(OrderResponse.from(order));
    }
}
