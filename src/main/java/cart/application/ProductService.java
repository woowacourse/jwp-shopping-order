package cart.application;

import cart.domain.Product;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import cart.repository.ProductRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

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

    public Long createProduct(final ProductRequest productRequest) {
        final Product product = new Product(
                productRequest.getName(),
                productRequest.getPrice(),
                productRequest.getImageUrl()
        );
        return productRepository.save(product).getId();
    }

    public void updateProduct(final Long productId, final ProductRequest productRequest) {
        final Product product = new Product(
                productRequest.getName(),
                productRequest.getPrice(),
                productRequest.getImageUrl()
        );
        productRepository.updateById(productId, product);
    }

    public void deleteProduct(final Long productId) {
        productRepository.deleteById(productId);
    }
}
