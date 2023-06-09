package cart.application.service;

import cart.application.domain.Product;
import cart.ui.dto.request.ProductRequest;
import cart.ui.dto.response.ProductResponse;
import cart.application.repository.ProductRepository;
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
        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public ProductResponse getProductById(final Long productId) {
        final Product product = productRepository.findById(productId);
        return ProductResponse.of(product);
    }

    public Long createProduct(final ProductRequest productRequest) {
        final Product product = new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
        final Product savedProduct = productRepository.save(product);
        return savedProduct.getId();
    }

    public void updateProduct(final Long productId, final ProductRequest productRequest) {
        final Product product = new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
        productRepository.updateById(productId, product);
    }

    public void removeProduct(final long id) {
        productRepository.deleteById(id);
    }
}