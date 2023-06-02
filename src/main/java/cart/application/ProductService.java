package cart.application;

import cart.domain.product.Product;
import cart.repository.ProductRepository;
import cart.ui.dto.product.ProductRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Long createProduct(final ProductRequest request) {
        final Product product = new Product(request.getName(), request.getPrice(), request.getImageUrl());
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(final Long productId) {
        return productRepository.findById(productId);
    }

    @Transactional
    public void updateProduct(final Long productId, final ProductRequest request) {
        final Product product = new Product(
                productId,
                request.getName(),
                request.getPrice(),
                request.getImageUrl());
        productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(final Long productId) {
        productRepository.deleteById(productId);
    }
}
