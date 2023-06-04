package cart.ui;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cart.application.product.ProductCommandService;
import cart.application.product.ProductQueryService;
import cart.application.product.dto.ProductRequest;
import cart.application.product.dto.ProductResponse;

@CrossOrigin(origins = {"https://feb-dain.github.io", "http://localhost:3000"},
	allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH,
	RequestMethod.DELETE, RequestMethod.OPTIONS},
	allowCredentials = "true", exposedHeaders = "Location")
@RestController
@RequestMapping("/products")
public class ProductApiController {

	private final ProductQueryService productQueryService;
	private final ProductCommandService productCommandService;

	public ProductApiController(final ProductQueryService productQueryService,
		final ProductCommandService productCommandService) {
		this.productQueryService = productQueryService;
		this.productCommandService = productCommandService;
	}

	@GetMapping
	public ResponseEntity<List<ProductResponse>> getAllProducts() {
		final List<ProductResponse> productResponses = productQueryService.getAllProducts();
		return ResponseEntity.ok(productResponses);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
		final ProductResponse productResponse = productQueryService.getProductById(id);
		return ResponseEntity.ok(productResponse);
	}

	@PostMapping
	public ResponseEntity<Void> createProduct(@Valid @RequestBody ProductRequest productRequest) {
		final Long id = productCommandService.createProduct(productRequest);
		return ResponseEntity.created(URI.create("/products/" + id)).build();
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> updateProduct(@PathVariable Long id,
		@Valid @RequestBody ProductRequest productRequest) {
		productCommandService.updateProduct(id, productRequest);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
		productCommandService.deleteProduct(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
