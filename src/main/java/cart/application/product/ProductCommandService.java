package cart.application.product;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cart.application.product.dto.ProductDto;
import cart.domain.product.Product;
import cart.domain.product.ProductRepository;

@Transactional
@Service
public class ProductCommandService {

	private final ProductRepository productRepository;

	public ProductCommandService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public Long createProduct(ProductDto productDto) {
		Product product = new Product(productDto.getName(), productDto.getPrice(), productDto.getImageUrl());
		return productRepository.save(product);
	}

	public void updateProduct(ProductDto productDto) {
		Product product = new Product(productDto.getId(), productDto.getName(), productDto.getPrice(),
			productDto.getImageUrl());
		productRepository.update(product);
	}

	public void deleteProduct(Long productId) {
		productRepository.deleteById(productId);
	}
}
