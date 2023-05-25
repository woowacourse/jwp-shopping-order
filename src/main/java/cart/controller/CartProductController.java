package cart.controller;

import cart.auth.Auth;
import cart.auth.Credential;
import cart.dto.CartProductSaveRequest;
import cart.dto.CartProductSearchResponse;
import cart.service.CartProductService;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/cart-products")
@RestController
public class CartProductController {

    private final CartProductService cartProductService;

    public CartProductController(final CartProductService cartProductService) {
        this.cartProductService = cartProductService;
    }

    @PostMapping
    public ResponseEntity<Void> save(@Auth final Credential credential,
                                     @RequestBody final CartProductSaveRequest request) {
        final Long id = cartProductService.save(credential.getMemberId(), request);
        return ResponseEntity.created(URI.create("/cart-products/" + id)).build();
    }

    @GetMapping
    public ResponseEntity<CartProductSearchResponse> findAll(@Auth final Credential credential) {
        final CartProductSearchResponse result = cartProductService.findAll(credential.getMemberId());
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("{productId}")
    public ResponseEntity<Void> delete(
            @Auth final Credential credential,
            @PathVariable final Long productId
    ) {
        cartProductService.delete(productId, credential.getMemberId());
        return ResponseEntity.noContent().build();
    }
}
