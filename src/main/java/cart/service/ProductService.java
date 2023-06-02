package cart.service;

import cart.controller.dto.request.ProductRequest;
import cart.controller.dto.response.ProductResponse;
import cart.domain.Ids;
import cart.domain.Price;
import cart.domain.Product;
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

    public List<ProductResponse> findAll() {
        final List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toUnmodifiableList());
    }

    public ProductResponse findById(final long id) {
        final Product product = productRepository.findById(id);
        return ProductResponse.of(product);
    }

    public List<ProductResponse> findByIds(final String idsParameter) {
        final Ids ids = Ids.from(idsParameter);
        List<Product> products = productRepository.findByIds(ids.getIds());
        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toUnmodifiableList());
    }

    public long save(final ProductRequest productRequest) {
        final Product product = new Product(productRequest.getName(),
                new Price(productRequest.getPrice()), productRequest.getImageUrl());
        return productRepository.save(product);
    }

    public void update(final long id, final ProductRequest productRequest) {
        final Product product = new Product(productRequest.getName(),
                new Price(productRequest.getPrice()), productRequest.getImageUrl());
        productRepository.update(product);
    }

    public void delete(final long id) {
        productRepository.delete(id);
    }
}
