package cart.order.presentation;

import cart.member.domain.Member;
import cart.order.application.OrderQueryService;
import cart.order.application.OrderService;
import cart.order.application.dto.PlaceOrderCommand;
import cart.order.presentation.dto.OrderResponse;
import cart.order.presentation.dto.OrderResponses;
import cart.order.presentation.dto.PlaceOrderRequest;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/orders")
@RestController
public class OrderApiController {

    private final OrderService orderService;
    private final OrderQueryService orderQueryService;

    public OrderApiController(OrderService orderService, OrderQueryService orderQueryService) {
        this.orderService = orderService;
        this.orderQueryService = orderQueryService;
    }

    @PostMapping
    ResponseEntity<Void> placeOrder(
            Member member,
            @RequestBody PlaceOrderRequest request
    ) {
        PlaceOrderCommand command = new PlaceOrderCommand(member.getId(), request.getCartItemIds(), request.getCouponIds());
        Long id = orderService.place(command);
        return ResponseEntity.created(URI.create("/orders/" + id)).build();
    }

    @GetMapping
    ResponseEntity<OrderResponses> findAllByMember(Member member) {
        OrderResponses orderResponses = orderQueryService.findAllByMemberId(member.getId());
        return ResponseEntity.ok(orderResponses);
    }

    @GetMapping("/{id}")
    ResponseEntity<OrderResponse> findById(
            @PathVariable Long id,
            Member member
    ) {
        OrderResponse orderResponse = orderQueryService.findByIdAndMemberId(id, member.getId());
        return ResponseEntity.ok(orderResponse);
    }
}

