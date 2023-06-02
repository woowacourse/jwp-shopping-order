package cart.service;

import cart.domain.Money;
import cart.domain.Product;
import cart.dto.ProductResponse;
import cart.dto.ProductSaveRequest;
import cart.dto.ProductUpdateRequest;
import cart.exception.ProductNotFoundException;
import cart.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toUnmodifiableList;

@Transactional
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Long save(final ProductSaveRequest request) {
        final Product product = new Product(request.getName(), request.getImageUrl(), new Money(request.getPrice()));
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
        final Product savedProduct = new Product(id, request.getName(), request.getImageUrl(), new Money(request.getPrice()));
        productRepository.save(savedProduct);
    }

    public void delete(final Long id) {
        productRepository.deleteById(id);
    }
}
