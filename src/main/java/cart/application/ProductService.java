package cart.application;

import cart.dao.ProductDao;
import cart.domain.Product;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import cart.entity.ProductEntity;
import cart.exception.ResourceNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public Long createProduct(ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(),
                productRequest.getImageUrl());
        return productDao.create(ProductEntity.from(product));
    }

    public List<ProductResponse> findAllProducts() {
        final List<Product> products = productDao.findAll().stream()
                .map(ProductEntity::toDomain)
                .collect(Collectors.toUnmodifiableList());
        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toUnmodifiableList());
    }

    public ProductResponse findProductById(Long productId) {
        Product product = ProductEntity.toDomain(productDao.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("아이디에 해당하는 상품이 없습니다.")));
        return ProductResponse.of(product);
    }

    @Transactional
    public void updateProduct(Long productId, ProductRequest productRequest) {
        ProductEntity productEntity = new ProductEntity(
                productRequest.getName(),
                productRequest.getPrice(),
                productRequest.getImageUrl()
        );
        productDao.update(productId, productEntity);
    }

    @Transactional
    public void deleteProduct(Long productId) {
        productDao.deleteById(productId);
    }
}
