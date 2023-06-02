package cart.repository;

import cart.dao.ProductDao;
import cart.domain.Product;
import cart.domain.repository.ProductRepository;
import cart.entity.ProductEntity;
import cart.exception.ProductException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ProductRepositoryImpl implements ProductRepository {
    private final ProductDao productDao;

    public ProductRepositoryImpl(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public List<Product> getAllProducts() {
        return productDao.getAllProducts().stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public Product getProductById(Long productId) {
        return toDomain(productDao.getProductById(productId)
                .orElseThrow(() -> new ProductException("잘못된 상품입니다.")));
    }

    @Override
    public Long createProduct(Product product) {
        return productDao.createProduct(product);
    }

    @Override
    public void updateProduct(Long productId, Product product) {
        productDao.updateProduct(productId, product);
    }

    @Override
    public void deleteProduct(Long productId) {
        productDao.deleteProduct(productId);
    }

    private Product toDomain(ProductEntity entity) {
        return new Product(entity.getId(), entity.getName(), entity.getPrice(), entity.getImageUrl());
    }
}
