package cart.application;

import cart.domain.Product;
import cart.repository.ProductRepository;
import cart.ui.dto.request.ProductRequest;
import cart.ui.dto.response.ProductResponse;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> findAll() {
        final List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public ProductResponse findById(final Long productId) {
        final Product product = productRepository.findById(productId);
        return ProductResponse.of(product);
    }

    @Transactional
    public Long createProduct(final ProductRequest productRequest) {
        final Product product = new Product(
                productRequest.getName(),
                productRequest.getPrice(),
                productRequest.getImageUrl(),
                productRequest.getIsDiscounted(),
                productRequest.getDiscountRate()
        );
        return productRepository.save(product).getId();
    }

    @Transactional
    public void updateProduct(final Long productId, final ProductRequest productRequest) {
        final Product product = new Product(
                productRequest.getName(),
                productRequest.getPrice(),
                productRequest.getImageUrl(),
                productRequest.getIsDiscounted(),
                productRequest.getDiscountRate()
        );
        productRepository.updateById(productId, product);
    }

    @Transactional
    public void deleteProduct(final Long productId) {
        productRepository.deleteById(productId);
    }
}
