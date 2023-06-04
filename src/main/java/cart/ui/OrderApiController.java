package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.request.OrderRequest;
import cart.dto.response.OrderResponse;
import java.net.URI;
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
    public ResponseEntity<OrderResponse> createOrder(final Member member, @RequestBody OrderRequest orderRequest) {
        final OrderResponse orderResponse = orderService.order(orderRequest, member);
        return ResponseEntity.created(URI.create("/orders/" + orderResponse.getId())).body(orderResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> showOrderDetail(final Member member, @PathVariable Long id) {
        final OrderResponse orderResponse = orderService.showOrderDetail(member, id);
        return ResponseEntity.ok(orderResponse);
    }
}
