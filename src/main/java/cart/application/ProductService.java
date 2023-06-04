package cart.application;

import cart.domain.Product;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import cart.repository.ProductRepository;
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

    public ProductResponse getProductById(Long productId) {
        Product product = productRepository.findById(productId);
        return ProductResponse.of(product);
    }

    public Long createProduct(ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
        return productRepository.create(product);
    }

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

    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId);
        productRepository.delete(product);
    }
}
