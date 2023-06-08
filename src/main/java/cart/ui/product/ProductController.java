package cart.ui.product;

import cart.application.service.product.ProductReadService;
import cart.application.service.product.ProductWriteService;
import cart.application.service.product.dto.ProductCommandDto;
import cart.application.service.product.dto.ProductResultDto;
import cart.ui.product.dto.ProductRequest;
import cart.ui.product.dto.ProductResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductReadService productReadService;
    private final ProductWriteService productWriteService;

    public ProductController(ProductReadService productReadService, final ProductWriteService productWriteService) {
        this.productReadService = productReadService;
        this.productWriteService = productWriteService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        final List<ProductResultDto> productResultDtos = productReadService.getAllProducts();

        final List<ProductResponse> productResponses = productResultDtos.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toUnmodifiableList());
        return ResponseEntity.ok(productResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        final ProductResultDto productResultDto = productReadService.getProductById(id);
        return ResponseEntity.ok(ProductResponse.of(productResultDto));
    }

    @PostMapping
    public ResponseEntity<Void> createProduct(@RequestBody ProductRequest productRequest) {
        final Long id = productWriteService.createProduct(ProductCommandDto.from(productRequest));
        return ResponseEntity.created(URI.create("/products/" + id)).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
        productWriteService.updateProduct(id, ProductCommandDto.from(productRequest));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productWriteService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
