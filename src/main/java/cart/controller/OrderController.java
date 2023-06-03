package cart.controller;

import cart.application.OrderService;
import cart.domain.member.Member;
import cart.dto.request.OrderRequest;
import cart.dto.response.OrderResponse;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> addOrder(final Member member, @Valid @RequestBody OrderRequest orderRequest) {
        final Long orderId = orderService.addOrder(member, orderRequest);

        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> showOrders(final Member member) {
        return ResponseEntity.ok().body(orderService.findMemberOrders(member));
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderResponse> showOrderById(final Member member, @PathVariable("id") final Long orderId) {
        return ResponseEntity.ok().body(orderService.findByOrderId(member, orderId));
    }

}
