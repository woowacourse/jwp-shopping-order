package cart.application.product;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cart.application.product.dto.ProductDto;
import cart.domain.product.Product;
import cart.domain.product.ProductRepository;

@Transactional(readOnly = true)
@Service
public class ProductQueryService {

	private final ProductRepository productRepository;

	public ProductQueryService(final ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public List<ProductDto> getAllProducts() {
		List<Product> products = productRepository.findAll();

		return products.stream()
			.map(ProductDto::from)
			.collect(Collectors.toList());
	}

	public ProductDto getProductById(Long productId) {
		Product product = productRepository.findById(productId)
			.orElseThrow();
		return ProductDto.from(product);
	}

}
