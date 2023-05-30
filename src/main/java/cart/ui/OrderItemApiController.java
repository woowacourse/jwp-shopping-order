package cart.ui;

import cart.application.OrderItemService;
import cart.domain.Member;
import cart.dto.order.OrderItemRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/orders")
public class OrderItemApiController {

    private final OrderItemService orderItemService;

    public OrderItemApiController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(Member member, @RequestBody OrderItemRequest request) {
        Long id = orderItemService.createOrder(member.getId(),request);
        return ResponseEntity.created(URI.create("/orders/" + id)).build();
    }
}
