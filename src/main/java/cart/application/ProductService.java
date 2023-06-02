package cart.application;

import cart.domain.Product;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import cart.repository.ProductRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Long createProduct(final ProductRequest request) {
        final Product product = new Product(
                request.getName(),
                request.getPrice(),
                request.getImageUrl()
        );
        return productRepository.save(product);
    }

    public List<ProductResponse> findAllProducts() {
        final List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }

    public ProductResponse findProductById(final Long id) {
        Product product = productRepository.findById(id);
        return ProductResponse.from(product);
    }

    @Transactional
    public void updateProduct(final Long id, final ProductRequest request) {
        final Product product = new Product(
                id,
                request.getName(),
                request.getPrice(),
                request.getImageUrl()
        );
        productRepository.update(product);
    }

    @Transactional
    public void deleteProduct(final Long productId) {
        productRepository.deleteById(productId);
    }
}
