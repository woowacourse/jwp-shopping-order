package cart.ui;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.request.OrderRequest;
import cart.dto.response.OrderDetailResponse;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderApiController {
    private final OrderService orderService;

    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> orderSave(Member member,
                                          @Validated @RequestBody OrderRequest orderRequest) {
        Long id = orderService.save(member, orderRequest);
        return ResponseEntity.created(URI.create("/orders/" + id)).build();
    }

    @GetMapping
    public ResponseEntity<List<OrderDetailResponse>> orderDetails(Member member) {
        return ResponseEntity.ok(orderService.findByMember(member));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailResponse> orderDetails(Member member,
                                                            @PathVariable Long id) {
        return ResponseEntity.ok(orderService.findById(member, id));
    }
}
