package cart.controller;

import cart.config.auth.Auth;
import cart.domain.coupon.Coupon;
import cart.domain.Member;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import cart.service.CartItemService;
import cart.service.CouponService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cart-items")
public class CartItemController {

    private final CartItemService cartItemService;
    private final CouponService couponService;

    public CartItemController(CartItemService cartItemService, CouponService couponService) {
        this.cartItemService = cartItemService;
        this.couponService = couponService;
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> showCartItems(@Auth Member member) {
        return ResponseEntity.ok(cartItemService.findByMember(member));
    }

    @PostMapping
    public ResponseEntity<Void> addCartItems(@Auth Member member, @RequestBody CartItemRequest cartItemRequest) {
        Long cartItemId = cartItemService.add(member, cartItemRequest);

        return ResponseEntity.created(URI.create("/cart-items/" + cartItemId)).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateCartItemQuantity(@Auth Member member, @PathVariable Long id, @RequestBody CartItemQuantityUpdateRequest request) {
        cartItemService.updateQuantity(member, id, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCartItems(@Auth Member member, @PathVariable Long id) {
        cartItemService.remove(member, id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/coupons/{couponIds}")
    public ResponseEntity applyCoupons(@Auth Member member, @PathVariable List<Long> couponIds) {
        List<Long> notNullCouponIds = couponIds.stream().
                filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<Coupon> coupons = couponService.findByIds(notNullCouponIds);

//        cartItemService.applyCoupon(member, );

        return null;
    }
}
