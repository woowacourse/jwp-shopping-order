package cart.application;

import cart.domain.product.Product;
import cart.dto.request.ProductRequest;
import cart.dto.response.ProductResponse;
import cart.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return ProductResponse.makeProductResponses(products);
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long productId) {
        Product product = productRepository.findById(productId);
        return ProductResponse.of(product);
    }

    @Transactional
    public Long createProduct(ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
        return productRepository.create(product);
    }

    @Transactional
    public void updateProduct(Long productId, ProductRequest productRequest) {
        Product product = productRepository.findById(productId);
        Product editProduct = new Product(
                product.getId(),
                productRequest.getName(),
                productRequest.getPrice(),
                productRequest.getImageUrl()
        );
        productRepository.update(editProduct);
    }

    @Transactional
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId);
        productRepository.delete(product);
    }
}
