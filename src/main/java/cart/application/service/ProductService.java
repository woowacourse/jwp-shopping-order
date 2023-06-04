package cart.application.service;

import cart.application.exception.ProductNotFoundException;
import cart.application.repository.ProductRepository;
import cart.application.domain.Product;
import cart.presentation.dto.request.ProductRequest;
import cart.presentation.dto.response.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
        return ProductResponse.of(product);
    }

    public Long createProduct(ProductRequest productRequest) {
        Product product = new Product(null, productRequest.getName(), productRequest.getPrice(),
                productRequest.getImageUrl(), productRequest.getPointRatio(), productRequest.getPointAvailable());
        Product inserted = productRepository.insert(product);
        return inserted.getId();
    }

    public void updateProduct(Long productId, ProductRequest productRequest) {
        Product product = new Product(productId, productRequest.getName(), productRequest.getPrice(),
                productRequest.getImageUrl(), productRequest.getPointRatio(), productRequest.getPointAvailable());
        productRepository.update(product);
    }

    public void deleteProduct(Long productId) {
        productRepository.delete(productId);
    }
}
