package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.OrderCreateRequest;
import cart.dto.OrderDetailResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

@RestController
@RequestMapping("/orders")
@Tag(name = "주문 관련 API", description = "주문 정보를 관리하는 API 입니다.")
@SecurityRequirement(name = "basic")
public class OrderController {

    private final OrderService orderService;

    public OrderController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "주문 생성")
    public ResponseEntity<Void> createOrder(
            Member member,
            @RequestBody OrderCreateRequest orderCreateRequest
    ) {
        Long orderId = orderService.saveOrder(member, orderCreateRequest);
        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }

    @GetMapping
    @Operation(summary = "멤버별 주문 전체 조회")
    public ResponseEntity<List<OrderDetailResponse>> findOrders(
            Member member
    ) {
        final List<OrderDetailResponse> response = orderService.findOrdersByMember(member);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "주문 상세 조회")
    public ResponseEntity<OrderDetailResponse> findOrderDetailById(
            Member member,
            @Parameter(description = "상세 정보를 조회할 주문 ID", example = "1") @PathVariable Long id) {
        final OrderDetailResponse response = orderService.findOrderDetailById(member, id);
        return ResponseEntity.ok(response);
    }
}
