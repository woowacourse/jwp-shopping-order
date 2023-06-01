package cart.product.application;

import cart.product.domain.Product;
import cart.product.dto.ProductRequest;
import cart.product.dto.ProductResponse;
import cart.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class ProductService {
    private final ProductRepository productRepository;
    
    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        final List<Product> products = productRepository.getAllProducts();
        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long productId) {
        Product product = productRepository.getProductById(productId);
        return ProductResponse.of(product);
    }
    
    public Long createProduct(ProductRequest productRequest) {
        Product product = new Product(
                productRequest.getName(),
                productRequest.getPrice(),
                productRequest.getImageUrl(),
                productRequest.getPointRatio(),
                productRequest.getPointAvailable()
        );
        return productRepository.createProduct(product);
    }
    
    public void updateProduct(Long productId, ProductRequest productRequest) {
        Product product = new Product(
                productId,
                productRequest.getName(),
                productRequest.getPrice(),
                productRequest.getImageUrl(),
                productRequest.getPointRatio(),
                productRequest.getPointAvailable()
        );
        productRepository.updateProduct(product);
    }
    
    public void deleteProduct(Long productId) {
        productRepository.removeProduct(productId);
    }
}
