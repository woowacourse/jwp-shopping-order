package cart.ui;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.response.MemberCouponsResponse;
import cart.dto.request.OrderRequest;

@RestController
@RequestMapping("/orders")
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> addOrder(Member member, @RequestBody OrderRequest orderRequest) {
        Long orderId = orderService.add(member, orderRequest);
        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }

    @GetMapping("/coupons")
    public ResponseEntity<MemberCouponsResponse> getAllCouponOfMember(Member member, @RequestParam("cartItemId") List<Long> cartItemIds) {
        MemberCouponsResponse memberCouponsResponse = orderService.getMemberCoupons(member, cartItemIds);
        return ResponseEntity.ok().body(memberCouponsResponse);
    }
}
