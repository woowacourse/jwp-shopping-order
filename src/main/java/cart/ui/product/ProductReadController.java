package cart.ui.product;

import cart.application.product.ProductReadService;
import cart.application.product.dto.ProductResultDto;
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
        List<ProductResultDto> productResultDtos = productReadService.getAllProducts();
        List<ProductResponse> productResponses = productResultDtos.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toUnmodifiableList());
        return ResponseEntity.ok(productResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResultDto productResultDto = productReadService.getProductById(id);
        return ResponseEntity.ok(ProductResponse.of(productResultDto));
    }

}
