package cart.ui;

import cart.application.ProductService;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "product", description = "물품 API")
@RestController
@RequestMapping("/products")
public class ProductApiController {

    private final ProductService productService;

    public ProductApiController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(
            summary = "물품 전체 조회",
            description = "물품 전체를 조회한다.",
            responses = {
                    @ApiResponse(description = "조회 성공", responseCode = "200")
            }
    )
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @Operation(
            summary = "물품 단일 조회",
            description = "물품 하나를 조회한다.",
            responses = {
                    @ApiResponse(description = "조회 성공", responseCode = "200")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@Parameter(description = "물품 id", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @Operation(
            summary = "물품 생성",
            description = "물품 하나를 생성한다.",
            responses = {
                    @ApiResponse(description = "생성 성공", responseCode = "201", headers = {@Header(name = "Location")})
            }
    )
    @PostMapping
    public ResponseEntity<Void> createProduct(@RequestBody ProductRequest productRequest) {
        Long id = productService.createProduct(productRequest);
        return ResponseEntity.created(URI.create("/products/" + id)).build();
    }

    @Operation(
            summary = "물품 갱신",
            description = "물품을 갱신한다.",
            responses = {
                    @ApiResponse(description = "갱신 성공", responseCode = "200")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@Parameter(description = "물품 id", required = true) @PathVariable Long id,
                                              @RequestBody ProductRequest productRequest) {
        productService.updateProduct(id, productRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(
            summary = "물품 제거",
            description = "물품을 제거한다.",
            responses = {
                    @ApiResponse(description = "제거 성공", responseCode = "204")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@Parameter(description = "물품 id", required = true) @PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
