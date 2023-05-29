package cart.application.service;

import cart.application.mapper.ProductMapper;
import cart.application.repository.ProductRepository;
import cart.domain.Product;
import cart.exception.ProductNotFoundException;
import cart.infrastructure.repository.dao.ProductDao;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import java.util.Optional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(ProductResponse::of).collect(Collectors.toList());
    }

    public ProductResponse getProductById(final Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
        return ProductResponse.of(product);
    }

    public Long createProduct(final ProductRequest request) {
        Product product = ProductMapper.toProduct(request);
        return productRepository.create(product);
    }

    public void updateProduct(final Long productId, final ProductRequest request) {
        Product product = ProductMapper.toProduct(productId, request);
        productRepository.update(product);
    }

    public void deleteProduct(final Long productId) {
        productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
        productRepository.deleteById(productId);
    }
}
