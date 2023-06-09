package cart.application;

import cart.domain.Product;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import cart.exception.ProductException;
import cart.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;


    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        final List<Product> products = this.productRepository.findAll();
        return products.stream().map(ProductResponse::of).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(final Long productId) {
        final Product product = this.productRepository.findById(productId)
                .orElseThrow(() -> new ProductException.NotFoundException(productId));
        return ProductResponse.of(product);
    }

    public Long createProduct(final ProductRequest productRequest) {
        final Product product = Product.of(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
        return this.productRepository.create(product);
    }

    public void updateProduct(final Long productId, final ProductRequest productRequest) {
        final Product product = Product.of(productId, productRequest.getName(), productRequest.getPrice(),
                productRequest.getImageUrl());
        this.productRepository.update(product);
    }

    public void deleteProduct(final Long productId) {
        this.productRepository.deleteById(productId);
    }
}