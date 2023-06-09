package cart.application.product;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cart.application.product.dto.ProductResponse;
import cart.domain.product.Product;
import cart.domain.product.ProductRepository;

@Transactional(readOnly = true)
@Service
public class ProductQueryService {

	private final ProductRepository productRepository;

	public ProductQueryService(final ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public List<ProductResponse> getAllProducts() {
		List<Product> products = productRepository.findAll();

		return products.stream()
			.map(ProductResponse::from)
			.collect(Collectors.toList());
	}

	public ProductResponse getProductById(final Long productId) {
		Product product = productRepository.findById(productId)
			.orElseThrow();
		return ProductResponse.from(product);
	}

}
