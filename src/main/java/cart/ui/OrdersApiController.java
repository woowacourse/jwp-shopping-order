package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.OrdersRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/orders")
public class OrdersApiController {

    private final OrderService orderService;

    public OrdersApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> order(Member member, OrdersRequest ordersRequest) {
        Long orderId = orderService.orderItems(member.getId(), ordersRequest);
        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }
}
