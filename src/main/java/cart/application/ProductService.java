package cart.application;

import static java.util.stream.Collectors.toList;

import cart.domain.Product;
import cart.dto.request.ProductRequest;
import cart.dto.response.ProductResponse;
import cart.exception.ProductNotFound;
import cart.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream()
                .map(ProductResponse::of)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(ProductNotFound::new);
        return ProductResponse.of(product);
    }

    @Transactional
    public Long createProduct(ProductRequest productRequest) {
        Product product = productRequest.toEntity();
        return productRepository.save(product);
    }


    @Transactional
    public void updateProduct(Long productId, ProductRequest productRequest) {
        checkProduct(productId);
        Product product = new Product(productId, productRequest.getName(), productRequest.getPrice(),
                productRequest.getImageUrl());
        productRepository.update(product);
    }

    private void checkProduct(Long productId) {
        if (productRepository.findById(productId).isEmpty()) {
            throw new ProductNotFound();
        }
    }

    @Transactional
    public void deleteProduct(Long productId) {
        checkProduct(productId);
        productRepository.deleteById(productId);
    }
}
