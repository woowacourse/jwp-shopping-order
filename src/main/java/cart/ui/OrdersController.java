package cart.ui;

import cart.application.OrdersService;
import cart.domain.Member;
import cart.dto.OrdersRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    private final OrdersService ordersService;

    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @PostMapping
    public ResponseEntity<Void> takeOrders(Member member, @RequestBody OrdersRequest ordersRequest){
        final long ordersId = ordersService.takeOrders(member,ordersRequest);
        return ResponseEntity.created(URI.create("orders/" + ordersId)).build();
    }
}
