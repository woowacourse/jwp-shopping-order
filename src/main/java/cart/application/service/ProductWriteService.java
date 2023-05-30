package cart.application.service;

import cart.application.repository.ProductRepository;
import cart.domain.Product;
import cart.ui.product.dto.ProductRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductWriteService {

    private final ProductRepository productRepository;

    public ProductWriteService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Long createProduct(ProductRequest productRequest) {
        final Product product = new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
        return productRepository.createProduct(product);
    }

    public void updateProduct(Long productId, ProductRequest productRequest) {
        final Product product = new Product(productId, productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
        productRepository.updateProduct(product);
    }

    public void deleteProduct(Long productId) {
        productRepository.deleteProduct(productId);
    }

}
