package cart.ui;

import cart.application.CouponService;
import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderApiController {
    private final CouponService couponService;
    private final OrderService orderService;

    public OrderApiController(final CouponService couponService, final OrderService orderService) {
        this.couponService = couponService;
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> order(
            @RequestBody OrderRequest request,
            Member member
    ) {
        Long orderId = orderService.order(member, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .location(URI.create("/orders/" + orderId))
                .build();
    }

    @GetMapping
    public ResponseEntity<AllOrderResponse> findAllOrders(Member member) {
        AllOrderResponse allOrderResponse = new AllOrderResponse(
                List.of(
                        new OrderResponse(
                                1L,
                                List.of(
                                        new OrderItemResponse(
                                                1L,
                                                "지구별",
                                                1000,
                                                "https://cdn.pixabay.com/photo/2011/12/13/14/28/earth-11009__480.jpg",
                                                2
                                        ),
                                        new OrderItemResponse(
                                                2L,
                                                "화성",
                                                200000,
                                                "https://cdn.pixabay.com/photo/2011/12/13/14/30/mars-11012__480.jpg",
                                                4
                                        )
                                )
                        )
                )
        );
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(allOrderResponse);
    }

    @GetMapping("/coupons")
    public ResponseEntity<List<OrderCouponResponse>> findCoupons(
            @RequestParam final List<Long> cartItemId,
            Member member
    ) {
        List<OrderCouponResponse> orderCouponResponses = couponService.calculateCouponForCarts(member, cartItemId);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(orderCouponResponses);
    }
}
