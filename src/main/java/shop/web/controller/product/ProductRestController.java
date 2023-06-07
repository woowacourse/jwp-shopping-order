package shop.web.controller.product;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.application.product.ProductService;
import shop.application.product.dto.ProductDto;
import shop.application.product.dto.ProductModificationDto;
import shop.web.controller.product.dto.request.ProductModificationRequest;
import shop.web.controller.product.dto.response.ProductResponse;

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
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductDto> productDtos = productService.getAllProducts();
        List<ProductResponse> responses = ProductResponse.of(productDtos);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        ProductDto productDto = productService.getProductById(id);

        return ResponseEntity.ok(ProductResponse.of(productDto));
    }

    @PostMapping
    public ResponseEntity<Void> createProduct(@RequestBody ProductModificationRequest request) {
        ProductModificationDto productModificationDto = toProductModificationDto(request);
        Long savedId = productService.createProduct(productModificationDto);

        return ResponseEntity.created(URI.create("/products/" + savedId)).build();
    }

    private ProductModificationDto toProductModificationDto(ProductModificationRequest request) {
        return new ProductModificationDto(request.getName(), request.getPrice(), request.getImageUrl());
    }


    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable Long id,
                                              @RequestBody ProductModificationRequest request) {
        ProductModificationDto productModificationDto = toProductModificationDto(request);
        productService.updateProduct(id, productModificationDto);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
