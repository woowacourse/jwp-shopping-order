package cart.ui;

import cart.application.OrdersService;
import cart.domain.Member;
import cart.dto.OrdersRequest;
import cart.dto.OrdersResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

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
    @GetMapping
    public ResponseEntity<List<OrdersResponse>> showMembersAllOrders(Member member){
        ordersService.findMembersAllOrders(member);
        return ResponseEntity.ok().body(ordersService.findMembersAllOrders(member));
    }
}
