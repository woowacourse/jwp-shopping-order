package cart.ui.product;

import cart.application.service.product.ProductReadService;
import cart.application.service.product.dto.ProductResultDto;
import cart.ui.product.dto.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductReadController {

    private final ProductReadService productReadService;

    public ProductReadController(ProductReadService productReadService) {
        this.productReadService = productReadService;
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

}
