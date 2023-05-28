package cart.ui;

import java.net.URI;
import java.util.List;

import cart.application.ProductService;
import cart.config.AuthPrincipal;
import cart.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductApiController {

    private final ProductService productService;

    public ProductApiController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/cart-items")
    public ResponseEntity<ProductPagingResponse> getAllPagingProductCartItems(@AuthPrincipal AuthMember authMember,
                                                                              @RequestParam Long lastId, @RequestParam int pageItemCount) {
        ProductPagingResponse allPagingProductCartItems = productService.getAllPagingProductCartItems(authMember, lastId, pageItemCount);
        return ResponseEntity.ok(allPagingProductCartItems);
    }

    @GetMapping("/{id}/cart-items")
    public ResponseEntity<ProductCartItemResponse> getProductCartItems(@AuthPrincipal AuthMember authMember, @PathVariable Long id) {
        ProductCartItemResponse productCartItems = productService.findProductCartItems(authMember, id);
        return ResponseEntity.ok(productCartItems);
    }

    @PostMapping
    public ResponseEntity<Void> createProduct(@RequestBody ProductRequest productRequest) {
        Long id = productService.createProduct(productRequest);
        return ResponseEntity.created(URI.create("/products/" + id)).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
        productService.updateProduct(id, productRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
