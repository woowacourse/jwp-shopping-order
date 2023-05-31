package cart.ui;

import cart.application.CartItemService;
import cart.domain.Member;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.Response;
import cart.dto.ResultResponse;
import java.net.URI;
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

    private final CartItemService cartItemService;

    public CartItemApiController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @GetMapping
    public ResponseEntity<Response> showCartItems(Member member) {
        return ResponseEntity.ok()
                .body(new ResultResponse<>("장바구니에 담긴 상품이 조회되었습니다.", cartItemService.findAllByMember(member)));
    }

    @PostMapping
    public ResponseEntity<Response> addCartItems(Member member, @RequestBody CartItemRequest cartItemRequest) {
        Long cartItemId = cartItemService.add(member, cartItemRequest);

        return ResponseEntity.created(URI.create("/cart-items/" + cartItemId))
                .body(new Response("장바구니에 상품을 담았습니다."));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Response> updateCartItemQuantity(Member member, @PathVariable Long id, @RequestBody CartItemQuantityUpdateRequest request) {
        cartItemService.updateQuantity(member, id, request);

        return ResponseEntity.ok()
                .body(new Response("장바구니에 담긴 상품의 수량을 변경했습니다."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> removeCartItems(Member member, @PathVariable Long id) {
        cartItemService.remove(member, id);

        return ResponseEntity.ok()
                .body(new Response("장바구니에 담긴 상품을 삭제했습니다."));
    }
}
