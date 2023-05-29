package cart.ui;

import cart.domain.Member;
import cart.dto.OrderCouponResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderApiController {

    @GetMapping("/coupons")
    public ResponseEntity<List<OrderCouponResponse>> findCoupons(
            @RequestParam final List<Long> cartItemId,
            Member member
    ) {
        List<OrderCouponResponse> orderCouponResponses = List.of(
                new OrderCouponResponse(cartItemId.get(0), "반짝할인(10%)", 10000, true, 3000),
                new OrderCouponResponse(cartItemId.get(1), "반짝할인(20%)", 20000, false, null)
        );

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(orderCouponResponses);
    }
}
