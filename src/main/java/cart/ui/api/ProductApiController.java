package cart.ui.api;

import cart.application.ProductService;
import cart.dto.product.ProductRequest;
import cart.dto.product.ProductResponse;
import cart.dto.product.ProductsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "상품(product)", description = "상품에 대한 api 입니다.")
@RestController
@RequestMapping("/products")
public class ProductApiController {

    private final ProductService productService;

    public ProductApiController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "상품 전체 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ProductsResponse.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")})
    @GetMapping
    public ResponseEntity<ProductsResponse> getAllProducts(
            @Parameter(description = "페이지에 표시될 상품 수") @RequestParam("unit-size") int size,
            @Parameter(description = "조회할 페이지") @RequestParam("page") int page) {
        return ResponseEntity.ok(productService.getProducts(size, page));
    }

    @Operation(summary = "상품 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")})
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@Parameter(description = "조회할 상품 ID") @PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @Operation(summary = "상품 추가")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED", headers = @Header(name = "Location", description = "products/추가된 상품 ID")),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")})
    @PostMapping
    public ResponseEntity<Void> createProduct(
            @Parameter(description = "추가할 상품 정보") @RequestBody @Valid ProductRequest productRequest) {
        Long id = productService.createProduct(productRequest);
        return ResponseEntity.created(URI.create("/products/" + id)).build();
    }

    @Operation(summary = "상품 정보 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")})
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@Parameter(description = "수정할 상품 ID") @PathVariable Long id,
                                              @Parameter(description = "수정할 상품 정보") @RequestBody @Valid ProductRequest productRequest) {
        productService.updateProduct(id, productRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "상품 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "203", description = "NO CONTENT"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@Parameter(description = "삭제할 상품 ID") @PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
