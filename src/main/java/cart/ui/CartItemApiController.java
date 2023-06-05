package cart.ui;

import cart.application.CartItemService;
import cart.config.auth.Principal;
import cart.dto.User;
import cart.dto.request.CartItemQuantityUpdateRequest;
import cart.dto.request.CartItemRequest;
import cart.dto.response.CartItemResponse;
import cart.dto.response.Response;
import cart.dto.response.ResultResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "CartItem Controller")
@RestController
@RequestMapping("/cart-items")
public class CartItemApiController {
    private final CartItemService cartItemService;

    public CartItemApiController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @ApiOperation(value = "장바구니에 담긴 상품 조회", authorizations = {@Authorization("Basic")})
    @GetMapping
    public ResponseEntity<Response> showCartItems(@Principal User user) {
        List<CartItemResponse> cartItemResponses = cartItemService.findAllByMember(user.getMemberId());
        return ResponseEntity.ok()
                .body(new ResultResponse<>("장바구니에 담긴 상품이 조회되었습니다.", cartItemResponses));
    }

    @ApiOperation(value = "장바구니에 상품 담기", authorizations = {@Authorization("Basic")})
    @PostMapping
    public ResponseEntity<Response> addCartItems(@Principal User user,
                                                 @RequestBody @Valid CartItemRequest cartItemRequest) {
        Long cartItemId = cartItemService.add(user.getMemberId(), cartItemRequest);
        return ResponseEntity.created(URI.create("/cart-items/" + cartItemId))
                .body(new Response("장바구니에 상품을 담았습니다."));
    }

    @ApiOperation(value = "장바구니에 담긴 상품 수량 변경", authorizations = {@Authorization("Basic")})
    @PatchMapping("/{cartItemId}")
    public ResponseEntity<Response> updateCartItemQuantity(@Principal User user,
                                                           @PathVariable Long cartItemId,
                                                           @RequestBody @Valid CartItemQuantityUpdateRequest request) {
        cartItemService.updateQuantity(user.getMemberId(), cartItemId, request);
        return ResponseEntity.ok()
                .body(new Response("장바구니에 담긴 상품의 수량을 변경했습니다."));
    }

    @ApiOperation(value = "장바구니에 담긴 상품 삭제", authorizations = {@Authorization("Basic")})
    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Response> removeCartItems(@Principal User user, @PathVariable Long cartItemId) {
        cartItemService.remove(user.getMemberId(), cartItemId);
        return ResponseEntity.ok()
                .body(new Response("장바구니에 담긴 상품을 삭제했습니다."));
    }
}
