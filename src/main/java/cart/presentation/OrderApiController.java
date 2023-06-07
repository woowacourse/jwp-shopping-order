package cart.presentation;

import cart.domain.Member;
import cart.domain.OrderEntity;
import cart.domain.OrderItemEntity;
import cart.domain.Product;
import cart.dto.request.OrderRequest;
import cart.dto.response.OrderItemResponse;
import cart.dto.response.OrderResponse;
import cart.service.OrderService;
import cart.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/orders")
public class OrderApiController {

    private static final int SHOW_ORDER_ITEM_COUNT = 2;

    private final OrderService orderService;
    private final ProductService productService;

    public OrderApiController(OrderService orderService, ProductService productService) {
        this.orderService = orderService;
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Void> order(final Member member, final @RequestBody OrderRequest orderRequest) {
        long orderId = orderService.createOrder(member, orderRequest);

        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders(final Member member) {
        List<OrderResponse> response = new ArrayList<>();

        for (final OrderEntity orderEntity : orderService.getAllOrderBy(member)) {
            List<OrderItemResponse> orderItemResponses = toSpecificCountOrderItemResponses(orderEntity);

            response.add(OrderResponse.of(orderEntity, orderItemResponses));
        }

        return ResponseEntity.ok(response);
    }

    private List<OrderItemResponse> toSpecificCountOrderItemResponses(final OrderEntity orderEntity) {
        List<OrderItemEntity> orderItemEntities = orderService.getSpecificCountOrderItemBy(orderEntity, SHOW_ORDER_ITEM_COUNT);

        return createOrderItemResponses(orderItemEntities);
    }

    private List<OrderItemResponse> createOrderItemResponses(final List<OrderItemEntity> orderItemEntities) {
        List<OrderItemResponse> result = new ArrayList<>();

        List<Product> products = orderItemEntities.stream()
                .map(orderItemEntity -> productService.getProductById(orderItemEntity.getProductId()))
                .collect(toList());

        for (int index = 0; index < orderItemEntities.size(); index++) {
            result.add(OrderItemResponse.of(orderItemEntities.get(index), products.get(index)));
        }
        return result;
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(final @PathVariable Long orderId) {
        OrderEntity orderEntity = orderService.findByOrderId(orderId);

        List<OrderItemResponse> orderItemResponses = toAllOrderItemResponses(orderEntity);

        return ResponseEntity.ok(OrderResponse.of(orderEntity, orderItemResponses));
    }

    private List<OrderItemResponse> toAllOrderItemResponses(final OrderEntity orderEntity) {
        List<OrderItemEntity> orderItemEntities = orderService.getAllOrderItemBy(orderEntity);

        return createOrderItemResponses(orderItemEntities);
    }
}
