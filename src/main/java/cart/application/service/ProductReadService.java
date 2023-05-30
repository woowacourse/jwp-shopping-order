package cart.application.service;

import cart.application.repository.ProductRepository;
import cart.domain.Product;
import cart.ui.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ProductReadService {

    private final ProductRepository productRepository;

    public ProductReadService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> getAllProducts() {
        final List<Product> products = productRepository.findAll();
        return products.stream().map(ProductResponse::of).collect(Collectors.toList());
    }

    public ProductResponse getProductById(Long productId) {
        final Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("일치하는 상품이 없습니다."));
        return ProductResponse.of(product);
    }

}
