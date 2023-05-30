package cart.ui;

import cart.application.OrderItemService;
import cart.domain.Member;
import cart.dto.order.OrdersResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderItemApiController {

    private final OrderItemService orderItemService;

    public OrderItemApiController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

//    @PostMapping
//    public ResponseEntity<Void> createOrder(Member member, @RequestBody OrderItemRequest request) {
//        Long id = orderItemService.createOrder(member.getId(),request);
//        return ResponseEntity.created(URI.create("/orders/" + id)).build();
//    }

    @GetMapping
    public ResponseEntity<List<OrdersResponse>> showOrders(Member member) {
        return ResponseEntity.ok(orderItemService.findAllOrdersByMember(member));
    }
}
