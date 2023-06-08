package cart.ui.order;

import cart.application.service.order.OrderReadService;
import cart.application.service.order.OrderWriteService;
import cart.application.service.order.dto.CreateOrderDto;
import cart.application.service.order.dto.OrderResultDto;
import cart.domain.member.Member;
import cart.ui.order.dto.request.CreateOrderRequest;
import cart.ui.order.dto.response.OrderResponse;
import cart.ui.order.dto.response.OrdersResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderReadService orderReadService;
    private final OrderWriteService orderWriteService;

    public OrderController(final OrderReadService orderReadService, final OrderWriteService orderWriteService) {
        this.orderReadService = orderReadService;
        this.orderWriteService = orderWriteService;
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

    @PostMapping
    public ResponseEntity<Void> createOrder(
            final Member member,
            @RequestBody final CreateOrderRequest createOrderRequest
    ) {
        final Long orderId = orderWriteService.createOrder(member, CreateOrderDto.from(createOrderRequest));

        final String createOrderUri = generateCreateUri(orderId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, createOrderUri)
                .build();
    }

    private String generateCreateUri(final Long lineId) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(lineId)
                .toUriString();
    }

}
