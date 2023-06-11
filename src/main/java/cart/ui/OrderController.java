package cart.ui;

import cart.domain.Member;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.net.URI;
import java.util.List;

@Validated
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(Member member, @RequestBody @Valid OrderRequest orderRequest) {
        Long id = orderService.createOrder(member, orderRequest);

        return ResponseEntity.created(URI.create("/orders/" + id)).build();
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders(Member member) {
        List<OrderResponse> orders = orderService.findAll(member.getId());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderDetail(Member member,
                                                        @PathVariable("id") @Min(value = 1, message = "ID는 1 이상의 정수로 입력해주세요") Long id) {
        OrderResponse orderResponse = orderService.findById(member, id);
        return ResponseEntity.ok().body(orderResponse);
    }
}
