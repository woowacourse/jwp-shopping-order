package cart.ui;

import cart.application.ProductService;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.util.List;
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
@Tag(name = "상품 관련 API", description = "상품 정보를 관리하는 API 입니다.")
public class ProductApiController {

    private final ProductService productService;

    public ProductApiController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "모든 상품 정보 조회")
    public ResponseEntity<List<ProductResponse>> findAllProducts() {
        return ResponseEntity.ok(productService.findAllProducts());
    }

    @GetMapping("/{id}")
    @Operation(summary = "상품 상세 정보 조회")
    public ResponseEntity<ProductResponse> findProductById(
            @Parameter(description = "상세 정보를 조회할 상품 ID") @PathVariable Long id
    ) {
        return ResponseEntity.ok(productService.findProductById(id));
    }

    @PostMapping
    @Operation(summary = "상품 생성")
    public ResponseEntity<Void> createProduct(@RequestBody ProductRequest productRequest) {
        Long id = productService.createProduct(productRequest);
        return ResponseEntity.created(URI.create("/products/" + id)).build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "상품 정보 수정")
    public ResponseEntity<Void> updateProduct(
            @Parameter(description = "수정할 상품의 ID") @PathVariable Long id,
            @RequestBody ProductRequest productRequest
    ) {
        productService.updateProduct(id, productRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "상품 삭제")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "삭제할 상품의 ID") @PathVariable Long id
    ) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

}
