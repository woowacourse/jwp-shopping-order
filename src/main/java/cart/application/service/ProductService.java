package cart.application.service;

import static cart.exception.noexist.NoExistErrorType.PRODUCT_NO_EXIST;

import cart.application.dto.ProductRequest;
import cart.application.dto.ProductResponse;
import cart.application.repository.ProductRepository;
import cart.domain.Product;
import cart.exception.noexist.NoExistException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(ProductResponse::of).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductResponse findProductById(final Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoExistException(PRODUCT_NO_EXIST));
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
                .orElseThrow(() -> new NoExistException(PRODUCT_NO_EXIST));
    }

    public void deleteProduct(final Long productId) {
        productRepository.deleteById(productId);
    }
}
