package cart.application;

import cart.domain.Product;
import cart.dto.request.ProductRequest;
import cart.dto.response.ProductResponse;
import cart.exception.ProductException;
import cart.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Long save(ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(),
                productRequest.getImageUrl());

        return productRepository.save(product);
    }

    public List<ProductResponse> findAll() {
        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public ProductResponse findById(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException.NotFound(productId));

        return ProductResponse.of(product);
    }

    @Transactional
    public void update(Long productId, ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(),
                productRequest.getImageUrl());

        productRepository.update(productId, product);
    }

    @Transactional
    public void deleteById(Long productId) {
        productRepository.deleteById(productId);
    }
}
