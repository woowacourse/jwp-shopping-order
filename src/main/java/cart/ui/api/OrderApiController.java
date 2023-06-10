package cart.ui.api;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.order.DiscountPolicyResponse;
import cart.dto.order.OrderRequest;
import cart.dto.order.OrderResponse;
import cart.dto.order.OrderSimpleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

@Tag(name = "주문(order)", description = "주문에 대한 api 입니다.")
@RestController
@RequestMapping("/orders")
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "주문 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED", headers = @Header(name = "Location", description = "orders/추가된 주문 ID")),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")})
    @PostMapping
    public ResponseEntity<Void> createOrder(@Parameter(description = "사용자 정보") Member member,
                                            @Parameter(description = "추가할 주문 정보") @RequestBody @Valid OrderRequest request) {
        Long orderId = orderService.createOrder(member, request);
        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }

    @Operation(summary = "주문 내역 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")})
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> showOrder(@Parameter(description = "사용자 정보") Member member,
                                                   @Parameter(description = "조회할 주문 ID") @PathVariable Long id) {
        OrderResponse orderResponse = orderService.findOrder(id, member);
        return ResponseEntity.ok().body(orderResponse);
    }

    @Operation(summary = "전체 주문 내역 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                    array = @ArraySchema(schema = @Schema(implementation = OrderSimpleResponse.class)))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")})
    @GetMapping
    public ResponseEntity<List<OrderSimpleResponse>> showOrders(@Parameter(description = "사용자 정보") Member member) {
        List<OrderSimpleResponse> orderSimpleResponses = orderService.findAllByMember(member);
        return ResponseEntity.ok().body(orderSimpleResponses);
    }

    @Operation(summary = "할인 정책 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = DiscountPolicyResponse.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")})
    @GetMapping("/discount-policies")
    public ResponseEntity<DiscountPolicyResponse> showDiscountPolicies() {
        DiscountPolicyResponse discountPolicyResponse = orderService.getDiscountInfo();
        return ResponseEntity.ok().body(discountPolicyResponse);
    }
}
