package cart.ui.product;

import cart.application.service.product.ProductWriteService;
import cart.application.service.product.dto.ProductCreateDto;
import cart.ui.product.dto.ProductRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/products")
public class ProductWriteController {

    private final ProductWriteService productWriteService;

    public ProductWriteController(final ProductWriteService productWriteService) {
        this.productWriteService = productWriteService;
    }

    @PostMapping
    public ResponseEntity<Void> createProduct(@RequestBody ProductRequest productRequest) {
        final Long id = productWriteService.createProduct(ProductCreateDto.from(productRequest));
        return ResponseEntity.created(URI.create("/products/" + id)).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
        productWriteService.updateProduct(id, ProductCreateDto.from(productRequest));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productWriteService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
