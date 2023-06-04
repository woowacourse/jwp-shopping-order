package cart.controller;

import cart.config.auth.Auth;
import cart.domain.Order;
import cart.domain.Member;
import cart.dto.*;
import cart.service.CartItemService;
import cart.service.CouponService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

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

    @GetMapping("/coupons")
    public ResponseEntity<OrderResponse> applyCoupons(@Auth Member member, @RequestParam(name = "id") List<Long> couponIds) {
        Order order = cartItemService.prepareOrder(member, couponIds);
        return ResponseEntity.ok().body(OrderResponse.from(order));
    }

}

