package cart.controller;

import cart.auth.Auth;
import cart.auth.Credential;
import cart.dto.CartItemDto;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemSaveRequest;
import cart.service.CartItemService;
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

@RequestMapping("/cart-items")
@RestController
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(final CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @PostMapping
    public ResponseEntity<Void> save(
            @Auth final Credential credential,
            @RequestBody final CartItemSaveRequest request
    ) {
        final Long id = cartItemService.save(credential.getMemberId(), request);
        return ResponseEntity.created(URI.create("/cart-items/" + id)).build();
    }

    @GetMapping
    public ResponseEntity<List<CartItemDto>> findAll(@Auth final Credential credential) {
        final List<CartItemDto> result = cartItemService.findAll(credential.getMemberId());
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{cartItemId}")
    public ResponseEntity<Void> updateQuantity(
            @Auth final Credential credential,
            @PathVariable final Long cartItemId,
            @Valid @RequestBody final CartItemQuantityUpdateRequest request
    ) {
        cartItemService.updateQuantity(credential.getMemberId(), cartItemId, request);
        return ResponseEntity.ok().build();
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
