package cart.application;

import cart.dao.CartItemDao;
import cart.domain.Product;
import cart.dao.ProductDao;
import cart.dto.request.ProductRequest;
import cart.dto.response.ProductResponse;
import cart.entity.ProductEntity;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

	private final ProductDao productDao;
	private final CartItemDao cartItemDao;

	public ProductService(final ProductDao productDao, final CartItemDao cartItemDao) {
		this.productDao = productDao;
		this.cartItemDao = cartItemDao;
	}

	public Long createProduct(ProductRequest productRequest) {
		Product product = new Product(productRequest.getName(), productRequest.getPrice(),
			productRequest.getImageUrl());
		return productDao.createProduct(product);
	}

	@Transactional(readOnly = true)
	public List<ProductResponse> getAllProducts() {
		List<Product> products = productDao.getAllProducts();
		return products.stream().map(ProductResponse::of).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public ProductResponse getProductById(Long productId) {
		final ProductEntity productEntity = productDao.getProductById(productId);
		final Product product = Product.from(productEntity);
		return ProductResponse.of(product);
	}

	public void updateProduct(Long productId, ProductRequest productRequest) {
		Product product = new Product(productRequest.getName(), productRequest.getPrice(),
			productRequest.getImageUrl());
		productDao.updateProduct(productId, product);
	}

	public void deleteProduct(Long productId) {
		productDao.deleteProduct(productId);
		cartItemDao.deleteByProductId(productId);
	}
}
