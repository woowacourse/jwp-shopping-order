package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.request.OrderRequestDto;
import cart.dto.response.OrderDetail;
import cart.dto.response.OrderInfo;
import cart.dto.response.OrderResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> order(final Member member, final @RequestBody OrderRequestDto orderRequestDto) {
        return ResponseEntity.ok(orderService.order(member, orderRequestDto));
    }

    @GetMapping
    public ResponseEntity<List<OrderInfo>> orderList(final Member member) {
        return ResponseEntity.ok(orderService.findOrderOf(member));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetail> orderDetail(final Member member, @PathVariable final Integer orderId) {
        return ResponseEntity.ok(orderService.findOrderDetail(member, orderId));
    }
}
