package cart.ui.order;

import cart.application.service.order.OrderReadService;
import cart.application.service.order.dto.OrderResultDto;
import cart.ui.MemberAuth;
import cart.ui.order.dto.OrderResponse;
import cart.ui.order.dto.OrdersResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderReadController {

    private final OrderReadService orderReadService;

    public OrderReadController(OrderReadService orderReadService) {
        this.orderReadService = orderReadService;
    }

    @GetMapping
    public ResponseEntity<OrdersResponse> findOrders(MemberAuth memberAuth) {
        List<OrderResultDto> orderDtosByMember = orderReadService.findOrdersByMember(memberAuth);
        return ResponseEntity.ok(new OrdersResponse(orderDtosByMember));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> findProductsByOrder(MemberAuth memberAuth, @PathVariable Long orderId) {
        return ResponseEntity.ok(OrderResponse.from(orderReadService.findOrderBy(orderId)));
    }

}
