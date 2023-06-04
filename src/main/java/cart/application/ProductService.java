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

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Long save(ProductRequest productRequest) {
        Product product = new Product(
                null,
                productRequest.getName(),
                productRequest.getPrice(),
                productRequest.getImageUrl());
        return productRepository.save(product);
    }

    public List<ProductResponse> findAll() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public ProductResponse findById(Long productId) {
        Product product = productRepository.findById(productId);
        return ProductResponse.of(product);
    }

    public void update(Long productId, ProductRequest productRequest) {
        Product product = new Product(
                productId,
                productRequest.getName(),
                productRequest.getPrice(),
                productRequest.getImageUrl());
        productRepository.update(product);
    }

    public void deleteById(Long productId) {
        productRepository.deleteById(productId);
    }
}
