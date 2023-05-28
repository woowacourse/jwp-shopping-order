package cart.application;

import cart.application.dto.ProductRequest;
import cart.application.dto.ProductResponse;
import cart.domain.product.Product;
import cart.domain.product.ProductRepository;
import cart.domain.product.dto.ProductWithId;
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

    public List<ProductResponse> getAllProducts() {
        final List<ProductWithId> products = productRepository.getAllProducts();
        return products.stream()
            .map(productWithId -> new ProductResponse(productWithId.getId(), productWithId.getProduct().getName(),
                productWithId.getProduct().getPrice(), productWithId.getProduct().getImageUrl()))
            .collect(Collectors.toList());
    }

    public ProductResponse getProductById(Long productId) {
        final Product product = productRepository.getProductById(productId);
        return new ProductResponse(productId, product.getName(), product.getPrice(), product.getImageUrl());
    }

    @Transactional
    public Long createProduct(ProductRequest productRequest) {
        final Product product = new Product(productRequest.getName(), productRequest.getPrice(),
            productRequest.getImageUrl());
        return productRepository.save(product);
    }

    @Transactional
    public void updateProduct(Long productId, ProductRequest productRequest) {
        final Product product = new Product(productRequest.getName(), productRequest.getPrice(),
            productRequest.getImageUrl());
        productRepository.updateProduct(productId, product);
    }

    @Transactional
    public void deleteProduct(Long productId) {
        productRepository.deleteProduct(productId);
    }
}
