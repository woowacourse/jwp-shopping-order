package cart.application;

import cart.dao.entity.ProductEntity;
import cart.domain.Money;
import cart.domain.Product;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import cart.exception.ProductException;
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

    public List<ProductResponse> getAllProducts() {
        final List<Product> products = productRepository.findAll();
        return products.stream().map(ProductResponse::of).collect(Collectors.toList());
    }

    public ProductResponse getProductById(final Long productId) {
        final Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException.IllegalId(productId));
        return ProductResponse.of(product);
    }

    public Long createProduct(final ProductRequest productRequest) {
        final ProductEntity product = new ProductEntity(productRequest.getName(), new Money(productRequest.getPrice()),
                productRequest.getImageUrl());
        return productRepository.add(product);
    }

    public void updateProduct(final Long productId, final ProductRequest productRequest) {
        final ProductEntity product = new ProductEntity(
                productId,
                productRequest.getName(),
                new Money(productRequest.getPrice()),
                productRequest.getImageUrl());
        productRepository.update(product);
    }

    public void deleteProduct(final Long productId) {
        productRepository.remove(productId);
    }
}
