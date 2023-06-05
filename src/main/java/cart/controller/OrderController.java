package cart.controller;

import cart.auth.Authenticate;
import cart.auth.Credentials;
import cart.controller.dto.OrderResponse;
import cart.service.dto.OrderRequest;
import cart.service.order.OrderProvider;
import cart.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@SecurityRequirement(name = "basicAuth")
@Tag(name = "주문", description = "주문을 관리한다.")
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderProvider orderProvider;

    public OrderController(final OrderService orderService, final OrderProvider orderProvider) {
        this.orderService = orderService;
        this.orderProvider = orderProvider;
    }

    @Operation(summary = "주문 요청", description = "주문을 요청한다.")
    @ApiResponse(
            responseCode = "201",
            description = "주문 성공"
    )
    @PostMapping
    public ResponseEntity<Void> order(@Valid @RequestBody final OrderRequest orderItemRequest) {
        final Long orderId = orderService.order(orderItemRequest);

        final URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{orderId}")
                .buildAndExpand(orderId)
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "주문 목록 조회", description = "전체 주문 목록을 조회한다.")
    @ApiResponse(
            responseCode = "200",
            description = "주문 목록 조회 성공"
    )
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrder(@Authenticate final Credentials credentials) {
        final List<OrderResponse> orderResponses = orderProvider.findOrderByMember(credentials);
        return ResponseEntity.ok(orderResponses);
    }
}
