package cart.application;

import cart.domain.Product;
import cart.domain.repository.ProductRepository;
import cart.dto.PageInfo;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import cart.dto.ProductResponses;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(ProductResponse::of).collect(Collectors.toList());
    }

    public ProductResponses findProductsByPage(final Integer page, final Integer size) {
        List<Product> products = productRepository.findAll();
        final List<Product> partition = Lists.partition(products, size).get(page - 1);
        final List<ProductResponse> productResponses = partition.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
        final PageInfo pageInfo = new PageInfo(page, size, products.size(), productResponses.size());
        return new ProductResponses(pageInfo,productResponses);
    }

    public ProductResponse getProductById(Long productId) {
        Product product = productRepository.findById(productId);
        return ProductResponse.of(product);
    }

    public Long createProduct(ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
        return productRepository.save(product);
    }

    public void updateProduct(Long productId, ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
        productRepository.update(productId, product);
    }

    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }
}