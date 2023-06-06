package cart.service;

import cart.domain.Money;
import cart.domain.Product;
import cart.dto.ProductSaveRequest;
import cart.dto.ProductUpdateRequest;
import cart.exception.cart.ProductNotFoundException;
import cart.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Product findById(final Long id) {
        return productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);
    }

    public Product update(final Long id, final ProductUpdateRequest request) {
        final Product savedProduct = new Product(id, request.getName(), request.getImageUrl(), new Money(request.getPrice()));
        return productRepository.save(savedProduct);
    }

    public void delete(final Long id) {
        productRepository.deleteById(id);
    }
}
