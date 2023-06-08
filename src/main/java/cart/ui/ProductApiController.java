package cart.ui;

import cart.application.ProductService;
import cart.dto.request.ProductCreateRequest;
import cart.dto.response.ProductResponse;
import cart.dto.response.ShoppingOrderResponse;
import cart.dto.response.ShoppingOrderResultResponse;
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

@RestController
@RequestMapping("/products")
public class ProductApiController {
    private final ProductService productService;

    public ProductApiController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<ShoppingOrderResultResponse<List<ProductResponse>>> getAllProducts() {
        List<ProductResponse> products = productService.getAllProducts();
        return ResponseEntity.ok()
                .body(new ShoppingOrderResultResponse<>("모든 상품이 조회되었습니다.", products));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShoppingOrderResultResponse<ProductResponse>> getProductById(@PathVariable Long id) {
        ProductResponse product = productService.getProductById(id);
        return ResponseEntity.ok()
                .body(new ShoppingOrderResultResponse<>("상품이 조회되었습니다.", product));
    }

    @PostMapping
    public ResponseEntity<ShoppingOrderResponse> createProduct(@RequestBody @Valid ProductCreateRequest productCreateRequest) {
        Long id = productService.createProduct(productCreateRequest);
        return ResponseEntity.created(URI.create("/products/" + id))
                .body(new ShoppingOrderResponse("상품이 생성되었습니다."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShoppingOrderResponse> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductCreateRequest productCreateRequest) {
        productService.updateProduct(id, productCreateRequest);
        return ResponseEntity.ok()
                .body(new ShoppingOrderResponse("상품이 수정되었습니다."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ShoppingOrderResponse> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok()
                .body(new ShoppingOrderResponse("상품이 삭제되었습니다."));
    }
}
