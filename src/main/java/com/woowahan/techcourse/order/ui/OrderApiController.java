package com.woowahan.techcourse.order.ui;

import com.woowahan.techcourse.member.domain.Member;
import com.woowahan.techcourse.order.domain.Order;
import com.woowahan.techcourse.order.service.OrderCommandService;
import com.woowahan.techcourse.order.service.OrderQueryService;
import com.woowahan.techcourse.order.service.dto.request.CreateOrderRequest;
import com.woowahan.techcourse.order.ui.dto.response.OrderIdResponse;
import com.woowahan.techcourse.order.ui.dto.response.OrderResponse;
import com.woowahan.techcourse.order.ui.dto.response.OrdersResponse;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderApiController {

    private final OrderCommandService orderCommandService;
    private final OrderQueryService orderQueryService;

    public OrderApiController(OrderCommandService orderCommandService, OrderQueryService orderQueryService) {
        this.orderCommandService = orderCommandService;
        this.orderQueryService = orderQueryService;
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderResponse> findById(@PathVariable long orderId) {
        Order result = orderQueryService.findById(orderId);
        return ResponseEntity.ok(OrderResponse.from(result));
    }

    @GetMapping("/orders")
    public ResponseEntity<OrdersResponse> findAllByMemberId(Member member) {
        List<Order> result = orderQueryService.findAllByMemberId(member.getId());
        return ResponseEntity.ok(OrdersResponse.from(result));
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderIdResponse> create(Member member,
            @RequestBody @Valid CreateOrderRequest requestDto) {
        Long orderId = orderCommandService.createOrder(member.getId(), requestDto);
        return ResponseEntity.created(URI.create("/orders/" + orderId))
                .body(new OrderIdResponse(orderId));
    }
}
