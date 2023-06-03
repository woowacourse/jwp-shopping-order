package cart.ui.order;

import cart.application.service.order.OrderReadService;
import cart.application.service.order.dto.OrderResultDto;
import cart.domain.member.Member;
import cart.ui.order.dto.response.OrderResponse;
import cart.ui.order.dto.response.OrdersResponse;
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

    public OrderReadController(final OrderReadService orderReadService) {
        this.orderReadService = orderReadService;
    }

    @GetMapping
    public ResponseEntity<OrdersResponse> findOrders(final Member member) {
        final List<OrderResultDto> orderDtos = orderReadService.findAllByMember(member);

        return ResponseEntity.ok(OrdersResponse.from(orderDtos));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> findProductsByOrder(
            final Member member,
            @PathVariable("orderId") Long orderId
    ) {
        final OrderResultDto orderResultDto = orderReadService.findMemberOrderByOrderId(member, orderId);
        return ResponseEntity.ok(OrderResponse.from(orderResultDto));
    }

}
