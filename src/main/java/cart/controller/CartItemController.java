package cart.controller;

import cart.auth.Auth;
import cart.auth.Credential;
import cart.dto.CartItemSaveRequest;
import cart.dto.CartItemSearchResponse;
import cart.service.CartItemService;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/cart-items")
@RestController
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(final CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @PostMapping
    public ResponseEntity<Void> save(@Auth final Credential credential,
                                     @RequestBody final CartItemSaveRequest request) {
        final Long id = cartItemService.save(credential.getMemberId(), request);
        return ResponseEntity.created(URI.create("/cart-items/" + id)).build();
    }

    @GetMapping
    public ResponseEntity<CartItemSearchResponse> findAll(@Auth final Credential credential) {
        final CartItemSearchResponse result = cartItemService.findAll(credential.getMemberId());
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("{productId}")
    public ResponseEntity<Void> delete(
            @Auth final Credential credential,
            @PathVariable final Long productId
    ) {
        cartItemService.delete(productId, credential.getMemberId());
        return ResponseEntity.noContent().build();
    }
}
