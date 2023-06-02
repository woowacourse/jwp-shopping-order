package cart.ui.api;

import cart.application.OrderService;
import cart.application.request.OrderRequest;
import cart.application.response.OrderWithOutTotalPriceResponse;
import cart.application.response.OrderWithTotalPriceResponse;
import cart.domain.member.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RequestMapping("/orders")
@RestController
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> order(Member member, @Valid @RequestBody OrderRequest request) {
        Long orderId = orderService.createOrder(member, request);

        return ResponseEntity
                .status(CREATED)
                .location(URI.create("/orders/" + orderId))
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderWithTotalPriceResponse> findByOrderId(Member member, @PathVariable(name = "id") @NotNull Long orderId) {
        OrderWithTotalPriceResponse orderResponse = orderService.findByOrderId(member, orderId);

        return ResponseEntity
                .status(OK)
                .body(orderResponse);
    }

    @GetMapping
    public ResponseEntity<List<OrderWithOutTotalPriceResponse>> findAllByMember(Member member) {
        List<OrderWithOutTotalPriceResponse> orderResponses = orderService.findAllByMemberId(member.getId());

        return ResponseEntity
                .status(OK)
                .body(orderResponses);
    }
}
