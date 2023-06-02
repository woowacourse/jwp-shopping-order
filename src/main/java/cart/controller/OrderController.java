package cart.controller;

import cart.controller.request.OrderRequestDto;
import cart.controller.response.OrderResponseDto;
import cart.domain.Member;
import cart.service.OrderService;
import java.net.URI;
import org.springframework.http.ResponseEntity;
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

        return ResponseEntity.created(URI.create("/order/" + orderResponseDto.getId()))
                .body(orderResponseDto);
    }

}