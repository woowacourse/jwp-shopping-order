package shop.web.controller.order;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.application.order.OrderService;
import shop.application.order.dto.OrderCreationDto;
import shop.application.order.dto.OrderDetailDto;
import shop.application.order.dto.OrderDto;
import shop.application.order.dto.OrderProductDto;
import shop.domain.member.Member;
import shop.web.controller.order.dto.request.OrderCreationRequest;
import shop.web.controller.order.dto.request.OrderItemRequest;
import shop.web.controller.order.dto.response.OrderDetailResponse;
import shop.web.controller.order.dto.response.OrderHistoryResponse;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderRestController {
    private final OrderService orderService;

    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(Member member, @RequestBody OrderCreationRequest request) {
        List<OrderProductDto> orderProductDtos = toOrderProducts(request.getItems());
        OrderCreationDto orderCreationDto = OrderCreationDto.of(orderProductDtos, request.getCouponId());

        Long orderId = orderService.order(member, orderCreationDto);

        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }

    private List<OrderProductDto> toOrderProducts(List<OrderItemRequest> orderItmes) {
        return orderItmes.stream()
                .map(item -> new OrderProductDto(item.getProductId(), item.getQuantity()))
                .collect(Collectors.toList());
    }

    @GetMapping
    public ResponseEntity<List<OrderHistoryResponse>> getAllOrderHistory(Member member) {
        List<OrderDto> orders = orderService.getAllOrderHistoryOfMember(member);

        List<OrderHistoryResponse> orderHistoryResponses = OrderHistoryResponse.of(orders);

        return ResponseEntity.ok(orderHistoryResponses);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponse> getOrderDetails(Member member, @PathVariable Long orderId) {
        OrderDetailDto orderDetailDto = orderService.getOrderDetailsOfMember(member, orderId);

        OrderDetailResponse orderDetailResponse = OrderDetailResponse.of(orderDetailDto);

        return ResponseEntity.ok(orderDetailResponse);
    }
}
