package cart.service;

import static java.util.stream.Collectors.toUnmodifiableList;

import cart.domain.cart.Product;
import cart.dto.cart.ProductResponse;
import cart.dto.cart.ProductSaveRequest;
import cart.dto.cart.ProductUpdateRequest;
import cart.exception.cart.ProductNotFoundException;
import cart.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Long save(final ProductSaveRequest request) {
        final Product product = new Product(request.getName(), request.getImageUrl(), request.getPrice());
        return productRepository.save(product).getId();
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream()
                .map(ProductResponse::from)
                .collect(toUnmodifiableList());
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(final Long id) {
        final Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);
        return ProductResponse.from(product);
    }

    public void update(final Long id, final ProductUpdateRequest request) {
        final Product savedProduct = new Product(id, request.getName(), request.getImageUrl(), request.getPrice());
        productRepository.save(savedProduct);
    }

    public void delete(final Long id) {
        productRepository.deleteById(id);
    }
}
