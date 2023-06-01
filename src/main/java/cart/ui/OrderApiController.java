package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.request.OrderRequest;
import cart.dto.response.OrderResponse;
import cart.dto.response.SortedOrdersResponse;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> order(Member member, @RequestBody @Valid OrderRequest orderRequest) {
        Long persistOrderId = orderService.order(member, orderRequest.getCartItemIds(), orderRequest.getUsedPoints());

        return ResponseEntity.created(URI.create("/orders/" + persistOrderId)).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findByOrderId(Member member, @PathVariable Long id) {
        OrderResponse response = orderService.findByMemberAndOrderId(member, id);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<SortedOrdersResponse> findByLastIdAndSize(
            Member member, @RequestParam(defaultValue = "2147483647") Long lastId,
            @RequestParam(defaultValue = "20") int size) {
        SortedOrdersResponse response = orderService.findByMemberAndLastOrderId(member, lastId, size);

        return ResponseEntity.ok(response);
    }
}
