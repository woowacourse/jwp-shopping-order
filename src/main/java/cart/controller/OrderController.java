package cart.controller;

import cart.auth.Auth;
import cart.auth.Credential;
import cart.dto.order.OrderResponse;
import cart.dto.order.OrderSaveRequest;
import cart.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

@SecurityRequirement(name = "basicAuth")
@Tag(name = "주문", description = "주문을 관리한다.")
@RequestMapping("/orders")
@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "상품 주문", description = "장바구니에 담은 상품을 주문한다.")
    @ApiResponse(
            responseCode = "201",
            description = "상품 주문 성공."
    )
    @PostMapping
    public ResponseEntity<Void> save(@Auth final Credential credential, @Valid @RequestBody OrderSaveRequest request) {
        final Long orderId = orderService.save(request, credential.getMemberId());
        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }

    @Operation(summary = "전체 주문 조회", description = "전체 주문을 조회한다.")
    @ApiResponse(
            responseCode = "200",
            description = "전체 주문 조회 성공.",
            content = @Content(schema = @Schema(implementation = OrderResponse.class))
    )
    @GetMapping
    public ResponseEntity<List<OrderResponse>> findAll(@Auth final Credential credential) {
        final List<OrderResponse> orderResponses = orderService.findAll(credential.getMemberId());
        return ResponseEntity.ok(orderResponses);
    }

    @Operation(summary = "단일 주문 조회", description = "주문을 조회한다.")
    @ApiResponse(
            responseCode = "200",
            description = "단일 주문 조회 성공.",
            content = @Content(schema = @Schema(implementation = OrderResponse.class))
    )
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findById(@Auth final Credential credential, @PathVariable Long id) {
        final OrderResponse orderResponse = orderService.findById(id, credential.getMemberId());
        return ResponseEntity.ok(orderResponse);
    }
}
