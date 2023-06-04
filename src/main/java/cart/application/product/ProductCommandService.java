package cart.application.product;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cart.domain.product.Product;
import cart.domain.product.ProductRepository;
import cart.application.product.dto.ProductRequest;

@Transactional
@Service
public class ProductCommandService {

	private final ProductRepository productRepository;

	public ProductCommandService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public Long createProduct(final ProductRequest request) {
		Product product = new Product(request.getName(), request.getPrice(), request.getImageUrl());
		return productRepository.save(product);
	}

	public void updateProduct(final Long id, final ProductRequest request) {
		Product product = new Product(id, request.getName(), request.getPrice(), request.getImageUrl());
		productRepository.update(product);
	}

	public void deleteProduct(final Long productId) {
		productRepository.deleteById(productId);
	}
}
