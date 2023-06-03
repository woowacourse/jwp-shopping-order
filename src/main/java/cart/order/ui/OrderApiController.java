package cart.order.ui;

import cart.member.domain.Member;
import cart.order.application.OrderService;
import cart.order.application.dto.OrderDto;
import cart.order.ui.request.OrderCartItemRequest;
import cart.order.ui.request.OrderCartItemsRequest;
import cart.order.ui.response.OrderResponse;
import cart.order.ui.response.OrdersResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<OrdersResponse> getOrders(Member member) {
        final List<OrderDto> orderDtos = orderService.findAllByMemberId(member.getId());

        final List<OrderResponse> orderResponses = orderDtos.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());

        final OrdersResponse response = new OrdersResponse(orderResponses);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{cartOrderId}")
    public ResponseEntity<OrderResponse> getOrder(Member member,
                                                  @PathVariable final Long cartOrderId) {
        final OrderDto orderDto = orderService.findByCartOrderId(cartOrderId);

        final OrderResponse response = OrderResponse.from(orderDto);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> postOrder(Member member,
                                          @RequestBody final OrderCartItemsRequest request) {
        final List<OrderCartItemRequest> orderCartItemDtos = request.getOrderCartItemDtos();

        final Long cartOrderId = orderService.addCartOrder(member, orderCartItemDtos);

        return ResponseEntity.created(URI.create("/orders/" + cartOrderId)).build();
    }
}
