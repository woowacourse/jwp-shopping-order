package com.woowahan.techcourse.cart.ui;

import com.woowahan.techcourse.cart.application.CartCommandService;
import com.woowahan.techcourse.cart.dao.CartItemQueryDao;
import com.woowahan.techcourse.cart.dto.CartItemIdResponse;
import com.woowahan.techcourse.cart.dto.CartItemQuantityUpdateRequest;
import com.woowahan.techcourse.cart.dto.CartItemRequest;
import com.woowahan.techcourse.cart.dto.CartItemResponse;
import com.woowahan.techcourse.member.domain.Member;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart-items")
public class CartItemApiController {

    private final CartCommandService cartCommandService;
    private final CartItemQueryDao cartItemQueryDao;

    public CartItemApiController(CartCommandService cartCommandService, CartItemQueryDao cartItemQueryDao) {
        this.cartCommandService = cartCommandService;
        this.cartItemQueryDao = cartItemQueryDao;
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> showCartItems(Member member) {
        return ResponseEntity.ok(cartItemQueryDao.findByMemberId(member.getId()));
    }

    @PostMapping
    public ResponseEntity<CartItemIdResponse> addCartItems(Member member,
            @RequestBody CartItemRequest cartItemRequest) {
        Long cartItemId = cartCommandService.add(member.getId(), cartItemRequest);

        return ResponseEntity.created(URI.create("/cart-items/" + cartItemId))
                .body(new CartItemIdResponse(cartItemId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateCartItemQuantity(Member member, @PathVariable Long id,
            @RequestBody CartItemQuantityUpdateRequest request) {
        cartCommandService.updateQuantity(member.getId(), id, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCartItems(Member member, @PathVariable Long id) {
        cartCommandService.remove(member.getId(), id);

        return ResponseEntity.noContent().build();
    }
}
