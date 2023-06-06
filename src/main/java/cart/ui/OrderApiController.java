package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.request.OrderPostRequest;
import cart.dto.response.OrderPreviewResponse;
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

    @GetMapping
    public ResponseEntity<List<OrderPreviewResponse>> findAllOrdersByMember(final Member member) {
        final List<OrderPreviewResponse> allOrdersByMember = orderService.findAllOrdersByMember(member);

        return ResponseEntity.ok(allOrdersByMember);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findOrderById(final Member member, @PathVariable final Long id) {
        final OrderResponse orderResponse = orderService.findOrderById(member, id);

        return ResponseEntity.ok(orderResponse);
    }

    @PostMapping
    public ResponseEntity<Void> addOrder(final Member member, @RequestBody final OrderPostRequest request) {
        final Long savedOrderId = orderService.addOrder(member, request);

        return ResponseEntity.created(URI.create("/orders/" + savedOrderId)).build();
    }
}
