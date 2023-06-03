package cart.ui;

import cart.application.dto.order.FindOrderCouponsResponse;
import cart.application.service.MemberCouponService;
import cart.domain.Member;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderApiController {

    private final MemberCouponService memberCouponService;

    public OrderApiController(final MemberCouponService memberCouponService) {
        this.memberCouponService = memberCouponService;
    }

    @GetMapping("/coupons")
    public ResponseEntity<FindOrderCouponsResponse> getCouponsByCartItemIds(Member member,
            @RequestParam(name = "cartItemId") final List<Long> cartItemIds) {
        return ResponseEntity.ok(memberCouponService.findOrderCoupons(member, cartItemIds));
    }
}
