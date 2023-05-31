package com.woowahan.techcourse.order.ui;

import com.woowahan.techcourse.member.ui.resolver.Authentication;
import com.woowahan.techcourse.member.ui.resolver.MemberId;
import com.woowahan.techcourse.order.domain.OrderResult;
import com.woowahan.techcourse.order.service.OrderCommandService;
import com.woowahan.techcourse.order.service.OrderQueryService;
import com.woowahan.techcourse.order.service.dto.request.CreateOrderRequestDto;
import com.woowahan.techcourse.order.ui.dto.response.OrderIdResponse;
import com.woowahan.techcourse.order.ui.dto.response.OrderResponse;
import com.woowahan.techcourse.order.ui.dto.response.OrdersResponse;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
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
        OrderResult result = orderQueryService.findById(orderId);
        return ResponseEntity.ok(OrderResponse.from(result));
    }

    @GetMapping("/orders")
    public ResponseEntity<OrdersResponse> findAllByMemberId(@Authentication MemberId memberId) {
        List<OrderResult> result = orderQueryService.findAllByMemberId(memberId.getId());
        List<OrderResponse> orders = result.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new OrdersResponse(orders));
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderIdResponse> create(@Authentication MemberId memberId,
            @RequestBody @Valid CreateOrderRequestDto requestDto) {
        Long orderId = orderCommandService.createOrder(memberId.getId(), requestDto);
        return ResponseEntity.created(URI.create("/orders/" + orderId))
                .body(new OrderIdResponse(orderId));
    }
}
