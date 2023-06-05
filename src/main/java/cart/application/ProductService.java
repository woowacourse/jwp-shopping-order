package cart.application;

import cart.domain.product.Product;
import cart.domain.product.ProductRepository;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import cart.util.ModelSortHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findAllProducts() {
        List<Product> products = productRepository.findAll();
        ModelSortHelper.sortByIdInDescending(products);
        return products.stream().map(ProductResponse::from).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductResponse findProductById(Long productId) {
        Product product = productRepository.findById(productId);
        return ProductResponse.from(product);
    }

    public Long createProduct(ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
        return productRepository.add(product);
    }

    public void updateProduct(Long productId, ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
        productRepository.update(productId, product);
    }

    public void deleteProduct(Long productId) {
        productRepository.delete(productId);
    }
}
