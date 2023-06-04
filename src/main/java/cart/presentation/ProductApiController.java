package cart.presentation;

import cart.dto.request.ProductAddRequest;
import cart.dto.response.ProductResponse;
import cart.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductApiController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @PostMapping
    public ResponseEntity<Void> createProduct(@RequestBody ProductAddRequest productAddRequest) {
        Long productId = productService.createProduct(productAddRequest);

        return ResponseEntity.created(URI.create("/products/" + productId)).build();
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Void> updateProduct(@PathVariable Long productId, @RequestBody ProductAddRequest productAddRequest) {
        productService.updateProduct(productId, productAddRequest);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
