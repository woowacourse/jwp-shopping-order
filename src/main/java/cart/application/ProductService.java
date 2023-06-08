package cart.application;

import cart.domain.Product;
import cart.domain.repository.ProductRepository;
import cart.dto.request.ProductRequest;
import cart.dto.response.ProductResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> findAll() {
        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

    public ProductResponse findById(Long productId) {
        Product product = productRepository.findById(productId);

        return ProductResponse.from(product);
    }

    public Long save(ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());

        return productRepository.save(product);
    }

    public void updateById(Long productId, ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
        productRepository.updateById(productId, product);
    }

    public void deleteById(Long productId) {
        productRepository.deleteById(productId);
    }
}
