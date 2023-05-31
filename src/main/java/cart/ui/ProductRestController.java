package cart.ui;

import cart.application.ProductService;
import cart.domain.product.Product;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductRestController {

    private final ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> productResponses = productService.getAllProducts()
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok(productResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);

        return ResponseEntity.ok(ProductResponse.of(product));
    }

    @PostMapping
    public ResponseEntity<Void> createProduct(@RequestBody ProductRequest request) {
        Product product = new Product(request.getName(), request.getPrice(), request.getImageUrl());
        Long savedId = productService.createProduct(product);

        return ResponseEntity.created(URI.create("/products/" + savedId)).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable Long id, @RequestBody ProductRequest request) {
        Product product = new Product(id, request.getName(), request.getPrice(), request.getImageUrl());
        productService.updateProduct(product);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
