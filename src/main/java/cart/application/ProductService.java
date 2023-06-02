package cart.application;

import cart.domain.Product;
import cart.domain.repository.ProductRepository;
import cart.domain.vo.Amount;
import cart.dto.request.ProductRequest;
import cart.dto.response.ProductResponse;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> findAll() {
        final List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public ProductResponse findById(final Long id) {
        final Product product = productRepository.findById(id);
        return ProductResponse.of(product);
    }

    public Long create(final ProductRequest productRequest) {
        final Product product = new Product(productRequest.getName(), Amount.of(productRequest.getPrice()),
            productRequest.getImageUrl());
        return productRepository.create(product);
    }

    public void update(final Long id, final ProductRequest productRequest) {
        final Product product = new Product(productRequest.getName(), Amount.of(productRequest.getPrice()),
            productRequest.getImageUrl());
        productRepository.update(id, product);
    }

    public void delete(final Long id) {
        productRepository.deleteById(id);
    }
}
