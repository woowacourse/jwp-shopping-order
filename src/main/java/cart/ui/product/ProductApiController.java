package cart.ui.product;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

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

import cart.application.product.ProductService;
import cart.application.product.dto.ProductDto;
import cart.ui.product.dto.ProductRequest;
import cart.ui.product.dto.ProductResponse;

@CrossOrigin(origins = {"https://feb-dain.github.io", "http://localhost:3000"},
	allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH,
	RequestMethod.DELETE, RequestMethod.OPTIONS},
	allowCredentials = "true", exposedHeaders = "Location")
@RestController
@RequestMapping("/products")
public class ProductApiController {

	private final ProductService productService;

	public ProductApiController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping
	public ResponseEntity<List<ProductResponse>> getAllProducts() {
		final List<ProductDto> allProducts = productService.getAllProducts();
		final List<ProductResponse> productResponses = allProducts.stream()
			.map(ProductResponse::from)
			.collect(Collectors.toList());

		return ResponseEntity.ok(productResponses);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
		final ProductDto productDto = productService.getProductById(id);
		final ProductResponse productResponse = ProductResponse.from(productDto);

		return ResponseEntity.ok(productResponse);
	}

	@PostMapping
	public ResponseEntity<Void> createProduct(@RequestBody ProductRequest productRequest) {
		final ProductDto productDto = ProductDto.of(null, productRequest);
		Long id = productService.createProduct(productDto);

		return ResponseEntity.created(URI.create("/products/" + id)).build();
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
		final ProductDto productDto = ProductDto.of(id, productRequest);
		productService.updateProduct(productDto);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
		productService.deleteProduct(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
