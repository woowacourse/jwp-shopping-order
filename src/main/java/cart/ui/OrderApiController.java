package cart.ui;

import cart.application.OrderService;
import cart.application.ProductService;
import cart.domain.Member;
import cart.dto.OrderCartItemDto;
import cart.dto.OrderCartItemsRequest;
import cart.dto.OrderResponse;
import cart.dto.OrdersResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderApiController {

    private final OrderService orderService;
    private final ProductService productService;

    public OrderApiController(final OrderService orderService, final ProductService productService) {
        this.orderService = orderService;
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Void> postOrder(Member member,
                                          @RequestBody final OrderCartItemsRequest request) {
        final List<OrderCartItemDto> orderCartItemDtos = request.getOrderCartItems();

        final Long cartOrderId = orderService.addCartOrder(member, orderCartItemDtos);

        return ResponseEntity.created(URI.create("/orders/" + cartOrderId)).build();
    }
}
