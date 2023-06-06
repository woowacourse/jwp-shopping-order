package cart.ui;

import java.net.URI;
import java.util.List;

import cart.application.ProductService;
import cart.dto.ProductRequest;
import cart.dto.ProductStockResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductApiController {

    private final ProductService productService;

    public ProductApiController(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductStockResponse> createProduct(@RequestBody final ProductRequest productRequest) {
        final ProductStockResponse productStockResponse = productService.createProduct(productRequest);
        return ResponseEntity.created(URI.create("/products/" + productStockResponse.getId())).body(productStockResponse);
    }

    @GetMapping
    public ResponseEntity<List<ProductStockResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProductStockResponses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductStockResponse> getProductById(@PathVariable final Long id) {
        return ResponseEntity.ok(productService.getProductStockResponseById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductStockResponse> updateProduct(@PathVariable final Long id, @RequestBody final ProductRequest productRequest) {
        return ResponseEntity.ok(productService.updateProduct(id, productRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable final Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
