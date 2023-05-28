package cart.application;

import cart.domain.Product;
import cart.domain.ProductRepository;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
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
        final List<Product> products = productRepository.findAllProducts();
        return products.stream().map(ProductResponse::of).collect(Collectors.toList());
    }

    public ProductResponse getProductById(final Long productId) {
        final Product product = productRepository.findProductById(productId);
        return ProductResponse.of(product);
    }

    public Long createProduct(final ProductRequest productRequest) {
        final Product product = new Product(productRequest.getName(), productRequest.getPrice(),
                productRequest.getImageUrl());
        return productRepository.saveProduct(product);
    }

    public void updateProduct(final Long productId, final ProductRequest productRequest) {
        final Product product = new Product(productRequest.getName(), productRequest.getPrice(),
                productRequest.getImageUrl());
        productRepository.updateProduct(productId, product);
    }

    public void deleteProduct(final Long productId) {
        productRepository.deleteProduct(productId);
    }
}
