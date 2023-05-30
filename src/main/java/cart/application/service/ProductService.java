package cart.application.service;

import cart.application.repository.ProductRepository;
import cart.domain.Product;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import cart.exception.ProductNotFoundException;
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
        List<Product> products = productRepository.findAll();
        return products.stream().map(ProductResponse::of).collect(Collectors.toList());
    }

    public ProductResponse getProductById(final Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
        return ProductResponse.of(product);
    }

    public Long createProduct(final ProductRequest request) {
        Product product = new Product(request.getName(), request.getPrice(), request.getImageUrl());
        return productRepository.create(product);
    }

    public void updateProduct(final long productId, final ProductRequest request) {
        checkProductExistence(productId);
        Product product = new Product(productId, request.getName(), request.getPrice(), request.getImageUrl());
        productRepository.update(product);
    }

    private void checkProductExistence(final Long productId) {
        productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
    }

    public void deleteProduct(final Long productId) {
        productRepository.deleteById(productId);
    }
}
