package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.request.OrderRequest;
import cart.dto.response.OrderDetailResponse;
import cart.dto.response.OrderResponse;
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
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderDetailResponse> createOrder(final Member member, @RequestBody OrderRequest orderRequest) {
        final OrderDetailResponse orderDetailResponse = orderService.order(orderRequest, member);
        return ResponseEntity.created(URI.create("/orders/" + orderDetailResponse.getId())).body(orderDetailResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailResponse> showOrderDetail(final Member member, @PathVariable Long id) {
        final OrderDetailResponse orderDetailResponse = orderService.showOrderDetail(member, id);
        return ResponseEntity.ok(orderDetailResponse);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> showAllOrders(final Member member) {
        final List<OrderResponse> orderResponses = orderService.showAllOrders(member);
        return ResponseEntity.ok(orderResponses);
    }
}
