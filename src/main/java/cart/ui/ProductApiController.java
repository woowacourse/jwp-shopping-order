package cart.ui;

import java.net.URI;
import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import cart.application.ProductService;
import cart.dto.request.ProductRequest;
import cart.dto.response.ExceptionResponse;
import cart.dto.response.ProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/products")
public class ProductApiController {

    private final ProductService productService;

    public ProductApiController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "전체 상품 조회", description = "전체 상품을 조회한다.")
    @ApiResponse(
        responseCode = "200",
        description = "전체 상품 조회 성공"
    )
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.findAll());
    }

    @Operation(summary = "특정 상품 조회", description = "특정 상품을 조회한다.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "특정 상품 조회 성공"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "특정 상품 조회 실패",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @Operation(summary = "상품 정보 추가", description = "상품 정보를 추가한다.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "상품 정보 추가 성공"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "상품 정보 추가 실패",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
        )
    })
    @PostMapping
    public ResponseEntity<Void> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        Long id = productService.add(productRequest);
        return ResponseEntity.created(URI.create("/products/" + id)).build();
    }

    @Operation(summary = "상품 정보 수정", description = "상품 정보를 수정한다.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "상품 정보 수정 성공"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "상품 정보 수정 실패",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable Long id,
        @Valid @RequestBody ProductRequest productRequest) {
        productService.update(id, productRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "상품 정보 삭제", description = "상품 정보를 삭제한다.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "상품 정보 삭제 성공"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "상품 정보 삭제 실패",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.remove(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
