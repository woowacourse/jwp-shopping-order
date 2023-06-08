package cart.ui;

import cart.application.OrderService;
import cart.domain.member.Member;
import cart.ui.dto.order.OrderDetailResponse;
import cart.ui.dto.order.OrderRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> order(Member member,
                                      @Valid @RequestBody OrderRequest request) {
        final Long orderId = orderService.order(member, request);
        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponse> orderDetail(Member member,
                                                           @PathVariable Long orderId) {
        final OrderDetailResponse orderDetail = orderService.getOrderDetail(member, orderId);
        return ResponseEntity.ok(orderDetail);
    }

    @GetMapping
    public ResponseEntity<List<OrderDetailResponse>> orderDetails(Member member) {
        final List<OrderDetailResponse> orderDetails = orderService.getAllOrderDetails(member);
        return ResponseEntity.ok(orderDetails);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteByIds(Member member,
                                            @RequestParam List<Long> orderId) {
        orderService.deleteByIds(member, orderId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAll(Member member) {
        orderService.deleteAll(member);
        return ResponseEntity.noContent().build();
    }
}
