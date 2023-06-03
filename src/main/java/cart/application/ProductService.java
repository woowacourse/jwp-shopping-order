package cart.application;

import cart.application.request.CreateProductRequest;
import cart.application.response.ProductResponse;
import cart.domain.product.Product;
import cart.dao.entity.ProductEntity;
import cart.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Long createProduct(CreateProductRequest request) {
        ProductEntity productEntity = new ProductEntity(request.getName(), BigDecimal.valueOf(request.getPrice()), request.getImageUrl());

        return productRepository.saveProduct(productEntity);
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public ProductResponse getProductById(Long productId) {
        Product product = productRepository.findByProductId(productId);

        return ProductResponse.of(product);
    }

    @Transactional
    public void updateProduct(Long productId, CreateProductRequest request) {
        ProductEntity productEntity = new ProductEntity(
                request.getName(),
                BigDecimal.valueOf(request.getPrice()),
                request.getImageUrl());

        productRepository.updateProduct(productId, productEntity);
    }

    @Transactional
    public void deleteProduct(Long productId) {
        productRepository.deleteProduct(productId);
    }
}
