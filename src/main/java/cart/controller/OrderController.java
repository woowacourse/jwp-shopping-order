package cart.controller;

import cart.controller.request.OrderRequestDto;
import cart.controller.response.OrderResponseDto;
import cart.domain.Member;
import cart.service.OrderService;
import java.net.URI;
import java.util.List;
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
    public ResponseEntity<OrderResponseDto> addOrder(Member member, @RequestBody OrderRequestDto orderRequestDto) {
        OrderResponseDto orderResponseDto = orderService.orderCartItems(member, orderRequestDto);

        return ResponseEntity.created(URI.create("/orders/" + orderResponseDto.getId()))
                .body(orderResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAllOrder(Member member) {
        return ResponseEntity.ok()
                .body(orderService.findAllOrder(member));
    }

    @GetMapping("/{order-id}")
    public ResponseEntity<OrderResponseDto> getOrder(Member member, @PathVariable(name = "order-id") Long orderId) {
        return ResponseEntity.ok()
                .body(orderService.findOrderById(member, orderId));
    }

}
