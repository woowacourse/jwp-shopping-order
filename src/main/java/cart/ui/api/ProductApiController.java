package cart.ui.api;

import cart.application.ProductService;
import cart.domain.product.Product;
import cart.dto.product.ProductRequest;
import cart.dto.product.ProductResponse;
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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductApiController {

    private final ProductService productService;

    public ProductApiController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<Product> allProducts = productService.getAllProducts();
        return ResponseEntity.ok(toProductResponses(allProducts));
    }

    private List<ProductResponse> toProductResponses(List<Product> products) {
        return products.stream()
                .map(this::toProductResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable @NotNull Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(toProductResponse(product));
    }

    private ProductResponse toProductResponse(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getImageUrl());
    }

    @PostMapping
    public ResponseEntity<Void> createProduct(@RequestBody @Valid ProductRequest productRequest) {
        Long id = productService.createProduct(productRequest);
        return ResponseEntity.created(URI.create("/products/" + id)).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable @NotNull Long id, @RequestBody @Valid ProductRequest productRequest) {
        productService.updateProduct(id, productRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable @NotNull Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
