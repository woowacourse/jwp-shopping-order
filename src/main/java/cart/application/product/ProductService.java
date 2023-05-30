package cart.application.product;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import cart.application.product.dto.ProductDto;
import cart.domain.product.Product;
import cart.domain.product.ProductRepository;

@Service
public class ProductService {

	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
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

	public Long createProduct(ProductDto productDto) {
		Product product = new Product(productDto.getName(), productDto.getPrice(), productDto.getImageUrl());
		return productRepository.createProduct(product);
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
