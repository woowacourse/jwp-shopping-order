package cart.ui;

import cart.application.ProductService;
import cart.dto.request.ProductRequest;
import cart.dto.response.ProductResponse;
import cart.dto.response.Response;
import cart.dto.response.ResultResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Product Controller")
@RestController
@RequestMapping("/products")
public class ProductApiController {
    private final ProductService productService;

    public ProductApiController(ProductService productService) {
        this.productService = productService;
    }

    @ApiOperation(value = "모든 상품 조회")
    @GetMapping
    public ResponseEntity<Response> getAllProducts() {
        List<ProductResponse> products = productService.getAllProducts();
        return ResponseEntity.ok()
                .body(new ResultResponse<>("모든 상품이 조회되었습니다.", products));
    }

    @ApiOperation(value = "상품 상세 조회")
    @GetMapping("/{productId}")
    public ResponseEntity<Response> getProductById(@PathVariable Long productId) {
        ProductResponse product = productService.getProductById(productId);
        return ResponseEntity.ok()
                .body(new ResultResponse<>("상품이 조회되었습니다.", product));
    }

    @ApiOperation(value = "상품 생성")
    @PostMapping
    public ResponseEntity<Response> createProduct(@RequestBody @Valid ProductRequest productRequest) {
        Long id = productService.createProduct(productRequest);
        return ResponseEntity.created(URI.create("/products/" + id))
                .body(new Response("상품이 생성되었습니다."));
    }

    @ApiOperation(value = "상품 수정")
    @PutMapping("/{productId}")
    public ResponseEntity<Response> updateProduct(@PathVariable Long productId, @RequestBody @Valid ProductRequest productRequest) {
        productService.updateProduct(productId, productRequest);
        return ResponseEntity.ok()
                .body(new Response("상품이 수정되었습니다."));
    }

    @ApiOperation(value = "상품 삭제")
    @DeleteMapping("/{productId}")
    public ResponseEntity<Response> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok()
                .body(new Response("상품이 삭제되었습니다."));
    }
}
