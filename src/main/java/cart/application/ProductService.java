package cart.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cart.domain.Product;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import cart.repository.ProductRepository;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> findAll() {
        final List<Product> products = productRepository.findAll();
        return products.stream().map(ProductResponse::of).collect(Collectors.toList());
    }

    public ProductResponse findById(Long productId) {
        final Product product = productRepository.findById(productId);
        return ProductResponse.of(product);
    }

    @Transactional
    public Long add(ProductRequest productRequest) {
        Product product = new Product(
                productRequest.getName(),
                productRequest.getPrice(),
                productRequest.getImageUrl()
        );
        return productRepository.add(product);
    }

    @Transactional
    public void update(Long productId, ProductRequest productRequest) {
        Product product = new Product(
                productId,
                productRequest.getName(),
                productRequest.getPrice(),
                productRequest.getImageUrl()
        );
        productRepository.update(product);
    }

    @Transactional
    public void remove(Long productId) {
        productRepository.remove(productId);
    }
}
