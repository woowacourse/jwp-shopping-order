package cart.ui.api;

import cart.application.ProductService;
import cart.application.request.CreateProductRequest;
import cart.application.response.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RequestMapping("/products")
@RestController
public class ProductApiController {

    private final ProductService productService;

    public ProductApiController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> findProductResponses = productService.getAllProducts();

        return ResponseEntity
                .status(OK)
                .body(findProductResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable @NotNull Long id) {
        ProductResponse findProductResponse = productService.getProductById(id);

        return ResponseEntity
                .status(OK)
                .body(findProductResponse);
    }

    @PostMapping
    public ResponseEntity<Void> createProduct(@Valid @RequestBody CreateProductRequest request) {
        Long saveProductId = productService.createProduct(request);

        return ResponseEntity.created(URI.create("/products/" + saveProductId)).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable(name = "id") @NotNull Long productId, @Valid @RequestBody CreateProductRequest request) {
        productService.updateProduct(productId, request);

        return ResponseEntity.status(OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable(name = "id") @NotNull Long productId) {
        productService.deleteProduct(productId);

        return ResponseEntity.status(NO_CONTENT).build();
    }

}
