package cart.ui;

import cart.application.ProductService;
import cart.dto.request.ProductRequest;
import cart.dto.response.ProductResponse;
import io.swagger.v3.oas.annotations.Operation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductApiController {

	private final ProductService productService;

	public ProductApiController(ProductService productService) {
		this.productService = productService;
	}

	@Operation(summary = "상품 전체 조회")
	@GetMapping
	public ResponseEntity<List<ProductResponse>> getAllProducts() {
		return ResponseEntity.ok(productService.getAllProducts());
	}

	@Operation(summary = "상품 단건 조회")
	@GetMapping("/{id}")
	public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
		return ResponseEntity.ok(productService.getProductById(id));
	}

	@Operation(summary = "상품 생성")
	@PostMapping
	public ResponseEntity<Void> createProduct(@RequestBody ProductRequest productRequest) {
		Long id = productService.createProduct(productRequest);
		return ResponseEntity.created(URI.create("/products/" + id)).build();
	}

	@Operation(summary = "상품 수정")
	@PutMapping("/{id}")
	public ResponseEntity<Void> updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
		productService.updateProduct(id, productRequest);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@Operation(summary = "상품 삭제")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
		productService.deleteProduct(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
