package cart.controller;

import cart.domain.Product;
import cart.dto.ProductResponse;
import cart.dto.ProductSaveRequest;
import cart.dto.ProductUpdateRequest;
import cart.exception.ExceptionResponse;
import cart.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "상품", description = "상품을 관리한다.")
@RequestMapping("/products")
@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(final ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "전체 상품 조회", description = "전체 상품을 조회한다.")
    @ApiResponse(
            responseCode = "200",
            description = "전체 상품 조회 성공.",
            content = @Content(schema = @Schema(implementation = ProductResponse.class))
    )
    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll() {
        final List<ProductResponse> products = productService.findAll().stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "단일 상품 조회", description = "단일 상품을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "단일 상품 조회 성공.",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "단일 상품 조회 실패.",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(
            @Parameter(description = "상품 Id") @PathVariable final Long id
    ) {
        final Product product = productService.findById(id);
        return ResponseEntity.ok(ProductResponse.from(product));
    }

    @Operation(summary = "상품 저장", description = "상품을 저장한다.")
    @ApiResponse(
            responseCode = "201",
            description = "상품 저장 성공.",
            headers = {@Header(name = "location", description = "저장된 상품의 경로")}
    )
    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody final ProductSaveRequest request) {
        final Long id = productService.save(request);
        return ResponseEntity.created(URI.create("/products/" + id)).build();
    }

    @Operation(summary = "상품 수정", description = "상품을 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "상품 수정 성공."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "상품 수정 실패.",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @Parameter(description = "상품 Id") @PathVariable final Long id,
            @Valid @RequestBody ProductUpdateRequest request
    ) {
        productService.update(id, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "상품 삭제", description = "상품을 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "상품 삭제 성공."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "상품 삭제 실패.",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "상품 Id") @PathVariable final Long id
    ) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
