package shop.ui.product;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.application.product.ProductService;
import shop.application.product.dto.ProductDto;
import shop.application.product.dto.ProductModificationDto;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductRestController {

    private final ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> productDtos = productService.getAllProducts();

        return ResponseEntity.ok(productDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        ProductDto productDto = productService.getProductById(id);

        return ResponseEntity.ok(productDto);
    }

    @PostMapping
    public ResponseEntity<Void> createProduct(@RequestBody ProductModificationDto request) {
        Long savedId = productService.createProduct(request);

        return ResponseEntity.created(URI.create("/products/" + savedId)).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable Long id, @RequestBody ProductModificationDto request) {
        productService.updateProduct(id, request);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
