package cart.application;

import cart.domain.product.Product;
import cart.repository.CartItemRepository;
import cart.repository.ProductRepository;
import cart.ui.controller.dto.request.ProductRequest;
import cart.ui.controller.dto.response.ProductResponse;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public ProductService(ProductRepository productRepository, CartItemRepository cartItemRepository) {
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public List<ProductResponse> findAll() {
        List<Product> products = productRepository.findAll();
        return ProductResponse.listOf(products);
    }

    public ProductResponse findById(Long productId) {
        Product product = productRepository.findById(productId);
        return ProductResponse.from(product);
    }

    @Transactional
    public Long createProduct(ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
        return productRepository.save(product);
    }

    @Transactional
    public void updateProduct(Long productId, ProductRequest productRequest) {
        Product product =
                new Product(productId, productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
        productRepository.update(product);
    }

    @Transactional
    public void deleteProduct(Long productId) {
        cartItemRepository.deleteAllByProductId(productId);
        productRepository.delete(productId);
    }
}
