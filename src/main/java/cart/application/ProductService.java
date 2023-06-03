package cart.application;

import cart.domain.Product;
import cart.domain.respository.product.ProductRepository;
import cart.dto.request.ProductRequest;
import cart.dto.response.ProductResponse;
import cart.exception.ProductException.ProductNotExistException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> getAllProducts() {
        final List<Product> products = productRepository.getAllProducts();
        return products.stream().map(ProductResponse::of).collect(Collectors.toList());
    }

    public ProductResponse getProductById(Long productId) {
        final Product product = productRepository.getProductById(productId)
            .orElseThrow(() -> new ProductNotExistException("상품이 존재하지 않습니다."));
        return ProductResponse.of(product);
    }

    public Product createProduct(ProductRequest productRequest) {
        final Product product = new Product(productRequest.getName(), productRequest.getPrice(),
            productRequest.getImageUrl());
        return productRepository.createProduct(product);
    }

    public void updateProduct(Long productId, ProductRequest productRequest) {
        final Product product = new Product(productRequest.getName(), productRequest.getPrice(),
            productRequest.getImageUrl());
        productRepository.updateProduct(productId, product);
    }

    public void deleteProduct(Long productId) {
        productRepository.deleteProduct(productId);
    }
}
